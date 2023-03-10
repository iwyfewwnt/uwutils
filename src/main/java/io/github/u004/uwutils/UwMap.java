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
import io.vavr.control.Try;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An object mapping utility.
 *
 * <p>{@code UwMap} is the utility class
 * that provide functionality to create maps based
 * on the instance fields. Highly usable for enum types.
 *
 * @since 0.1.0
 */
@SuppressWarnings("unused")
public final class UwMap {

	/**
	 * Extend provided map by mapping collection of objects by its field.
	 *
	 * @param getter		field value getter
	 * @param objects		collection of objects
	 * @param map			map to be extended
	 * @param <K>			field type
	 * @param <T>			object type
	 * @return				boolean value as a result,
	 *						true - success, false - failure
	 */
	public static <K, T> boolean extendMapByField(Function<T, K> getter, Collection<T> objects, Map<K, T> map) {
		if (ObjectUtils.anyNull(getter, objects, map)) {
			return false;
		}

		for (T entry : objects) {
			K key = getter.apply(entry);

			if (key == null || map.containsKey(key)) {
				return false;
			}

			map.put(key, entry);
		}

		return true;
	}

	/**
	 * Extend provided map by mapping array of objects by its field.
	 *
	 * <p>Wraps {@link UwMap#extendMapByField(Function, Collection, Map)}.
	 *
	 * @param getter		field value getter
	 * @param objects		array of objects
	 * @param map			map to be extended
	 * @param <K>			field type
	 * @param <T>			object type
	 * @return				boolean value as a result,
	 *						true - success, false - failure
	 */
	public static <K, T> boolean extendMapByField(Function<T, K> getter, T[] objects, Map<K, T> map) {
		return extendMapByField(getter, (Collection<T>) UwObject.applyIfNotNull(objects, Arrays::asList), map);
	}

	/**
	 * Create a new map and extend it by mapping collection of objects by its field.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 * </ul>
	 *
	 * @param getter			field value getter
	 * @param objects			collection of objects
	 * @param mapSupplier		map supplier
	 * @param <K>				field type
	 * @param <T>				object type
	 * @return					map that wrapped in {@link Try}
	 */
	public static <K, T> Try<Map<K, T>> newMapByField(Function<T, K> getter, Collection<T> objects, Supplier<Map<K, T>> mapSupplier) {
		Map<K, T> map = UwObject.applyIfNotNull(mapSupplier, Supplier::get);

		if (extendMapByField(getter, objects, map)) {
			return Try.success(map);
		}

		return Try.failure(new IllegalArgumentException());
	}

	/**
	 * Create a new map and extend it by mapping array of objects by its field.
	 *
	 * <p>Wraps {@link UwMap#newMapByField(Function, Collection, Supplier)}
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 * </ul>
	 *
	 * @param getter			field value getter
	 * @param objects			array of objects
	 * @param mapSupplier		map supplier
	 * @param <K>				field type
	 * @param <T>				object type
	 * @return					map that wrapped in {@link Try}
	 */
	public static <K, T> Try<Map<K, T>> newMapByField(Function<T, K> getter, T[] objects, Supplier<Map<K, T>> mapSupplier) {
		return newMapByField(getter, (Collection<T>) UwObject.applyIfNotNull(objects, Arrays::asList), mapSupplier);
	}

	/**
	 * Create a new map and extend it by mapping collection of objects by its field.
	 *
	 * <p>Wraps {@link UwMap#newMapByField(Function, Collection, Supplier)}
	 * with {@link HashMap#HashMap()} as the map supplier.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 * </ul>
	 *
	 * @param getter			field value getter
	 * @param objects			collection of objects
	 * @param <K>				field type
	 * @param <T>				object type
	 * @return					map that wrapped in {@link Try}
	 */
	public static <K, T> Try<Map<K, T>> newMapByField(Function<T, K> getter, Collection<T> objects) {
		return newMapByField(getter, objects, HashMap::new);
	}

	/**
	 * Create a new map and extend it by mapping array of objects by its field.
	 *
	 * <p>Wraps {@link UwMap#newMapByField(Function, Object[], Supplier)}
	 * with {@link HashMap#HashMap()} as the map supplier.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 * </ul>
	 *
	 * @param getter			field value getter
	 * @param objects			array of objects
	 * @param <K>				field type
	 * @param <T>				object type
	 * @return					map that wrapped in {@link Try}
	 */
	public static <K, T> Try<Map<K, T>> newMapByField(Function<T, K> getter, T[] objects) {
		return newMapByField(getter, objects, HashMap::new);
	}

	/**
	 * Create a new concurrent map and extend it by mapping collection of objects by its field.
	 *
	 * <p>Wraps {@link UwMap#newMapByField(Function, Collection, Supplier)}
	 * with {@link ConcurrentHashMap#ConcurrentHashMap()} as the map supplier.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 * </ul>
	 *
	 * @param getter			field value getter
	 * @param objects			collection of objects
	 * @param <K>				field type
	 * @param <T>				object type
	 * @return					concurrent map that wrapped in {@link Try}
	 */
	public static <K, T> Try<Map<K, T>> newConcurrentMapByField(Function<T, K> getter, Collection<T> objects) {
		return newMapByField(getter, objects, ConcurrentHashMap::new);
	}


