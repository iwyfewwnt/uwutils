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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Supplier;

/**
 * A reflection utility.
 *
 * <p>{@code UwReflect} is the utility class
 * that provide functionality to operate with
 * Java Reflection API.
 */
@SuppressWarnings("unused")
public final class UwReflect {

	/**
	 * Safely cast an array of objects to an array of their types.
	 *
	 * @param objects	array of objects to cast
	 * @return			array of the object types that wrapped in {@link Option}
	 */
	public static Option<Class<?>[]> toClassArray(Object[] objects) {
		return Option.of(toClassArrayOrNull(objects));
	}

	/**
	 * Safely cast an array of objects to an array of their type or return a default value.
	 *
	 * @param objects			array of objects to cast
	 * @param defaultValue 		default value to return on failure
	 * @return					array of the object types or the default value
	 */
	public static Class<?>[] toClassArrayOrElse(Object[] objects, Class<?>[] defaultValue) {
		if (objects == null) {
			return defaultValue;
		}

		Class<?>[] types = new Class<?>[objects.length];

		for (int i = 0; i < types.length; i++) {
			types[i] = objects[i].getClass();
		}

		return types;
	}

	/**
	 * Safely cast an array of objects to an array of their type or return a default value.
	 *
	 * @param objects		array of objects to cast
	 * @param supplier		supplier from which get the default value
	 * @return				array of the object types or the default value
	 */
	public static Class<?>[] toClassArrayOrElse(Object[] objects, Supplier<Class<?>[]> supplier) {
		return UwObject.getIfNull(toClassArrayOrNull(objects), supplier);
	}

	/**
	 * Safely cast an array of objects to an array of their type or return an empty array.
	 *
	 * <p>Wraps {@link UwReflect#toClassArrayOrElse(Object[], Class[])}
	 * w/ {@link UwArray#CLASS_EMPTY} as the default value.
	 *
	 * @param objects		array of objects to cast
	 * @return				array of the object types or the empty array
	 */
	public static Class<?>[] toClassArrayOrEmpty(Object[] objects) {
		return toClassArrayOrElse(objects, UwArray.CLASS_EMPTY);
	}

	/**
	 * Safely cast an array of objects to an array of their type or return {@code null}.
	 *
	 * <p>Wraps {@link UwReflect#toClassArrayOrElse(Object[], Class[])}
	 * w/ {@code null} as the default value.
	 *
	 * @param objects		array of objects to cast
	 * @return				array of the object types or {@code null}
	 */
	public static Class<?>[] toClassArrayOrNull(Object[] objects) {
		return toClassArrayOrElse(objects, (Class<?>[]) null);
	}

	/**
	 * Safely cast an array of types to an array of classes.
	 *
	 * @param types		array of types to cast
	 * @return			array of classes that wrapped in {@link Option}
	 */
	public static Option<Class<?>[]> toClassArray(Type[] types) {
		return Option.of(toClassArrayOrNull(types));
	}

	/**
	 * Safely cast an array of types to an array of classes or return a default value.
	 *
	 * @param types				array of types to cast
	 * @param defaultValue		default value to return on failure
	 * @return					array of classes or the default value
	 */
	public static Class<?>[] toClassArrayOrElse(Type[] types, Class<?>[] defaultValue) {
		if (types == null) {
			return defaultValue;
		}

		try {
			Class<?>[] classes = new Class<?>[types.length];

			//noinspection SuspiciousSystemArraycopy
			System.arraycopy(types, 0, classes, 0, classes.length);

			return classes;
		} catch (IndexOutOfBoundsException | ArrayStoreException
				| NullPointerException e) {
			e.printStackTrace();
		}

		return defaultValue;
	}

	/**
	 * Safely cast an array of types to an array of classes or return a default value.
	 *
	 * @param types			array of types to cast
	 * @param supplier		supplier from which get the default value
	 * @return				array of classes or the default value
	 */
	public static Class<?>[] toClassArrayOrElse(Type[] types, Supplier<Class<?>[]> supplier) {
		return UwObject.getIfNull(toClassArrayOrNull(types), supplier);
	}

	/**
	 * Safely cast an array of types to an array of classes or return an empty array.
	 *
	 * <p>Wraps {@link UwReflect#toClassArrayOrElse(Type[], Class[])}
	 * w/ {@link UwArray#CLASS_EMPTY} as the default value.
	 *
	 * @param types		array of types to cast
	 * @return			array of classes or the empty array
	 */
	public static Class<?>[] toClassArrayOrEmpty(Type[] types) {
		return toClassArrayOrElse(types, UwArray.CLASS_EMPTY);
	}

