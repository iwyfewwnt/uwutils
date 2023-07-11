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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.*;

/**
 * A set utility.
 *
 * <p>{@code UwSet} is the utility class
 * that provide functionality to operate
 * with sets.
 */
@SuppressWarnings("unused")
public final class UwSet {

	/**
	 * An empty set instance.
	 */
	@SuppressWarnings("rawtypes")
	public static final Set EMPTY = Collections.EMPTY_SET;

	/**
	 * Check if the provided set is unmodifiable.
	 *
	 * <p>Redirects call to the {@link UwCollection#isUnmodifiable(Collection)}.
	 *
	 * @param set	set to check for
	 * @return		boolean value as a result,
	 * 				true - yes, false - no
	 */
	public static boolean isUnmodifiable(Set<?> set) {
		return UwCollection.isUnmodifiable(set);
	}

	/**
	 * Get an unmodifiable view of the provided set.
	 *
	 * @param set	set to get the view for
	 * @param <T>	element type
	 * @return		unmodifiable view of the set
	 */
	public static <T> Set<T> toUnmodifiable(Set<T> set) {
		if (set == null) {
			return null;
		}

		if (isUnmodifiable(set)) {
			return set;
		}

		return Collections.unmodifiableSet(set);
	}

	/**
	 * Safely convert a collection of objects to a set or return a default value.
	 *
	 * @param collection	collection of objects
	 * @param defaultValue	default value to return on failure
	 * @param <T>			object type
	 * @return				set of objects or the default value
	 */
	public static <T> Set<T> createOrElse(Collection<T> collection, Set<T> defaultValue) {
		return createOrElse(collection, HashSet::new, defaultValue);
	}

	/**
	 * Safely convert a collection of objects to a set or return a default value.
	 *
	 * @param collection			collection of objects
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <T>					object type
	 * @return						set of objects or the default value
	 */
	public static <T> Set<T> createOrElse(Collection<T> collection, Supplier<Set<T>> defaultValueSupplier) {
		return UwObject.ifNull(createOrNull(collection), defaultValueSupplier);
	}

	/**
	 * Safely convert a collection of objects to a set or return an empty one.
	 *
	 * <p>Wraps {@link #createOrElse(Collection, Supplier)}
	 * w/ {@link HashSet#HashSet()} as the default value supplier.
	 *
	 * @param collection	collection of objects
	 * @param <T>			object type
	 * @return				set of objects
	 */
	public static <T> Set<T> createOrEmpty(Collection<T> collection) {
		return createOrElse(collection, HashSet::new);
	}

	/**
	 * Safely convert a collection of objects to a set or return {@code null}.
	 *
	 * <p>Wraps {@link #createOrElse(Collection, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param collection	collection of objects
	 * @param <T>			object type
	 * @return				set of objects or {@code null}
	 */
	public static <T> Set<T> createOrNull(Collection<T> collection) {
		return createOrElse(collection, (Set<T>) null);
	}

	/**
	 * Safely convert an array of objects to a set or return a default value.
	 *
	 * <p>Wraps {@link #createOrElse(Collection, Set)}
	 * w/ {@link Arrays#asList(Object[])} call.
	 *
	 * @param array			array of objects
	 * @param defaultValue	default value to return on failure
	 * @param <T>			object type
	 * @return				set of objects or the default value
	 */
	public static <T> Set<T> createOrElse(T[] array, Set<T> defaultValue) {
		return createOrElse((Collection<T>) UwObject.ifNotNull(array, Arrays::asList), defaultValue);
	}

	/**
	 * Safely convert an array of objects to a set or return a default value.
	 *
	 * @param array					array of objects
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <T>					object type
	 * @return						set of objects or the default value
	 */
	public static <T> Set<T> createOrElse(T[] array, Supplier<Set<T>> defaultValueSupplier) {
		return UwObject.ifNull(createOrNull(array), defaultValueSupplier);
	}

