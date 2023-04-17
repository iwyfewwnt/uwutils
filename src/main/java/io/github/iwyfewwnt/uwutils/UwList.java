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
import java.util.List;

/**
 * A list utility.
 *
 * <p>{@code UwList} is the utility class
 * that provide functionality to operate
 * with lists.
 */
@SuppressWarnings("unused")
public final class UwList {

	/**
	 * An empty list instance.
	 */
	@SuppressWarnings("rawtypes")
	public static final List EMPTY = Collections.EMPTY_LIST;

	/**
	 * Check if the provided list is unmodifiable.
	 *
	 * <p>Redirects call to the {@link UwCollection#isUnmodifiable(Collection)}.
	 *
	 * @param list	list to check for
	 * @return		boolean value as a result,
	 * 				true - yes, false - no
	 */
	public static boolean isUnmodifiable(List<?> list) {
		return UwCollection.isUnmodifiable(list);
	}

	/**
	 * Get an unmodifiable view of the provided list.
	 *
	 * @param list	list to get the view for
	 * @param <T>	element type
	 * @return		unmodifiable view of the list
	 */
	public static <T> List<T> toUnmodifiable(List<T> list) {
		if (list == null) {
			return null;
		}

		if (isUnmodifiable(list)) {
			return list;
		}

		return Collections.unmodifiableList(list);
	}

	private UwList() {
		throw new UnsupportedOperationException();
	}
}
