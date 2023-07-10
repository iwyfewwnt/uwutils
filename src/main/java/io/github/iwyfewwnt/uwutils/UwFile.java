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

import java.util.function.Supplier;

/**
 * A file utility.
 *
 * <p>{@code UwFile} is the utility class
 * that provide functionality to operate
 * with file-related things.
 */
@SuppressWarnings("unused")
public final class UwFile {

	/**
	 * Get a normalized file path of the provided path string or return a default value.
	 *
	 * <p><ul>
	 *     <li>Trims the file path.
	 *     <li>Replaces back-slashes with forward-slashes.
	 *     <li>Replaces duplicated forward-slashes with only 1.
	 *     <li>Removes leading and trailing forward-slashes.
	 * </ul>
	 *
	 * @param path			file path to normalize
	 * @param defaultValue	default value to return on failure
	 * @return				normalized file path
	 */
	public static String getPathOrElse(String path, String defaultValue) {
		if (path == null) {
			return defaultValue;
		}

		path = path.trim()
				.replace("\\", "/")
				.replaceAll("/+", "/")
				.replaceAll("^/|/$", "");

		if (path.isEmpty()) {
			return defaultValue;
		}

		return path;
	}

	/**
	 * Get a normalized file path of the provided path string or return a default value.
	 *
	 * <p><ul>
	 *     <li>Trims the file path.
	 *     <li>Replaces back-slashes with forward-slashes.
	 *     <li>Replaces duplicated forward-slashes with only 1.
	 *     <li>Removes leading and trailing forward-slashes.
	 * </ul>
	 *
	 * @param path					file path to normalize
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						normalized file path
	 */
	public static String getPathOrElse(String path, Supplier<String> defaultValueSupplier) {
		return UwObject.ifNull(getPathOrNull(path), defaultValueSupplier);
	}

	/**
	 * Get a normalized file path of the provided path string or return an empty string.
	 *
	 * <p><ul>
	 *     <li>Trims the file path.
	 *     <li>Replaces back-slashes with forward-slashes.
	 *     <li>Replaces duplicated forward-slashes with only 1.
	 *     <li>Removes leading and trailing forward-slashes.
	 * </ul>
	 *
	 * <p>Wraps {@link #getPathOrElse(String, String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @param path	file path to normalize
	 * @return		normalized file path or the empty string
	 */
	public static String getPathOrEmpty(String path) {
		return getPathOrElse(path, UwString.EMPTY);
	}

	/**
	 * Get a normalized file path of the provided path string or return {@code null}.
	 *
	 * <p><ul>
	 *     <li>Trims the file path.
	 *     <li>Replaces back-slashes with forward-slashes.
	 *     <li>Replaces duplicated forward-slashes with only 1.
	 *     <li>Removes leading and trailing forward-slashes.
	 * </ul>
	 *
	 * <p>Wraps {@link #getPathOrElse(String, String)}
	 * w/ {@code null} as the default value.
	 *
	 * @param path	file path to normalize
	 * @return		normalized file path or {@code null}
	 */
	public static String getPathOrNull(String path) {
		return getPathOrElse(path, (String) null);
	}

	/**
	 * Get a normalized file path of the provided path string or return unchanged string.
	 *
	 * <p><ul>
	 *     <li>Trims the file path.
	 *     <li>Replaces back-slashes with forward-slashes.
	 *     <li>Replaces duplicated forward-slashes with only 1.
	 *     <li>Removes leading and trailing forward-slashes.
	 * </ul>
	 *
	 * <p>Wraps {@link #getPathOrElse(String, String)}
	 * w/ 1st argument as the default value.
	 *
	 * @param path	file path to normalize
	 * @return		normalized file path or unchanged string
	 */
	public static String getPathOrSelf(String path) {
		return getPathOrElse(path, path);
	}

	/**
	 * Get file name from the provided file path or return a default value.
	 *
	 * @param path			file path to get the name from
	 * @param defaultValue	default value to return on failure
	 * @return				file name or the default value
	 */
	public static String getNameOrElse(String path, String defaultValue) {
		path = getPathOrNull(path);
		if (path == null) {
			return defaultValue;
		}

		return path.substring(path.lastIndexOf('/') + 1);
	}

	/**
	 * Get file name from the provided file path or return a default value.
	 *
	 * @param path					file path to get the name from
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						file name or the default value
	 */
	public static String getNameOrElse(String path, Supplier<String> defaultValueSupplier) {
		return UwObject.ifNull(getNameOrNull(path), defaultValueSupplier);
	}

	/**
	 * Get file name from the provided file path or return an empty string.
	 *
	 * <p>Wraps {@link #getNameOrElse(String, String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @param path	file path to get the name from
	 * @return		file name or the empty string
	 */
	public static String getNameOrEmpty(String path) {
		return getNameOrElse(path, UwString.EMPTY);
	}

	/**
	 * Get file name from the provided file path or return {@code null}.
	 *
	 * <p>Wraps {@link #getNameOrElse(String, String)}
	 * w/ {@code null} as the default value.
	 *
	 * @param path	file path to get the name from
	 * @return		file name or {@code null}
	 */
	public static String getNameOrNull(String path) {
		return getNameOrElse(path, (String) null);
	}

	private UwFile() {
		throw new UnsupportedOperationException();
	}
}
