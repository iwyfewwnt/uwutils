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

import io.vavr.control.Option;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Stream;

/**
 * A reflection utility.
 *
 * <p>{@code UwReflect} is the utility class
 * that provide functionality to operate with
 * Java Reflection API.
 *
 * @since 0.1.3
 */
@SuppressWarnings("unused")
public final class UwReflect {

	/**
	 * Get generic types of the provided parameterized type.
	 *
	 * @param type		parameterized type
	 * @return			array of generic types that wrapped in {@link Option}
	 */
	public static Option<Class<?>[]> getGenericTypes(ParameterizedType type) {
		if (type == null) {
			return Option.none();
		}

		Type[] types = type.getActualTypeArguments();

		if (types.length == 0) {
			return Option.none();
		}

		Class<?>[] classes = new Class<?>[types.length];

		//noinspection SuspiciousSystemArraycopy
		System.arraycopy(types, 0, classes, 0, classes.length);

		return Option.some(classes);
	}

	/**
	 * Get generic types of the provided type.
	 *
	 * @param type		type
	 * @return			array of generic types that wrapped in {@link Option}
	 */
	public static Option<Class<?>[]> getGenericTypes(Type type) {
		if (type == null || !TypeUtils.isAssignable(type, ParameterizedType.class)) {
			return Option.none();
		}

		return getGenericTypes((ParameterizedType) type);
	}

	/**
	 * Get generic types of the provided class.
	 *
	 * @param clazz		class
	 * @return			array of generic types that wrapped in {@link Option}
	 */
	public static Option<Class<?>[]> getGenericTypes(Class<?> clazz) {
		if (clazz == null) {
			return Option.none();
		}

		return getGenericTypes(clazz.getGenericSuperclass());
	}

	/**
	 * Get generic types of the provided parameterized type.
	 *
	 * @param type		parameterized type
	 * @return			array of generic types or null
	 */
	public static Class<?>[] getGenericTypesRaw(ParameterizedType type) {
		return getGenericTypes(type).getOrNull();
	}

	/**
	 * Get generic types of the provided type.
	 *
	 * @param type		parameterized type
	 * @return			array of generic types or null
	 */
	public static Class<?>[] getGenericTypesRaw(Type type) {
		return getGenericTypes(type).getOrNull();
	}

	/**
	 * Get generic types of the provided class.
	 *
	 * @param clazz		class
	 * @return			array of generic types or null
	 */
	public static Class<?>[] getGenericTypesRaw(Class<?> clazz) {
		return getGenericTypes(clazz).getOrNull();
	}

	/**
	 * Get generic type of the provided parameterized type by its index.
	 *
	 * @param type		parameterized type
	 * @param index		index
	 * @return			generic type that wrapped in {@link Option}
	 */
	public static Option<Class<?>> getGenericType(ParameterizedType type, Integer index) {
		return getGenericTypes(type)
				.flatMap(types -> UwArray.get(index, types));
	}

	/**
	 * Get generic type of the provided type by its index.
	 *
	 * @param type		type
	 * @param index		index
	 * @return			generic type that wrapped in {@link Option}
	 */
	public static Option<Class<?>> getGenericType(Type type, Integer index) {
		return getGenericTypes(type)
				.flatMap(types -> UwArray.get(index, types));
	}

	/**
	 * Get generic type of the provided class by its index.
	 *
	 * @param clazz		class
	 * @param index		index
	 * @return			generic type that wrapped in {@link Option}
	 */
	public static Option<Class<?>> getGenericType(Class<?> clazz, Integer index) {
		return getGenericTypes(clazz)
				.flatMap(types -> UwArray.get(index, types));
	}

	/**
	 * Get 1st generic type of the provided parameterized type.
	 *
	 * <p>Wraps {@link UwReflect#getGenericType(ParameterizedType, Integer)}.
	 *
	 * @param type		parameterized type
	 * @return			generic type that wrapped in {@link Option}
	 */
	public static Option<Class<?>> getGenericType(ParameterizedType type) {
		return getGenericType(type, 0);
	}

	/**
	 * Get 1st generic type of the provided type.
	 *
	 * <p>Wraps {@link UwReflect#getGenericType(Type, Integer)}.
	 *
	 * @param type		type
	 * @return			generic type that wrapped in {@link Option}
	 */
	public static Option<Class<?>> getGenericType(Type type) {
		return getGenericType(type, 0);
	}

	/**
	 * Get 1st generic type of the provided class.
	 *
	 * <p>Wraps {@link UwReflect#getGenericType(Class, Integer)}.
	 *
	 * @param clazz		class
	 * @return			generic type that wrapped in {@link Option}
	 */
	public static Option<Class<?>> getGenericType(Class<?> clazz) {
		return getGenericType(clazz, 0);
	}

	/**
	 * Get generic type of the provided parameterized type by its index.
	 *
	 * @param type		parameterized type
	 * @param index		index
	 * @return			generic type or null
	 */
	public static Class<?> getGenericTypeRaw(ParameterizedType type, Integer index) {
		return getGenericType(type, index).getOrNull();
	}

