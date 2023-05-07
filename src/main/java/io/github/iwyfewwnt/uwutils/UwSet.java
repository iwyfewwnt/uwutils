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
	public static <T> Set<T> toSetOrElse(Collection<T> collection, Set<T> defaultValue) {
		return toSetOrElse(collection, HashSet::new, defaultValue);
	}

	/**
	 * Safely convert a collection of objects to a set or return a default value.
	 *
	 * @param collection			collection of objects
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <T>					object type
	 * @return						set of objects or the default value
	 */
	public static <T> Set<T> toSetOrElse(Collection<T> collection, Supplier<Set<T>> defaultValueSupplier) {
		return UwObject.ifNull(toSetOrNull(collection), defaultValueSupplier);
	}

	/**
	 * Safely convert a collection of objects to a set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(Collection, Supplier)}
	 * w/ {@link HashSet#HashSet()} as the default value supplier.
	 *
	 * @param collection	collection of objects
	 * @param <T>			object type
	 * @return				set of objects
	 */
	public static <T> Set<T> toSetOrEmpty(Collection<T> collection) {
		return toSetOrElse(collection, HashSet::new);
	}

	/**
	 * Safely convert a collection of objects to a set or return {@code null}.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(Collection, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param collection	collection of objects
	 * @param <T>			object type
	 * @return				set of objects or {@code null}
	 */
	public static <T> Set<T> toSetOrNull(Collection<T> collection) {
		return toSetOrElse(collection, (Set<T>) null);
	}

	/**
	 * Safely convert an array of objects to a set or return a default value.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(Collection, Set)}
	 * w/ {@link Arrays#asList(Object[])} call.
	 *
	 * @param array			array of objects
	 * @param defaultValue	default value to return on failure
	 * @param <T>			object type
	 * @return				set of objects or the default value
	 */
	public static <T> Set<T> toSetOrElse(T[] array, Set<T> defaultValue) {
		return toSetOrElse((Collection<T>) UwObject.ifNotNull(array, Arrays::asList), defaultValue);
	}

	/**
	 * Safely convert an array of objects to a set or return a default value.
	 *
	 * @param array					array of objects
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <T>					object type
	 * @return						set of objects or the default value
	 */
	public static <T> Set<T> toSetOrElse(T[] array, Supplier<Set<T>> defaultValueSupplier) {
		return UwObject.ifNull(toSetOrNull(array), defaultValueSupplier);
	}

	/**
	 * Safely convert an array of objects to a set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(Object[], Supplier)}
	 * w/ {@link HashSet#HashSet()} as the default value supplier.
	 *
	 * @param array		array of objects
	 * @param <T>		object type
	 * @return			set of objects
	 */
	public static <T> Set<T> toSetOrEmpty(T[] array) {
		return toSetOrElse(array, HashSet::new);
	}

	/**
	 * Safely convert an array of objects to a set or return {@code null}.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(Object[], Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param array		array of objects
	 * @param <T>		object type
	 * @return			set of objects or {@code null}
	 */
	public static <T> Set<T> toSetOrNull(T[] array) {
		return toSetOrElse(array, (Set<T>) null);
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
	public static <T> Set<T> toSetOrElse(Stream<T> stream, Set<T> defaultValue) {
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
	public static <T> Set<T> toSetOrElse(Stream<T> stream, Supplier<Set<T>> defaultValueSupplier) {
		return UwObject.ifNull(toSetOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of objects to a set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(Stream, Supplier)}
	 * w/ {@link HashSet#HashSet()} as the default value supplier.
	 *
	 * @param stream	stream of objects
	 * @param <T>		object type
	 * @return			set of objects
	 */
	public static <T> Set<T> toSetOrEmpty(Stream<T> stream) {
		return toSetOrElse(stream, HashSet::new);
	}

	/**
	 * Safely convert a stream of objects to a set or return {@code null}.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(Stream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream	stream of objects
	 * @param <T>		object type
	 * @return			set of objects or {@code null}
	 */
	public static <T> Set<T> toSetOrNull(Stream<T> stream) {
		return toSetOrElse(stream, (Set<T>) null);
	}

	/**
	 * Safely convert a stream of integers to a set or return a default value.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(Stream, Set)}
	 * w/ {@link IntStream#boxed()} call.
	 *
	 * @param stream		stream of integers
	 * @param defaultValue 	default value to return on failure
	 * @return				set of integers or the default value
	 */
	public static Set<Integer> toSetOrElse(IntStream stream, Set<Integer> defaultValue) {
		return toSetOrElse(UwObject.ifNotNull(stream, IntStream::boxed), defaultValue);
	}

	/**
	 * Safely convert a stream of integers to a set or return a default value.
	 *
	 * @param stream				stream of integers
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						set of integers or the default value
	 */
	public static Set<Integer> toSetOrElse(IntStream stream, Supplier<Set<Integer>> defaultValueSupplier) {
		return UwObject.ifNull(toSetOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of integers to a set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(IntStream, Supplier)}
	 * w/ {@link HashSet#HashSet()} as the default value supplier.
	 *
	 * @param stream	stream of integers
	 * @return			set of integers
	 */
	public static Set<Integer> toSetOrEmpty(IntStream stream) {
		return toSetOrElse(stream, HashSet::new);
	}

	/**
	 * Safely convert a stream of integers to a set or return {@code null}.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(IntStream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream	stream of integers
	 * @return			set of integers or {@code null}
	 */
	public static Set<Integer> toSetOrNull(IntStream stream) {
		return toSetOrElse(stream, (Set<Integer>) null);
	}

	/**
	 * Safely convert a stream of doubles to a set or return a default value.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(Stream, Set)}
	 * w/ {@link DoubleStream#boxed()} call.
	 *
	 * @param stream		stream of doubles
	 * @param defaultValue 	default value to return on failure
	 * @return				set of doubles or the default value
	 */
	public static Set<Double> toSetOrElse(DoubleStream stream, Set<Double> defaultValue) {
		return toSetOrElse(UwObject.ifNotNull(stream, DoubleStream::boxed), defaultValue);
	}

	/**
	 * Safely convert a stream of doubles to a set or return a default value.
	 *
	 * @param stream				stream of doubles
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						set of doubles or the default value
	 */
	public static Set<Double> toSetOrElse(DoubleStream stream, Supplier<Set<Double>> defaultValueSupplier) {
		return UwObject.ifNull(toSetOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of doubles to a set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(DoubleStream, Supplier)}
	 * w/ {@link HashSet#HashSet()} as the default value supplier.
	 *
	 * @param stream	stream of doubles
	 * @return			set of doubles
	 */
	public static Set<Double> toSetOrEmpty(DoubleStream stream) {
		return toSetOrElse(stream, HashSet::new);
	}

	/**
	 * Safely convert a stream of doubles to a set or return {@code null}.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(DoubleStream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream	stream of doubles
	 * @return			set of doubles or {@code null}
	 */
	public static Set<Double> toSetOrNull(DoubleStream stream) {
		return toSetOrElse(stream, (Set<Double>) (null));
	}

	/**
	 * Safely convert a stream of longs to a set or return a default value.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(Stream, Set)}
	 * w/ {@link LongStream#boxed()} call.
	 *
	 * @param stream		stream of longs
	 * @param defaultValue 	default value to return on failure
	 * @return				set of longs or the default value
	 */
	public static Set<Long> toSetOrElse(LongStream stream, Set<Long> defaultValue) {
		return toSetOrElse(UwObject.ifNotNull(stream, LongStream::boxed), defaultValue);
	}

	/**
	 * Safely convert a stream of longs to a set or return a default value.
	 *
	 * @param stream				stream of longs
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						set of longs or the default value
	 */
	public static Set<Long> toSetOrElse(LongStream stream, Supplier<Set<Long>> defaultValueSupplier) {
		return UwObject.ifNull(toSetOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of longs to a set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(LongStream, Supplier)}
	 * w/ {@link HashSet#HashSet()} as the default value supplier.
	 *
	 * @param stream	stream of longs
	 * @return			set of longs
	 */
	public static Set<Long> toSetOrEmpty(LongStream stream) {
		return toSetOrElse(stream, HashSet::new);
	}

	/**
	 * Safely convert a stream of longs to a set or return {@code null}.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(LongStream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream	stream of longs
	 * @return			set of longs or {@code null}
	 */
	public static Set<Long> toSetOrNull(LongStream stream) {
		return toSetOrElse(stream, (Set<Long>) null);
	}

	/**
	 * Safely convert a collection of objects to a concurrent set or return a default value.
	 *
	 * @param collection	collection of objects
	 * @param defaultValue	default value to return on failure
	 * @param <T>			object type
	 * @return				concurrent set of objects or the default value
	 */
	public static <T> Set<T> toConcurrentSetOrElse(Collection<T> collection, Set<T> defaultValue) {
		return toSetOrElse(collection, UwSet::newConcurrentSet, defaultValue);
	}

	/**
	 * Safely convert a collection of objects to a concurrent set or return a default value.
	 *
	 * @param collection			collection of objects
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <T>					object type
	 * @return						concurrent set of objects or the default value
	 */
	public static <T> Set<T> toConcurrentSetOrElse(Collection<T> collection, Supplier<Set<T>> defaultValueSupplier) {
		return UwObject.ifNull(toConcurrentSetOrNull(collection), defaultValueSupplier);
	}

	/**
	 * Safely convert a collection of objects to a concurrent set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toConcurrentSetOrElse(Collection, Supplier)}
	 * w/ {@link UwSet#newConcurrentSet()} as the default value supplier.
	 *
	 * @param collection	collection of objects
	 * @param <T>			object type
	 * @return				concurrent set of objects
	 */
	public static <T> Set<T> toConcurrentSetOrEmpty(Collection<T> collection) {
		return toConcurrentSetOrElse(collection, UwSet::newConcurrentSet);
	}

	/**
	 * Safely convert a collection of objects to a concurrent set or return {@code null}.
	 *
	 * <p>Wraps {@link UwSet#toConcurrentSetOrElse(Collection, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param collection	collection of objects
	 * @param <T>			object type
	 * @return				concurrent set of objects or {@code null}
	 */
	public static <T> Set<T> toConcurrentSetOrNull(Collection<T> collection) {
		return toConcurrentSetOrElse(collection, (Set<T>) null);
	}

	/**
	 * Safely convert an array of objects to a concurrent set or return a default value.
	 *
	 * <p>Wraps {@link UwSet#toConcurrentSetOrElse(Collection, Set)}
	 * w/ {@link Arrays#asList(Object[])} call.
	 *
	 * @param array			array of objects
	 * @param defaultValue	default value to return on failure
	 * @param <T>			object type
	 * @return				concurrent set of objects or the default value
	 */
	public static <T> Set<T> toConcurrentSetOrElse(T[] array, Set<T> defaultValue) {
		return toConcurrentSetOrElse((Collection<T>) UwObject.ifNotNull(array, Arrays::asList), defaultValue);
	}

	/**
	 * Safely convert an array of objects to a concurrent set or return a default value.
	 *
	 * @param array					array of objects
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <T>					object type
	 * @return						concurrent set of objects or the default value
	 */
	public static <T> Set<T> toConcurrentSetOrElse(T[] array, Supplier<Set<T>> defaultValueSupplier) {
		return UwObject.ifNull(toConcurrentSetOrNull(array), defaultValueSupplier);
	}

	/**
	 * Safely convert an array of objects to a concurrent set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toConcurrentSetOrElse(Object[], Supplier)}
	 * w/ {@link UwSet#newConcurrentSet()} as the default value supplier.
	 *
	 * @param array		array of objects
	 * @param <T>		object type
	 * @return			concurrent set of objects
	 */
	public static <T> Set<T> toConcurrentSetOrEmpty(T[] array) {
		return toConcurrentSetOrElse(array, UwSet::newConcurrentSet);
	}

	/**
	 * Safely convert an array of objects to a concurrent set or return {@code null}.
	 *
	 * <p>Wraps {@link UwSet#toConcurrentSetOrElse(Object[], Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param array		array of objects
	 * @param <T>		object type
	 * @return			concurrent set of objects or {@code null}
	 */
	public static <T> Set<T> toConcurrentSetOrNull(T[] array) {
		return toConcurrentSetOrElse(array, (Set<T>) null);
	}

	/**
	 * Safely convert a stream of objects to a concurrent set or return a default value.
	 *
	 * <p>Wraps {@link Collections#synchronizedSet(Set)}
	 * w/ {@link Stream#collect(Collector)} {@literal &} {@link Collectors#toSet()} calls.
	 *
	 * @param stream		stream of objects
	 * @param defaultValue 	default value to return on failure
	 * @param <T>			object type
	 * @return				concurrent set of objects or the default value
	 */
	public static <T> Set<T> toConcurrentSetOrElse(Stream<T> stream, Set<T> defaultValue) {
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
	public static <T> Set<T> toConcurrentSetOrElse(Stream<T> stream, Supplier<Set<T>> defaultValueSupplier) {
		return UwObject.ifNull(toConcurrentSetOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of objects to a concurrent set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toConcurrentSetOrElse(Stream, Supplier)}
	 * w/ {@link UwSet#newConcurrentSet()} as the default value supplier.
	 *
	 * @param stream	stream of objects
	 * @param <T>		object type
	 * @return			concurrent set of objects or the default value
	 */
	public static <T> Set<T> toConcurrentSetOrEmpty(Stream<T> stream) {
		return toConcurrentSetOrElse(stream, UwSet::newConcurrentSet);
	}

	/**
	 * Safely convert a stream of objects to a concurrent set or return {@code null}.
	 *
	 * <p>Wraps {@link UwSet#toConcurrentSetOrElse(Stream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream	stream of objects
	 * @param <T>		object type
	 * @return			concurrent set of objects or {@code null}
	 */
	public static <T> Set<T> toConcurrentSetOrNull(Stream<T> stream) {
		return toConcurrentSetOrElse(stream, (Set<T>) null);
	}

	/**
	 * Safely convert a stream of integers to a concurrent set or return a default value.
	 *
	 * <p>Wraps {@link UwSet#toConcurrentSetOrElse(Stream, Set)}
	 * w/ {@link IntStream#boxed()} call.
	 *
	 * @param stream		stream of integers
	 * @param defaultValue 	default value to return on failure
	 * @return				concurrent set of integers or the default value
	 */
	public static Set<Integer> toConcurrentSetOrElse(IntStream stream, Set<Integer> defaultValue) {
		return toConcurrentSetOrElse(UwObject.ifNotNull(stream, IntStream::boxed), defaultValue);
	}

	/**
	 * Safely convert a stream of integers to a concurrent set or return a default value.
	 *
	 * @param stream				stream of integers
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						concurrent set of integers or the default value
	 */
	public static Set<Integer> toConcurrentSetOrElse(IntStream stream, Supplier<Set<Integer>> defaultValueSupplier) {
		return UwObject.ifNull(toConcurrentSetOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of integers to a concurrent set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toConcurrentSetOrElse(IntStream, Supplier)}
	 * w/ {@link UwSet#newConcurrentSet()} as the default value supplier.
	 *
	 * @param stream	stream of integers
	 * @return			concurrent set of integers or the default value
	 */
	public static Set<Integer> toConcurrentSetOrEmpty(IntStream stream) {
		return toConcurrentSetOrElse(stream, UwSet::newConcurrentSet);
	}

	/**
	 * Safely convert a stream of integers to a concurrent set or return {@code null}.
	 *
	 * <p>Wraps {@link UwSet#toConcurrentSetOrElse(IntStream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream	stream of integers
	 * @return			concurrent set of integers or {@code null}
	 */
	public static Set<Integer> toConcurrentSetOrNull(IntStream stream) {
		return toConcurrentSetOrElse(stream, (Set<Integer>) null);
	}

	/**
	 * Safely convert a stream of doubles to a concurrent set or return a default value.
	 *
	 * <p>Wraps {@link UwSet#toConcurrentSetOrElse(Stream, Set)}
	 * w/ {@link DoubleStream#boxed()} call.
	 *
	 * @param stream		stream of doubles
	 * @param defaultValue 	default value to return on failure
	 * @return				concurrent set of doubles or the default value
	 */
	public static Set<Double> toConcurrentSetOrElse(DoubleStream stream, Set<Double> defaultValue) {
		return toConcurrentSetOrElse(UwObject.ifNotNull(stream, DoubleStream::boxed), defaultValue);
	}

	/**
	 * Safely convert a stream of doubles to a concurrent set or return a default value.
	 *
	 * @param stream				stream of doubles
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						concurrent set of doubles or the default value
	 */
	public static Set<Double> toConcurrentSetOrElse(DoubleStream stream, Supplier<Set<Double>> defaultValueSupplier) {
		return UwObject.ifNull(toConcurrentSetOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of doubles to a concurrent set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toConcurrentSetOrElse(DoubleStream, Supplier)}
	 * w/ {@link UwSet#newConcurrentSet()} as the default value supplier.
	 *
	 * @param stream	stream of doubles
	 * @return			concurrent set of doubles or the default value
	 */
	public static Set<Double> toConcurrentSetOrEmpty(DoubleStream stream) {
		return toConcurrentSetOrElse(stream, UwSet::newConcurrentSet);
	}

	/**
	 * Safely convert a stream of doubles to a concurrent set or return {@code null}.
	 *
	 * <p>Wraps {@link UwSet#toConcurrentSetOrElse(DoubleStream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream	stream of doubles
	 * @return			concurrent set of doubles or {@code null}
	 */
	public static Set<Double> toConcurrentSetOrNull(DoubleStream stream) {
		return toConcurrentSetOrElse(stream, (Set<Double>) (null));
	}

	/**
	 * Safely convert a stream of longs to a concurrent set or return a default value.
	 *
	 * <p>Wraps {@link UwSet#toConcurrentSetOrElse(Stream, Set)}
	 * w/ {@link LongStream#boxed()} call.
	 *
	 * @param stream		stream of longs
	 * @param defaultValue 	default value to return on failure
	 * @return				concurrent set of longs or the default value
	 */
	public static Set<Long> toConcurrentSetOrElse(LongStream stream, Set<Long> defaultValue) {
		return toConcurrentSetOrElse(UwObject.ifNotNull(stream, LongStream::boxed), defaultValue);
	}

	/**
	 * Safely convert a stream of longs to a concurrent set or return a default value.
	 *
	 * @param stream				stream of longs
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						concurrent set of longs or the default value
	 */
	public static Set<Long> toConcurrentSetOrElse(LongStream stream, Supplier<Set<Long>> defaultValueSupplier) {
		return UwObject.ifNull(toConcurrentSetOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of longs to a concurrent set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toConcurrentSetOrElse(LongStream, Supplier)}
	 * w/ {@link UwSet#newConcurrentSet()} as the default value supplier.
	 *
	 * @param stream	stream of longs
	 * @return			concurrent set of longs
	 */
	public static Set<Long> toConcurrentSetOrEmpty(LongStream stream) {
		return toConcurrentSetOrElse(stream, UwSet::newConcurrentSet);
	}

	/**
	 * Safely convert a stream of longs to a concurrent set or return {@code null}.
	 *
	 * <p>Wraps {@link UwSet#toConcurrentSetOrElse(LongStream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream	stream of longs
	 * @return			concurrent set of longs or {@code null}
	 */
	public static Set<Long> toConcurrentSetOrNull(LongStream stream) {
		return toConcurrentSetOrElse(stream, (Set<Long>) null);
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
	private static <U, T, R extends Set<T>> R toSetOrElse(U object, Function<U, R> function, R defaultValue) {
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
	private static <T> Set<T> newConcurrentSet(Collection<T> collection) {
		if (collection == null) {
			throw new IllegalArgumentException("Collection mustn't be <null>");
		}

		Set<T> set = newConcurrentSet();

		set.addAll(collection);

		return set;
	}

	/**
	 * Create a new concurrent set instance.
	 *
	 * @param <T>	object type
	 * @return		new concurrent set instance
	 */
	private static <T> Set<T> newConcurrentSet() {
		return ConcurrentHashMap.newKeySet();
	}

	private UwSet() {
		throw new UnsupportedOperationException();
	}
}
