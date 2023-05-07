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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.BaseStream;

/**
 * A map utility.
 *
 * <p>{@code UwMap} is the utility class
 * that provide functionality to operate
 * with maps.
 */
@SuppressWarnings("unused")
public final class UwMap {

	/**
	 * An empty map instance.
	 */
	@SuppressWarnings("rawtypes")
	public static final Map EMPTY = Collections.EMPTY_MAP;

	/**
	 * Check if the provided map is unmodifiable.
	 *
	 * @param map	map to check for
	 * @return		boolean value as result,
	 * 				true - yes, false - no
	 */
	@SuppressWarnings("unchecked")
	public static boolean isUnmodifiable(Map<?, ?> map) {
		if (map == null) {
			return false;
		}

		try {
			map.putAll(EMPTY);

			return false;
		} catch (UnsupportedOperationException ignored) {
		}

		return true;
	}

	/**
	 * Get an unmodifiable view of the provided map.
	 *
	 * @param map	map to get the view for
	 * @param <K>	key type
	 * @param <V>	value type
	 * @return		unmodifiable view of the map
	 */
	public static <K, V> Map<K, V> toUnmodifiable(Map<K, V> map) {
		if (map == null) {
			return null;
		}

		if (isUnmodifiable(map)) {
			return map;
		}

		return Collections.unmodifiableMap(map);
	}

	/**
	 * Safely get a value from a map by its key or return a default one.
	 *
	 * @param key			key assigned to the value
	 * @param map			map from which get the value
	 * @param defaultValue	default value to return on failure
	 * @param <K>			key type
	 * @param <T>			value type
	 * @return				value assigned to the key or the default one
	 */
	public static <K, T> T getOrElse(K key, Map<K, T> map, T defaultValue) {
		if (map == null) {
			return defaultValue;
		}

		T resultValue = null;

		try {
			resultValue = map.get(key);
		} catch (ClassCastException | NullPointerException e) {
			e.printStackTrace();
		}

		return UwObject.ifNull(resultValue, defaultValue);
	}

	/**
	 * Safely get a value from a map by its key or return a default one.
	 *
	 * @param key					key assigned to the value
	 * @param map					map from which get the value
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					key type
	 * @param <T>					value type
	 * @return						value assigned to the key or the default one
	 */
	public static <K, T> T getOrElse(K key, Map<K, T> map, Supplier<T> defaultValueSupplier) {
		return UwObject.ifNull(getOrNull(key, map), defaultValueSupplier);
	}

