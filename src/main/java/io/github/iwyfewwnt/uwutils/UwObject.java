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

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An object utility.
 *
 * <p>{@code UwObject} is the utility class
 * that provide functionality to operate
 * with objects.
 */
@SuppressWarnings("unused")
public final class UwObject {

	/**
	 * Safely apply argument to the specified function if argument isn't {@code null} or return a default value.
	 *
	 * @param object		argument to apply
	 * @param function		function to which apply argument
	 * @param defaultValue	default value to return on failure
	 * @param <T>			argument type
	 * @param <R>			return type
	 * @return				result of the function applying or the default value
	 */
	public static <T, R> R ifNotNull(T object, Function<T, R> function, R defaultValue) {
		if (object == null || function == null) {
			return defaultValue;
		}

		return ifNull(function.apply(object), defaultValue);
	}

	/**
	 * Safely apply argument to the specified function if argument isn't {@code null} or return a default value.
	 *
	 * @param object				argument to apply
	 * @param function				function to which apply argument
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <T>					argument type
	 * @param <R>					return type
	 * @return						result of the function applying or the default value
	 */
	public static <T, R> R ifNotNull(T object, Function<T, R> function, Supplier<R> defaultValueSupplier) {
		return ifNull(ifNotNull(object, function), defaultValueSupplier);
	}

	/**
	 * Safely apply argument to the specified function if argument isn't {@code null} or return {@code null}.
	 *
	 * <p>Wraps {@link #ifNotNull(Object, Function, Object)}
	 * w/ {@code null} as the default value.
	 *
	 * @param object	argument to apply
	 * @param function	function to which apply argument
	 * @param <T>		argument type
	 * @param <R>		return type
	 * @return			result of the function applying or {@code null}
	 */
	public static <T, R> R ifNotNull(T object, Function<T, R> function) {
		return ifNotNull(object, function, (R) null);
	}

	/**
	 * Get a default value if the specified object is {@code null}.
	 *
	 * @param object	object value to null-check
	 * @param value		default value to return on failure
	 * @param <T>		object type
	 * @return			default value if object is {@code null}
	 * 					else the object
	 */
	public static <T> T ifNull(T object, T value) {
		if (object == null) {
			return value;
		}

		return object;
	}

	/**
	 * Get a default value if the specified object is {@code null}.
	 *
	 * @param object	object value to null-check
	 * @param supplier	supplier from which get the default value
	 * @param <T>		object type
	 * @return			default value if object is {@code null} else the object
	 */
	public static <T> T ifNull(T object, Supplier<T> supplier) {
		if (object == null) {
			return ifNotNull(supplier, Supplier::get);
		}

		return object;
	}

	private UwObject() {
		throw new UnsupportedOperationException();
	}
}
