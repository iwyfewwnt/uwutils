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
import org.apache.commons.lang3.ObjectUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A set utility.
 *
 * <p>{@code UwSet} is the utility class
 * that provide functionality to operate
 * with sets.
 *
 * @since 0.1.2
 */
@SuppressWarnings("unused")
public final class UwSet {

	/**
	 * Null-safely convert a collection of objects to a set.
	 *
	 * @param collection	collection of objects
	 * @param function		function for collection mapping
	 * @param <T>			object type
	 * @return				set of mapped objects from collection
	 * 						that wrapped in {@link Option}
	 */
	public static <T> Option<Set<T>> toSet(Collection<T> collection, Function<Collection<T>, Set<T>> function) {
		if (ObjectUtils.anyNull(collection, function)) {
			return Option.none();
		}

		return Option.of(function.apply(collection));
	}

	/**
	 * Null-safely convert an array of objects to a set.
	 *
	 * @param array			array of objects
	 * @param function		function for collection mapping
	 * @param <T>			object type
	 * @return				set of mapped objects from array
	 * 						that wrapped in {@link Option}
	 */
	public static <T> Option<Set<T>> toSet(T[] array, Function<Collection<T>, Set<T>> function) {
		if (array == null) {
			return Option.none();
		}

		return toSet(Arrays.asList(array), function);
	}

	/**
	 * Null-safely convert a stream of objects to a set.
	 *
	 * @param stream		stream of objects
	 * @param function		function for collection mapping
	 * @param <T>			object type
	 * @return				set of mapped objects from stream
	 * 						that wrapped in {@link Option}
	 */
	public static <T> Option<Set<T>> toSet(Stream<T> stream, Function<Collection<T>, Set<T>> function) {
		if (stream == null) {
			return Option.none();
		}

		return toSet(stream.collect(Collectors.toList()), function);
	}

	/**
	 * Null-safely convert a stream of integers to a set.
	 *
	 * @param intStream		stream of integers
	 * @param function		function for collection mapping
	 * @return				set of mapped objects from stream
	 * 						that wrapped in {@link Option}
	 */
	public static Option<Set<Integer>> toSet(IntStream intStream, Function<Collection<Integer>, Set<Integer>> function) {
		if (intStream == null) {
			return Option.none();
		}

		return toSet(intStream.boxed(), function);
	}

	/**
	 * Null-safely convert a collection of objects to a set.
	 *
	 * @param collection	collection of objects
	 * @param <T> 			object type
	 * @return				new {@code HashSet} of mapped objects from collection
	 * 						that wrapped in {@link Option}
	 */
	public static <T> Option<Set<T>> toSet(Collection<T> collection) {
		return toSet(collection, HashSet::new);
	}

	/**
	 * Null-safely convert an array of objects to a set.
	 *
	 * @param array			array of objects
	 * @param <T> 			object type
	 * @return				new {@code HashSet} of mapped objects from array
	 * 						that wrapped in {@link Option}
	 */
	public static <T> Option<Set<T>> toSet(T[] array) {
		return toSet(array, HashSet::new);
	}

	/**
	 * Null-safely convert a stream of objects to a set.
	 *
	 * @param stream		stream of objects
	 * @param <T> 			object type
	 * @return				new {@code HashSet} of mapped objects from stream
	 * 						that wrapped in {@link Option}
	 */
	public static <T> Option<Set<T>> toSet(Stream<T> stream) {
		return toSet(stream, HashSet::new);
	}

	/**
	 * Null-safely convert a stream of integers to a set.
	 *
	 * @param intStream		stream of integers
	 * @return				new {@code HashSet} of mapped objects from stream
	 * 						that wrapped in {@link Option}
	 */
	public static Option<Set<Integer>> toSet(IntStream intStream) {
		return toSet(intStream, HashSet::new);
	}

	/**
	 * Null-safely convert a collection of objects to a synchronized set.
	 *
	 * @param collection	collection of objects
	 * @param <T> 			object type
	 * @return				new synchronized {@code HashSet} of mapped objects from collection
	 * 						that wrapped in {@link Option}
	 */
	public static <T> Option<Set<T>> toSynchronizedSet(Collection<T> collection) {
		return toSet(collection, UwSet::newSynchronizedSet);
	}

	/**
	 * Null-safely convert an array of objects to a synchronized set.
	 *
	 * @param array			array of objects
	 * @param <T> 			object type
	 * @return				new synchronized {@code HashSet} of mapped objects from array
	 * 						that wrapped in {@link Option}
	 */
	public static <T> Option<Set<T>> toSynchronizedSet(T[] array) {
		return toSet(array, UwSet::newSynchronizedSet);
	}

	/**
	 * Null-safely convert a stream of objects to a synchronized set.
	 *
	 * @param stream		stream of objects
	 * @param <T> 			object type
	 * @return				new synchronized {@code HashSet} of mapped objects from stream
	 * 						that wrapped in {@link Option}
	 */
	public static <T> Option<Set<T>> toSynchronizedSet(Stream<T> stream) {
		return toSet(stream, UwSet::newSynchronizedSet);
	}

	/**
	 * Null-safely convert a stream of integers to a synchronized set.
	 *
	 * @param intStream		stream of integers
	 * @return				new synchronized {@code HashSet} of mapped integers from stream
	 * 						that wrapped in {@link Option}
	 */
	public static Option<Set<Integer>> toSynchronizedSet(IntStream intStream) {
		return toSet(intStream, UwSet::newSynchronizedSet);
	}