	/**
	 * Safely get a value from a map by its key or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#getOrElse(Object, Map, Object)}
	 * w/ {@code null} as the default value.
	 *
	 * @param key	key assigned to the value
	 * @param map	map from which get the value
	 * @param <K>	key type
	 * @param <T>	value type
	 * @return		value assigned to the key or {@code null}
	 */
	public static <K, T> T getOrNull(K key, Map<K, T> map) {
		return getOrElse(key, map, (T) null);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return a default value.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterator of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValue			default value to return on failure
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrElse(Function<T, K> getter, Iterator<T> entries, R map, Supplier<R> createMapSupplier, R defaultValue) {
		if (getter == null || entries == null
				|| map == null || createMapSupplier == null) {
			return defaultValue;
		}

		R localMap = createMapSupplier.get();

		while (entries.hasNext()) {
			T val = entries.next();
			K key = getter.apply(val);

			try {
				if (map.containsKey(key)
						|| localMap.containsKey(key)) {
					return defaultValue;
				}

				localMap.put(key, val);
			} catch (UnsupportedOperationException | ClassCastException
					| NullPointerException | IllegalArgumentException e) {
				e.printStackTrace();

				return defaultValue;
			}
		}

		return localMap;
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrElse(Function, Iterator, Map, Supplier, Map)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterable of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValue			default value to return on failure
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrElse(Function<T, K> getter, Iterable<T> entries, R map, Supplier<R> createMapSupplier, R defaultValue) {
		return extendMapByFieldOrElse(getter, entries.iterator(), map, createMapSupplier, defaultValue);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrElse(Function, Iterator, Map, Supplier, Map)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				stream of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValue			default value to return on failure
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrElse(Function<T, K> getter, BaseStream<T, ?> entries, R map, Supplier<R> createMapSupplier, R defaultValue) {
		return extendMapByFieldOrElse(getter, entries.iterator(), map, createMapSupplier, defaultValue);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrElse(Function, Iterator, Map, Supplier, Map)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				array of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValue			default value to return on failure
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrElse(Function<T, K> getter, T[] entries, R map, Supplier<R> createMapSupplier, R defaultValue) {
		return extendMapByFieldOrElse(getter, UwArray.iterator(entries), map, createMapSupplier, defaultValue);
	}

	/**
	 * Safely extend the provided map by mapping enum constants by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrElse(Function, Object[], Map, Supplier, Map)}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter				supplier for getting an enum's field
	 * @param clazz					enum class
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValue			default value to return on failure
	 * @param <K>					field type
	 * @param <T>					enum type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T extends Enum<T>, R extends Map<K, T>> R extendMapByFieldOrElse(Function<T, K> getter, Class<T> clazz, R map, Supplier<R> createMapSupplier, R defaultValue) {
		return extendMapByFieldOrElse(getter, UwEnum.values(clazz), map, createMapSupplier, defaultValue);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return a default value.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterator of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrElse(Function<T, K> getter, Iterator<T> entries, R map, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return UwObject.ifNull(extendMapByFieldOrNull(getter, entries, map, createMapSupplier), defaultValueSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrElse(Function, Iterator, Map, Supplier, Supplier)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterable of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrElse(Function<T, K> getter, Iterable<T> entries, R map, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return extendMapByFieldOrElse(getter, entries.iterator(), map, createMapSupplier, defaultValueSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrElse(Function, Iterator, Map, Supplier, Supplier)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				stream of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrElse(Function<T, K> getter, BaseStream<T, ?> entries, R map, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return extendMapByFieldOrElse(getter, entries.iterator(), map, createMapSupplier, defaultValueSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrElse(Function, Iterator, Map, Supplier, Supplier)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				array of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrElse(Function<T, K> getter, T[] entries, R map, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return extendMapByFieldOrElse(getter, UwArray.iterator(entries), map, createMapSupplier, defaultValueSupplier);
	}

	/**
	 * Safely extend the provided map by mapping enum constants by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrElse(Function, Object[], Map, Supplier, Supplier)}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter				supplier for getting an enum's field
	 * @param clazz					enum class
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					enum type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T extends Enum<T>, R extends Map<K, T>> R extendMapByFieldOrElse(Function<T, K> getter, Class<T> clazz, R map, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return extendMapByFieldOrElse(getter, UwEnum.values(clazz), map, createMapSupplier, defaultValueSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrElse(Function, Iterator, Map, Supplier, Supplier)}
	 * w/ create map supplier as the default value supplier.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterator of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or an empty one
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrEmpty(Function<T, K> getter, Iterator<T> entries, R map, Supplier<R> createMapSupplier) {
		return extendMapByFieldOrElse(getter, entries, map, createMapSupplier, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrEmpty(Function, Iterator, Map, Supplier)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterable of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or an empty one
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrEmpty(Function<T, K> getter, Iterable<T> entries, R map, Supplier<R> createMapSupplier) {
		return extendMapByFieldOrEmpty(getter, entries.iterator(), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrEmpty(Function, Iterator, Map, Supplier)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				stream of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or an empty one
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrEmpty(Function<T, K> getter, BaseStream<T, ?> entries, R map, Supplier<R> createMapSupplier) {
		return extendMapByFieldOrEmpty(getter, entries.iterator(), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrEmpty(Function, Iterator, Map, Supplier)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				array of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or an empty one
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrEmpty(Function<T, K> getter, T[] entries, R map, Supplier<R> createMapSupplier) {
		return extendMapByFieldOrEmpty(getter, UwArray.iterator(entries), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping enum constants by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrEmpty(Function, Object[], Map, Supplier)}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter				supplier for getting an enum's field
	 * @param clazz					enum class
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					enum type
	 * @param <R>					map type
	 * @return						new extended map instance or an empty one
	 */
	public static <K, T extends Enum<T>, R extends Map<K, T>> R extendMapByFieldOrEmpty(Function<T, K> getter, Class<T> clazz, R map, Supplier<R> createMapSupplier) {
		return extendMapByFieldOrEmpty(getter, UwEnum.values(clazz), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrElse(Function, Iterator, Map, Supplier, Map)}
	 * w/ {@code null} as the default value.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterator of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or {@code null}
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrNull(Function<T, K> getter, Iterator<T> entries, R map, Supplier<R> createMapSupplier) {
		return extendMapByFieldOrElse(getter, entries, map, createMapSupplier, (R) null);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrNull(Function, Iterator, Map, Supplier)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterable of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or {@code null}
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrNull(Function<T, K> getter, Iterable<T> entries, R map, Supplier<R> createMapSupplier) {
		return extendMapByFieldOrNull(getter, entries.iterator(), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrNull(Function, Iterator, Map, Supplier)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				stream of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or {@code null}
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrNull(Function<T, K> getter, BaseStream<T, ?> entries, R map, Supplier<R> createMapSupplier) {
		return extendMapByFieldOrNull(getter, entries.iterator(), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrNull(Function, Iterator, Map, Supplier)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				array of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or {@code null}
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrNull(Function<T, K> getter, T[] entries, R map, Supplier<R> createMapSupplier) {
		return extendMapByFieldOrNull(getter, UwArray.iterator(entries), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping enum constants by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrNull(Function, Object[], Map, Supplier)}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter				supplier for getting an enum's field
	 * @param clazz					enum class
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					enum type
	 * @param <R>					map type
	 * @return						new extended map instance or {@code null}
	 */
	public static <K, T extends Enum<T>, R extends Map<K, T>> R extendMapByFieldOrNull(Function<T, K> getter, Class<T> clazz, R map, Supplier<R> createMapSupplier) {
		return extendMapByFieldOrNull(getter, UwEnum.values(clazz), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return the unmodified map.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrElse(Function, Iterator, Map, Supplier, Map)}
	 * w/ the provided map as the default value.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterator of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the unmodified one
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrSelf(Function<T, K> getter, Iterator<T> entries, R map, Supplier<R> createMapSupplier) {
		return extendMapByFieldOrElse(getter, entries, map, createMapSupplier, map);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return the unmodified map.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrSelf(Function, Iterator, Map, Supplier)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterable of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the unmodified one
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrSelf(Function<T, K> getter, Iterable<T> entries, R map, Supplier<R> createMapSupplier) {
		return extendMapByFieldOrSelf(getter, entries.iterator(), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return the unmodified map.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrSelf(Function, Iterator, Map, Supplier)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				stream of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the unmodified one
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrSelf(Function<T, K> getter, BaseStream<T, ?> entries, R map, Supplier<R> createMapSupplier) {
		return extendMapByFieldOrSelf(getter, entries.iterator(), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return the unmodified map.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrSelf(Function, Iterator, Map, Supplier)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				array of entries
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the unmodified one
	 */
	public static <K, T, R extends Map<K, T>> R extendMapByFieldOrSelf(Function<T, K> getter, T[] entries, R map, Supplier<R> createMapSupplier) {
		return extendMapByFieldOrSelf(getter, UwArray.iterator(entries), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries by theirs field or return the unmodified map.
	 *
	 * <p>Wraps {@link UwMap#extendMapByFieldOrSelf(Function, Object[], Map, Supplier)}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter				supplier for getting an enum's field
	 * @param clazz					enum class
	 * @param map					map instance to extend
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					enum type
	 * @param <R>					map type
	 * @return						new extended map instance or the unmodified one
	 */
	public static <K, T extends Enum<T>, R extends Map<K, T>> R extendMapByFieldOrSelf(Function<T, K> getter, Class<T> clazz, R map, Supplier<R> createMapSupplier) {
		return extendMapByFieldOrSelf(getter, UwEnum.values(clazz), map, createMapSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Creates a fresh map instance from the provided create map supplier
	 * and wraps {@link UwMap#extendMapByFieldOrElse(Function, Iterator, Map, Supplier, Map)}
	 * w/ that map as the map argument.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterator of entries
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValue			default value to return on failure
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T, R extends Map<K, T>> R newMapByFieldOrElse(Function<T, K> getter, Iterator<T> entries, Supplier<R> createMapSupplier, R defaultValue) {
		R map = UwObject.ifNotNull(createMapSupplier, Supplier::get);

		return extendMapByFieldOrElse(getter, entries, map, createMapSupplier, defaultValue);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Iterator, Supplier, Map)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterable of entries
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValue			default value to return on failure
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T, R extends Map<K, T>> R newMapByFieldOrElse(Function<T, K> getter, Iterable<T> entries, Supplier<R> createMapSupplier, R defaultValue) {
		return newMapByFieldOrElse(getter, entries.iterator(), createMapSupplier, defaultValue);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Iterator, Supplier, Map)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				stream of entries
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValue			default value to return on failure
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T, R extends Map<K, T>> R newMapByFieldOrElse(Function<T, K> getter, BaseStream<T, ?> entries, Supplier<R> createMapSupplier, R defaultValue) {
		return newMapByFieldOrElse(getter, entries.iterator(), createMapSupplier, defaultValue);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Iterator, Supplier, Map)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				array of entries
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValue			default value to return on failure
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T, R extends Map<K, T>> R newMapByFieldOrElse(Function<T, K> getter, T[] entries, Supplier<R> createMapSupplier, R defaultValue) {
		return newMapByFieldOrElse(getter, UwArray.iterator(entries), createMapSupplier, defaultValue);
	}

	/**
	 * Safely create a new map instance and extend it by mapping enum constants by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Object[], Supplier, Map)}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter				supplier for getting an enum's field
	 * @param clazz					enum class
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValue			default value to return on failure
	 * @param <K>					field type
	 * @param <T>					enum type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T extends Enum<T>, R extends Map<K, T>> R newMapByFieldOrElse(Function<T, K> getter, Class<T> clazz, Supplier<R> createMapSupplier, R defaultValue) {
		return newMapByFieldOrElse(getter, UwEnum.values(clazz), createMapSupplier, defaultValue);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterator of entries
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T, R extends Map<K, T>> R newMapByFieldOrElse(Function<T, K> getter, Iterator<T> entries, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return UwObject.ifNull(newMapByFieldOrNull(getter, entries, createMapSupplier), defaultValueSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Iterator, Supplier, Supplier)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterable of entries
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T, R extends Map<K, T>> R newMapByFieldOrElse(Function<T, K> getter, Iterable<T> entries, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return newMapByFieldOrElse(getter, entries.iterator(), createMapSupplier, defaultValueSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Iterator, Supplier, Supplier)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				stream of entries
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T, R extends Map<K, T>> R newMapByFieldOrElse(Function<T, K> getter, BaseStream<T, ?> entries, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return newMapByFieldOrElse(getter, entries.iterator(), createMapSupplier, defaultValueSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Iterator, Supplier, Supplier)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				array of entries
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T, R extends Map<K, T>> R newMapByFieldOrElse(Function<T, K> getter, T[] entries, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return newMapByFieldOrElse(getter, UwArray.iterator(entries), createMapSupplier, defaultValueSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping enum constants by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Object[], Supplier, Supplier)}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter				supplier for getting an enum's field
	 * @param clazz					enum class
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					enum type
	 * @return						new extended map instance or the default value
	 */
	public static <K, T extends Enum<T>, R extends Map<K, T>> R newMapByFieldOrElse(Function<T, K> getter, Class<T> clazz, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return newMapByFieldOrElse(getter, UwEnum.values(clazz), createMapSupplier, defaultValueSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries by theirs field or return a new empty map instance.
	 *
	 * <p>Creates a fresh map instance from the provided create map supplier
	 * and wraps {@link UwMap#extendMapByFieldOrElse(Function, Iterator, Map, Supplier, Map)}
	 * w/ that map as the map {@literal &} default value arguments.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterator of entries
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or an empty one
	 */
	public static <K, T, R extends Map<K, T>> R newMapByFieldOrEmpty(Function<T, K> getter, Iterator<T> entries, Supplier<R> createMapSupplier) {
		R map = UwObject.ifNotNull(createMapSupplier, Supplier::get);

		return extendMapByFieldOrElse(getter, entries, map, createMapSupplier, map);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrEmpty(Function, Iterator, Supplier)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterable of entries
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or an empty one
	 */
	public static <K, T, R extends Map<K, T>> R newMapByFieldOrEmpty(Function<T, K> getter, Iterable<T> entries, Supplier<R> createMapSupplier) {
		return newMapByFieldOrEmpty(getter, entries.iterator(), createMapSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrEmpty(Function, Iterator, Supplier)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				stream of entries
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or an empty one
	 */
	public static <K, T, R extends Map<K, T>> R newMapByFieldOrEmpty(Function<T, K> getter, BaseStream<T, ?> entries, Supplier<R> createMapSupplier) {
		return newMapByFieldOrEmpty(getter, entries.iterator(), createMapSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrEmpty(Function, Iterator, Supplier)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				array of entries
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or an empty one
	 */
	public static <K, T, R extends Map<K, T>> R newMapByFieldOrEmpty(Function<T, K> getter, T[] entries, Supplier<R> createMapSupplier) {
		return newMapByFieldOrEmpty(getter, UwArray.iterator(entries), createMapSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping enum constants by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrEmpty(Function, Object[], Supplier)}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter				supplier for getting an enum's field
	 * @param clazz					enum class
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					enum type
	 * @param <R>					map type
	 * @return						new extended map instance or an empty one
	 */
	public static <K, T extends Enum<T>, R extends Map<K, T>> R newMapByFieldOrEmpty(Function<T, K> getter, Class<T> clazz, Supplier<R> createMapSupplier) {
		return newMapByFieldOrEmpty(getter, UwEnum.values(clazz), createMapSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Iterator, Supplier, Map)}
	 * w/ {@code null} as the default value.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterator of entries
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or {@code null}
	 */
	public static <K, T, R extends Map<K, T>> R newMapByFieldOrNull(Function<T, K> getter, Iterator<T> entries, Supplier<R> createMapSupplier) {
		return newMapByFieldOrElse(getter, entries, createMapSupplier, (R) null);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrNull(Function, Iterator, Supplier)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterable of entries
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or {@code null}
	 */
	public static <K, T, R extends Map<K, T>> R newMapByFieldOrNull(Function<T, K> getter, Iterable<T> entries, Supplier<R> createMapSupplier) {
		return newMapByFieldOrNull(getter, entries.iterator(), createMapSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrNull(Function, Iterator, Supplier)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				stream of entries
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or {@code null}
	 */
	public static <K, T, R extends Map<K, T>> R newMapByFieldOrNull(Function<T, K> getter, BaseStream<T, ?> entries, Supplier<R> createMapSupplier) {
		return newMapByFieldOrNull(getter, entries.iterator(), createMapSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrNull(Function, Iterator, Supplier)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				array of entries
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @param <R>					map type
	 * @return						new extended map instance or {@code null}
	 */
	public static <K, T, R extends Map<K, T>> R newMapByFieldOrNull(Function<T, K> getter, T[] entries, Supplier<R> createMapSupplier) {
		return newMapByFieldOrNull(getter, UwArray.iterator(entries), createMapSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping enum constants by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrNull(Function, Object[], Supplier)}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter				supplier for getting an enum's field
	 * @param clazz					enum class
	 * @param createMapSupplier		supplier for creating a new map instance
	 * @param <K>					field type
	 * @param <T>					enum type
	 * @param <R>					map type
	 * @return						new extended map instance or {@code null}
	 */
	public static <K, T extends Enum<T>, R extends Map<K, T>> R newMapByFieldOrNull(Function<T, K> getter, Class<T> clazz, Supplier<R> createMapSupplier) {
		return newMapByFieldOrNull(getter, UwEnum.values(clazz), createMapSupplier);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Iterator, Supplier, Map)}
	 * w/ {@link HashMap#HashMap()} as the create map supplier.
	 *
	 * @param getter		supplier for getting an entry's field
	 * @param entries		iterator of entries
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			entry type
	 * @return				new extended {@link HashMap} instance or the default value
	 */
	public static <K, T> Map<K, T> newMapByFieldOrElse(Function<T, K> getter, Iterator<T> entries, Map<K, T> defaultValue) {
		return newMapByFieldOrElse(getter, entries, HashMap::new, defaultValue);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Iterator, Map)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter		supplier for getting an entry's field
	 * @param entries		iterable of entries
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			entry type
	 * @return				new extended {@link HashMap} instance or the default value
	 */
	public static <K, T> Map<K, T> newMapByFieldOrElse(Function<T, K> getter, Iterable<T> entries, Map<K, T> defaultValue) {
		return newMapByFieldOrElse(getter, entries.iterator(), defaultValue);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Iterator, Map)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter		supplier for getting an entry's field
	 * @param entries		stream of entries
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			entry type
	 * @return				new extended {@link HashMap} instance or the default value
	 */
	public static <K, T> Map<K, T> newMapByFieldOrElse(Function<T, K> getter, BaseStream<T, ?> entries, Map<K, T> defaultValue) {
		return newMapByFieldOrElse(getter, entries.iterator(), defaultValue);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Iterator, Map)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter		supplier for getting an entry's field
	 * @param entries		array of entries
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			entry type
	 * @return				new extended {@link HashMap} instance or the default value
	 */
	public static <K, T> Map<K, T> newMapByFieldOrElse(Function<T, K> getter, T[] entries, Map<K, T> defaultValue) {
		return newMapByFieldOrElse(getter, UwArray.iterator(entries), defaultValue);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping enum constants by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Object[], Map)}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter		supplier for getting an enum's field
	 * @param clazz			enum class
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			enum type
	 * @return				new extended {@link HashMap} instance or the default value
	 */
	public static <K, T extends Enum<T>> Map<K, T> newMapByFieldOrElse(Function<T, K> getter, Class<T> clazz, Map<K, T> defaultValue) {
		return newMapByFieldOrElse(getter, UwEnum.values(clazz), defaultValue);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterator of entries
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @return						new extended {@link HashMap} instance or the default value
	 */
	public static <K, T> Map<K, T> newMapByFieldOrElse(Function<T, K> getter, Iterator<T> entries, Supplier<Map<K, T>> defaultValueSupplier) {
		return UwObject.ifNull(newMapByFieldOrNull(getter, entries, defaultValueSupplier), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Iterator, Supplier)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterable of entries
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @return						new extended {@link HashMap} instance or the default value
	 */
	public static <K, T> Map<K, T> newMapByFieldOrElse(Function<T, K> getter, Iterable<T> entries, Supplier<Map<K, T>> defaultValueSupplier) {
		return newMapByFieldOrElse(getter, entries.iterator(), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Iterator, Supplier)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				stream of entries
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @return						new extended {@link HashMap} instance or the default value
	 */
	public static <K, T> Map<K, T> newMapByFieldOrElse(Function<T, K> getter, BaseStream<T, ?> entries, Supplier<Map<K, T>> defaultValueSupplier) {
		return newMapByFieldOrElse(getter, entries.iterator(), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Iterator, Supplier)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				array of entries
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @return						new extended {@link HashMap} instance or the default value
	 */
	public static <K, T> Map<K, T> newMapByFieldOrElse(Function<T, K> getter, T[] entries, Supplier<Map<K, T>> defaultValueSupplier) {
		return newMapByFieldOrElse(getter, UwArray.iterator(entries), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping enum constants by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Object[], Supplier)}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter				supplier for getting an enum's field
	 * @param clazz					enum class
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					enum type
	 * @return						new extended {@link HashMap} instance or the default value
	 */
	public static <K, T extends Enum<T>> Map<K, T> newMapByFieldOrElse(Function<T, K> getter, Class<T> clazz, Supplier<Map<K, T>> defaultValueSupplier) {
		return newMapByFieldOrElse(getter, UwEnum.values(clazz), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrEmpty(Function, Iterator, Supplier)}
	 * w/ {@link HashMap#HashMap()} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	iterator of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link HashMap} instance or an empty one
	 */
	public static <K, T> Map<K, T> newMapByFieldOrEmpty(Function<T, K> getter, Iterator<T> entries) {
		return newMapByFieldOrEmpty(getter, entries, HashMap::new);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrEmpty(Function, Iterator)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	iterable of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link HashMap} instance or an empty one
	 */
	public static <K, T> Map<K, T> newMapByFieldOrEmpty(Function<T, K> getter, Iterable<T> entries) {
		return newMapByFieldOrEmpty(getter, entries.iterator());
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrEmpty(Function, Iterator)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	stream of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link HashMap} instance or an empty one
	 */
	public static <K, T> Map<K, T> newMapByFieldOrEmpty(Function<T, K> getter, BaseStream<T, ?> entries) {
		return newMapByFieldOrEmpty(getter, entries.iterator());
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrEmpty(Function, Iterator)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	array of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link HashMap} instance or an empty one
	 */
	public static <K, T> Map<K, T> newMapByFieldOrEmpty(Function<T, K> getter, T[] entries) {
		return newMapByFieldOrEmpty(getter, UwArray.iterator(entries));
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping enum constants by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrEmpty(Function, Object[])}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter	supplier for getting an enum's field
	 * @param clazz		enum class
	 * @param <K>		field type
	 * @param <T>		enum type
	 * @return			new extended {@link HashMap} instance or an empty one
	 */
	public static <K, T extends Enum<T>> Map<K, T> newMapByFieldOrEmpty(Function<T, K> getter, Class<T> clazz) {
		return newMapByFieldOrEmpty(getter, UwEnum.values(clazz));
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Iterator, Map)}
	 * w/ {@code null} as the default value.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	iterator of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link HashMap} instance or {@code null}
	 */
	public static <K, T> Map<K, T> newMapByFieldOrNull(Function<T, K> getter, Iterator<T> entries) {
		return newMapByFieldOrElse(getter, entries, (Map<K, T>) null);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrNull(Function, Iterator)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	iterable of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link HashMap} instance or {@code null}
	 */
	public static <K, T> Map<K, T> newMapByFieldOrNull(Function<T, K> getter, Iterable<T> entries) {
		return newMapByFieldOrNull(getter, entries.iterator());
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrNull(Function, Iterator)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	stream of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link HashMap} instance or {@code null}
	 */
	public static <K, T> Map<K, T> newMapByFieldOrNull(Function<T, K> getter, BaseStream<T, ?> entries) {
		return newMapByFieldOrNull(getter, entries.iterator());
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrNull(Function, Iterator)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	array of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link HashMap} instance or {@code null}
	 */
	public static <K, T> Map<K, T> newMapByFieldOrNull(Function<T, K> getter, T[] entries) {
		return newMapByFieldOrNull(getter, UwArray.iterator(entries));
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping enum constants by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrNull(Function, Object[])}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter	supplier for getting an enum's field
	 * @param clazz		enum class
	 * @param <K>		field type
	 * @param <T>		enum type
	 * @return			new extended {@link HashMap} instance or {@code null}
	 */
	public static <K, T extends Enum<T>> Map<K, T> newMapByFieldOrNull(Function<T, K> getter, Class<T> clazz) {
		return newMapByFieldOrNull(getter, UwEnum.values(clazz));
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrElse(Function, Iterator, Supplier, Map)}
	 * w/ {@link ConcurrentHashMap#ConcurrentHashMap()} as the create map supplier.
	 *
	 * @param getter		supplier for getting an entry's field
	 * @param entries		iterator of entries
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			entry type
	 * @return				new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T> ConcurrentMap<K, T> newConcurrentMapByFieldOrElse(Function<T, K> getter, Iterator<T> entries, ConcurrentMap<K, T> defaultValue) {
		return newMapByFieldOrElse(getter, entries, ConcurrentHashMap::new, defaultValue);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newConcurrentMapByFieldOrElse(Function, Iterator, ConcurrentMap)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter		supplier for getting an entry's field
	 * @param entries		iterable of entries
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			entry type
	 * @return				new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T> ConcurrentMap<K, T> newConcurrentMapByFieldOrElse(Function<T, K> getter, Iterable<T> entries, ConcurrentMap<K, T> defaultValue) {
		return newConcurrentMapByFieldOrElse(getter, entries.iterator(), defaultValue);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newConcurrentMapByFieldOrElse(Function, Iterator, ConcurrentMap)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter		supplier for getting an entry's field
	 * @param entries		stream of entries
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			entry type
	 * @return				new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T> ConcurrentMap<K, T> newConcurrentMapByFieldOrElse(Function<T, K> getter, BaseStream<T, ?> entries, ConcurrentMap<K, T> defaultValue) {
		return newConcurrentMapByFieldOrElse(getter, entries.iterator(), defaultValue);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newConcurrentMapByFieldOrElse(Function, Iterator, ConcurrentMap)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter		supplier for getting an entry's field
	 * @param entries		array of entries
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			entry type
	 * @return				new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T> ConcurrentMap<K, T> newConcurrentMapByFieldOrElse(Function<T, K> getter, T[] entries, ConcurrentMap<K, T> defaultValue) {
		return newConcurrentMapByFieldOrElse(getter, UwArray.iterator(entries), defaultValue);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping enum constants by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newConcurrentMapByFieldOrElse(Function, Object[], ConcurrentMap)}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter		supplier for getting an enum's field
	 * @param clazz			enum class
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			enum type
	 * @return				new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T extends Enum<T>> ConcurrentMap<K, T> newConcurrentMapByFieldOrElse(Function<T, K> getter, Class<T> clazz, ConcurrentMap<K, T> defaultValue) {
		return newConcurrentMapByFieldOrElse(getter, UwEnum.values(clazz), defaultValue);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterator of entries
	 * @param defaultValueSupplier	supplier from which return get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @return						new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T> ConcurrentMap<K, T> newConcurrentMapByFieldOrElse(Function<T, K> getter, Iterator<T> entries, Supplier<ConcurrentMap<K, T>> defaultValueSupplier) {
		return UwObject.ifNull(newConcurrentMapByFieldOrNull(getter, entries), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newConcurrentMapByFieldOrElse(Function, Iterator, Supplier)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterable of entries
	 * @param defaultValueSupplier	supplier from which return get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @return						new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T> ConcurrentMap<K, T> newConcurrentMapByFieldOrElse(Function<T, K> getter, Iterable<T> entries, Supplier<ConcurrentMap<K, T>> defaultValueSupplier) {
		return newConcurrentMapByFieldOrElse(getter, entries.iterator(), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newConcurrentMapByFieldOrElse(Function, Iterator, Supplier)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				stream of entries
	 * @param defaultValueSupplier	supplier from which return get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @return						new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T> ConcurrentMap<K, T> newConcurrentMapByFieldOrElse(Function<T, K> getter, BaseStream<T, ?> entries, Supplier<ConcurrentMap<K, T>> defaultValueSupplier) {
		return newConcurrentMapByFieldOrElse(getter, entries.iterator(), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newConcurrentMapByFieldOrElse(Function, Iterator, Supplier)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				array of entries
	 * @param defaultValueSupplier	supplier from which return get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @return						new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T> ConcurrentMap<K, T> newConcurrentMapByFieldOrElse(Function<T, K> getter, T[] entries, Supplier<ConcurrentMap<K, T>> defaultValueSupplier) {
		return newConcurrentMapByFieldOrElse(getter, UwArray.iterator(entries), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping enum constants by theirs field or return a default value.
	 *
	 * <p>Wraps {@link UwMap#newConcurrentMapByFieldOrElse(Function, Object[], Supplier)}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter				supplier for getting an enum's field
	 * @param clazz					enum class
	 * @param defaultValueSupplier	supplier from which return get the default value
	 * @param <K>					field type
	 * @param <T>					enum type
	 * @return						new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T extends Enum<T>> ConcurrentMap<K, T> newConcurrentMapByFieldOrElse(Function<T, K> getter, Class<T> clazz, Supplier<ConcurrentMap<K, T>> defaultValueSupplier) {
		return newConcurrentMapByFieldOrElse(getter, UwEnum.values(clazz), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#newMapByFieldOrEmpty(Function, Iterator, Supplier)}
	 * w/ {@link ConcurrentHashMap#ConcurrentHashMap()} as the create map supplier.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	iterator of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link ConcurrentHashMap} instance or an empty one
	 */
	public static <K, T> ConcurrentMap<K, T> newConcurrentMapByFieldOrEmpty(Function<T, K> getter, Iterator<T> entries) {
		return newMapByFieldOrEmpty(getter, entries, ConcurrentHashMap::new);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#newConcurrentMapByFieldOrEmpty(Function, Iterator)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	iterable of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link ConcurrentHashMap} instance or an empty one
	 */
	public static <K, T> ConcurrentMap<K, T> newConcurrentMapByFieldOrEmpty(Function<T, K> getter, Iterable<T> entries) {
		return newConcurrentMapByFieldOrEmpty(getter, entries.iterator());
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#newConcurrentMapByFieldOrEmpty(Function, Iterator)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	stream of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link ConcurrentHashMap} instance or an empty one
	 */
	public static <K, T> ConcurrentMap<K, T> newConcurrentMapByFieldOrEmpty(Function<T, K> getter, BaseStream<T, ?> entries) {
		return newConcurrentMapByFieldOrEmpty(getter, entries.iterator());
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#newConcurrentMapByFieldOrEmpty(Function, Iterator)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	array of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link ConcurrentHashMap} instance or an empty one
	 */
	public static <K, T> ConcurrentMap<K, T> newConcurrentMapByFieldOrEmpty(Function<T, K> getter, T[] entries) {
		return newConcurrentMapByFieldOrEmpty(getter, UwArray.iterator(entries));
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping enum constants by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link UwMap#newConcurrentMapByFieldOrEmpty(Function, Object[])}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter	supplier for getting an enum's field
	 * @param clazz		enum class
	 * @param <K>		field type
	 * @param <T>		enum type
	 * @return			new extended {@link ConcurrentHashMap} instance or an empty one
	 */
	public static <K, T extends Enum<T>> ConcurrentMap<K, T> newConcurrentMapByFieldOrEmpty(Function<T, K> getter, Class<T> clazz) {
		return newConcurrentMapByFieldOrEmpty(getter, UwEnum.values(clazz));
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#newConcurrentMapByFieldOrElse(Function, Iterator, ConcurrentMap)}
	 * w/ {@code null} as the default value.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	iterator of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link ConcurrentHashMap} instance or {@code null}
	 */
	public static <K, T> ConcurrentMap<K, T> newConcurrentMapByFieldOrNull(Function<T, K> getter, Iterator<T> entries) {
		return newConcurrentMapByFieldOrElse(getter, entries, (ConcurrentMap<K, T>) null);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#newConcurrentMapByFieldOrNull(Function, Iterator)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	iterable of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link ConcurrentHashMap} instance or {@code null}
	 */
	public static <K, T> ConcurrentMap<K, T> newConcurrentMapByFieldOrNull(Function<T, K> getter, Iterable<T> entries) {
		return newConcurrentMapByFieldOrNull(getter, entries.iterator());
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#newConcurrentMapByFieldOrNull(Function, Iterator)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	stream of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link ConcurrentHashMap} instance or {@code null}
	 */
	public static <K, T> ConcurrentMap<K, T> newConcurrentMapByFieldOrNull(Function<T, K> getter, BaseStream<T, ?> entries) {
		return newConcurrentMapByFieldOrNull(getter, entries.iterator());
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#newConcurrentMapByFieldOrNull(Function, Iterator)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	array of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link ConcurrentHashMap} instance or {@code null}
	 */
	public static <K, T> ConcurrentMap<K, T> newConcurrentMapByFieldOrNull(Function<T, K> getter, T[] entries) {
		return newConcurrentMapByFieldOrNull(getter, UwArray.iterator(entries));
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping enum constants by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link UwMap#newConcurrentMapByFieldOrNull(Function, Object[])}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter	supplier for getting an enum's field
	 * @param clazz		enum class
	 * @param <K>		field type
	 * @param <T>		enum type
	 * @return			new extended {@link ConcurrentHashMap} instance or {@code null}
	 */
	public static <K, T extends Enum<T>> ConcurrentMap<K, T> newConcurrentMapByFieldOrNull(Function<T, K> getter, Class<T> clazz) {
		return newConcurrentMapByFieldOrNull(getter, UwEnum.values(clazz));
	}

	private UwMap() {
		throw new UnsupportedOperationException();
	}
}