	/**
	 * Create a new concurrent map and extend it by mapping array of objects by its field.
	 *
	 * <p>Wraps {@link UwMap#newMapByField(Function, Object[], Supplier)}
	 * with {@link ConcurrentHashMap#ConcurrentHashMap()} as the map supplier.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 * </ul>
	 *
	 * @param getter			field value getter
	 * @param objects			array of objects
	 * @param <K>				field type
	 * @param <T>				object type
	 * @return					concurrent map that wrapped in {@link Try}
	 */
	public static <K, T> Try<Map<K, T>> newConcurrentMapByField(Function<T, K> getter, T[] objects) {
		return newMapByField(getter, objects, ConcurrentHashMap::new);
	}

	/**
	 * Create a new enum map and extend it by mapping array of the enum constants by its field.
	 *
	 * <p>Wraps {@link UwMap#newMapByField(Function, Object[], Supplier)}.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 * </ul>
	 *
	 * @param getter			field value getter
	 * @param clazz				enum class
	 * @param mapSupplier 		map supplier
	 * @param <K>				field type
	 * @param <T>				enum type
	 * @return					map that wrapped in {@link Try}
	 */
	public static <K, T extends Enum<T>> Try<Map<K, T>> newEnumMapByField(Function<T, K> getter, Class<T> clazz, Supplier<Map<K, T>> mapSupplier) {
		return newMapByField(getter, (T[]) UwObject.applyIfNotNull(clazz, Class::getEnumConstants), mapSupplier);
	}

	/**
	 * Create a new enum map and extend it by mapping array of the enum constants by its field.
	 *
	 * <p>Wraps {@link UwMap#newEnumMapByField(Function, Class, Supplier)}
	 * with {@link HashMap#HashMap()} as the map supplier.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 * </ul>
	 *
	 * @param getter			field value getter
	 * @param clazz				enum class
	 * @param <K>				field type
	 * @param <T>				enum type
	 * @return					map that wrapped in {@link Try}
	 */
	public static <K, T extends Enum<T>> Try<Map<K, T>> newEnumMapByField(Function<T, K> getter, Class<T> clazz) {
		return newEnumMapByField(getter, clazz, HashMap::new);
	}

	/**
	 * Create a new concurrent enum map and extend it by mapping array of the enum constants by its field.
	 *
	 * <p>Wraps {@link UwMap#newEnumMapByField(Function, Class, Supplier)}
	 * with {@link ConcurrentHashMap#ConcurrentHashMap()} as the map supplier.
	 *
	 * <p>Possible failure exceptions:
	 * <ul>
	 *     <li>{@link IllegalArgumentException}
	 * </ul>
	 *
	 * @param getter			field value getter
	 * @param clazz				enum class
	 * @param <K>				field type
	 * @param <T>				enum type
	 * @return					concurrent map that wrapped in {@link Try}
	 */
	public static <K, T extends Enum<T>> Try<Map<K, T>> newConcurrentEnumMapByField(Function<T, K> getter, Class<T> clazz) {
		return newEnumMapByField(getter, clazz, ConcurrentHashMap::new);
	}

	/**
	 * Null-safely get value from the map by its key.
	 *
	 * @param key		key that value assigned to
	 * @param map		map from which to get the value
	 * @param <K>		key type
	 * @param <T>		value type
	 * @return			value assigned to the key
	 * 					that wrapped in {@link Option}
	 */
	public static <K, T> Option<T> get(K key, Map<K, T> map) {
		return Option.of(UwObject.applyIfNotNull(map, key, Map::get));
	}

	/**
	 * Create a new map and extend it by mapping collection of objects by its field.
	 *
	 * @param getter			field value getter
	 * @param objects			collection of objects
	 * @param mapSupplier		map supplier
	 * @param <K>				field type
	 * @param <T>				object type
	 * @return					map or null
	 */
	public static <K, T> Map<K, T> newMapByFieldRaw(Function<T, K> getter, Collection<T> objects, Supplier<Map<K, T>> mapSupplier) {
		return newMapByField(getter, objects, mapSupplier).getOrNull();
	}

	/**
	 * Create a new map and extend it by mapping array of objects by its field.
	 *
	 * <p>Wraps {@link UwMap#newMapByField(Function, Collection, Supplier)}
	 *
	 * @param getter			field value getter
	 * @param objects			array of objects
	 * @param mapSupplier		map supplier
	 * @param <K>				field type
	 * @param <T>				object type
	 * @return					map or null
	 */
	public static <K, T> Map<K, T> newMapByFieldRaw(Function<T, K> getter, T[] objects, Supplier<Map<K, T>> mapSupplier) {
		return newMapByField(getter, objects, mapSupplier).getOrNull();
	}