	/**
	 * Safely cast an array of types to an array of classes or return {@code null}.
	 *
	 * <p>Wraps {@link UwReflect#toClassArrayOrElse(Type[], Class[])}
	 * w/ {@code null} as the default value.
	 *
	 * @param types		array of types to cast
	 * @return			array of classes or {@code null}
	 */
	public static Class<?>[] toClassArrayOrNull(Type[] types) {
		return toClassArrayOrElse(types, (Class<?>[]) null);
	}

	/**
	 * Safely get generic types of the provided parameterized type.
	 *
	 * @param type		parameterized type from which get the generic types
	 * @return			array of generic types that wrapped in {@link Option}
	 */
	public static Option<Class<?>[]> getGenericTypes(ParameterizedType type) {
		return Option.of(getGenericTypesOrNull(type));
	}

	/**
	 * Safely get generic types of the provided type.
	 *
	 * @param type		type from which get the generic types
	 * @return			array of generic types that wrapped in {@link Option}
	 */
	public static Option<Class<?>[]> getGenericTypes(Type type) {
		return Option.of(getGenericTypesOrNull(type));
	}

	/**
	 * Safely get generic types of the provided class.
	 *
	 * @param clazz		class from which get the generic types
	 * @return			array of generic types that wrapped in {@link Option}
	 */
	public static Option<Class<?>[]> getGenericTypes(Class<?> clazz) {
		return Option.of(getGenericTypesOrNull(clazz));
	}

	/**
	 * Safely get generic types of the provided parameterized type or return a default value.
	 *
	 * @param type				parameterized type from which get the generic types
	 * @param defaultValue 		default value to return on failure
	 * @return					array of generic types or the default value
	 */
	public static Class<?>[] getGenericTypesOrElse(ParameterizedType type, Class<?>[] defaultValue) {
		if (type == null) {
			return defaultValue;
		}

		return toClassArrayOrElse(type.getActualTypeArguments(), defaultValue);
	}

	/**
	 * Safely get generic types of the provided type or return a default value.
	 *
	 * @param type		type from which get the generic types
	 * @return			array of generic types or the default value
	 */
	public static Class<?>[] getGenericTypesOrElse(Type type, Class<?>[] defaultValue) {
		if (type instanceof ParameterizedType) {
			return getGenericTypesOrElse((ParameterizedType) type, defaultValue);
		}

		return defaultValue;
	}

	/**
	 * Safely get generic types of the provided class or return a default value.
	 *
	 * @param clazz		class from which get the generic types
	 * @return			array of generic types or the default value
	 */
	public static Class<?>[] getGenericTypesOrElse(Class<?> clazz, Class<?>[] defaultValue) {
		if (clazz == null) {
			return defaultValue;
		}

		return getGenericTypesOrElse(clazz.getGenericSuperclass(), defaultValue);
	}


	/**
	 * Safely get generic types of the provided parameterized type or return a default value.
	 *
	 * @param type		parameterized type from which get the generic types
	 * @param supplier 	supplier from which get the default value
	 * @return			array of generic types or the default value
	 */
	public static Class<?>[] getGenericTypesOrElse(ParameterizedType type, Supplier<Class<?>[]> supplier) {
		return UwObject.getIfNull(getGenericTypesOrNull(type), supplier);
	}

	/**
	 * Safely get generic types of the provided type or return a default value.
	 *
	 * @param type		type from which get the generic types
	 * @param supplier 	supplier from which get the default value
	 * @return			array of generic types or the default value
	 */
	public static Class<?>[] getGenericTypesOrElse(Type type, Supplier<Class<?>[]> supplier) {
		return UwObject.getIfNull(getGenericTypesOrNull(type), supplier);
	}

	/**
	 * Safely get generic types of the provided class or return a default value.
	 *
	 * @param clazz		class from which get the generic types
	 * @param supplier 	supplier from which get the default value
	 * @return			array of generic types or the default value
	 */
	public static Class<?>[] getGenericTypesOrElse(Class<?> clazz, Supplier<Class<?>[]> supplier) {
		return UwObject.getIfNull(getGenericTypesOrNull(clazz), supplier);
	}