	/**
	 * Safely convert an array of objects to a set or return an empty one.
	 *
	 * <p>Wraps {@link #createOrElse(Object[], Supplier)}
	 * w/ {@link HashSet#HashSet()} as the default value supplier.
	 *
	 * @param array		array of objects
	 * @param <T>		object type
	 * @return			set of objects
	 */
	public static <T> Set<T> createOrEmpty(T[] array) {
		return createOrElse(array, HashSet::new);
	}

	/**
	 * Safely convert an array of objects to a set or return {@code null}.
	 *
	 * <p>Wraps {@link #createOrElse(Object[], Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param array		array of objects
	 * @param <T>		object type
	 * @return			set of objects or {@code null}
	 */
	public static <T> Set<T> createOrNull(T[] array) {
		return createOrElse(array, (Set<T>) null);
	}

	/**
	 * Safely convert a stream of objects to a set or return a default value.
	 *
	 * <p>Wraps {@link Stream#collect(Collector)}
	 * w/ {@link Collectors#toSet()} call.
	 *
	 * @param stream		stream of objects
	 * @param defaultValue 	default value to return on failure
	 * @param <T>			object type
	 * @return				set of objects or the default value
	 */
	public static <T> Set<T> createOrElse(Stream<T> stream, Set<T> defaultValue) {
		if (stream == null) {
			return defaultValue;
		}

		return stream.collect(Collectors.toSet());
	}

