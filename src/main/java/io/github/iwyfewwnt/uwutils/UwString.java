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
 * A string utility.
 *
 * <p>{@code UwString} is the utility class
 * that provide functionality to operate
 * with strings.
 */
@SuppressWarnings("unused")
public final class UwString {

	/**
	 * An empty string instance.
	 */
	public static final String EMPTY = "";

	/**
	 * Safely trim the specified number of characters at the beginning and end of string or return a default value.
	 *
	 * @param str			string to trim
	 * @param diff			number of characters to trim
	 * @param defaultValue	default value to return on failure
	 * @return				default value, empty or trimmed string
	 */
	public static String trimOrElse(String str, Integer diff, String defaultValue) {
		if (str == null || diff == null) {
			return defaultValue;
		}

		if (diff == 0) {
			return str;
		}

		int count = Math.abs(diff);
		int signum = Integer.signum(diff);
		int length = str.length();
		int lendiv = length >> 1;
		int lenmod = length & 1;

		if (count >= lendiv + signum * lenmod) {
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
	 * Safely trim the specified number of characters at the beginning and end of string or return a default value.
	 *
	 * @param str					string to trim
	 * @param diff					number of characters to trim
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						default value, empty or trimmed string
	 */
	public static String trimOrElse(String str, Integer diff, Supplier<String> defaultValueSupplier) {
		return UwObject.ifNull(trimOrNull(str, diff), defaultValueSupplier);
	}

	/**
	 * Safely trim the specified number of characters at the beginning and end of string or return an empty one.
	 *
	 * <p>Wraps {@link #trimOrElse(String, Integer, String)}
	 * w/ {@link #EMPTY} as the default value.
	 *
	 * @param str	string to trim
	 * @param diff	number of characters to trim
	 * @return		empty or trimmed string
	 */
	public static String trimOrEmpty(String str, Integer diff) {
		return trimOrElse(str, diff, EMPTY);
	}

	/**
	 * Safely trim the specified number of characters at the beginning and end of string or return {@code null}.
	 *
	 * <p>Wraps {@link #trimOrElse(String, Integer, String)}
	 * w/ {@code null} as the default value.
	 *
	 * @param str	string to trim
	 * @param diff	number of characters to trim
	 * @return		{@code null}, empty or trimmed string
	 */
	public static String trimOrNull(String str, Integer diff) {
		return trimOrElse(str, diff, (String) null);
	}

	/**
	 * Safely trim the specified number of characters at the beginning and end of string or return the unchanged string.
	 *
	 * <p>Wraps {@link #trimOrElse(String, Integer, String)}
	 * w/ the input string as the default value.
	 *
	 * @param str	string to trim
	 * @param diff	number of characters to trim
	 * @return		same, empty or trimmed string
	 */
	public static String trimOrSelf(String str, Integer diff) {
		return trimOrElse(str, diff, str);
	}

	/**
	 * Safely replace certain characters from the provided string base w/ characters from the next base or return a default value.
	 *
	 * @param str			string to convert to the next character base
	 * @param currBase		current character base the string represented in
	 * @param nextBase		next character base to represent string in
	 * @param defaultValue	default value to return on failure
	 * @return				same or new string, or the default value
	 */
	public static String toBaseOrElse(String str, String currBase, String nextBase, String defaultValue) {
		if (str == null || currBase == null
				|| nextBase == null) {
			return defaultValue;
		}

		int stringLength = str.length();
		int currBaseLength = currBase.length();
		int nextBaseLength = nextBase.length();

		if (stringLength == 0 || currBaseLength == 0
				|| nextBaseLength == 0) {
			return str;
		}

		StringBuilder sb = new StringBuilder(stringLength);

		for (int i = 0; i < stringLength; i++) {
			int idx = currBase.indexOf(str.charAt(i));

			if (idx == -1) {
				return defaultValue;
			}

			sb.append(nextBase.charAt(idx % nextBaseLength));
		}

		return sb.toString();
	}

	/**
	 * Safely replace certain characters from the provided string base w/ characters from the next base or return a default value.
	 *
	 * @param str					string to convert to the next character base
	 * @param currBase				current character base the string represented in
	 * @param nextBase				next character base to represent string in
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						same or new string, or the default value
	 */
	public static String toBaseOrElse(String str, String currBase, String nextBase, Supplier<String> defaultValueSupplier) {
		return UwObject.ifNull(toBaseOrNull(str, currBase, nextBase), defaultValueSupplier);
	}

	/**
	 * Safely replace certain characters from the provided string base w/ characters from the next base or return an empty string.
	 *
	 * <p>Wraps {@link #toBaseOrElse(String, String, String, String)}
	 * w/ {@link #EMPTY} as the default value.
	 *
	 * @param str		string to convert to the next character base
	 * @param currBase	current character base the string represented in
	 * @param nextBase	next character base to represent string in
	 * @return			same or new string, or the empty one
	 */
	public static String toBaseOrEmpty(String str, String currBase, String nextBase) {
		return toBaseOrElse(str, currBase, nextBase, EMPTY);
	}

	/**
	 * Safely replace certain characters from the provided string base w/ characters from the next base or return {@code null}.
	 *
	 * <p>Wraps {@link #toBaseOrElse(String, String, String, String)}
	 * w/ {@code null} as the default value.
	 *
	 * @param str		string to convert to the next character base
	 * @param currBase	current character base the string represented in
	 * @param nextBase	next character base to represent string in
	 * @return			same or new string, or {@code null}
	 */
	public static String toBaseOrNull(String str, String currBase, String nextBase) {
		return toBaseOrElse(str, currBase, nextBase, (String) null);
	}

	/**
	 * Safely replace certain characters from the provided string base w/ characters from the next base or return the unchanged string.
	 *
	 * <p>Wraps {@link #toBaseOrElse(String, String, String, String)}
	 * w/ 1st argument as the default value.
	 *
	 * @param str		string to convert to the next character base
	 * @param currBase	current character base the string represented in
	 * @param nextBase	next character base to represent string in
	 * @return			same or new string
	 */
	public static String toBaseOrSelf(String str, String currBase, String nextBase) {
		return toBaseOrElse(str, currBase, nextBase, str);
	}

	/**
	 * Safely capitalize provided string or return a default value.
	 *
	 * @param str			string to capitalize
	 * @param defaultValue	default value to return on failure
	 * @return				capitalized string or the default value
	 */
	public static String capitalizeOrElse(String str, String defaultValue) {
		if (str == null) {
			return defaultValue;
		}

		if (str.isEmpty()) {
			return str;
		}

		String firstLetter = str.substring(0, 1)
				.toUpperCase();

		if (str.length() == 1) {
			return firstLetter;
		}

		return firstLetter + str.substring(1);
	}

	/**
	 * Safely capitalize provided string or return a default value.
	 *
	 * @param str					string to capitalize
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						capitalized string or the default value
	 */
	public static String capitalizeOrElse(String str, Supplier<String> defaultValueSupplier) {
		return UwObject.ifNull(capitalizeOrNull(str), defaultValueSupplier);
	}

	/**
	 * Safely capitalize provided string or return an empty string.
	 *
	 * <p>Wraps {@link #capitalizeOrElse(String, String)}
	 * w/ {@link #EMPTY} as the default value.
	 *
	 * @param str	string to capitalize
	 * @return		capitalized string or the empty one
	 */
	public static String capitalizeOrEmpty(String str) {
		return capitalizeOrElse(str, EMPTY);
	}

	/**
	 * Safely capitalize provided string or return {@code null}.
	 *
	 * <p>Wraps {@link #capitalizeOrElse(String, String)}
	 * w/ {@code null} as the default value.
	 *
	 * @param str	string to capitalize
	 * @return		capitalized string or {@code null}
	 */
	public static String capitalizeOrNull(String str) {
		return capitalizeOrElse(str, (String) null);
	}

	private UwString() {
		throw new UnsupportedOperationException();
	}
}