	/**
	 * Safely get generic types of the provided parameterized type or return {@code null}.
	 *
	 * <p>Wraps {@link UwReflect#getGenericTypesOrElse(ParameterizedType, Class[])}
	 * w/ {@code null} as the default value.
	 *
	 * @param type		parameterized type from which get the generic types
	 * @return			array of generic types or {@code null}
	 */
	public static Class<?>[] getGenericTypesOrNull(ParameterizedType type) {
		return getGenericTypesOrElse(type, (Class<?>[]) null);
	}

	/**
	 * Safely get generic types of the provided type or return {@code null}.
	 *
	 * <p>Wraps {@link UwReflect#getGenericTypesOrElse(Type, Class[])}
	 * w/ {@code null} as the default value.
	 *
	 * @param type		type from which get the generic types
	 * @return			array of generic types or {@code null}
	 */
	public static Class<?>[] getGenericTypesOrNull(Type type) {
		return getGenericTypesOrElse(type, (Class<?>[]) null);
	}

	/**
	 * Safely get generic types of the provided class or return {@code null}.
	 *
	 * <p>Wraps {@link UwReflect#getGenericTypesOrElse(Class, Class[])}
	 * w/ {@code null} as the default value.
	 *
	 * @param clazz		class from which get the generic types
	 * @return			array of generic types or {@code null}
	 */
	public static Class<?>[] getGenericTypesOrNull(Class<?> clazz) {
		return getGenericTypesOrElse(clazz, (Class<?>[]) null);
	}

	/**
	 * Safely get generic type of the provided parameterized type by its index.
	 *
	 * @param type		parameterized type from which get the generic type
	 * @param index		index of the generic type
	 * @return			generic type that wrapped in {@link Option}
	 */
	public static Option<Class<?>> getGenericType(ParameterizedType type, Integer index) {
		return Option.of(getGenericTypeOrNull(type, index));
	}

	/**
	 * Safely get generic type of the provided type by its index.
	 *
	 * @param type		type from which get the generic type
	 * @param index		index of the generic type
	 * @return			generic type that wrapped in {@link Option}
	 */
	public static Option<Class<?>> getGenericType(Type type, Integer index) {
		return Option.of(getGenericTypeOrNull(type, index));
	}

	/**
	 * Safely get generic type of the provided class by its index.
	 *
	 * @param clazz		class from which get the generic type
	 * @param index		index of the generic type
	 * @return			generic type that wrapped in {@link Option}
	 */
	public static Option<Class<?>> getGenericType(Class<?> clazz, Integer index) {
		return Option.of(getGenericTypeOrNull(clazz, index));
	}

	/**
	 * Safely get generic type of the provided parameterized type by its index or return a default value.
	 *
	 * @param type				parameterized type from which get the generic type
	 * @param index				index of the generic type
	 * @param defaultValue		default value to return on failure
	 * @return					generic type or the default value
	 */
	public static Class<?> getGenericTypeOrElse(ParameterizedType type, Integer index, Class<?> defaultValue) {
		return UwArray.getOrElse(index, getGenericTypesOrNull(type), defaultValue);
	}

	/**
	 * Safely get generic type of the provided type by its index or return a default value.
	 *
	 * @param type		type from which get the generic type
	 * @param index		index of the generic type
	 * @return			generic type or the default value
	 */
	public static Class<?> getGenericTypeOrElse(Type type, Integer index, Class<?> defaultValue) {
		return UwArray.getOrElse(index, getGenericTypesOrNull(type), defaultValue);
	}

	/**
	 * Safely get generic type of the provided class by its index or return a default value.
	 *
	 * @param clazz		class from which get the generic type
	 * @param index		index of the generic type
	 * @return			generic type or the default value
	 */
	public static Class<?> getGenericTypeOrElse(Class<?> clazz, Integer index, Class<?> defaultValue) {
		return UwArray.getOrElse(index, getGenericTypesOrNull(clazz), defaultValue);
	}

	/**
	 * Safely get generic type of the provided parameterized type by its index or return a default value.
	 *
	 * @param type			parameterized type from which get the generic type
	 * @param index			index of the generic type
	 * @param supplier		supplier from which get the default value
	 * @return				generic type or the default value
	 */
	public static Class<?> getGenericTypeOrElse(ParameterizedType type, Integer index, Supplier<Class<?>> supplier) {
		return UwObject.getIfNull(getGenericTypeOrNull(type, index), supplier);
	}

