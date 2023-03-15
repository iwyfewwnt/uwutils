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

import java.util.HashMap;
import java.util.Map;

/**
 * An enum utility.
 *
 * <p>{@code UwEnum} is the utility class
 * that provide functionality to operate
 * with enums.
 */
@SuppressWarnings("unused")
public final class UwEnum {

	/**
	 * An enum values cache by the {@link Class} as the key and enum constants as the value.
	 */
	private static final Map<Class<Enum<?>>, Object[]> VALUES_CACHE = new HashMap<>();

	/**
	 * Get an array of enum constants for the provided enum class.
	 *
	 * <p>Prevents values cloning by caching them in the map.
	 *
	 * @param clazz		enum class
	 * @param <T>		enum type
	 * @return			array of enum constants
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum<T>> T[] values(Class<T> clazz) {
		if (clazz == null) {
			return null;
		}

		T[] values = (T[]) VALUES_CACHE.get(clazz);

		if (values == null) {
			VALUES_CACHE.put((Class<Enum<?>>) clazz, (values = clazz.getEnumConstants()));
		}

		return values;
	}

	private UwEnum() {
		throw new UnsupportedOperationException();
	}
}
