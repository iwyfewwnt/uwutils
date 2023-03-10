/*
 * Copyright 2022 u004
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

import io.vavr.*;
import org.apache.commons.lang3.ObjectUtils;

import java.util.function.BiFunction;
import java.util.function.Function;

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
	 * Apply argument to the specified function if it's not null or return default value.
	 *
	 * @param object			argument to be applied
	 * @param function			function to which apply argument
	 * @param defaultValue		default value to be returned
	 * @param <T>				argument type
	 * @param <R>				return type
	 * @return					result of the function applying or default value
	 */
	public static <T, R> R applyIfNotNull(T object, Function<T, R> function, R defaultValue) {
		if (ObjectUtils.anyNull(object, function)) {
			return defaultValue;
		}

		return ObjectUtils.defaultIfNull(function.apply(object), defaultValue);
	}

	/**
	 * Apply argument to the specified function if it's not null or return null.
	 *
	 * <p>Wraps {@link UwObject#applyIfNotNull(Object, Function, Object)}.
	 *
	 * @param object			argument to be applied
	 * @param function			function to which apply argument
	 * @param <T>				argument type
	 * @param <R>				return type
	 * @return					result of the function applying or null
	 */
	public static <T, R> R applyIfNotNull(T object, Function<T, R> function) {
		return applyIfNotNull(object, function, (R) null);
	}

	/**
	 * Apply arguments to the specified function is they're not null or return default value.
	 *
	 * @param obj1				1st argument to be applied
	 * @param obj2				2nd argument to be applied
	 * @param function			function to which apply arguments
	 * @param defaultValue		default value to be returned
	 * @param <T1>				1st argument type
	 * @param <T2> 				2nd argument type
	 * @param <R>				return type
	 * @return					result of the function applying or default value
	 */
	public static <T1, T2, R> R applyIfNotNull(T1 obj1, T2 obj2, BiFunction<T1, T2, R> function, R defaultValue) {
		if (ObjectUtils.anyNull(obj1, obj1, function)) {
			return defaultValue;
		}

		return ObjectUtils.defaultIfNull(function.apply(obj1, obj2), null);
	}

	/**
	 * Apply arguments to the specified function if they're not null or return null.
	 *
	 * <p>Wraps {@link UwObject#applyIfNotNull(Object, Object, BiFunction, Object)}.
	 *
	 * @param obj1				1st argument to be applied
	 * @param obj2				2nd argument to be applied
	 * @param function			function to which apply arguments
	 * @param <T1>				1st argument type
	 * @param <T2> 				2nd argument type
	 * @param <R>				return type
	 * @return					result of the function applying or null
	 */
	public static <T1, T2, R> R applyIfNotNull(T1 obj1, T2 obj2, BiFunction<T1, T2, R> function) {
		return applyIfNotNull(obj1, obj2, function, (R) null);
	}

	/**
	 * Apply arguments to the specified function if they're not null or return default value.
	 *
	 * @param obj1				1st argument to be applied
	 * @param obj2				2nd argument to be applied
	 * @param obj3 				3rd argument to be applied
	 * @param function			function to which apply arguments
	 * @param defaultValue		default value to be returned
	 * @param <T1>				1st argument type
	 * @param <T2> 				2nd argument type
	 * @param <T3> 				3rd argument type
	 * @param <R>				return type
	 * @return					result of the function applying or default value
	 */
	public static <T1, T2, T3, R> R applyIfNotNull(T1 obj1, T2 obj2, T3 obj3, Function3<T1, T2, T3, R> function, R defaultValue) {
		if (ObjectUtils.anyNull(obj1, obj2, obj3, function)) {
			return defaultValue;
		}

		return ObjectUtils.defaultIfNull(function.apply(obj1, obj2, obj3), defaultValue);
	}

	/**
	 * Apply arguments to the specified function if they're not null or return null.
	 *
	 * <p>Wraps {@link UwObject#applyIfNotNull(Object, Object, Object, Function3, Object)}.
	 *
	 * @param obj1				1st argument to be applied
	 * @param obj2				2nd argument to be applied
	 * @param obj3 				3rd argument to be applied
	 * @param function			function to which apply arguments
	 * @param <T1>				1st argument type
	 * @param <T2> 				2nd argument type
	 * @param <T3> 				3rd argument type
	 * @param <R>				return type
	 * @return					result of the function applying or null
	 */
	public static <T1, T2, T3, R> R applyIfNotNull(T1 obj1, T2 obj2, T3 obj3, Function3<T1, T2, T3, R> function) {
		return applyIfNotNull(obj1, obj2, obj3, function, (R) null);
	}

	/**
	 * Apply arguments to the specified function if they're not null or return default value.
	 *
	 * @param obj1				1st argument to be applied
	 * @param obj2				2nd argument to be applied
	 * @param obj3 				3rd argument to be applied
	 * @param obj4 				4th argument to be applied
	 * @param function			function to which apply arguments
	 * @param defaultValue		default value to be returned
	 * @param <T1>				1st argument type
	 * @param <T2> 				2nd argument type
	 * @param <T3> 				3rd argument type
	 * @param <T4> 				4th argument type
	 * @param <R>				return type
	 * @return					result of the function applying or default value
	 */
	public static <T1, T2, T3, T4, R> R applyIfNotNull(T1 obj1, T2 obj2, T3 obj3, T4 obj4, Function4<T1, T2, T3, T4, R> function, R defaultValue) {
		if (ObjectUtils.anyNull(obj1, obj2, obj3, obj4, function)) {
			return defaultValue;
		}

		return ObjectUtils.defaultIfNull(function.apply(obj1, obj2, obj3, obj4), defaultValue);
	}

	/**
	 * Apply arguments to the specified function if they're not null or null.
	 *
	 * <p>Wraps {@link UwObject#applyIfNotNull(Object, Object, Object, Object, Function4, Object)}.
	 *
	 * @param obj1				1st argument to be applied
	 * @param obj2				2nd argument to be applied
	 * @param obj3 				3rd argument to be applied
	 * @param obj4 				4th argument to be applied
	 * @param function			function to which apply arguments
	 * @param <T1>				1st argument type
	 * @param <T2> 				2nd argument type
	 * @param <T3> 				3rd argument type
	 * @param <T4> 				4th argument type
	 * @param <R>				return type
	 * @return					result of the function applying or null
	 */
	public static <T1, T2, T3, T4, R> R applyIfNotNull(T1 obj1, T2 obj2, T3 obj3, T4 obj4, Function4<T1, T2, T3, T4, R> function) {
		return applyIfNotNull(obj1, obj2, obj3, obj4, function, (R) null);
	}

	/**
	 * Apply arguments to the specified function if they're not null or return default value.
	 *
	 * @param obj1				1st argument to be applied
	 * @param obj2				2nd argument to be applied
	 * @param obj3 				3rd argument to be applied
	 * @param obj4 				4th argument to be applied
	 * @param obj5 				5th argument to be applied
	 * @param function			function to which apply arguments
	 * @param defaultValue		default value to be returned
	 * @param <T1>				1st argument type
	 * @param <T2> 				2nd argument type
	 * @param <T3> 				3rd argument type
	 * @param <T4> 				4th argument type
	 * @param <T5> 				5th argument type
	 * @param <R>				return type
	 * @return					result of the function applying or default value
	 */
	public static <T1, T2, T3, T4, T5, R> R applyIfNotNull(T1 obj1, T2 obj2, T3 obj3, T4 obj4, T5 obj5, Function5<T1, T2, T3, T4, T5, R> function, R defaultValue) {
		if (ObjectUtils.anyNull(obj1, obj2, obj3, obj4, obj5, function)) {
			return defaultValue;
		}

		return ObjectUtils.defaultIfNull(function.apply(obj1, obj2, obj3, obj4, obj5), defaultValue);
	}

	/**
	 * Apply arguments to the specified function if they're not null or return null.
	 *
	 * <p>Wraps {@link UwObject#applyIfNotNull(Object, Object, Object, Object, Object, Function5, Object)}.
	 *
	 * @param obj1				1st argument to be applied
	 * @param obj2				2nd argument to be applied
	 * @param obj3 				3rd argument to be applied
	 * @param obj4 				4th argument to be applied
	 * @param obj5 				5th argument to be applied
	 * @param function			function to which apply arguments
	 * @param <T1>				1st argument type
	 * @param <T2> 				2nd argument type
	 * @param <T3> 				3rd argument type
	 * @param <T4> 				4th argument type
	 * @param <T5> 				5th argument type
	 * @param <R>				return type
	 * @return					result of the function applying or null
	 */
	public static <T1, T2, T3, T4, T5, R> R applyIfNotNull(T1 obj1, T2 obj2, T3 obj3, T4 obj4, T5 obj5, Function5<T1, T2, T3, T4, T5, R> function) {
		return applyIfNotNull(obj1, obj2, obj3, obj4, obj5, function, (R) null);
	}

	/**
	 * Apply arguments to the specified function if they're not null or return default value.
	 *
	 * @param obj1				1st argument to be applied
	 * @param obj2				2nd argument to be applied
	 * @param obj3 				3rd argument to be applied
	 * @param obj4 				4th argument to be applied
	 * @param obj5 				5th argument to be applied
	 * @param obj6 				6th argument to be applied
	 * @param function			function to which apply arguments
	 * @param defaultValue		default value to be returned
	 * @param <T1>				1st argument type
	 * @param <T2> 				2nd argument type
	 * @param <T3> 				3rd argument type
	 * @param <T4> 				4th argument type
	 * @param <T5> 				5th argument type
	 * @param <T6> 				6th argument type
	 * @param <R>				return type
	 * @return					result of the function applying or default value
	 */
	public static <T1, T2, T3, T4, T5, T6, R> R applyIfNotNull(T1 obj1, T2 obj2, T3 obj3, T4 obj4, T5 obj5, T6 obj6, Function6<T1, T2, T3, T4, T5, T6, R> function, R defaultValue) {
		if (ObjectUtils.anyNull(obj1, obj2, obj3, obj4, obj5, obj6, function)) {
			return defaultValue;
		}

		return ObjectUtils.defaultIfNull(function.apply(obj1, obj2, obj3, obj4, obj5, obj6), defaultValue);
	}

	/**
	 * Apply arguments to the specified function if they're not null or return null.
	 *
	 * <p>Wraps {@link UwObject#applyIfNotNull(Object, Object, Object, Object, Object, Object, Function6, Object)}.
	 *
	 * @param obj1				1st argument to be applied
	 * @param obj2				2nd argument to be applied
	 * @param obj3 				3rd argument to be applied
	 * @param obj4 				4th argument to be applied
	 * @param obj5 				5th argument to be applied
	 * @param obj6 				6th argument to be applied
	 * @param function			function to which apply arguments
	 * @param <T1>				1st argument type
	 * @param <T2> 				2nd argument type
	 * @param <T3> 				3rd argument type
	 * @param <T4> 				4th argument type
	 * @param <T5> 				5th argument type
	 * @param <T6> 				6th argument type
	 * @param <R>				return type
	 * @return					result of the function applying or null
	 */
	public static <T1, T2, T3, T4, T5, T6, R> R applyIfNotNull(T1 obj1, T2 obj2, T3 obj3, T4 obj4, T5 obj5, T6 obj6, Function6<T1, T2, T3, T4, T5, T6, R> function) {
		return applyIfNotNull(obj1, obj2, obj3, obj4, obj5, obj6, function, (R) null);
	}

	/**
	 * Apply arguments to the specified function if they're not null or return default value.
	 *
	 * @param obj1				1st argument to be applied
	 * @param obj2				2nd argument to be applied
	 * @param obj3 				3rd argument to be applied
	 * @param obj4 				4th argument to be applied
	 * @param obj5 				5th argument to be applied
	 * @param obj6 				6th argument to be applied
	 * @param obj7 				7th argument to be applied
	 * @param function			function to which apply arguments
	 * @param defaultValue		default value to be returned
	 * @param <T1>				1st argument type
	 * @param <T2> 				2nd argument type
	 * @param <T3> 				3rd argument type
	 * @param <T4> 				4th argument type
	 * @param <T5> 				5th argument type
	 * @param <T6> 				6th argument type
	 * @param <T7> 				7th argument type
	 * @param <R>				return type
	 * @return					result of the function applying or default value
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, R> R applyIfNotNull(T1 obj1, T2 obj2, T3 obj3, T4 obj4, T5 obj5, T6 obj6, T7 obj7, Function7<T1, T2, T3, T4, T5, T6, T7, R> function, R defaultValue) {
		if (ObjectUtils.anyNull(obj1, obj2, obj3, obj4, obj5, obj6, obj7, function)) {
			return defaultValue;
		}

		return ObjectUtils.defaultIfNull(function.apply(obj1, obj2, obj3, obj4, obj5, obj6, obj7), defaultValue);
	}

	/**
	 * Apply arguments to the specified function if they're not null or return null.
	 *
	 * <p>Wraps {@link UwObject#applyIfNotNull(Object, Object, Object, Object, Object, Object, Object, Function7, Object)}.
	 *
	 * @param obj1				1st argument to be applied
	 * @param obj2				2nd argument to be applied
	 * @param obj3 				3rd argument to be applied
	 * @param obj4 				4th argument to be applied
	 * @param obj5 				5th argument to be applied
	 * @param obj6 				6th argument to be applied
	 * @param obj7 				7th argument to be applied
	 * @param function			function to which apply arguments
	 * @param <T1>				1st argument type
	 * @param <T2> 				2nd argument type
	 * @param <T3> 				3rd argument type
	 * @param <T4> 				4th argument type
	 * @param <T5> 				5th argument type
	 * @param <T6> 				6th argument type
	 * @param <T7> 				7th argument type
	 * @param <R>				return type
	 * @return					result of the function applying or null
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, R> R applyIfNotNull(T1 obj1, T2 obj2, T3 obj3, T4 obj4, T5 obj5, T6 obj6, T7 obj7, Function7<T1, T2, T3, T4, T5, T6, T7, R> function) {
		return applyIfNotNull(obj1, obj2, obj3, obj4, obj5, obj6, obj7, function, (R) null);
	}

	/**
	 * Apply arguments to the specified function if they're not null or return default value.
	 *
	 * @param obj1				1st argument to be applied
	 * @param obj2				2nd argument to be applied
	 * @param obj3 				3rd argument to be applied
	 * @param obj4 				4th argument to be applied
	 * @param obj5 				5th argument to be applied
	 * @param obj6 				6th argument to be applied
	 * @param obj7 				7th argument to be applied
	 * @param obj8 				8th argument to be applied
	 * @param function			function to which apply arguments
	 * @param defaultValue		default value to be returned
	 * @param <T1>				1st argument type
	 * @param <T2> 				2nd argument type
	 * @param <T3> 				3rd argument type
	 * @param <T4> 				4th argument type
	 * @param <T5> 				5th argument type
	 * @param <T6> 				6th argument type
	 * @param <T7> 				7th argument type
	 * @param <T8> 				8th argument type
	 * @param <R>				return type
	 * @return					result of the function applying or default value
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, R> R applyIfNotNull(T1 obj1, T2 obj2, T3 obj3, T4 obj4, T5 obj5, T6 obj6, T7 obj7, T8 obj8, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> function, R defaultValue) {
		if (ObjectUtils.anyNull(obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8, function)) {
			return defaultValue;
		}

		return ObjectUtils.defaultIfNull(function.apply(obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8), defaultValue);
	}

	/**
	 * Apply arguments to the specified function if they're not null or return null.
	 *
	 * <p>Wraps {@link UwObject#applyIfNotNull(Object, Object, Object, Object, Object, Object, Object, Object, Function8, Object)}.
	 *
	 * @param obj1				1st argument to be applied
	 * @param obj2				2nd argument to be applied
	 * @param obj3 				3rd argument to be applied
	 * @param obj4 				4th argument to be applied
	 * @param obj5 				5th argument to be applied
	 * @param obj6 				6th argument to be applied
	 * @param obj7 				7th argument to be applied
	 * @param obj8 				8th argument to be applied
	 * @param function			function to which apply arguments
	 * @param <T1>				1st argument type
	 * @param <T2> 				2nd argument type
	 * @param <T3> 				3rd argument type
	 * @param <T4> 				4th argument type
	 * @param <T5> 				5th argument type
	 * @param <T6> 				6th argument type
	 * @param <T7> 				7th argument type
	 * @param <T8> 				8th argument type
	 * @param <R>				return type
	 * @return					result of the function applying or null
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, R> R applyIfNotNull(T1 obj1, T2 obj2, T3 obj3, T4 obj4, T5 obj5, T6 obj6, T7 obj7, T8 obj8, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> function) {
		return applyIfNotNull(obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8, function, null);
	}

	private UwObject() {
		throw new UnsupportedOperationException();
	}
}