	/**
	 * Safely get generic type of the provided type by its index or return a default value.
	 *
	 * @param type			type from which get the generic type
	 * @param index			index of the generic type
	 * @param supplier		supplier from which get the default value
	 * @return				generic type or the default value
	 */
	public static Class<?> getGenericTypeOrElse(Type type, Integer index, Supplier<Class<?>> supplier) {
		return UwObject.getIfNull(getGenericTypeOrNull(type, index), supplier);
	}

	/**
	 * Safely get generic type of the provided class by its index or return a default value.
	 *
	 * @param clazz			class from which get the generic type
	 * @param index			index of the generic type
	 * @param supplier		supplier from which get the default value
	 * @return				generic type or the default value
	 */
	public static Class<?> getGenericTypeOrElse(Class<?> clazz, Integer index, Supplier<Class<?>> supplier) {
		return UwObject.getIfNull(getGenericTypeOrNull(clazz, index), supplier);
	}

	/**
	 * Safely get generic type of the provided parameterized type by its index or return {@code null}.
	 *
	 * <p>Wraps {@link UwReflect#getGenericTypeOrElse(ParameterizedType, Integer, Class)}
	 * w/ {@code null} as the default value.
	 *
	 * @param type		parameterized type from which get the generic type
	 * @param index		index of the generic type
	 * @return			generic type or {@code null}
	 */
	public static Class<?> getGenericTypeOrNull(ParameterizedType type, Integer index) {
		return getGenericTypeOrElse(type, index, (Class<?>) null);
	}

	/**
	 * Safely get generic type of the provided type by its index or return {@code null}.
	 *
	 * <p>Wraps {@link UwReflect#getGenericTypeOrElse(Type, Integer, Class)}
	 * w/ {@code null} as the default value.
	 *
	 * @param type		type from which get the generic type
	 * @param index		index of the generic type
	 * @return			generic type or {@code null}
	 */
	public static Class<?> getGenericTypeOrNull(Type type, Integer index) {
		return getGenericTypeOrElse(type, index, (Class<?>) null);
	}

	/**
	 * Safely get generic type of the provided class by its index or return {@code null}.
	 *
	 * <p>Wraps {@link UwReflect#getGenericTypeOrElse(Class, Integer, Class)}
	 * w/ {@code null} as the default value.
	 *
	 * @param clazz		class from which get the generic type
	 * @param index		index of the generic type
	 * @return			generic type or {@code null}
	 */
	public static Class<?> getGenericTypeOrNull(Class<?> clazz, Integer index) {
		return getGenericTypeOrElse(clazz, index, (Class<?>) null);
	}

	/**
	 * Safely get 1st generic type of the provided parameterized type.
	 *
	 * <p>Wraps {@link UwReflect#getGenericTypeOrNull(ParameterizedType, Integer)}
	 * w/ {@code 0} as the index of the generic type.
	 *
	 * @param type		parameterized type from which get generic type
	 * @return			generic type that wrapped in {@link Option}
	 */
	public static Option<Class<?>> getGenericType(ParameterizedType type) {
		return Option.of(getGenericTypeOrNull(type));
	}

	/**
	 * Safely get 1st generic type of the provided type.
	 *
	 * <p>Wraps {@link UwReflect#getGenericTypeOrNull(Type, Integer)}
	 * w/ {@code 0} as the index of the generic type.
	 *
	 * @param type		type from which get the generic type
	 * @return			generic type that wrapped in {@link Option}
	 */
	public static Option<Class<?>> getGenericType(Type type) {
		return Option.of(getGenericTypeOrNull(type));
	}

	/**
	 * Safely get 1st generic type of the provided class.
	 *
	 * <p>Wraps {@link UwReflect#getGenericTypeOrNull(Class, Integer)}
	 * w/ {@code 0} as the index of the generic type.
	 *
	 * @param clazz		class from which get the generic type
	 * @return			generic type that wrapped in {@link Option}
	 */
	public static Option<Class<?>> getGenericType(Class<?> clazz) {
		return Option.of(getGenericTypeOrNull(clazz));
	}

