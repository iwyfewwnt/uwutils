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
	 * @param diff		number of characters to trim
	 * @return			trimmed string that wrapped in {@link Option}
	 */
	public static Option<String> trim(String str, Integer diff) {
		return Option.of(trimOrNull(str, diff));
	}

	/**
	 * Safely trim the specified number of characters at the beginning and end of string or return default value.
	 *
	 * @param str				string to trim
	 * @param diff				number of characters to trim
	 * @param defaultValue		default value to return on failure
	 * @return					default value, empty or trimmed string
	 */
	public static String trimOrElse(String str, Integer diff, String defaultValue) {
		if (str == null || diff == null) {
			return defaultValue;
		}

		if (diff == 0) {
			return str;
		}

		int count = Math.abs(diff);
		int length = str.length();
		int lendiv = length / 2;
		int lenmod = length % 2;

		if (count >= lendiv + Math.signum(diff) * lenmod) {
			return EMPTY;
		}

		try {
			if (diff < 0) {
				return str.substring(0, lendiv - count + lenmod)
						+ str.substring(lendiv + count, length);
			}

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
	 * @param str		string to trim
	 * @param diff		number of characters to trim
	 * @return			empty or trimmed string
	 */
	public static String trimOrEmpty(String str, Integer diff) {
		return trimOrElse(str, diff, EMPTY);
	}

	/**
	 * Safely trim the specified number of characters at the beginning and end of string or return {@code null}.
	 *
	 * <p>Wraps {@link UwString#trimOrElse(String, Integer, String)}
	 * w/ {@code null} as the default value.
	 *
	 * @param str		string to trim
	 * @param diff		number of characters to trim
	 * @return			{@code null}, empty or trimmed string
	 */
	public static String trimOrNull(String str, Integer diff) {
		return trimOrElse(str, diff, null);
	}

	/**
	 * Safely trim the specified number of characters at the beginning and end of string or return the same string.
	 *
	 * <p>Wraps {@link UwString#trimOrElse(String, Integer, String)}
	 * w/ the input string as the default value.
	 *
	 * @param str		string to trim
	 * @param diff		number of characters to trim
	 * @return			same, empty or trimmed string
	 */
	public static String trimOrSelf(String str, Integer diff) {
		return trimOrElse(str, diff, str);
	}

	private UwString() {
		throw new UnsupportedOperationException();
	}
}