	/**
	 * Get generic type of the provided type by its index.
	 *
	 * @param type		type
	 * @param index		index
	 * @return			generic type or null
	 */
	public static Class<?> getGenericTypeRaw(Type type, Integer index) {
		return getGenericType(type, index).getOrNull();
	}

	/**
	 * Get generic type of the provided class by its index.
	 *
	 * @param clazz		class
	 * @param index		index
	 * @return			generic type or null
	 */
	public static Class<?> getGenericTypeRaw(Class<?> clazz, Integer index) {
		return getGenericType(clazz, index).getOrNull();
	}

	/**
	 * Get 1st generic type of the provided parameterized type.
	 *
	 * @param type		parameterized type
	 * @return			generic type or null
	 */
	public static Class<?> getGenericTypeRaw(ParameterizedType type) {
		return getGenericType(type).getOrNull();
	}

	/**
	 * Get 1st generic type of the provided type.
	 *
	 * @param type		type
	 * @return			generic type or null
	 */
	public static Class<?> getGenericTypeRaw(Type type) {
		return getGenericType(type).getOrNull();
	}

	/**
	 * Get 1st generic type of the provided class.
	 *
	 * @param clazz		class
	 * @return			generic type or null
	 */
	public static Class<?> getGenericTypeRaw(Class<?> clazz) {
		return getGenericType(clazz).getOrNull();
	}

	/**
	 * Get constructor of the provided class.
	 *
	 * @param clazz		class
	 * @param types		argument types
	 * @param <T>		object type
	 * @return			constructor that wrapped in {@link Option}
	 */
	public static <T> Option<Constructor<T>> getConstructor(Class<T> clazz, Class<?>... types) {
		if (clazz == null) {
			return Option.none();
		}

		if (types == null) {
			types = new Class<?>[0];
		}

		try {
			return Option.some(clazz.getConstructor(types));
		} catch (Throwable ignored) {
		}

		return Option.none();
	}

	/**
	 * Get default constructor of the provided class.
	 *
	 * <p>Wraps {@link UwReflect#getConstructor(Class, Class[])}.
	 *
	 * @param clazz		class
	 * @param <T>		object type
	 * @return			constructor that wrapped in {@link Option}
	 */
	public static <T> Option<Constructor<T>> getConstructor(Class<T> clazz) {
		return getConstructor(clazz, (Class<?>[]) null);
	}

	/**
	 * Get constructor of the provided class.
	 *
	 * @param clazz		class
	 * @param types		argument types
	 * @param <T>		object type
	 * @return			constructor or null
	 */
	public static <T> Constructor<T> getConstructorRaw(Class<T> clazz, Class<?>... types) {
		return getConstructor(clazz, types).getOrNull();
	}

	/**
	 * Get default constructor of the provided class.
	 *
	 * @param clazz		class
	 * @param <T>		object type
	 * @return			constructor or null
	 */
	public static <T> Constructor<T> getConstructorRaw(Class<T> clazz) {
		return getConstructor(clazz).getOrNull();
	}

	/**
	 * Create a new instance of the provided class.
	 *
	 * @param clazz			class
	 * @param arguments		constructor arguments
	 * @param <T>			object type
	 * @return				new instance that wrapped in {@link Option}
	 */
	public static <T> Option<T> newInstance(Class<T> clazz, Object... arguments) {
		if (clazz == null) {
			return Option.none();
		}

		if (arguments == null) {
			arguments = new Object[0];
		}

		Class<?>[] types = Stream.of(arguments)
				.map(Object::getClass)
				.toArray(Class[]::new);

		try {
			Constructor<T> constructor = getConstructorRaw(clazz, types);
			return Option.some(constructor.newInstance(arguments));
		} catch (Throwable ignored) {
		}

		return Option.none();
	}

	/**
	 * Create a new instance of the provided class by its default constructor.
	 *
	 * <p>Wraps {@link UwReflect#newInstance(Class, Object...)}.
	 *
	 * @param clazz			class
	 * @param <T>			object type
	 * @return				new instance that wrapped in {@link Option}
	 */
	public static <T> Option<T> newInstance(Class<T> clazz) {
		return newInstance(clazz, (Object[]) null);
	}

	/**
	 * Create a new instance of the provided class.
	 *
	 * @param clazz			class
	 * @param arguments		constructor arguments
	 * @param <T>			object type
	 * @return				new instance or null
	 */
	public static <T> T newInstanceRaw(Class<T> clazz, Object... arguments) {
		return newInstance(clazz, arguments).getOrNull();
	}

	/**
	 * Create a new instance of the provided class by its default constructor.
	 *
	 * @param clazz			class
	 * @param <T>			object type
	 * @return				new instance or null
	 */
	public static <T> T newInstanceRaw(Class<T> clazz) {
		return newInstance(clazz).getOrNull();
	}

	private UwReflect() {
		throw new UnsupportedOperationException();
	}
}
