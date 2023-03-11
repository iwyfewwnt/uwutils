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
	 * An empty string instance.
	 */
	public static final String EMPTY = "";

	/**
	 * Safely trim the specified number of characters at the beginning and end of string.
	 *
	 * @param str		string to trim
	 * @param count		number of characters to trim
	 * @return			trimmed string that wrapped in {@link Option}
	 */
	public static Option<String> trim(String str, Integer count) {
		return Option.of(trimOrNull(str, count));
	}

	/**
	 * Safely trim the specified number of characters at the beginning and end of string or return default value.
	 *
	 * @param str				string to trim
	 * @param count				number of characters to trim
	 * @param defaultValue		default value to return on failure
	 * @return					default value, empty or trimmed string
	 */
	public static String trimOrElse(String str, Integer count, String defaultValue) {
		// TODO: Add case where count's sign describes
		//  the position & direction of the trimming

		if (str == null
				|| count == null
				|| count <= 0) {
			return defaultValue;
		}

		int length = str.length();

		if (count > length / 2) {
			return EMPTY;
		}

		try {
			return str.substring(count, length - count);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}

		return defaultValue;
	}

	/**
	 * Safely trim the specified number of characters at the beginning and end of string or return an empty one.
	 *
	 * <p>Wraps {@link UwString#trimOrElse(String, Integer, String)}
	 * w/ empty string as the default value.
	 *
	 * @param str				string to trim
	 * @param count				number of characters to trim
	 * @return					empty or trimmed string
	 */
	public static String trimOrEmpty(String str, Integer count) {
		return trimOrElse(str, count, EMPTY);
	}

	/**
	 * Safely trim the specified number of characters at the beginning and end of string or return {@code null}.
	 *
	 * <p>Wraps {@link UwString#trimOrElse(String, Integer, String)}
	 * w/ {@code null} as the default value.
	 *
	 * @param str				string to trim
	 * @param count				number of characters to trim
	 * @return					{@code null}, empty or trimmed string
	 */
	public static String trimOrNull(String str, Integer count) {
		return trimOrElse(str, count, null);
	}

	/**
	 * Safely trim the specified number of characters at the beginning and end of string or return the same string.
	 *
	 * <p>Wraps {@link UwString#trimOrElse(String, Integer, String)}
	 * w/ the input string as the default value.
	 *
	 * @param str				string to trim
	 * @param count				number of characters to trim
	 * @return					same, empty or trimmed string
	 */
	public static String trimOrSelf(String str, Integer count) {
		return trimOrElse(str, count, str);
	}

	private UwString() {
		throw new UnsupportedOperationException();
	}
}
