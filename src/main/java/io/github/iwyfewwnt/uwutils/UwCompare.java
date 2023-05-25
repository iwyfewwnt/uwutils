/*
 * Copyright 2023 iwyfewwnt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.iwyfewwnt.uwutils;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A compare utility.
 *
 * <p>{@code UwCompare} is the utility class
 * that provide functionality to cover the
 * compare operations.
 */
@SuppressWarnings("unused")
public final class UwCompare {

	/**
	 * Create a new {@link NullFirst} comparator instance.
	 *
	 * @param comparator	comparator to delegate comparison to
	 * @param <T>			object type
	 * @return				new {@link NullFirst} comparator instance
	 */
	public static <T> Comparator<T> nullFirst(Comparator<T> comparator) {
		return new NullFirst<>(comparator);
	}

	/**
	 * Create a new {@link NullLast} comparator instance.
	 *
	 * @param comparator	comparator to delegate comparison to
	 * @param <T>			object type
	 * @return				new {@link NullLast} comparator instance
	 */
	public static <T> Comparator<T> nullLast(Comparator<T> comparator) {
		return new NullLast<>(comparator);
	}

	/**
	 * Get a singleton instance of the {@link NullFirst} class
	 * w/o #compare method delegation.
	 *
	 * @param <T>	object type
	 * @return		{@link NullFirst} comparator instance
	 */
	@SuppressWarnings("unchecked")
	public static <T> Comparator<T> nullFirst() {
		return (Comparator<T>) Singleton.NULL_FIRST_COMPARATOR;
	}

	/**
	 * Get a singleton instance of the {@link NullLast} class
	 * w/o #compare method delegation.
	 *
	 * @param <T>	object type
	 * @return		{@link NullLast} comparator instance
	 */
	@SuppressWarnings("unchecked")
	public static <T> Comparator<T> nullLast() {
		return (Comparator<T>) Singleton.NULL_LAST_COMPARATOR;
	}

	private UwCompare() {
		throw new UnsupportedOperationException();
	}

	/**
	 * A {@code null} first comparator.
	 *
	 * <p>Compares objects based on their nullability
	 * and then delegates comparison to the provided
	 * comparator.
	 *
	 * @param <T>	object type
	 */
	public static final class NullFirst<T> implements Comparator<T>, Serializable {

		/**
		 * A delegate comparator.
		 */
		private final Comparator<T> comparator;

		/**
		 * Initialize a {@link NullFirst} instance.
		 *
		 * <p>Wraps {@link #NullFirst(Comparator)}
		 * w/ {@code null} as the comparator.
		 */
		public NullFirst() {
			this(null);
		}

		/**
		 * Initialize a {@link NullFirst} instance.
		 *
		 * @param comparator	comparator to delegate comparison to
		 */
		@SuppressWarnings("unchecked")
		public NullFirst(Comparator<? super T> comparator) {
			this.comparator = (Comparator<T>) comparator;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(T obj0, T obj1) {
			if (obj0 == null && obj1 != null) {
				return -1;
			}

			if (obj0 != null && obj1 == null) {
				return 1;
			}

			if (this.comparator == null) {
				return 0;
			}

			return this.comparator.compare(obj0, obj1);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Comparator<T> thenComparing(Comparator<? super T> other) {
			if (other == null) {
				return this;
			}

			if (this.comparator != null) {
				other = this.comparator.thenComparing(other);
			}

			return new NullFirst<>(other);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Comparator<T> reversed() {
			Comparator<T> other = this.comparator;

			if (this.comparator != null) {
				other = this.comparator.reversed();
			}

			return new NullLast<>(other);
		}
	}

	/**
	 * A {@code null} last comparator.
	 *
	 * <p>Compares objects based on their nullability
	 * and then delegates comparison to the provided
	 * comparator.
	 *
	 * @param <T>	object type
	 */
	public static final class NullLast<T> implements Comparator<T>, Serializable {

		/**
		 * A delegate comparator.
		 */
		private final Comparator<T> comparator;

		/**
		 * Initialize a {@link NullLast} instance.
		 *
		 * <p>Wraps {@link #NullLast(Comparator)}
		 * w/ {@code null} as the comparator.
		 */
		public NullLast() {
			this(null);
		}

		/**
		 * Initialize a {@link NullLast} instance.
		 *
		 * @param comparator	comparator to delegate comparison to
		 */
		@SuppressWarnings("unchecked")
		public NullLast(Comparator<? super T> comparator) {
			this.comparator = (Comparator<T>) comparator;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(T obj0, T obj1) {
			if (obj0 == null && obj1 != null) {
				return 1;
			}

			if (obj0 != null && obj1 == null) {
				return -1;
			}

			if (comparator == null) {
				return 0;
			}

			return this.comparator.compare(obj0, obj1);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Comparator<T> thenComparing(Comparator<? super T> other) {
			if (other == null) {
				return this;
			}

			if (this.comparator != null) {
				other = this.comparator.thenComparing(other);
			}

			return new NullLast<>(other);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Comparator<T> reversed() {
			Comparator<T> other = this.comparator;

			if (this.comparator != null) {
				other = this.comparator.reversed();
			}

			return new NullFirst<>(other);
		}
	}

	/**
	 * An on demand collection of singleton instances.
	 */
	private static final class Singleton {

		/**
		 * A {@link NullFirst} comparator instance.
		 */
		public static final Comparator<?> NULL_FIRST_COMPARATOR = new NullFirst<>();

		/**
		 * A {@link NullLast} comparator instance
		 */
		public static final Comparator<?> NULL_LAST_COMPARATOR = new NullLast<>();

	}
}
