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

import java.util.*;
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
	 * Safely convert a collection of objects to a set.
	 *
	 * @param collection			collection of objects
	 * @param <T>					object type
	 * @return						set of objects that wrapped in {@link Option}
	 */
	public static <T> Option<Set<T>> toSet(Collection<T> collection) {
		return Option.of(toSetOrNull(collection));
	}

	/**
	 * Safely convert a collection of objects to a set or return a default value.
	 *
	 * @param collection			collection of objects
	 * @param defaultValue			default value to return on failure
	 * @param <T>					object type
	 * @return						set of objects or the default value
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
		return UwObject.getIfNull(toSetOrNull(collection), defaultValueSupplier);
	}

	/**
	 * Safely convert a collection of objects to a set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(Collection, Supplier)}
	 * w/ {@link HashSet#HashSet()} as the default value supplier.
	 *
	 * @param collection			collection of objects
	 * @param <T>					object type
	 * @return						set of objects
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
	 * @param collection			collection of objects
	 * @param <T>					object type
	 * @return						set of objects or {@code null}
	 */
	public static <T> Set<T> toSetOrNull(Collection<T> collection) {
		return toSetOrElse(collection, (Set<T>) null);
	}

	/**
	 * Safely convert an array of objects to a set.
	 *
	 * @param array					array of objects
	 * @param <T>					object type
	 * @return						set of objects that wrapped in {@link Option}
	 */
	public static <T> Option<Set<T>> toSet(T[] array) {
		return Option.of(toSetOrNull(array));
	}

	/**
	 * Safely convert an array of objects to a set or return a default value.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(Collection, Set)}
	 * w/ {@link Arrays#asList(Object[])} call.
	 *
	 * @param array					array of objects
	 * @param defaultValue			default value to return on failure
	 * @param <T>					object type
	 * @return						set of objects or the default value
	 */
	public static <T> Set<T> toSetOrElse(T[] array, Set<T> defaultValue) {
		if (array == null) {
			return defaultValue;
		}

		return toSetOrElse(Arrays.asList(array), defaultValue);
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
		return UwObject.getIfNull(toSetOrNull(array), defaultValueSupplier);
	}

	/**
	 * Safely convert an array of objects to a set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(Object[], Supplier)}
	 * w/ {@link HashSet#HashSet()} as the default value supplier.
	 *
	 * @param array					array of objects
	 * @param <T>					object type
	 * @return						set of objects
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
	 * @param array					array of objects
	 * @param <T>					object type
	 * @return						set of objects or {@code null}
	 */
	public static <T> Set<T> toSetOrNull(T[] array) {
		return toSetOrElse(array, (Set<T>) null);
	}

	/**
	 * Safely convert a stream of objects to a set.
	 *
	 * @param stream				stream of objects
	 * @param <T>					object type
	 * @return						set of objects that wrapped in {@link Option}
	 */
	public static <T> Option<Set<T>> toSet(Stream<T> stream) {
		return Option.of(toSetOrNull(stream));
	}

	/**
	 * Safely convert a stream of objects to a set or return a default value.
	 *
	 * <p>Wraps {@link Stream#collect(Collector)}
	 * w/ {@link Collectors#toSet()} call.
	 *
	 * @param stream				stream of objects
	 * @param defaultValue 			default value to return on failure
	 * @param <T>					object type
	 * @return						set of objects or the default value
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
		return UwObject.getIfNull(toSetOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of objects to a set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(Stream, Supplier)}
	 * w/ {@link HashSet#HashSet()} as the default value supplier.
	 *
	 * @param stream				stream of objects
	 * @param <T>					object type
	 * @return						set of objects
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
	 * @param stream				stream of objects
	 * @param <T>					object type
	 * @return						set of objects or {@code null}
	 */
	public static <T> Set<T> toSetOrNull(Stream<T> stream) {
		return toSetOrElse(stream, (Set<T>) null);
	}

	/**
	 * Safely convert a stream of integers to a set.
	 *
	 * @param stream				stream of integers
	 * @return						set of integers that wrapped in {@link Option}
	 */
	public static Option<Set<Integer>> toSet(IntStream stream) {
		return Option.of(toSetOrNull(stream));
	}

	/**
	 * Safely convert a stream of integers to a set or return a default value.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(Stream, Set)}
	 * w/ {@link IntStream#boxed()} call.
	 *
	 * @param stream				stream of integers
	 * @param defaultValue 			default value to return on failure
	 * @return						set of integers or the default value
	 */
	public static Set<Integer> toSetOrElse(IntStream stream, Set<Integer> defaultValue) {
		if (stream == null) {
			return defaultValue;
		}

		return toSetOrElse(stream.boxed(), defaultValue);
	}

	/**
	 * Safely convert a stream of integers to a set or return a default value.
	 *
	 * @param stream				stream of integers
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						set of integers or the default value
	 */
	public static Set<Integer> toSetOrElse(IntStream stream, Supplier<Set<Integer>> defaultValueSupplier) {
		return UwObject.getIfNull(toSetOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of integers to a set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(IntStream, Supplier)}
	 * w/ {@link HashSet#HashSet()} as the default value supplier.
	 *
	 * @param stream				stream of integers
	 * @return						set of integers
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
	 * @param stream				stream of integers
	 * @return						set of integers or {@code null}
	 */
	public static Set<Integer> toSetOrNull(IntStream stream) {
		return toSetOrElse(stream, (Set<Integer>) null);
	}

	/**
	 * Safely convert a stream of doubles to a set.
	 *
	 * @param stream				stream of doubles
	 * @return						set of doubles that wrapped in {@link Option}
	 */
	public static Option<Set<Double>> toSet(DoubleStream stream) {
		return Option.of(toSetOrNull(stream));
	}

	/**
	 * Safely convert a stream of doubles to a set or return a default value.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(Stream, Set)}
	 * w/ {@link DoubleStream#boxed()} call.
	 *
	 * @param stream				stream of doubles
	 * @param defaultValue 			default value to return on failure
	 * @return						set of doubles or the default value
	 */
	public static Set<Double> toSetOrElse(DoubleStream stream, Set<Double> defaultValue) {
		if (stream == null) {
			return defaultValue;
		}

		return toSetOrElse(stream.boxed(), defaultValue);
	}

	/**
	 * Safely convert a stream of doubles to a set or return a default value.
	 *
	 * @param stream				stream of doubles
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						set of doubles or the default value
	 */
	public static Set<Double> toSetOrElse(DoubleStream stream, Supplier<Set<Double>> defaultValueSupplier) {
		return UwObject.getIfNull(toSetOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of doubles to a set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(DoubleStream, Supplier)}
	 * w/ {@link HashSet#HashSet()} as the default value supplier.
	 *
	 * @param stream				stream of doubles
	 * @return						set of doubles
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
	 * @param stream				stream of doubles
	 * @return						set of doubles or {@code null}
	 */
	public static Set<Double> toSetOrNull(DoubleStream stream) {
		return toSetOrElse(stream, (Set<Double>) (null));
	}

	/**
	 * Safely convert a stream of longs to a set.
	 *
	 * @param stream				stream of longs
	 * @return						set of longs that wrapped in {@link Option}
	 */
	public static Option<Set<Long>> toSet(LongStream stream) {
		return Option.of(toSetOrNull(stream));
	}

	/**
	 * Safely convert a stream of longs to a set or return a default value.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(Stream, Set)}
	 * w/ {@link LongStream#boxed()} call.
	 *
	 * @param stream				stream of longs
	 * @param defaultValue 			default value to return on failure
	 * @return						set of longs or the default value
	 */
	public static Set<Long> toSetOrElse(LongStream stream, Set<Long> defaultValue) {
		if (stream == null) {
			return defaultValue;
		}

		return toSetOrElse(stream.boxed(), defaultValue);
	}

	/**
	 * Safely convert a stream of longs to a set or return a default value.
	 *
	 * @param stream				stream of longs
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						set of longs or the default value
	 */
	public static Set<Long> toSetOrElse(LongStream stream, Supplier<Set<Long>> defaultValueSupplier) {
		return UwObject.getIfNull(toSetOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of longs to a set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toSetOrElse(LongStream, Supplier)}
	 * w/ {@link HashSet#HashSet()} as the default value supplier.
	 *
	 * @param stream				stream of longs
	 * @return						set of longs
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
	 * @param stream				stream of longs
	 * @return						set of longs or {@code null}
	 */
	public static Set<Long> toSetOrNull(LongStream stream) {
		return toSetOrElse(stream, (Set<Long>) null);
	}

	/**
	 * Safely convert a collection of objects to a synchronized set.
	 *
	 * @param collection			collection of objects
	 * @param <T>					object type
	 * @return						synchronized set of objects that wrapped in {@link Option}
	 */
	public static <T> Option<Set<T>> toSynchronizedSet(Collection<T> collection) {
		return Option.of(toSynchronizedSetOrNull(collection));
	}

	/**
	 * Safely convert a collection of objects to a synchronized set or return a default value.
	 *
	 * @param collection			collection of objects
	 * @param defaultValue			default value to return on failure
	 * @param <T>					object type
	 * @return						synchronized set of objects or the default value
	 */
	public static <T> Set<T> toSynchronizedSetOrElse(Collection<T> collection, Set<T> defaultValue) {
		return toSetOrElse(collection, UwSet::newSynchronizedSet, defaultValue);
	}

	/**
	 * Safely convert a collection of objects to a synchronized set or return a default value.
	 *
	 * @param collection			collection of objects
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <T>					object type
	 * @return						synchronized set of objects or the default value
	 */
	public static <T> Set<T> toSynchronizedSetOrElse(Collection<T> collection, Supplier<Set<T>> defaultValueSupplier) {
		return UwObject.getIfNull(toSynchronizedSetOrNull(collection), defaultValueSupplier);
	}

	/**
	 * Safely convert a collection of objects to a synchronized set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toSynchronizedSetOrElse(Collection, Supplier)}
	 * w/ {@link UwSet#newSynchronizedSet()} as the default value supplier.
	 *
	 * @param collection			collection of objects
	 * @param <T>					object type
	 * @return						synchronized set of objects
	 */
	public static <T> Set<T> toSynchronizedSetOrEmpty(Collection<T> collection) {
		return toSynchronizedSetOrElse(collection, UwSet::newSynchronizedSet);
	}

	/**
	 * Safely convert a collection of objects to a synchronized set or return {@code null}.
	 *
	 * <p>Wraps {@link UwSet#toSynchronizedSetOrElse(Collection, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param collection			collection of objects
	 * @param <T>					object type
	 * @return						synchronized set of objects or {@code null}
	 */
	public static <T> Set<T> toSynchronizedSetOrNull(Collection<T> collection) {
		return toSynchronizedSetOrElse(collection, (Set<T>) null);
	}

	/**
	 * Safely convert an array of objects to a synchronized set.
	 *
	 * @param array					array of objects
	 * @param <T>					object type
	 * @return						synchronized set of objects that wrapped in {@link Option}
	 */
	public static <T> Option<Set<T>> toSynchronizedSet(T[] array) {
		return Option.of(toSynchronizedSetOrNull(array));
	}

	/**
	 * Safely convert an array of objects to a synchronized set or return a default value.
	 *
	 * <p>Wraps {@link UwSet#toSynchronizedSetOrElse(Collection, Set)}
	 * w/ {@link Arrays#asList(Object[])} call.
	 *
	 * @param array					array of objects
	 * @param defaultValue			default value to return on failure
	 * @param <T>					object type
	 * @return						synchronized set of objects or the default value
	 */
	public static <T> Set<T> toSynchronizedSetOrElse(T[] array, Set<T> defaultValue) {
		if (array == null) {
			return defaultValue;
		}

		return toSynchronizedSetOrElse(Arrays.asList(array), defaultValue);
	}

	/**
	 * Safely convert an array of objects to a synchronized set or return a default value.
	 *
	 * @param array					array of objects
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <T>					object type
	 * @return						synchronized set of objects or the default value
	 */
	public static <T> Set<T> toSynchronizedSetOrElse(T[] array, Supplier<Set<T>> defaultValueSupplier) {
		return UwObject.getIfNull(toSynchronizedSetOrNull(array), defaultValueSupplier);
	}

	/**
	 * Safely convert an array of objects to a synchronized set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toSynchronizedSetOrElse(Object[], Supplier)}
	 * w/ {@link UwSet#newSynchronizedSet()} as the default value supplier.
	 *
	 * @param array					array of objects
	 * @param <T>					object type
	 * @return						synchronized set of objects
	 */
	public static <T> Set<T> toSynchronizedSetOrEmpty(T[] array) {
		return toSynchronizedSetOrElse(array, UwSet::newSynchronizedSet);
	}

	/**
	 * Safely convert an array of objects to a synchronized set or return {@code null}.
	 *
	 * <p>Wraps {@link UwSet#toSynchronizedSetOrElse(Object[], Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param array					array of objects
	 * @param <T>					object type
	 * @return						synchronized set of objects or {@code null}
	 */
	public static <T> Set<T> toSynchronizedSetOrNull(T[] array) {
		return toSynchronizedSetOrElse(array, (Set<T>) null);
	}

	/**
	 * Safely convert a stream of objects to a synchronized set.
	 *
	 * @param stream				stream of objects
	 * @param <T>					object type
	 * @return						synchronized set of objects that wrapped in {@link Option}
	 */
	public static <T> Option<Set<T>> toSynchronizedSet(Stream<T> stream) {
		return Option.of(toSynchronizedSetOrNull(stream));
	}

	/**
	 * Safely convert a stream of objects to a synchronized set or return a default value.
	 *
	 * <p>Wraps {@link Collections#synchronizedSet(Set)}
	 * w/ {@link Stream#collect(Collector)} {@literal &} {@link Collectors#toSet()} calls.
	 *
	 * @param stream				stream of objects
	 * @param defaultValue 			default value to return on failure
	 * @param <T>					object type
	 * @return						synchronized set of objects or the default value
	 */
	public static <T> Set<T> toSynchronizedSetOrElse(Stream<T> stream, Set<T> defaultValue) {
		if (stream == null) {
			return defaultValue;
		}

		return Collections.synchronizedSet(stream.collect(Collectors.toSet()));
	}

	/**
	 * Safely convert a stream of objects to a synchronized set or return a default value.
	 *
	 * @param stream				stream of objects
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <T>					object type
	 * @return						synchronized set of objects or the default value
	 */
	public static <T> Set<T> toSynchronizedSetOrElse(Stream<T> stream, Supplier<Set<T>> defaultValueSupplier) {
		return UwObject.getIfNull(toSynchronizedSetOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of objects to a synchronized set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toSynchronizedSetOrElse(Stream, Supplier)}
	 * w/ {@link UwSet#newSynchronizedSet()} as the default value supplier.
	 *
	 * @param stream				stream of objects
	 * @param <T>					object type
	 * @return						synchronized set of objects or the default value
	 */
	public static <T> Set<T> toSynchronizedSetOrEmpty(Stream<T> stream) {
		return toSynchronizedSetOrElse(stream, UwSet::newSynchronizedSet);
	}

	/**
	 * Safely convert a stream of objects to a synchronized set or return {@code null}.
	 *
	 * <p>Wraps {@link UwSet#toSynchronizedSetOrElse(Stream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream				stream of objects
	 * @param <T>					object type
	 * @return						synchronized set of objects or {@code null}
	 */
	public static <T> Set<T> toSynchronizedSetOrNull(Stream<T> stream) {
		return toSynchronizedSetOrElse(stream, (Set<T>) null);
	}

	/**
	 * Safely convert a stream of integers to a synchronized set.
	 *
	 * @param stream				stream of integers
	 * @return						synchronized set of integers that wrapped in {@link Option}
	 */
	public static Option<Set<Integer>> toSynchronizedSet(IntStream stream) {
		return Option.of(toSynchronizedSetOrNull(stream));
	}

	/**
	 * Safely convert a stream of integers to a synchronized set or return a default value.
	 *
	 * <p>Wraps {@link UwSet#toSynchronizedSetOrElse(Stream, Set)}
	 * w/ {@link IntStream#boxed()} call.
	 *
	 * @param stream				stream of integers
	 * @param defaultValue 			default value to return on failure
	 * @return						synchronized set of integers or the default value
	 */
	public static Set<Integer> toSynchronizedSetOrElse(IntStream stream, Set<Integer> defaultValue) {
		if (stream == null) {
			return defaultValue;
		}

		return toSynchronizedSetOrElse(stream.boxed(), defaultValue);
	}

	/**
	 * Safely convert a stream of integers to a synchronized set or return a default value.
	 *
	 * @param stream				stream of integers
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						synchronized set of integers or the default value
	 */
	public static Set<Integer> toSynchronizedSetOrElse(IntStream stream, Supplier<Set<Integer>> defaultValueSupplier) {
		return UwObject.getIfNull(toSynchronizedSetOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of integers to a synchronized set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toSynchronizedSetOrElse(IntStream, Supplier)}
	 * w/ {@link UwSet#newSynchronizedSet()} as the default value supplier.
	 *
	 * @param stream				stream of integers
	 * @return						synchronized set of integers or the default value
	 */
	public static Set<Integer> toSynchronizedSetOrEmpty(IntStream stream) {
		return toSynchronizedSetOrElse(stream, UwSet::newSynchronizedSet);
	}

	/**
	 * Safely convert a stream of integers to a synchronized set or return {@code null}.
	 *
	 * <p>Wraps {@link UwSet#toSynchronizedSetOrElse(IntStream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream				stream of integers
	 * @return						synchronized set of integers or {@code null}
	 */
	public static Set<Integer> toSynchronizedSetOrNull(IntStream stream) {
		return toSynchronizedSetOrElse(stream, (Set<Integer>) null);
	}

	/**
	 * Safely convert a stream of doubles to a synchronized set.
	 *
	 * @param stream				stream of doubles
	 * @return						synchronized set of doubles that wrapped in {@link Option}
	 */
	public static Option<Set<Double>> toSynchronizedSet(DoubleStream stream) {
		return Option.of(toSynchronizedSetOrNull(stream));
	}

	/**
	 * Safely convert a stream of doubles to a synchronized set or return a default value.
	 *
	 * <p>Wraps {@link UwSet#toSynchronizedSetOrElse(Stream, Set)}
	 * w/ {@link DoubleStream#boxed()} call.
	 *
	 * @param stream				stream of doubles
	 * @param defaultValue 			default value to return on failure
	 * @return						synchronized set of doubles or the default value
	 */
	public static Set<Double> toSynchronizedSetOrElse(DoubleStream stream, Set<Double> defaultValue) {
		if (stream == null) {
			return defaultValue;
		}

		return toSynchronizedSetOrElse(stream.boxed(), defaultValue);
	}

	/**
	 * Safely convert a stream of doubles to a synchronized set or return a default value.
	 *
	 * @param stream				stream of doubles
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						synchronized set of doubles or the default value
	 */
	public static Set<Double> toSynchronizedSetOrElse(DoubleStream stream, Supplier<Set<Double>> defaultValueSupplier) {
		return UwObject.getIfNull(toSynchronizedSetOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of doubles to a synchronized set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toSynchronizedSetOrElse(DoubleStream, Supplier)}
	 * w/ {@link UwSet#newSynchronizedSet()} as the default value supplier.
	 *
	 * @param stream				stream of doubles
	 * @return						synchronized set of doubles or the default value
	 */
	public static Set<Double> toSynchronizedSetOrEmpty(DoubleStream stream) {
		return toSynchronizedSetOrElse(stream, UwSet::newSynchronizedSet);
	}

	/**
	 * Safely convert a stream of doubles to a synchronized set or return {@code null}.
	 *
	 * <p>Wraps {@link UwSet#toSynchronizedSetOrElse(DoubleStream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream				stream of doubles
	 * @return						synchronized set of doubles or {@code null}
	 */
	public static Set<Double> toSynchronizedSetOrNull(DoubleStream stream) {
		return toSynchronizedSetOrElse(stream, (Set<Double>) (null));
	}



	/**
	 * Safely convert a stream of longs to a synchronized set.
	 *
	 * @param stream				stream of longs
	 * @return						synchronized set of longs that wrapped in {@link Option}
	 */
	public static Option<Set<Long>> toSynchronizedSet(LongStream stream) {
		return Option.of(toSynchronizedSetOrNull(stream));
	}

	/**
	 * Safely convert a stream of longs to a synchronized set or return a default value.
	 *
	 * <p>Wraps {@link UwSet#toSynchronizedSetOrElse(Stream, Set)}
	 * w/ {@link LongStream#boxed()} call.
	 *
	 * @param stream				stream of longs
	 * @param defaultValue 			default value to return on failure
	 * @return						synchronized set of longs or the default value
	 */
	public static Set<Long> toSynchronizedSetOrElse(LongStream stream, Set<Long> defaultValue) {
		if (stream == null) {
			return defaultValue;
		}

		return toSynchronizedSetOrElse(stream.boxed(), defaultValue);
	}

	/**
	 * Safely convert a stream of longs to a synchronized set or return a default value.
	 *
	 * @param stream				stream of longs
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						synchronized set of longs or the default value
	 */
	public static Set<Long> toSynchronizedSetOrElse(LongStream stream, Supplier<Set<Long>> defaultValueSupplier) {
		return UwObject.getIfNull(toSynchronizedSetOrNull(stream), defaultValueSupplier);
	}

	/**
	 * Safely convert a stream of longs to a synchronized set or return an empty one.
	 *
	 * <p>Wraps {@link UwSet#toSynchronizedSetOrElse(LongStream, Supplier)}
	 * w/ {@link UwSet#newSynchronizedSet()} as the default value supplier.
	 *
	 * @param stream				stream of longs
	 * @return						synchronized set of longs
	 */
	public static Set<Long> toSynchronizedSetOrEmpty(LongStream stream) {
		return toSynchronizedSetOrElse(stream, UwSet::newSynchronizedSet);
	}

	/**
	 * Safely convert a stream of longs to a synchronized set or return {@code null}.
	 *
	 * <p>Wraps {@link UwSet#toSynchronizedSetOrElse(LongStream, Set)}
	 * w/ {@code null} as the default value.
	 *
	 * @param stream				stream of longs
	 * @return						synchronized set of longs or {@code null}
	 */
	public static Set<Long> toSynchronizedSetOrNull(LongStream stream) {
		return toSynchronizedSetOrElse(stream, (Set<Long>) null);
	}

	/**
	 * Safely convert an object to a set of elements or return a default value.
	 *
	 * @param object			object to convert
	 * @param function			function to apply for conversion
	 * @param defaultValue		default value to return on failure
	 * @param <U>				object type
	 * @param <T>				element type
	 * @param <R>				set type
	 * @return					set of elements or the default value.
	 */
	private static <U, T, R extends Set<T>> R toSetOrElse(U object, Function<U, R> function, R defaultValue) {
		if (object == null || function == null) {
			return defaultValue;
		}

		return UwObject.getIfNull(function.apply(object), defaultValue);
	}

	/**
	 * Create a new synchronized {@link HashSet}.
	 *
	 * @param collection	collection of objects
	 * @param <T>			object type
	 * @return				new synchronized {@link HashSet}.
	 */
	private static <T> Set<T> newSynchronizedSet(Collection<T> collection) {
		return Collections.synchronizedSet(new HashSet<>(collection));
	}

	/**
	 * Create a new synchronized {@link HashSet}.
	 *
	 * @param <T>			object type
	 * @return				new synchronized {@link HashSet}
	 */
	private static <T> Set<T> newSynchronizedSet() {
		return Collections.synchronizedSet(new HashSet<>());
	}

	private UwSet() {
		throw new UnsupportedOperationException();
	}
}