	/**
	 * Safely get 1st generic type of the provided parameterized type or return a default value.
	 *
	 * <p>Wraps {@link UwReflect#getGenericTypeOrElse(ParameterizedType, Integer, Class)}
	 * w/ {@code 0} as the index of the generic type.
	 *
	 * @param type				parameterized type from which get the generic type
	 * @param defaultValue		default value to return on failure
	 * @return					generic type or the default value
	 */
	public static Class<?> getGenericTypeOrElse(ParameterizedType type, Class<?> defaultValue) {
		return getGenericTypeOrElse(type, 0, defaultValue);
	}

	/**
	 * Safely get 1st generic type of the provided type or return a default value.
	 *
	 * <p>Wraps {@link UwReflect#getGenericTypeOrElse(Type, Integer, Class)}
	 * w/ {@code 0} as the index of the generic type.
	 *
	 * @param type				type from which get the generic type
	 * @param defaultValue 		default value to return on failure
	 * @return					generic type or the default value
	 */
	public static Class<?> getGenericTypeOrElse(Type type, Class<?> defaultValue) {
		return getGenericTypeOrElse(type, 0, defaultValue);
	}

	/**
	 * Safely get 1st generic type of the provided class or return a default value.
	 *
	 * <p>Wraps {@link UwReflect#getGenericTypeOrElse(Class, Integer, Class)}
	 * w/ {@code 0} as the index of the generic type.
	 *
	 * @param clazz				class from which get the generic type
	 * @param defaultValue 		default value to return on failure
	 * @return					generic type or the default value
	 */
	public static Class<?> getGenericTypeOrElse(Class<?> clazz, Class<?> defaultValue) {
		return getGenericTypeOrElse(clazz, 0, defaultValue);
	}

	/**
	 * Safely get 1st generic type of the provided parameterized type or return a default value.
	 *
	 * <p>Wraps {@link UwReflect#getGenericTypeOrElse(ParameterizedType, Integer, Supplier)}
	 * w/ {@code 0} as the index of the generic type.
	 *
	 * @param type			parameterized type from which get the generic type
	 * @param supplier		supplier from which get the default value
	 * @return				generic type or the default value
	 */
	public static Class<?> getGenericTypeOrElse(ParameterizedType type, Supplier<Class<?>> supplier) {
		return getGenericTypeOrElse(type, 0, supplier);
	}

	/**
	 * Safely get 1st generic type of the provided type or return a default value.
	 *
	 * <p>Wraps {@link UwReflect#getGenericTypeOrElse(Type, Integer, Supplier)}
	 * w/ {@code 0} as the index of the generic type.
	 *
	 * @param type			type from which get the generic type
	 * @param supplier		supplier from which get the default value
	 * @return				generic type or the default value
	 */
	public static Class<?> getGenericTypeOrElse(Type type, Supplier<Class<?>> supplier) {
		return getGenericTypeOrElse(type, 0, supplier);
	}

	/**
	 * Safely get 1st generic type of the provided class or return a default value.
	 *
	 * <p>Wraps {@link UwReflect#getGenericTypeOrElse(Class, Integer, Supplier)}
	 * w/ {@code 0} as the index of the generic type.
	 *
	 * @param clazz			class from which get the generic type
	 * @param supplier		supplier from which get the default value
	 * @return				generic type or the default value
	 */
	public static Class<?> getGenericTypeOrElse(Class<?> clazz, Supplier<Class<?>> supplier) {
		return getGenericTypeOrElse(clazz, 0, supplier);
	}

	/**
	 * Safely get 1st generic type of the provided parameterized type or return {@code null}.
	 *
	 * <p>Wraps {@link UwReflect#getGenericTypeOrElse(ParameterizedType, Class)}
	 * w/ {@code null} as the default value.
	 *
	 * @param type		parameterized type from which get the generic type
	 * @return			generic type or {@code null}
	 */
	public static Class<?> getGenericTypeOrNull(ParameterizedType type) {
		return getGenericTypeOrElse(type, (Class<?>) null);
	}

	/**
	 * Safely get 1st generic type of the provided type or return {@code null}.
	 *
	 * <p>Wraps {@link UwReflect#getGenericTypeOrElse(Type, Class)}
	 * w/ {@code null} as the default value.
	 *
	 * @param type		type from which get the generic type
	 * @return			generic type or {@code null}
	 */
	public static Class<?> getGenericTypeOrNull(Type type) {
		return getGenericTypeOrElse(type, (Class<?>) null);
	}

