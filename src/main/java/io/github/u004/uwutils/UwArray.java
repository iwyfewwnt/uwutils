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

package io.github.u004.uwutils;

import io.vavr.control.Option;

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
	public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];

	/**
	 * Safely get a value from an array by its index.
	 *
	 * @param index		index of the value
	 * @param array		array from which return the value
	 * @param <T>		value type
	 * @return			value assigned to the index
	 * 					that wrapped in {@link Option}
	 */
	public static <T> Option<T> get(Integer index, T[] array) {
		return Option.of(getOrNull(index, array));
	}

	/**
	 * Safely get a value from an array by its index or return a default value.
	 *
	 * @param index				index of the value
	 * @param array				array from which return the value
	 * @param defaultValue 		default value to return on failure
	 * @param <T>				value type
	 * @return					value assigned to the index or the default value
	 */
	public static <T> T getOrElse(Integer index, T[] array, T defaultValue) {
		if (index == null || array == null
				|| index < 0 || index >= array.length) {
			return defaultValue;
		}

		return UwObject.getIfNull(array[index], defaultValue);
	}

	/**
	 * Safely get a value from an array by its index or return a default value.
	 *
	 * @param index				index of the value
	 * @param array				array from which return the value
	 * @param supplier 			supplier from which return the default value
	 * @param <T>				value type
	 * @return					value assigned to the index or the default value
	 */
	public static <T> T getOrElse(Integer index, T[] array, Supplier<T> supplier) {
		return UwObject.getIfNull(getOrNull(index, array), supplier);
	}

	/**
	 * Safely get a value from an array by its index or return {@code null}.
	 *
	 * @param index				index of the value
	 * @param array				array from which return the value
	 * @param <T>				value type
	 * @return					value assigned to the index or {@code null}
	 */
	public static <T> T getOrNull(Integer index, T[] array) {
		return getOrElse(index, array, (T) null);
	}

	/**
	 * An {@link java.util.Iterator} implementation for fixed Java arrays.
	 *
	 * @param <T>	element type
	 */
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
		 * <p>Wraps {@link UwArray.Iterator#Iterator(Object[])}
		 * w/ {@code null} as the array of elements.
		 */
		public Iterator() {
			this(null);
		}

		/**
		 * Check if this array has a next element.
		 *
		 * @return		true if this array isn't {@code null}
		 * 				and current index {@literal <} this array length
		 */
		@Override
		public boolean hasNext() {
			return this.array != null
					&& this.index < this.array.length;
		}

		/**
		 * Get next element of the array or return {@code null}.
		 *
		 * <p><b>Doesn't throw any internal exception.</b>
		 *
		 * @return		next element or {@code null}
		 */
		@Override
		public T next() {
			return getOrNull(++this.index, this.array);
		}

		/**
		 * Reset current element w/ assigning it to {@code null}.
		 *
		 * <p><b>Doesn't throw any internal exception.</b>
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
		 * <p><b>Doesn't throw any internal exception.</b>
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
