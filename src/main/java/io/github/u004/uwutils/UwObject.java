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

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An object utility.
 *
 * <p>{@code UwObject} is the utility class
 * that provide functionality to operate
 * with objects.
 *
 * @since 0.1.0
 */
@SuppressWarnings("unused")
public final class UwObject {

	/**
	 * Apply argument to the specified function if it's not null or return a default value.
	 *
	 * @param object			argument to be applied
	 * @param function			function to which apply argument
	 * @param defaultValue		default value to be returned on failure
	 * @param <T>				argument type
	 * @param <R>				return type
	 * @return					result of the function applying or default value
	 */
	public static <T, R> R applyIfNotNull(T object, Function<T, R> function, R defaultValue) {
		if (object == null || function == null) {
			return defaultValue;
		}

		R returnValue = function.apply(object);

		if (returnValue == null) {
			return defaultValue;
		}

		return returnValue;
	}

	/**
	 * Apply argument to the specified function it's not null or return a default value.
	 *
	 * @param object		argument to apply
	 * @param function		function to which apply argument
	 * @param supplier		supplier from which get the default value
	 * @param <T>			argument type
	 * @param <R>			return type
	 * @return				result of the function applying or default value
	 */
	public static <T, R> R applyIfNotNull(T object, Function<T, R> function, Supplier<R> supplier) {
		R returnValue = applyIfNotNull(object, function);

		if (returnValue == null) {
			return applyIfNotNull(supplier, Supplier::get);
		}

		return returnValue;
	}

	/**
	 * Apply argument to the specified function if it's not null or return {@code null}.
	 *
	 * <p>Wraps {@link UwObject#applyIfNotNull(Object, Function, Object)}
	 * w/ {@code null} as the default value.
	 *
	 * @param object			argument to be applied
	 * @param function			function to which apply argument
	 * @param <T>				argument type
	 * @param <R>				return type
	 * @return					result of the function applying or {@code null}
	 */
	public static <T, R> R applyIfNotNull(T object, Function<T, R> function) {
		return applyIfNotNull(object, function, (R) null);
	}

	private UwObject() {
		throw new UnsupportedOperationException();
	}
}
