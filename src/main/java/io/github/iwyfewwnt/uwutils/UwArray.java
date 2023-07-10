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

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * An array utility.
 *
 * <p>{@code UwArray} is the utility class
 * that provide functionality to operate
 * with arrays.
 */
@SuppressWarnings("unused")
public final class UwArray {

	/**
	 * An empty class array instance.
	 */
	public static final Class<?>[] CLASS_EMPTY = new Class<?>[0];

	/**
	 * An empty object array instance.
	 */
	public static final Object[] OBJECT_EMPTY = new Object[0];

	/**
	 * Safely get a value from an array by its index or return a default value.
	 *
	 * @param index			index of the value
	 * @param array			array from which get the value
	 * @param defaultValue 	default value to return on failure
	 * @param <T>			value type
	 * @return				value assigned to the index or the default value
	 */
	public static <T> T getOrElse(Integer index, T[] array, T defaultValue) {
		if (index == null || array == null
				|| index < 0 || index >= array.length) {
			return defaultValue;
		}

		return UwObject.ifNull(array[index], defaultValue);
	}

	/**
	 * Safely get a value from an array by its index or return a default value.
	 *
	 * @param index					index of the value
	 * @param array					array from which get the value
	 * @param defaultValueSupplier 	supplier from which get the default value
	 * @param <T>					value type
	 * @return						value assigned to the index or the default value
	 */
	public static <T> T getOrElse(Integer index, T[] array, Supplier<T> defaultValueSupplier) {
		return UwObject.ifNull(getOrNull(index, array), defaultValueSupplier);
	}

	/**
	 * Safely get a value from an array by its index or return {@code null}.
	 *
	 * <p>Wraps {@link #getOrElse(Integer, Object[], Object)}
	 * w/ {@code null} as the default value.
	 *
	 * @param index		index of the value
	 * @param array		array from which get the value
	 * @param <T>		value type
	 * @return			value assigned to the index or {@code null}
	 */
	public static <T> T getOrNull(Integer index, T[] array) {
		return getOrElse(index, array, (T) null);
	}

	/**
	 * Safely propagate an item to a first {@code null} cell of the provided array.
	 *
	 * @param item		item to propagate
	 * @param array		array to insert the item to
	 * @param <T>		item type
	 */
	public static <T> void propagate(T item, T[] array) {
		if (item == null || array == null) {
			return;
		}

		for (int i = 0; i < array.length; i++) {
			if (array[i] != null) {
				continue;
			}

			array[i] = item;
			break;
		}
	}

	/**
	 * Safely consume items of the provided array until
	 * a first {@code null} element occurred.
	 *
	 * @param consumer	consumer to pass an item to
	 * @param array		array of elements
	 * @param <T>		element type
	 */
	public static <T> void consume(Consumer<T> consumer, T[] array) {
		if (consumer == null || array == null) {
			return;
		}

		for (T item : array) {
			if (item == null) {
				break;
			}

			consumer.accept(item);
		}
	}

	/**
	 * Create a new {@link java.lang.Iterable} instance for the provided array of elements.
	 *
	 * @param array		array of elements
	 * @param <T>		element type
	 * @return			new {@link java.lang.Iterable} instance
	 */
	public static <T> java.lang.Iterable<T> iterable(T[] array) {
		return new Iterable<>(array);
	}

	/**
	 * Create a new {@link java.util.Iterator} instance for the provided array of elements.
	 *
	 * @param array		array of elements
	 * @param <T>		element type
	 * @return			new {@link java.util.Iterator} instance
	 */
	public static <T> java.util.Iterator<T> iterator(T[] array) {
		return new Iterator<>(array);
	}

	/**
	 * An {@link java.lang.Iterable} implementation for fixed array.
	 *
	 * @param <T>	element type
	 */
	public static final class Iterable<T> implements java.lang.Iterable<T> {

		/**
		 * An array of elements.
		 */
		private final T[] array;

		/**
		 * Initialize an {@link UwArray.Iterable} instance.
		 *
		 * @param array		array of elements
		 */
		public Iterable(T[] array) {
			this.array = array;
		}

		/**
		 * Initialize an {@link UwArray.Iterable} instance.
		 *
		 * <p>Wraps {@link #Iterable(Object[])}
		 * w/ {@code null} as the array of elements.
		 *
		 */
		public Iterable() {
			this(null);
		}

		/**
		 * Return an iterator for this array of elements
		 *
		 * @return	iterator for this array of elements
		 */
		@Override
		public java.util.Iterator<T> iterator() {
			return new Iterator<>(this.array);
		}

		/**
		 * Perform the given action for each element of this array.
		 *
		 * <p><b>Doesn't throw any internal exceptions.</b>
		 *
		 * @param action	action to perform for each element of this array
		 */
		@Override
		public void forEach(Consumer<? super T> action) {
			if (this.array == null
					|| action == null) {
				return;
			}

			java.lang.Iterable.super.forEach(action);
		}
	}

	/**
	 * An {@link java.util.Iterator} implementation for fixed array.
	 *
	 * @param <T>	element type
	 */
	@SuppressWarnings("unused")
	public static final class Iterator<T> implements java.util.Iterator<T> {

		/**
		 * An array of elements.
		 */
		private final T[] array;

		/**
		 * Current element index.
		 */
		private int index;

		/**
		 * Initialize an {@link UwArray.Iterator} instance.
		 *
		 * @param array		array of elements
		 */
		public Iterator(T[] array) {
			this.array = array;
			this.index = -1;
		}

		/**
		 * Initialize an {@link UwArray.Iterator} instance.
		 *
		 * <p>Wraps {@link #Iterator(Object[])}
		 * w/ {@code null} as the array of elements.
		 */
		public Iterator() {
			this(null);
		}

		/**
		 * Check if this array has a next element.
		 *
		 * @return	true if this array isn't {@code null}
		 * 			and current index {@literal <} this array length
		 */
		@Override
		public boolean hasNext() {
			return this.array != null
					&& this.index < this.array.length - 1;
		}

		/**
		 * Get next element of the array or return {@code null}.
		 *
		 * <p><b>Doesn't throw any internal exceptions.</b>
		 *
		 * @return	next element or {@code null}
		 */
		@Override
		public T next() {
			return getOrNull(++this.index, this.array);
		}

		/**
		 * Reset current element w/ assigning it to {@code null}.
		 *
		 * <p><b>Doesn't throw any internal exceptions.</b>
		 */
		@Override
		public void remove() {
			if (this.array == null
					|| this.index < 0
					|| this.index >= this.array.length) {
				return;
			}

			this.array[this.index] = null;
		}

		/**
		 * Perform the given action for each remaining element.
		 *
		 * <p><b>Doesn't throw any internal exceptions.</b>
		 *
		 * @param action	action to perform for each remaining element
		 */
		@Override
		public void forEachRemaining(Consumer<? super T> action) {
			if (action == null) {
				return;
			}

			java.util.Iterator.super.forEachRemaining(action);
		}
	}

	private UwArray() {
		throw new UnsupportedOperationException();
	}
}