	/**
	 * Null-safely convert a collection of objects to a set.
	 *
	 * @param collection	collection of objects
	 * @param function		function for collection mapping
	 * @param <T>			object type
	 * @return				set of mapped objects from collection
	 * 						or null
	 */
	public static <T> Set<T> toSetRaw(Collection<T> collection, Function<Collection<T>, Set<T>> function) {
		return toSet(collection, function).getOrNull();
	}

	/**
	 * Null-safely convert an array of objects to a set.
	 *
	 * @param array			array of objects
	 * @param function		function for collection mapping
	 * @param <T>			object type
	 * @return				set of mapped objects from array
	 * 						or null
	 */
	public static <T> Set<T> toSetRaw(T[] array, Function<Collection<T>, Set<T>> function) {
		return toSet(array, function).getOrNull();
	}

	/**
	 * Null-safely convert a stream of objects to a set.
	 *
	 * @param stream		stream of objects
	 * @param function		function for collection mapping
	 * @param <T>			object type
	 * @return				set of mapped objects from stream
	 * 						or null
	 */
	public static <T> Set<T> toSetRaw(Stream<T> stream, Function<Collection<T>, Set<T>> function) {
		return toSet(stream, function).getOrNull();
	}

	/**
	 * Null-safely convert a stream of integers to a set.
	 *
	 * @param intStream		stream of integers
	 * @param function		function for collection mapping
	 * @return				set of mapped objects from stream
	 * 						or null
	 */
	public static Set<Integer> toSetRaw(IntStream intStream, Function<Collection<Integer>, Set<Integer>> function) {
		return toSet(intStream, function).getOrNull();
	}

	/**
	 * Null-safely convert a collection of objects to a set.
	 *
	 * @param collection	collection of objects
	 * @param <T> 			object type
	 * @return				new {@code HashSet} of mapped objects from collection
	 * 						or null
	 */
	public static <T> Set<T> toSetRaw(Collection<T> collection) {
		return toSet(collection, HashSet::new).getOrNull();
	}

	/**
	 * Null-safely convert an array of objects to a set.
	 *
	 * @param array			array of objects
	 * @param <T> 			object type
	 * @return				new {@code HashSet} of mapped objects from array
	 * 						or null
	 */
	public static <T> Set<T> toSetRaw(T[] array) {
		return toSet(array, HashSet::new).getOrNull();
	}

	/**
	 * Null-safely convert a stream of objects to a set.
	 *
	 * @param stream		stream of objects
	 * @param <T> 			object type
	 * @return				new {@code HashSet} of mapped objects from stream
	 * 						or null
	 */
	public static <T> Set<T> toSetRaw(Stream<T> stream) {
		return toSet(stream, HashSet::new).getOrNull();
	}

	/**
	 * Null-safely convert a stream of integers to a set.
	 *
	 * @param intStream		stream of integers
	 * @return				new {@code HashSet} of mapped objects from stream
	 * 						or null
	 */
	public static Set<Integer> toSetRaw(IntStream intStream) {
		return toSet(intStream, HashSet::new).getOrNull();
	}

	/**
	 * Null-safely convert a collection of objects to a synchronized set.
	 *
	 * @param collection	collection of objects
	 * @param <T> 			object type
	 * @return				new synchronized {@code HashSet} of mapped objects from collection
	 * 						or null
	 */
	public static <T> Set<T> toSynchronizedSetRaw(Collection<T> collection) {
		return toSynchronizedSet(collection).getOrNull();
	}

	/**
	 * Null-safely convert an array of objects to a synchronized set.
	 *
	 * @param array			array of objects
	 * @param <T> 			object type
	 * @return				new synchronized {@code HashSet} of mapped objects from array
	 * 						or null
	 */
	public static <T> Set<T> toSynchronizedSetRaw(T[] array) {
		return toSynchronizedSet(array).getOrNull();
	}

	/**
	 * Null-safely convert a stream of objects to a synchronized set.
	 *
	 * @param stream		stream of objects
	 * @param <T> 			object type
	 * @return				new synchronized {@code HashSet} of mapped objects from stream
	 * 						or null
	 */
	public static <T> Set<T> toSynchronizedSetRaw(Stream<T> stream) {
		return toSynchronizedSet(stream).getOrNull();
	}

	/**
	 * Null-safely convert a stream of integers to a synchronized set.
	 *
	 * @param intStream		stream of integers
	 * @return				new synchronized {@code HashSet} of mapped integers from stream
	 * 						or null
	 */
	public static Set<Integer> toSynchronizedSetRaw(IntStream intStream) {
		return toSynchronizedSet(intStream).getOrNull();
	}

	/**
	 * Create new synchronized {@code Set} of {@code HashSet}.
	 *
	 * @param collection	collection of objects
	 * @param <T>			object type
	 * @return				new synchronized {@code HashSet}
	 * 						as {@code Set}
	 */
	private static <T> Set<T> newSynchronizedSet(Collection<T> collection) {
		return Collections.synchronizedSet(new HashSet<>(collection));
	}

	private UwSet() {
		throw new UnsupportedOperationException();
	}
}