	/**
	 * Safely convert a stream of objects to a set or return a default value.
	 *
	 * @param stream				stream of objects
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <T>					object type
	 * @return						set of objects or the default value
	 */
	public static <T> Set<T> createOrElse(Stream<T> stream, Supplier<Set<T>> defaultValueSupplier) {
		return UwObject.ifNull(createOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of objects to a set or return an empty one.
	 *
	 * <p>Wraps {@link #createOrElse(Stream, Supplier)}
	 * w/ {@link HashSet#HashSet()} as the default value supplier.
	 *
	 * @param stream	stream of objects
	 * @param <T>		object type
	 * @return			set of objects
	 */
	public static <T> Set<T> createOrEmpty(Stream<T> stream) {
		return createOrElse(stream, HashSet::new);
	}

	/**
	 * Safely convert a stream of objects to a set or return {@code null}.
	 *
	 * <p>Wraps {@link #createOrElse(Stream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream	stream of objects
	 * @param <T>		object type
	 * @return			set of objects or {@code null}
	 */
	public static <T> Set<T> createOrNull(Stream<T> stream) {
		return createOrElse(stream, (Set<T>) null);
	}

	/**
	 * Safely convert a stream of integers to a set or return a default value.
	 *
	 * <p>Wraps {@link #createOrElse(Stream, Set)}
	 * w/ {@link IntStream#boxed()} call.
	 *
	 * @param stream		stream of integers
	 * @param defaultValue 	default value to return on failure
	 * @return				set of integers or the default value
	 */
	public static Set<Integer> createOrElse(IntStream stream, Set<Integer> defaultValue) {
		return createOrElse(UwObject.ifNotNull(stream, IntStream::boxed), defaultValue);
	}

	/**
	 * Safely convert a stream of integers to a set or return a default value.
	 *
	 * @param stream				stream of integers
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						set of integers or the default value
	 */
	public static Set<Integer> createOrElse(IntStream stream, Supplier<Set<Integer>> defaultValueSupplier) {
		return UwObject.ifNull(createOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of integers to a set or return an empty one.
	 *
	 * <p>Wraps {@link #createOrElse(IntStream, Supplier)}
	 * w/ {@link HashSet#HashSet()} as the default value supplier.
	 *
	 * @param stream	stream of integers
	 * @return			set of integers
	 */
	public static Set<Integer> createOrEmpty(IntStream stream) {
		return createOrElse(stream, HashSet::new);
	}

	/**
	 * Safely convert a stream of integers to a set or return {@code null}.
	 *
	 * <p>Wraps {@link #createOrElse(IntStream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream	stream of integers
	 * @return			set of integers or {@code null}
	 */
	public static Set<Integer> createOrNull(IntStream stream) {
		return createOrElse(stream, (Set<Integer>) null);
	}

	/**
	 * Safely convert a stream of doubles to a set or return a default value.
	 *
	 * <p>Wraps {@link #createOrElse(Stream, Set)}
	 * w/ {@link DoubleStream#boxed()} call.
	 *
	 * @param stream		stream of doubles
	 * @param defaultValue 	default value to return on failure
	 * @return				set of doubles or the default value
	 */
	public static Set<Double> createOrElse(DoubleStream stream, Set<Double> defaultValue) {
		return createOrElse(UwObject.ifNotNull(stream, DoubleStream::boxed), defaultValue);
	}

	/**
	 * Safely convert a stream of doubles to a set or return a default value.
	 *
	 * @param stream				stream of doubles
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						set of doubles or the default value
	 */
	public static Set<Double> createOrElse(DoubleStream stream, Supplier<Set<Double>> defaultValueSupplier) {
		return UwObject.ifNull(createOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of doubles to a set or return an empty one.
	 *
	 * <p>Wraps {@link #createOrElse(DoubleStream, Supplier)}
	 * w/ {@link HashSet#HashSet()} as the default value supplier.
	 *
	 * @param stream	stream of doubles
	 * @return			set of doubles
	 */
	public static Set<Double> createOrEmpty(DoubleStream stream) {
		return createOrElse(stream, HashSet::new);
	}

	/**
	 * Safely convert a stream of doubles to a set or return {@code null}.
	 *
	 * <p>Wraps {@link #createOrElse(DoubleStream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream	stream of doubles
	 * @return			set of doubles or {@code null}
	 */
	public static Set<Double> createOrNull(DoubleStream stream) {
		return createOrElse(stream, (Set<Double>) (null));
	}

	/**
	 * Safely convert a stream of longs to a set or return a default value.
	 *
	 * <p>Wraps {@link #createOrElse(Stream, Set)}
	 * w/ {@link LongStream#boxed()} call.
	 *
	 * @param stream		stream of longs
	 * @param defaultValue 	default value to return on failure
	 * @return				set of longs or the default value
	 */
	public static Set<Long> createOrElse(LongStream stream, Set<Long> defaultValue) {
		return createOrElse(UwObject.ifNotNull(stream, LongStream::boxed), defaultValue);
	}

	/**
	 * Safely convert a stream of longs to a set or return a default value.
	 *
	 * @param stream				stream of longs
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						set of longs or the default value
	 */
	public static Set<Long> createOrElse(LongStream stream, Supplier<Set<Long>> defaultValueSupplier) {
		return UwObject.ifNull(createOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of longs to a set or return an empty one.
	 *
	 * <p>Wraps {@link #createOrElse(LongStream, Supplier)}
	 * w/ {@link HashSet#HashSet()} as the default value supplier.
	 *
	 * @param stream	stream of longs
	 * @return			set of longs
	 */
	public static Set<Long> createOrEmpty(LongStream stream) {
		return createOrElse(stream, HashSet::new);
	}

	/**
	 * Safely convert a stream of longs to a set or return {@code null}.
	 *
	 * <p>Wraps {@link #createOrElse(LongStream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream	stream of longs
	 * @return			set of longs or {@code null}
	 */
	public static Set<Long> createOrNull(LongStream stream) {
		return createOrElse(stream, (Set<Long>) null);
	}

	/**
	 * Safely convert a collection of objects to a concurrent set or return a default value.
	 *
	 * @param collection	collection of objects
	 * @param defaultValue	default value to return on failure
	 * @param <T>			object type
	 * @return				concurrent set of objects or the default value
	 */
	public static <T> Set<T> createConcurrentOrElse(Collection<T> collection, Set<T> defaultValue) {
		return createOrElse(collection, UwSet::newConcurrent, defaultValue);
	}

	/**
	 * Safely convert a collection of objects to a concurrent set or return a default value.
	 *
	 * @param collection			collection of objects
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <T>					object type
	 * @return						concurrent set of objects or the default value
	 */
	public static <T> Set<T> createConcurrentOrElse(Collection<T> collection, Supplier<Set<T>> defaultValueSupplier) {
		return UwObject.ifNull(createConcurrentOrNull(collection), defaultValueSupplier);
	}

	/**
	 * Safely convert a collection of objects to a concurrent set or return an empty one.
	 *
	 * <p>Wraps {@link #createConcurrentOrElse(Collection, Supplier)}
	 * w/ {@link #newConcurrent()} as the default value supplier.
	 *
	 * @param collection	collection of objects
	 * @param <T>			object type
	 * @return				concurrent set of objects
	 */
	public static <T> Set<T> createConcurrentOrEmpty(Collection<T> collection) {
		return createConcurrentOrElse(collection, UwSet::newConcurrent);
	}

	/**
	 * Safely convert a collection of objects to a concurrent set or return {@code null}.
	 *
	 * <p>Wraps {@link #createConcurrentOrElse(Collection, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param collection	collection of objects
	 * @param <T>			object type
	 * @return				concurrent set of objects or {@code null}
	 */
	public static <T> Set<T> createConcurrentOrNull(Collection<T> collection) {
		return createConcurrentOrElse(collection, (Set<T>) null);
	}

	/**
	 * Safely convert an array of objects to a concurrent set or return a default value.
	 *
	 * <p>Wraps {@link #createConcurrentOrElse(Collection, Set)}
	 * w/ {@link Arrays#asList(Object[])} call.
	 *
	 * @param array			array of objects
	 * @param defaultValue	default value to return on failure
	 * @param <T>			object type
	 * @return				concurrent set of objects or the default value
	 */
	public static <T> Set<T> createConcurrentOrElse(T[] array, Set<T> defaultValue) {
		return createConcurrentOrElse((Collection<T>) UwObject.ifNotNull(array, Arrays::asList), defaultValue);
	}

	/**
	 * Safely convert an array of objects to a concurrent set or return a default value.
	 *
	 * @param array					array of objects
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <T>					object type
	 * @return						concurrent set of objects or the default value
	 */
	public static <T> Set<T> createConcurrentOrElse(T[] array, Supplier<Set<T>> defaultValueSupplier) {
		return UwObject.ifNull(createConcurrentOrNull(array), defaultValueSupplier);
	}

	/**
	 * Safely convert an array of objects to a concurrent set or return an empty one.
	 *
	 * <p>Wraps {@link #createConcurrentOrElse(Object[], Supplier)}
	 * w/ {@link #newConcurrent()} as the default value supplier.
	 *
	 * @param array		array of objects
	 * @param <T>		object type
	 * @return			concurrent set of objects
	 */
	public static <T> Set<T> createConcurrentOrEmpty(T[] array) {
		return createConcurrentOrElse(array, UwSet::newConcurrent);
	}

	/**
	 * Safely convert an array of objects to a concurrent set or return {@code null}.
	 *
	 * <p>Wraps {@link #createConcurrentOrElse(Object[], Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param array		array of objects
	 * @param <T>		object type
	 * @return			concurrent set of objects or {@code null}
	 */
	public static <T> Set<T> createConcurrentOrNull(T[] array) {
		return createConcurrentOrElse(array, (Set<T>) null);
	}

	/**
	 * Safely convert a stream of objects to a concurrent set or return a default value.
	 *
	 * <p>Wraps {@link Collections#synchronizedSet(Set)}
	 * w/ {@link Stream#collect(Collector)} {@literal &}
	 * {@link Collectors#toSet()} calls.
	 *
	 * @param stream		stream of objects
	 * @param defaultValue 	default value to return on failure
	 * @param <T>			object type
	 * @return				concurrent set of objects or the default value
	 */
	public static <T> Set<T> createConcurrentOrElse(Stream<T> stream, Set<T> defaultValue) {
		if (stream == null) {
			return defaultValue;
		}

		return Collections.synchronizedSet(stream.collect(Collectors.toSet()));
	}

	/**
	 * Safely convert a stream of objects to a concurrent set or return a default value.
	 *
	 * @param stream				stream of objects
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <T>					object type
	 * @return						concurrent set of objects or the default value
	 */
	public static <T> Set<T> createConcurrentOrElse(Stream<T> stream, Supplier<Set<T>> defaultValueSupplier) {
		return UwObject.ifNull(createConcurrentOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of objects to a concurrent set or return an empty one.
	 *
	 * <p>Wraps {@link #createConcurrentOrElse(Stream, Supplier)}
	 * w/ {@link #newConcurrent()} as the default value supplier.
	 *
	 * @param stream	stream of objects
	 * @param <T>		object type
	 * @return			concurrent set of objects or the default value
	 */
	public static <T> Set<T> createConcurrentOrEmpty(Stream<T> stream) {
		return createConcurrentOrElse(stream, UwSet::newConcurrent);
	}

	/**
	 * Safely convert a stream of objects to a concurrent set or return {@code null}.
	 *
	 * <p>Wraps {@link #createConcurrentOrElse(Stream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream	stream of objects
	 * @param <T>		object type
	 * @return			concurrent set of objects or {@code null}
	 */
	public static <T> Set<T> createConcurrentOrNull(Stream<T> stream) {
		return createConcurrentOrElse(stream, (Set<T>) null);
	}

	/**
	 * Safely convert a stream of integers to a concurrent set or return a default value.
	 *
	 * <p>Wraps {@link #createConcurrentOrElse(Stream, Set)}
	 * w/ {@link IntStream#boxed()} call.
	 *
	 * @param stream		stream of integers
	 * @param defaultValue 	default value to return on failure
	 * @return				concurrent set of integers or the default value
	 */
	public static Set<Integer> createConcurrentOrElse(IntStream stream, Set<Integer> defaultValue) {
		return createConcurrentOrElse(UwObject.ifNotNull(stream, IntStream::boxed), defaultValue);
	}

	/**
	 * Safely convert a stream of integers to a concurrent set or return a default value.
	 *
	 * @param stream				stream of integers
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						concurrent set of integers or the default value
	 */
	public static Set<Integer> createConcurrentOrElse(IntStream stream, Supplier<Set<Integer>> defaultValueSupplier) {
		return UwObject.ifNull(createConcurrentOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of integers to a concurrent set or return an empty one.
	 *
	 * <p>Wraps {@link #createConcurrentOrElse(IntStream, Supplier)}
	 * w/ {@link #newConcurrent()} as the default value supplier.
	 *
	 * @param stream	stream of integers
	 * @return			concurrent set of integers or the default value
	 */
	public static Set<Integer> createConcurrentOrEmpty(IntStream stream) {
		return createConcurrentOrElse(stream, UwSet::newConcurrent);
	}

	/**
	 * Safely convert a stream of integers to a concurrent set or return {@code null}.
	 *
	 * <p>Wraps {@link #createConcurrentOrElse(IntStream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream	stream of integers
	 * @return			concurrent set of integers or {@code null}
	 */
	public static Set<Integer> createConcurrentOrNull(IntStream stream) {
		return createConcurrentOrElse(stream, (Set<Integer>) null);
	}

	/**
	 * Safely convert a stream of doubles to a concurrent set or return a default value.
	 *
	 * <p>Wraps {@link #createConcurrentOrElse(Stream, Set)}
	 * w/ {@link DoubleStream#boxed()} call.
	 *
	 * @param stream		stream of doubles
	 * @param defaultValue 	default value to return on failure
	 * @return				concurrent set of doubles or the default value
	 */
	public static Set<Double> createConcurrentOrElse(DoubleStream stream, Set<Double> defaultValue) {
		return createConcurrentOrElse(UwObject.ifNotNull(stream, DoubleStream::boxed), defaultValue);
	}

	/**
	 * Safely convert a stream of doubles to a concurrent set or return a default value.
	 *
	 * @param stream				stream of doubles
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						concurrent set of doubles or the default value
	 */
	public static Set<Double> createConcurrentOrElse(DoubleStream stream, Supplier<Set<Double>> defaultValueSupplier) {
		return UwObject.ifNull(createConcurrentOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of doubles to a concurrent set or return an empty one.
	 *
	 * <p>Wraps {@link #createConcurrentOrElse(DoubleStream, Supplier)}
	 * w/ {@link #newConcurrent()} as the default value supplier.
	 *
	 * @param stream	stream of doubles
	 * @return			concurrent set of doubles or the default value
	 */
	public static Set<Double> createConcurrentOrEmpty(DoubleStream stream) {
		return createConcurrentOrElse(stream, UwSet::newConcurrent);
	}

	/**
	 * Safely convert a stream of doubles to a concurrent set or return {@code null}.
	 *
	 * <p>Wraps {@link #createConcurrentOrElse(DoubleStream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream	stream of doubles
	 * @return			concurrent set of doubles or {@code null}
	 */
	public static Set<Double> createConcurrentOrNull(DoubleStream stream) {
		return createConcurrentOrElse(stream, (Set<Double>) (null));
	}

	/**
	 * Safely convert a stream of longs to a concurrent set or return a default value.
	 *
	 * <p>Wraps {@link #createConcurrentOrElse(Stream, Set)}
	 * w/ {@link LongStream#boxed()} call.
	 *
	 * @param stream		stream of longs
	 * @param defaultValue 	default value to return on failure
	 * @return				concurrent set of longs or the default value
	 */
	public static Set<Long> createConcurrentOrElse(LongStream stream, Set<Long> defaultValue) {
		return createConcurrentOrElse(UwObject.ifNotNull(stream, LongStream::boxed), defaultValue);
	}

	/**
	 * Safely convert a stream of longs to a concurrent set or return a default value.
	 *
	 * @param stream				stream of longs
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						concurrent set of longs or the default value
	 */
	public static Set<Long> createConcurrentOrElse(LongStream stream, Supplier<Set<Long>> defaultValueSupplier) {
		return UwObject.ifNull(createConcurrentOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of longs to a concurrent set or return an empty one.
	 *
	 * <p>Wraps {@link #createConcurrentOrElse(LongStream, Supplier)}
	 * w/ {@link #newConcurrent()} as the default value supplier.
	 *
	 * @param stream	stream of longs
	 * @return			concurrent set of longs
	 */
	public static Set<Long> createConcurrentOrEmpty(LongStream stream) {
		return createConcurrentOrElse(stream, UwSet::newConcurrent);
	}

	/**
	 * Safely convert a stream of longs to a concurrent set or return {@code null}.
	 *
	 * <p>Wraps {@link #createConcurrentOrElse(LongStream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream	stream of longs
	 * @return			concurrent set of longs or {@code null}
	 */
	public static Set<Long> createConcurrentOrNull(LongStream stream) {
		return createConcurrentOrElse(stream, (Set<Long>) null);
	}

	/**
	 * Safely convert an object to a set of elements or return a default value.
	 *
	 * @param object		object to convert
	 * @param function		function to apply for conversion
	 * @param defaultValue	default value to return on failure
	 * @param <U>			object type
	 * @param <T>			element type
	 * @param <R>			set type
	 * @return				set of elements or the default value.
	 */
	private static <U, T, R extends Set<T>> R createOrElse(U object, Function<U, R> function, R defaultValue) {
		if (object == null || function == null) {
			return defaultValue;
		}

		return UwObject.ifNull(function.apply(object), defaultValue);
	}

	/**
	 * Create a new concurrent set instance.
	 *
	 * @param collection	collection of objects
	 * @param <T>			object type
	 * @return				new concurrent set instance.
	 *
	 * @throws IllegalArgumentException		if collection is {@code null}
	 */
	private static <T> Set<T> newConcurrent(Collection<T> collection) {
		if (collection == null) {
			throw new IllegalArgumentException("Collection mustn't be <null>");
		}

		Set<T> set = newConcurrent();

		set.addAll(collection);

		return set;
	}

	/**
	 * Create a new concurrent set instance.
	 *
	 * @param <T>	object type
	 * @return		new concurrent set instance
	 */
	private static <T> Set<T> newConcurrent() {
		return ConcurrentHashMap.newKeySet();
	}

	private UwSet() {
		throw new UnsupportedOperationException();
	}
}
