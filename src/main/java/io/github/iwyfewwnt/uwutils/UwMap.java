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

		try {
			return map.getOrDefault(key, defaultValue);
		} catch (ClassCastException | NullPointerException e) {
			e.printStackTrace();
		}

		return defaultValue;
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
	 * <p>Wraps {@link #getOrElse(Object, Map, Object)}
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
	 * Safely extend the provided map by mapping entries to their field or return a default value.
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrElse(Function<T, K> getter, Iterator<T> entries, R map, Supplier<R> createMapSupplier, R defaultValue) {
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
	 * Safely extend the provided map by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #extendByFieldOrElse(Function, Iterator, Map, Supplier, Map)}
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrElse(Function<T, K> getter, Iterable<T> entries, R map, Supplier<R> createMapSupplier, R defaultValue) {
		return extendByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, Iterable::iterator), map, createMapSupplier, defaultValue);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #extendByFieldOrElse(Function, Iterator, Map, Supplier, Map)}
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrElse(Function<T, K> getter, BaseStream<T, ?> entries, R map, Supplier<R> createMapSupplier, R defaultValue) {
		return extendByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, BaseStream::iterator), map, createMapSupplier, defaultValue);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #extendByFieldOrElse(Function, Iterator, Map, Supplier, Map)}
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrElse(Function<T, K> getter, T[] entries, R map, Supplier<R> createMapSupplier, R defaultValue) {
		return extendByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, UwArray::iterator), map, createMapSupplier, defaultValue);
	}

	/**
	 * Safely extend the provided map by mapping enum constants by theirs field or return a default value.
	 *
	 * <p>Wraps {@link #extendByFieldOrElse(Function, Object[], Map, Supplier, Map)}
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
	public static <K, T extends Enum<T>, R extends Map<K, T>> R extendByFieldOrElse(Function<T, K> getter, Class<T> clazz, R map, Supplier<R> createMapSupplier, R defaultValue) {
		return extendByFieldOrElse(getter, UwEnum.values(clazz), map, createMapSupplier, defaultValue);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return a default value.
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrElse(Function<T, K> getter, Iterator<T> entries, R map, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return UwObject.ifNull(extendByFieldOrNull(getter, entries, map, createMapSupplier), defaultValueSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #extendByFieldOrElse(Function, Iterator, Map, Supplier, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrElse(Function<T, K> getter, Iterable<T> entries, R map, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return extendByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, Iterable::iterator), map, createMapSupplier, defaultValueSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #extendByFieldOrElse(Function, Iterator, Map, Supplier, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrElse(Function<T, K> getter, BaseStream<T, ?> entries, R map, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return extendByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, BaseStream::iterator), map, createMapSupplier, defaultValueSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #extendByFieldOrElse(Function, Iterator, Map, Supplier, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrElse(Function<T, K> getter, T[] entries, R map, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return extendByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, UwArray::iterator), map, createMapSupplier, defaultValueSupplier);
	}

	/**
	 * Safely extend the provided map by mapping enum constants by theirs field or return a default value.
	 *
	 * <p>Wraps {@link #extendByFieldOrElse(Function, Object[], Map, Supplier, Supplier)}
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
	public static <K, T extends Enum<T>, R extends Map<K, T>> R extendByFieldOrElse(Function<T, K> getter, Class<T> clazz, R map, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return extendByFieldOrElse(getter, UwEnum.values(clazz), map, createMapSupplier, defaultValueSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #extendByFieldOrElse(Function, Iterator, Map, Supplier, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrEmpty(Function<T, K> getter, Iterator<T> entries, R map, Supplier<R> createMapSupplier) {
		return extendByFieldOrElse(getter, entries, map, createMapSupplier, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #extendByFieldOrEmpty(Function, Iterator, Map, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrEmpty(Function<T, K> getter, Iterable<T> entries, R map, Supplier<R> createMapSupplier) {
		return extendByFieldOrEmpty(getter, (Iterator<T>) UwObject.ifNotNull(entries, Iterable::iterator), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #extendByFieldOrEmpty(Function, Iterator, Map, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrEmpty(Function<T, K> getter, BaseStream<T, ?> entries, R map, Supplier<R> createMapSupplier) {
		return extendByFieldOrEmpty(getter, (Iterator<T>) UwObject.ifNotNull(entries, BaseStream::iterator), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #extendByFieldOrEmpty(Function, Iterator, Map, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrEmpty(Function<T, K> getter, T[] entries, R map, Supplier<R> createMapSupplier) {
		return extendByFieldOrEmpty(getter, (Iterator<T>) UwObject.ifNotNull(entries, UwArray::iterator), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping enum constants by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #extendByFieldOrEmpty(Function, Object[], Map, Supplier)}
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
	public static <K, T extends Enum<T>, R extends Map<K, T>> R extendByFieldOrEmpty(Function<T, K> getter, Class<T> clazz, R map, Supplier<R> createMapSupplier) {
		return extendByFieldOrEmpty(getter, UwEnum.values(clazz), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return {@code null}.
	 *
	 * <p>Wraps {@link #extendByFieldOrElse(Function, Iterator, Map, Supplier, Map)}
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrNull(Function<T, K> getter, Iterator<T> entries, R map, Supplier<R> createMapSupplier) {
		return extendByFieldOrElse(getter, entries, map, createMapSupplier, (R) null);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return {@code null}.
	 *
	 * <p>Wraps {@link #extendByFieldOrNull(Function, Iterator, Map, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrNull(Function<T, K> getter, Iterable<T> entries, R map, Supplier<R> createMapSupplier) {
		return extendByFieldOrNull(getter, (Iterator<T>) UwObject.ifNotNull(entries, Iterable::iterator), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return {@code null}.
	 *
	 * <p>Wraps {@link #extendByFieldOrNull(Function, Iterator, Map, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrNull(Function<T, K> getter, BaseStream<T, ?> entries, R map, Supplier<R> createMapSupplier) {
		return extendByFieldOrNull(getter, (Iterator<T>) UwObject.ifNotNull(entries, BaseStream::iterator), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return {@code null}.
	 *
	 * <p>Wraps {@link #extendByFieldOrNull(Function, Iterator, Map, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrNull(Function<T, K> getter, T[] entries, R map, Supplier<R> createMapSupplier) {
		return extendByFieldOrNull(getter, (Iterator<T>) UwObject.ifNotNull(entries, UwArray::iterator), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping enum constants by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link #extendByFieldOrNull(Function, Object[], Map, Supplier)}
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
	public static <K, T extends Enum<T>, R extends Map<K, T>> R extendByFieldOrNull(Function<T, K> getter, Class<T> clazz, R map, Supplier<R> createMapSupplier) {
		return extendByFieldOrNull(getter, UwEnum.values(clazz), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return the unmodified map.
	 *
	 * <p>Wraps {@link #extendByFieldOrElse(Function, Iterator, Map, Supplier, Map)}
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrSelf(Function<T, K> getter, Iterator<T> entries, R map, Supplier<R> createMapSupplier) {
		return extendByFieldOrElse(getter, entries, map, createMapSupplier, map);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return the unmodified map.
	 *
	 * <p>Wraps {@link #extendByFieldOrSelf(Function, Iterator, Map, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrSelf(Function<T, K> getter, Iterable<T> entries, R map, Supplier<R> createMapSupplier) {
		return extendByFieldOrSelf(getter, (Iterator<T>) UwObject.ifNotNull(entries, Iterable::iterator), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return the unmodified map.
	 *
	 * <p>Wraps {@link #extendByFieldOrSelf(Function, Iterator, Map, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrSelf(Function<T, K> getter, BaseStream<T, ?> entries, R map, Supplier<R> createMapSupplier) {
		return extendByFieldOrSelf(getter, (Iterator<T>) UwObject.ifNotNull(entries, BaseStream::iterator), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return the unmodified map.
	 *
	 * <p>Wraps {@link #extendByFieldOrSelf(Function, Iterator, Map, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R extendByFieldOrSelf(Function<T, K> getter, T[] entries, R map, Supplier<R> createMapSupplier) {
		return extendByFieldOrSelf(getter, (Iterator<T>) UwObject.ifNotNull(entries, UwArray::iterator), map, createMapSupplier);
	}

	/**
	 * Safely extend the provided map by mapping entries to their field or return the unmodified map.
	 *
	 * <p>Wraps {@link #extendByFieldOrSelf(Function, Object[], Map, Supplier)}
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
	public static <K, T extends Enum<T>, R extends Map<K, T>> R extendByFieldOrSelf(Function<T, K> getter, Class<T> clazz, R map, Supplier<R> createMapSupplier) {
		return extendByFieldOrSelf(getter, UwEnum.values(clazz), map, createMapSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Creates a fresh map instance from the provided create map supplier
	 * and wraps {@link #extendByFieldOrElse(Function, Iterator, Map, Supplier, Map)}
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
	public static <K, T, R extends Map<K, T>> R createByFieldOrElse(Function<T, K> getter, Iterator<T> entries, Supplier<R> createMapSupplier, R defaultValue) {
		R map = UwObject.ifNotNull(createMapSupplier, Supplier::get);

		return extendByFieldOrElse(getter, entries, map, createMapSupplier, defaultValue);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Iterator, Supplier, Map)}
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
	public static <K, T, R extends Map<K, T>> R createByFieldOrElse(Function<T, K> getter, Iterable<T> entries, Supplier<R> createMapSupplier, R defaultValue) {
		return createByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, Iterable::iterator), createMapSupplier, defaultValue);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Iterator, Supplier, Map)}
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
	public static <K, T, R extends Map<K, T>> R createByFieldOrElse(Function<T, K> getter, BaseStream<T, ?> entries, Supplier<R> createMapSupplier, R defaultValue) {
		return createByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, BaseStream::iterator), createMapSupplier, defaultValue);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Iterator, Supplier, Map)}
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
	public static <K, T, R extends Map<K, T>> R createByFieldOrElse(Function<T, K> getter, T[] entries, Supplier<R> createMapSupplier, R defaultValue) {
		return createByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, UwArray::iterator), createMapSupplier, defaultValue);
	}

	/**
	 * Safely create a new map instance and extend it by mapping enum constants by theirs field or return a default value.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Object[], Supplier, Map)}
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
	public static <K, T extends Enum<T>, R extends Map<K, T>> R createByFieldOrElse(Function<T, K> getter, Class<T> clazz, Supplier<R> createMapSupplier, R defaultValue) {
		return createByFieldOrElse(getter, UwEnum.values(clazz), createMapSupplier, defaultValue);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries to their field or return a default value.
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
	public static <K, T, R extends Map<K, T>> R createByFieldOrElse(Function<T, K> getter, Iterator<T> entries, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return UwObject.ifNull(createByFieldOrNull(getter, entries, createMapSupplier), defaultValueSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Iterator, Supplier, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R createByFieldOrElse(Function<T, K> getter, Iterable<T> entries, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return createByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, Iterable::iterator), createMapSupplier, defaultValueSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Iterator, Supplier, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R createByFieldOrElse(Function<T, K> getter, BaseStream<T, ?> entries, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return createByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, BaseStream::iterator), createMapSupplier, defaultValueSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Iterator, Supplier, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R createByFieldOrElse(Function<T, K> getter, T[] entries, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return createByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, UwArray::iterator), createMapSupplier, defaultValueSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping enum constants by theirs field or return a default value.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Object[], Supplier, Supplier)}
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
	public static <K, T extends Enum<T>, R extends Map<K, T>> R createByFieldOrElse(Function<T, K> getter, Class<T> clazz, Supplier<R> createMapSupplier, Supplier<R> defaultValueSupplier) {
		return createByFieldOrElse(getter, UwEnum.values(clazz), createMapSupplier, defaultValueSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries to their field or return a new empty map instance.
	 *
	 * <p>Creates a fresh map instance from the provided create map supplier
	 * and wraps {@link #extendByFieldOrElse(Function, Iterator, Map, Supplier, Map)}
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
	public static <K, T, R extends Map<K, T>> R createByFieldOrEmpty(Function<T, K> getter, Iterator<T> entries, Supplier<R> createMapSupplier) {
		R map = UwObject.ifNotNull(createMapSupplier, Supplier::get);

		return extendByFieldOrElse(getter, entries, map, createMapSupplier, map);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries to their field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #createByFieldOrEmpty(Function, Iterator, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R createByFieldOrEmpty(Function<T, K> getter, Iterable<T> entries, Supplier<R> createMapSupplier) {
		return createByFieldOrEmpty(getter, (Iterator<T>) UwObject.ifNotNull(entries, Iterable::iterator), createMapSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries to their field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #createByFieldOrEmpty(Function, Iterator, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R createByFieldOrEmpty(Function<T, K> getter, BaseStream<T, ?> entries, Supplier<R> createMapSupplier) {
		return createByFieldOrEmpty(getter, (Iterator<T>) UwObject.ifNotNull(entries, BaseStream::iterator), createMapSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries to their field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #createByFieldOrEmpty(Function, Iterator, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R createByFieldOrEmpty(Function<T, K> getter, T[] entries, Supplier<R> createMapSupplier) {
		return createByFieldOrEmpty(getter, (Iterator<T>) UwObject.ifNotNull(entries, UwArray::iterator), createMapSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping enum constants by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #createByFieldOrEmpty(Function, Object[], Supplier)}
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
	public static <K, T extends Enum<T>, R extends Map<K, T>> R createByFieldOrEmpty(Function<T, K> getter, Class<T> clazz, Supplier<R> createMapSupplier) {
		return createByFieldOrEmpty(getter, UwEnum.values(clazz), createMapSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries to their field or return {@code null}.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Iterator, Supplier, Map)}
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
	public static <K, T, R extends Map<K, T>> R createByFieldOrNull(Function<T, K> getter, Iterator<T> entries, Supplier<R> createMapSupplier) {
		return createByFieldOrElse(getter, entries, createMapSupplier, (R) null);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries to their field or return {@code null}.
	 *
	 * <p>Wraps {@link #createByFieldOrNull(Function, Iterator, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R createByFieldOrNull(Function<T, K> getter, Iterable<T> entries, Supplier<R> createMapSupplier) {
		return createByFieldOrNull(getter, (Iterator<T>) UwObject.ifNotNull(entries, Iterable::iterator), createMapSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries to their field or return {@code null}.
	 *
	 * <p>Wraps {@link #createByFieldOrNull(Function, Iterator, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R createByFieldOrNull(Function<T, K> getter, BaseStream<T, ?> entries, Supplier<R> createMapSupplier) {
		return createByFieldOrNull(getter, (Iterator<T>) UwObject.ifNotNull(entries, BaseStream::iterator), createMapSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping entries to their field or return {@code null}.
	 *
	 * <p>Wraps {@link #createByFieldOrNull(Function, Iterator, Supplier)}
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
	public static <K, T, R extends Map<K, T>> R createByFieldOrNull(Function<T, K> getter, T[] entries, Supplier<R> createMapSupplier) {
		return createByFieldOrNull(getter, (Iterator<T>) UwObject.ifNotNull(entries, UwArray::iterator), createMapSupplier);
	}

	/**
	 * Safely create a new map instance and extend it by mapping enum constants by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link #createByFieldOrNull(Function, Object[], Supplier)}
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
	public static <K, T extends Enum<T>, R extends Map<K, T>> R createByFieldOrNull(Function<T, K> getter, Class<T> clazz, Supplier<R> createMapSupplier) {
		return createByFieldOrNull(getter, UwEnum.values(clazz), createMapSupplier);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Iterator, Supplier, Map)}
	 * w/ {@link HashMap#HashMap()} as the create map supplier.
	 *
	 * @param getter		supplier for getting an entry's field
	 * @param entries		iterator of entries
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			entry type
	 * @return				new extended {@link HashMap} instance or the default value
	 */
	public static <K, T> Map<K, T> createByFieldOrElse(Function<T, K> getter, Iterator<T> entries, Map<K, T> defaultValue) {
		return createByFieldOrElse(getter, entries, HashMap::new, defaultValue);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Iterator, Map)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter		supplier for getting an entry's field
	 * @param entries		iterable of entries
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			entry type
	 * @return				new extended {@link HashMap} instance or the default value
	 */
	public static <K, T> Map<K, T> createByFieldOrElse(Function<T, K> getter, Iterable<T> entries, Map<K, T> defaultValue) {
		return createByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, Iterable::iterator), defaultValue);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Iterator, Map)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter		supplier for getting an entry's field
	 * @param entries		stream of entries
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			entry type
	 * @return				new extended {@link HashMap} instance or the default value
	 */
	public static <K, T> Map<K, T> createByFieldOrElse(Function<T, K> getter, BaseStream<T, ?> entries, Map<K, T> defaultValue) {
		return createByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, BaseStream::iterator), defaultValue);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Iterator, Map)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter		supplier for getting an entry's field
	 * @param entries		array of entries
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			entry type
	 * @return				new extended {@link HashMap} instance or the default value
	 */
	public static <K, T> Map<K, T> createByFieldOrElse(Function<T, K> getter, T[] entries, Map<K, T> defaultValue) {
		return createByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, UwArray::iterator), defaultValue);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping enum constants by theirs field or return a default value.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Object[], Map)}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter		supplier for getting an enum's field
	 * @param clazz			enum class
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			enum type
	 * @return				new extended {@link HashMap} instance or the default value
	 */
	public static <K, T extends Enum<T>> Map<K, T> createByFieldOrElse(Function<T, K> getter, Class<T> clazz, Map<K, T> defaultValue) {
		return createByFieldOrElse(getter, UwEnum.values(clazz), defaultValue);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries to their field or return a default value.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterator of entries
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @return						new extended {@link HashMap} instance or the default value
	 */
	public static <K, T> Map<K, T> createByFieldOrElse(Function<T, K> getter, Iterator<T> entries, Supplier<Map<K, T>> defaultValueSupplier) {
		return UwObject.ifNull(createByFieldOrNull(getter, entries, defaultValueSupplier), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Iterator, Supplier)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterable of entries
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @return						new extended {@link HashMap} instance or the default value
	 */
	public static <K, T> Map<K, T> createByFieldOrElse(Function<T, K> getter, Iterable<T> entries, Supplier<Map<K, T>> defaultValueSupplier) {
		return createByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, Iterable::iterator), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Iterator, Supplier)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				stream of entries
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @return						new extended {@link HashMap} instance or the default value
	 */
	public static <K, T> Map<K, T> createByFieldOrElse(Function<T, K> getter, BaseStream<T, ?> entries, Supplier<Map<K, T>> defaultValueSupplier) {
		return createByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, BaseStream::iterator), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Iterator, Supplier)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				array of entries
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @return						new extended {@link HashMap} instance or the default value
	 */
	public static <K, T> Map<K, T> createByFieldOrElse(Function<T, K> getter, T[] entries, Supplier<Map<K, T>> defaultValueSupplier) {
		return createByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, UwArray::iterator), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping enum constants by theirs field or return a default value.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Object[], Supplier)}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter				supplier for getting an enum's field
	 * @param clazz					enum class
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <K>					field type
	 * @param <T>					enum type
	 * @return						new extended {@link HashMap} instance or the default value
	 */
	public static <K, T extends Enum<T>> Map<K, T> createByFieldOrElse(Function<T, K> getter, Class<T> clazz, Supplier<Map<K, T>> defaultValueSupplier) {
		return createByFieldOrElse(getter, UwEnum.values(clazz), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries to their field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #createByFieldOrEmpty(Function, Iterator, Supplier)}
	 * w/ {@link HashMap#HashMap()} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	iterator of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link HashMap} instance or an empty one
	 */
	public static <K, T> Map<K, T> createByFieldOrEmpty(Function<T, K> getter, Iterator<T> entries) {
		return createByFieldOrEmpty(getter, entries, HashMap::new);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries to their field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #createByFieldOrEmpty(Function, Iterator)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	iterable of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link HashMap} instance or an empty one
	 */
	public static <K, T> Map<K, T> createByFieldOrEmpty(Function<T, K> getter, Iterable<T> entries) {
		return createByFieldOrEmpty(getter, (Iterator<T>) UwObject.ifNotNull(entries, Iterable::iterator));
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries to their field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #createByFieldOrEmpty(Function, Iterator)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	stream of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link HashMap} instance or an empty one
	 */
	public static <K, T> Map<K, T> createByFieldOrEmpty(Function<T, K> getter, BaseStream<T, ?> entries) {
		return createByFieldOrEmpty(getter, (Iterator<T>) UwObject.ifNotNull(entries, BaseStream::iterator));
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries to their field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #createByFieldOrEmpty(Function, Iterator)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	array of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link HashMap} instance or an empty one
	 */
	public static <K, T> Map<K, T> createByFieldOrEmpty(Function<T, K> getter, T[] entries) {
		return createByFieldOrEmpty(getter, (Iterator<T>) UwObject.ifNotNull(entries, UwArray::iterator));
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping enum constants by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #createByFieldOrEmpty(Function, Object[])}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter	supplier for getting an enum's field
	 * @param clazz		enum class
	 * @param <K>		field type
	 * @param <T>		enum type
	 * @return			new extended {@link HashMap} instance or an empty one
	 */
	public static <K, T extends Enum<T>> Map<K, T> createByFieldOrEmpty(Function<T, K> getter, Class<T> clazz) {
		return createByFieldOrEmpty(getter, UwEnum.values(clazz));
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries to their field or return {@code null}.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Iterator, Map)}
	 * w/ {@code null} as the default value.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	iterator of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link HashMap} instance or {@code null}
	 */
	public static <K, T> Map<K, T> createByFieldOrNull(Function<T, K> getter, Iterator<T> entries) {
		return createByFieldOrElse(getter, entries, (Map<K, T>) null);
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries to their field or return {@code null}.
	 *
	 * <p>Wraps {@link #createByFieldOrNull(Function, Iterator)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	iterable of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link HashMap} instance or {@code null}
	 */
	public static <K, T> Map<K, T> createByFieldOrNull(Function<T, K> getter, Iterable<T> entries) {
		return createByFieldOrNull(getter, (Iterator<T>) UwObject.ifNotNull(entries, Iterable::iterator));
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries to their field or return {@code null}.
	 *
	 * <p>Wraps {@link #createByFieldOrNull(Function, Iterator)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	stream of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link HashMap} instance or {@code null}
	 */
	public static <K, T> Map<K, T> createByFieldOrNull(Function<T, K> getter, BaseStream<T, ?> entries) {
		return createByFieldOrNull(getter, (Iterator<T>) UwObject.ifNotNull(entries, BaseStream::iterator));
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping entries to their field or return {@code null}.
	 *
	 * <p>Wraps {@link #createByFieldOrNull(Function, Iterator)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	array of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link HashMap} instance or {@code null}
	 */
	public static <K, T> Map<K, T> createByFieldOrNull(Function<T, K> getter, T[] entries) {
		return createByFieldOrNull(getter, (Iterator<T>) UwObject.ifNotNull(entries, UwArray::iterator));
	}

	/**
	 * Safely create a new {@link HashMap} instance and extend it by mapping enum constants by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link #createByFieldOrNull(Function, Object[])}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter	supplier for getting an enum's field
	 * @param clazz		enum class
	 * @param <K>		field type
	 * @param <T>		enum type
	 * @return			new extended {@link HashMap} instance or {@code null}
	 */
	public static <K, T extends Enum<T>> Map<K, T> createByFieldOrNull(Function<T, K> getter, Class<T> clazz) {
		return createByFieldOrNull(getter, UwEnum.values(clazz));
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createByFieldOrElse(Function, Iterator, Supplier, Map)}
	 * w/ {@link ConcurrentHashMap#ConcurrentHashMap()} as the create map supplier.
	 *
	 * @param getter		supplier for getting an entry's field
	 * @param entries		iterator of entries
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			entry type
	 * @return				new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T> ConcurrentMap<K, T> createConcurrentByFieldOrElse(Function<T, K> getter, Iterator<T> entries, ConcurrentMap<K, T> defaultValue) {
		return createByFieldOrElse(getter, entries, ConcurrentHashMap::new, defaultValue);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createConcurrentByFieldOrElse(Function, Iterator, ConcurrentMap)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter		supplier for getting an entry's field
	 * @param entries		iterable of entries
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			entry type
	 * @return				new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T> ConcurrentMap<K, T> createConcurrentByFieldOrElse(Function<T, K> getter, Iterable<T> entries, ConcurrentMap<K, T> defaultValue) {
		return createConcurrentByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, Iterable::iterator), defaultValue);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createConcurrentByFieldOrElse(Function, Iterator, ConcurrentMap)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter		supplier for getting an entry's field
	 * @param entries		stream of entries
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			entry type
	 * @return				new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T> ConcurrentMap<K, T> createConcurrentByFieldOrElse(Function<T, K> getter, BaseStream<T, ?> entries, ConcurrentMap<K, T> defaultValue) {
		return createConcurrentByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, BaseStream::iterator), defaultValue);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createConcurrentByFieldOrElse(Function, Iterator, ConcurrentMap)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter		supplier for getting an entry's field
	 * @param entries		array of entries
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			entry type
	 * @return				new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T> ConcurrentMap<K, T> createConcurrentByFieldOrElse(Function<T, K> getter, T[] entries, ConcurrentMap<K, T> defaultValue) {
		return createConcurrentByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, UwArray::iterator), defaultValue);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping enum constants by theirs field or return a default value.
	 *
	 * <p>Wraps {@link #createConcurrentByFieldOrElse(Function, Object[], ConcurrentMap)}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter		supplier for getting an enum's field
	 * @param clazz			enum class
	 * @param defaultValue 	default value to return on failure
	 * @param <K>			field type
	 * @param <T>			enum type
	 * @return				new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T extends Enum<T>> ConcurrentMap<K, T> createConcurrentByFieldOrElse(Function<T, K> getter, Class<T> clazz, ConcurrentMap<K, T> defaultValue) {
		return createConcurrentByFieldOrElse(getter, UwEnum.values(clazz), defaultValue);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries to their field or return a default value.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterator of entries
	 * @param defaultValueSupplier	supplier from which return get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @return						new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T> ConcurrentMap<K, T> createConcurrentByFieldOrElse(Function<T, K> getter, Iterator<T> entries, Supplier<ConcurrentMap<K, T>> defaultValueSupplier) {
		return UwObject.ifNull(createConcurrentByFieldOrNull(getter, entries), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createConcurrentByFieldOrElse(Function, Iterator, Supplier)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				iterable of entries
	 * @param defaultValueSupplier	supplier from which return get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @return						new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T> ConcurrentMap<K, T> createConcurrentByFieldOrElse(Function<T, K> getter, Iterable<T> entries, Supplier<ConcurrentMap<K, T>> defaultValueSupplier) {
		return createConcurrentByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, Iterable::iterator), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createConcurrentByFieldOrElse(Function, Iterator, Supplier)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				stream of entries
	 * @param defaultValueSupplier	supplier from which return get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @return						new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T> ConcurrentMap<K, T> createConcurrentByFieldOrElse(Function<T, K> getter, BaseStream<T, ?> entries, Supplier<ConcurrentMap<K, T>> defaultValueSupplier) {
		return createConcurrentByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, BaseStream::iterator), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries to their field or return a default value.
	 *
	 * <p>Wraps {@link #createConcurrentByFieldOrElse(Function, Iterator, Supplier)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter				supplier for getting an entry's field
	 * @param entries				array of entries
	 * @param defaultValueSupplier	supplier from which return get the default value
	 * @param <K>					field type
	 * @param <T>					entry type
	 * @return						new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T> ConcurrentMap<K, T> createConcurrentByFieldOrElse(Function<T, K> getter, T[] entries, Supplier<ConcurrentMap<K, T>> defaultValueSupplier) {
		return createConcurrentByFieldOrElse(getter, (Iterator<T>) UwObject.ifNotNull(entries, UwArray::iterator), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping enum constants by theirs field or return a default value.
	 *
	 * <p>Wraps {@link #createConcurrentByFieldOrElse(Function, Object[], Supplier)}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter				supplier for getting an enum's field
	 * @param clazz					enum class
	 * @param defaultValueSupplier	supplier from which return get the default value
	 * @param <K>					field type
	 * @param <T>					enum type
	 * @return						new extended {@link ConcurrentHashMap} instance or the default value
	 */
	public static <K, T extends Enum<T>> ConcurrentMap<K, T> createConcurrentByFieldOrElse(Function<T, K> getter, Class<T> clazz, Supplier<ConcurrentMap<K, T>> defaultValueSupplier) {
		return createConcurrentByFieldOrElse(getter, UwEnum.values(clazz), defaultValueSupplier);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries to their field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #createByFieldOrEmpty(Function, Iterator, Supplier)}
	 * w/ {@link ConcurrentHashMap#ConcurrentHashMap()} as the create map supplier.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	iterator of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link ConcurrentHashMap} instance or an empty one
	 */
	public static <K, T> ConcurrentMap<K, T> createConcurrentByFieldOrEmpty(Function<T, K> getter, Iterator<T> entries) {
		return createByFieldOrEmpty(getter, entries, ConcurrentHashMap::new);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries to their field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #createConcurrentByFieldOrEmpty(Function, Iterator)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	iterable of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link ConcurrentHashMap} instance or an empty one
	 */
	public static <K, T> ConcurrentMap<K, T> createConcurrentByFieldOrEmpty(Function<T, K> getter, Iterable<T> entries) {
		return createConcurrentByFieldOrEmpty(getter, (Iterator<T>) UwObject.ifNotNull(entries, Iterable::iterator));
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries to their field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #createConcurrentByFieldOrEmpty(Function, Iterator)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	stream of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link ConcurrentHashMap} instance or an empty one
	 */
	public static <K, T> ConcurrentMap<K, T> createConcurrentByFieldOrEmpty(Function<T, K> getter, BaseStream<T, ?> entries) {
		return createConcurrentByFieldOrEmpty(getter, (Iterator<T>) UwObject.ifNotNull(entries, BaseStream::iterator));
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries to their field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #createConcurrentByFieldOrEmpty(Function, Iterator)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	array of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link ConcurrentHashMap} instance or an empty one
	 */
	public static <K, T> ConcurrentMap<K, T> createConcurrentByFieldOrEmpty(Function<T, K> getter, T[] entries) {
		return createConcurrentByFieldOrEmpty(getter, (Iterator<T>) UwObject.ifNotNull(entries, UwArray::iterator));
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping enum constants by theirs field or return a new empty map instance.
	 *
	 * <p>Wraps {@link #createConcurrentByFieldOrEmpty(Function, Object[])}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter	supplier for getting an enum's field
	 * @param clazz		enum class
	 * @param <K>		field type
	 * @param <T>		enum type
	 * @return			new extended {@link ConcurrentHashMap} instance or an empty one
	 */
	public static <K, T extends Enum<T>> ConcurrentMap<K, T> createConcurrentByFieldOrEmpty(Function<T, K> getter, Class<T> clazz) {
		return createConcurrentByFieldOrEmpty(getter, UwEnum.values(clazz));
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries to their field or return {@code null}.
	 *
	 * <p>Wraps {@link #createConcurrentByFieldOrElse(Function, Iterator, ConcurrentMap)}
	 * w/ {@code null} as the default value.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	iterator of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link ConcurrentHashMap} instance or {@code null}
	 */
	public static <K, T> ConcurrentMap<K, T> createConcurrentByFieldOrNull(Function<T, K> getter, Iterator<T> entries) {
		return createConcurrentByFieldOrElse(getter, entries, (ConcurrentMap<K, T>) null);
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries to their field or return {@code null}.
	 *
	 * <p>Wraps {@link #createConcurrentByFieldOrNull(Function, Iterator)}
	 * w/ {@link Iterable#iterator()} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	iterable of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link ConcurrentHashMap} instance or {@code null}
	 */
	public static <K, T> ConcurrentMap<K, T> createConcurrentByFieldOrNull(Function<T, K> getter, Iterable<T> entries) {
		return createConcurrentByFieldOrNull(getter, (Iterator<T>) UwObject.ifNotNull(entries, Iterable::iterator));
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries to their field or return {@code null}.
	 *
	 * <p>Wraps {@link #createConcurrentByFieldOrNull(Function, Iterator)}
	 * w/ {@link BaseStream#iterator()} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	stream of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link ConcurrentHashMap} instance or {@code null}
	 */
	public static <K, T> ConcurrentMap<K, T> createConcurrentByFieldOrNull(Function<T, K> getter, BaseStream<T, ?> entries) {
		return createConcurrentByFieldOrNull(getter, (Iterator<T>) UwObject.ifNotNull(entries, BaseStream::iterator));
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping entries to their field or return {@code null}.
	 *
	 * <p>Wraps {@link #createConcurrentByFieldOrNull(Function, Iterator)}
	 * w/ {@link UwArray#iterator(Object[])} call.
	 *
	 * @param getter	supplier for getting an entry's field
	 * @param entries	array of entries
	 * @param <K>		field type
	 * @param <T>		entry type
	 * @return			new extended {@link ConcurrentHashMap} instance or {@code null}
	 */
	public static <K, T> ConcurrentMap<K, T> createConcurrentByFieldOrNull(Function<T, K> getter, T[] entries) {
		return createConcurrentByFieldOrNull(getter, (Iterator<T>) UwObject.ifNotNull(entries, UwArray::iterator));
	}

	/**
	 * Safely create a new {@link ConcurrentHashMap} instance and extend it by mapping enum constants by theirs field or return {@code null}.
	 *
	 * <p>Wraps {@link #createConcurrentByFieldOrNull(Function, Object[])}
	 * w/ {@link UwEnum#values(Class)} call.
	 *
	 * @param getter	supplier for getting an enum's field
	 * @param clazz		enum class
	 * @param <K>		field type
	 * @param <T>		enum type
	 * @return			new extended {@link ConcurrentHashMap} instance or {@code null}
	 */
	public static <K, T extends Enum<T>> ConcurrentMap<K, T> createConcurrentByFieldOrNull(Function<T, K> getter, Class<T> clazz) {
		return createConcurrentByFieldOrNull(getter, UwEnum.values(clazz));
	}

	private UwMap() {
		throw new UnsupportedOperationException();
	}
}