	/**
	 * Safely get 1st generic type of the provided class or return {@code null}.
	 *
	 * <p>Wraps {@link UwReflect#getGenericTypeOrElse(Class, Class)}
	 * w/ {@code null} as the default value.
	 *
	 * @param clazz		class from which get the generic type
	 * @return			generic type or {@code null}
	 */
	public static Class<?> getGenericTypeOrNull(Class<?> clazz) {
		return getGenericTypeOrElse(clazz, (Class<?>) null);
	}

	/**
	 * Safely get constructor of the provided class.
	 *
	 * @param clazz		class from which get the constructor
	 * @param types		array of constructor argument types
	 * @param <T>		object type
	 * @return			constructor that wrapped in {@link Option}
	 */
	public static <T> Option<Constructor<T>> getConstructor(Class<T> clazz, Class<?>[] types) {
		return Option.of(getConstructorOrNull(clazz, types));
	}

	/**
	 * Safely get default constructor of the provided class.
	 *
	 * @param clazz		class from which get the constructor
	 * @param <T>		object type
	 * @return			constructor that wrapped in {@link Option}
	 */
	public static <T> Option<Constructor<T>> getConstructor(Class<T> clazz) {
		return Option.of(getConstructorOrNull(clazz));
	}

	/**
	 * Safely get constructor of the provided class or return a default value.
	 *
	 * @param clazz				class from which get the constructor
	 * @param types				array of constructor argument types
	 * @param defaultValue		default value to return on failure
	 * @param <T>				object type
	 * @return					constructor or the default value
	 */
	public static <T> Constructor<T> getConstructorOrElse(Class<T> clazz, Class<?>[] types, Constructor<T> defaultValue) {
		if (clazz == null) {
			return defaultValue;
		}

		types = UwObject.getIfNull(types, UwArray.CLASS_EMPTY);

		try {
			return clazz.getConstructor(types);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}

		return defaultValue;
	}

	/**
	 * Safely get default constructor of the provided class or return a default value.
	 *
	 * <p>Wraps {@link UwReflect#getConstructorOrElse(Class, Class[], Constructor)}
	 * w/ {@code null} as an array of the constructor argument types.
	 *
	 * @param clazz				class from which get the constructor
	 * @param defaultValue		default value to return on failure
	 * @param <T>				object type
	 * @return					constructor or the default value
	 */
	public static <T> Constructor<T> getConstructorOrElse(Class<T> clazz, Constructor<T> defaultValue) {
		return getConstructorOrElse(clazz, null, defaultValue);
	}

	/**
	 * Safely get constructor of the provided class or return a default value.
	 *
	 * @param clazz			class from which get the constructor
	 * @param supplier		supplier from which get the default value
	 * @param <T>			object type
	 * @return				constructor or the default value
	 */
	public static <T> Constructor<T> getConstructorOrElse(Class<T> clazz, Class<?>[] types, Supplier<Constructor<T>> supplier) {
		return UwObject.getIfNull(getConstructorOrNull(clazz, types), supplier);
	}

	/**
	 * Safely get default constructor of the provided class or return a default value.
	 *
	 * @param clazz			class from which get the constructor
	 * @param supplier		supplier from which get the default value
	 * @param <T>			object type
	 * @return				constructor or the default value
	 */
	public static <T> Constructor<T> getConstructorOrElse(Class<T> clazz, Supplier<Constructor<T>> supplier) {
		return UwObject.getIfNull(getConstructorOrNull(clazz), supplier);
	}

	/**
	 * Safely get constructor of the provided class or return {@code null}.
	 *
	 * <p>Wraps {@link UwReflect#getConstructorOrElse(Class, Class[], Constructor)}
	 * w/ {@code null} as the default value.
	 *
	 * @param clazz		class from which get the constructor
	 * @param types		array of constructor argument types
	 * @param <T>		object type
	 * @return			constructor or {@code null}
	 */
	public static <T> Constructor<T> getConstructorOrNull(Class<T> clazz, Class<?>[] types) {
		return getConstructorOrElse(clazz, types, (Constructor<T>) null);
	}

	/**
	 * Safely get default constructor of the provided class or return {@code null}.
	 *
	 * <p>Wraps {@link UwReflect#getConstructorOrElse(Class, Constructor)}
	 * w/ {@code null} as the default value.
	 *
	 * @param clazz		class from which get the constructor
	 * @param <T>		object type
	 * @return			constructor or {@code null}
	 */
	public static <T> Constructor<T> getConstructorOrNull(Class<T> clazz) {
		return getConstructorOrElse(clazz, (Constructor<T>) null);
	}

