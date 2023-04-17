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

import java.util.Collection;
import java.util.Collections;

/**
 * A collection utility.
 *
 * <p>{@code UwCollection} is the utility class
 * that provide functionality to operate
 * with collections.
 */
@SuppressWarnings("unused")
public final class UwCollection {

	/**
	 * An empty collection instance.
	 */
	@SuppressWarnings("rawtypes")
	public static final Collection EMPTY = Collections.EMPTY_LIST;

	/**
	 * Check if the provided collection is unmodifiable.
	 *
	 * @param collection	collection to check for
	 * @return				boolean value as a result,
	 * 						true - yes, false - no
	 */
	@SuppressWarnings("unchecked")
	public static boolean isUnmodifiable(Collection<?> collection) {
		if (collection == null) {
			return false;
		}

		try {
			collection.addAll(EMPTY);

			return false;
		} catch (UnsupportedOperationException ignored) {
		}

		return true;
	}

	/**
	 * Get an unmodifiable view of the provided collection.
	 *
	 * @param collection	collection to get the view for
	 * @param <T>			element type
	 * @return				unmodifiable view of the collection
	 */
	public static <T> Collection<T> toUnmodifiable(Collection<T> collection) {
		if (collection == null) {
			return null;
		}

		if (isUnmodifiable(collection)) {
			return collection;
		}

		return Collections.unmodifiableCollection(collection);
	}

	private UwCollection() {
		throw new UnsupportedOperationException();
	}
}