	/**
	 * Create a new map and extend it by mapping collection of objects by its field.
	 *
	 * <p>Wraps {@link UwMap#newMapByField(Function, Collection, Supplier)}
	 * with {@link HashMap#HashMap()} as the map supplier.
	 *
	 * @param getter			field value getter
	 * @param objects			collection of objects
	 * @param <K>				field type
	 * @param <T>				object type
	 * @return					map or null
	 */
	public static <K, T> Map<K, T> newMapByFieldRaw(Function<T, K> getter, Collection<T> objects) {
		return newMapByField(getter, objects).getOrNull();
	}

	/**
	 * Create a new map and extend it by mapping array of objects by its field.
	 *
	 * <p>Wraps {@link UwMap#newMapByField(Function, Object[], Supplier)}
	 * with {@link HashMap#HashMap()} as the map supplier.
	 *
	 * @param getter			field value getter
	 * @param objects			array of objects
	 * @param <K>				field type
	 * @param <T>				object type
	 * @return					map or null
	 */
	public static <K, T> Map<K, T> newMapByFieldRaw(Function<T, K> getter, T[] objects) {
		return newMapByField(getter, objects).getOrNull();
	}

	/**
	 * Create a new concurrent map and extend it by mapping collection of objects by its field.
	 *
	 * <p>Wraps {@link UwMap#newMapByField(Function, Collection, Supplier)}
	 * with {@link ConcurrentHashMap#ConcurrentHashMap()} as the map supplier.
	 *
	 * @param getter			field value getter
	 * @param objects			collection of objects
	 * @param <K>				field type
	 * @param <T>				object type
	 * @return					concurrent map or null
	 */
	public static <K, T> Map<K, T> newConcurrentMapByFieldRaw(Function<T, K> getter, Collection<T> objects) {
		return newConcurrentMapByField(getter, objects).getOrNull();
	}

	/**
	 * Create a new concurrent map and extend it by mapping array of objects by its field.
	 *
	 * <p>Wraps {@link UwMap#newMapByField(Function, Object[], Supplier)}
	 * with {@link ConcurrentHashMap#ConcurrentHashMap()} as the map supplier.
	 *
	 * @param getter			field value getter
	 * @param objects			array of objects
	 * @param <K>				field type
	 * @param <T>				object type
	 * @return					concurrent map or null
	 */
	public static <K, T> Map<K, T> newConcurrentMapByFieldRaw(Function<T, K> getter, T[] objects) {
		return newConcurrentMapByField(getter, objects).getOrNull();
	}

	/**
	 * Create a new enum map and extend it by mapping array of the enum constants by its field.
	 *
	 * <p>Wraps {@link UwMap#newMapByField(Function, Object[], Supplier)}.
	 *
	 * @param getter			field value getter
	 * @param clazz				enum class
	 * @param mapSupplier 		map supplier
	 * @param <K>				field type
	 * @param <T>				enum type
	 * @return					map or null
	 */
	public static <K, T extends Enum<T>> Map<K, T> newEnumMapByFieldRaw(Function<T, K> getter, Class<T> clazz, Supplier<Map<K, T>> mapSupplier) {
		return newEnumMapByField(getter, clazz, mapSupplier).getOrNull();
	}

	/**
	 * Create a new enum map and extend it by mapping array of the enum constants by its field.
	 *
	 * <p>Wraps {@link UwMap#newEnumMapByField(Function, Class, Supplier)}
	 * with {@link HashMap#HashMap()} as the map supplier.
	 *
	 * @param getter			field value getter
	 * @param clazz				enum class
	 * @param <K>				field type
	 * @param <T>				enum type
	 * @return					map or null
	 */
	public static <K, T extends Enum<T>> Map<K, T> newEnumMapByFieldRaw(Function<T, K> getter, Class<T> clazz) {
		return newEnumMapByField(getter, clazz).getOrNull();
	}

	/**
	 * Create a new concurrent enum map and extend it by mapping array of the enum constants by its field.
	 *
	 * <p>Wraps {@link UwMap#newEnumMapByField(Function, Class, Supplier)}
	 * with {@link ConcurrentHashMap#ConcurrentHashMap()} as the map supplier.
	 *
	 * @param getter			field value getter
	 * @param clazz				enum class
	 * @param <K>				field type
	 * @param <T>				enum type
	 * @return					concurrent map or null
	 */
	public static <K, T extends Enum<T>> Map<K, T> newConcurrentEnumMapByFieldRaw(Function<T, K> getter, Class<T> clazz) {
		return newConcurrentEnumMapByField(getter, clazz).getOrNull();
	}

	/**
	 * Null-safely get value from the map by its key.
	 *
	 * @param key		key that value assigned to
	 * @param map		map from which to get the value
	 * @param <K>		key type
	 * @param <T>		value type
	 * @return			value assigned to the key or null
	 */
	public static <K, T> T getRaw(K key, Map<K, T> map) {
		return get(key, map).getOrNull();
	}

	private UwMap() {
		throw new UnsupportedOperationException();
	}
}