	/**
	 * Safely create a new instance of the provided class.
	 *
	 * @param clazz			class from which create the new instance
	 * @param arguments		array of constructor arguments
	 * @param <T>			object type
	 * @return				new instance that wrapped in {@link Option}
	 */
	public static <T> Option<T> newInstance(Class<T> clazz, Object[] arguments) {
		return Option.of(newInstanceOrNull(clazz, arguments));
	}

	/**
	 * Safely create a new instance of the provided class by its default constructor.
	 *
	 * @param clazz			class from which create the new instance
	 * @param <T>			object type
	 * @return				new instance that wrapped in {@link Option}
	 */
	public static <T> Option<T> newInstance(Class<T> clazz) {
		return Option.of(newInstanceOrNull(clazz));
	}

	/**
	 * Safely create a new instance of the provided class or return a default value.
	 *
	 * @param clazz				class from which create the new instance
	 * @param arguments			array of constructor arguments
	 * @param defaultValue		default value to return on failure
	 * @param <T>				object type
	 * @return					new instance or the default value
	 */
	public static <T> T newInstanceOrElse(Class<T> clazz, Object[] arguments, T defaultValue) {
		if (clazz == null) {
			return defaultValue;
		}

		Constructor<T> constructor = getConstructorOrNull(clazz, toClassArrayOrNull(arguments));

		if (constructor == null) {
			return defaultValue;
		}

		try {
			return constructor.newInstance(arguments);
		} catch (IllegalAccessException | IllegalArgumentException
				| InstantiationException | InvocationTargetException
				| ExceptionInInitializerError e) {
			e.printStackTrace();
		}

		return defaultValue;
	}

	/**
	 * Safely create a new instance of the provided class or return a default value.
	 *
	 * <p>Wraps {@link UwReflect#newInstanceOrElse(Class, Object[], Object)}
	 * w/ {@code null} as the array of constructor arguments.
	 *
	 * @param clazz				class from which create the new instance
	 * @param defaultValue		default value to return on failure
	 * @param <T>				object type
	 * @return					new instance or the default value
	 */
	public static <T> T newInstanceOrElse(Class<T> clazz, T defaultValue) {
		return newInstanceOrElse(clazz, null, defaultValue);
	}

	/**
	 * Safely create a new instance of the provided class or return a default value.
	 *
	 * @param clazz			class from which create the new instance
	 * @param arguments		array of constructor arguments
	 * @param supplier		supplier from which get the default value
	 * @param <T>			object type
	 * @return				new instance or the default value
	 */
	public static <T> T newInstanceOrElse(Class<T> clazz, Object[] arguments, Supplier<T> supplier) {
		return UwObject.getIfNull(newInstanceOrNull(clazz, arguments), supplier);
	}

	/**
	 * Safely create a new instance of the provided class or return a default value.
	 *
	 * @param clazz			class from which create the new instance
	 * @param supplier		supplier from which get the default value
	 * @param <T>			object type
	 * @return				new instance or the default value
	 */
	public static <T> T newInstanceOrElse(Class<T> clazz, Supplier<T> supplier) {
		return UwObject.getIfNull(newInstanceOrNull(clazz), supplier);
	}

	/**
	 * Safely create a new instance of the provided class or return {@code null}.
	 *
	 * <p>Wraps {@link UwReflect#newInstanceOrElse(Class, Object[], Object)}
	 * w/ {@code null} as the default value.
	 *
	 * @param clazz			class from which create the new instance
	 * @param arguments		array of constructor arguments
	 * @param <T>			object type
	 * @return				new instance or {@code null}
	 */
	public static <T> T newInstanceOrNull(Class<T> clazz, Object[] arguments) {
		return newInstanceOrElse(clazz, arguments, (T) null);
	}

	/**
	 * Safely create a new instance of the provided class by its default constructor or return {@code null}.
	 *
	 * <p>Wraps {@link UwReflect#newInstanceOrNull(Class, Object[])}
	 * w/ {@code null} as the array of constructor arguments.
	 *
	 * @param clazz			class from which create the new instance
	 * @param <T>			object type
	 * @return				new instance or {@code null}
	 */
	public static <T> T newInstanceOrNull(Class<T> clazz) {
		return newInstanceOrNull(clazz, null);
	}

	private UwReflect() {
		throw new UnsupportedOperationException();
	}
}
