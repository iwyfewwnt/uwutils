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

import java.util.function.Supplier;

/**
 * An array utility.
 *
 * <p>{@code UwArray} is the utility class
 * that provide functionality to operate
 * with arrays.
 *
 * @since 0.1.0
 */
@SuppressWarnings("unused")
public final class UwArray {

	/**
	 * An empty class array instance.
	 */
	public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];

	/**
	 * Null-safely get a value from an array by its index.
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
	 * Null-safely get a value from an array by its index or return a default value.
	 *
	 * @param index				index of the value
	 * @param array				array from which return the value
	 * @param defaultValue 		default value to be returned on failure
	 * @param <T>				value type
	 * @return					value assigned to the index or default value
	 */
	public static <T> T getOrElse(Integer index, T[] array, T defaultValue) {
		if (index == null || array == null
				|| index < 0 || index >= array.length) {
			return defaultValue;
		}

		T returnValue = array[index];

		if (returnValue == null) {
			return defaultValue;
		}

		return returnValue;
	}

	/**
	 * Null-safely get a value from an array by its index or return a default value.
	 *
	 * @param index				index of the value
	 * @param array				array from which return the value
	 * @param supplier 			supplier from which return the default value
	 * @param <T>				value type
	 * @return					value assigned to the index or default value
	 */
	public static <T> T getOrElse(Integer index, T[] array, Supplier<T> supplier) {
		T returnValue = getOrNull(index, array);

		if (returnValue == null) {
			return UwObject.applyIfNotNull(supplier, Supplier::get);
		}

		return returnValue;
	}

	/**
	 * Null-safely get a value from an array by its index or return null.
	 *
	 * @param index				index of the value
	 * @param array				array from which return the value
	 * @param <T>				value type
	 * @return					value assigned to the index or null
	 */
	public static <T> T getOrNull(Integer index, T[] array) {
		return getOrElse(index, array, (T) null);
	}

	private UwArray() {
		throw new UnsupportedOperationException();
	}
}
