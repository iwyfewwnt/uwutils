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

/**
 * A string utility.
 *
 * <p>{@code UwString} is the utility class
 * that provide functionality to operate
 * with strings.
 *
 * @since 0.1.0
 */
@SuppressWarnings("unused")
public final class UwString {

	/**
	 * Trim the specified number of characters at the beginning and end of string.
	 *
	 * @param str				string to trim
	 * @param count				number of characters to trim
	 * @param defaultValue		default value to return on failure
	 * @return					default value, empty or new string
	 */
	public static String trimOrElse(String str, Integer count, String defaultValue) {
		if (str == null
				|| count == null
				|| count <= 0) {
			return defaultValue;
		}

		int length = str.length();

		if (count > length / 2) {
			return "";
		}

		try {
			return str.substring(count, length - count);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}

		return defaultValue;
	}

	/**
	 * Trim the specified number of characters at the beginning and end of string.
	 *
	 * <p>Wraps {@link UwString#trimOrElse(String, Integer, String)}
	 * w/ empty string as the default value.
	 *
	 * @param str				string to trim
	 * @param count				number of characters to trim
	 * @return					empty or new string
	 */
	public static String trimOrEmpty(String str, Integer count) {
		return trimOrElse(str, count, "");
	}

	/**
	 * Trim the specified number of characters at the beginning and end of string.
	 *
	 * <p>Wraps {@link UwString#trimOrElse(String, Integer, String)}
	 * w/ {@code null} as the default value.
	 *
	 * @param str				string to trim
	 * @param count				number of characters to trim
	 * @return					{@code null}, empty or new string
	 */
	public static String trimOrNull(String str, Integer count) {
		return trimOrElse(str, count, null);
	}

	/**
	 * Trim the specified number of characters at the beginning and end of string.
	 *
	 * <p>Wraps {@link UwString#trimOrElse(String, Integer, String)}
	 * w/ the input string as the default value.
	 *
	 * @param str				string to trim
	 * @param count				number of characters to trim
	 * @return					same, empty or new string
	 */
	public static String trimOrSelf(String str, Integer count) {
		return trimOrElse(str, count, str);
	}

	private UwString() {
		throw new UnsupportedOperationException();
	}
}
