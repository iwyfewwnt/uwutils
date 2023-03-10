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

import org.apache.commons.lang3.ObjectUtils;

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
	 * Trim the specified number of characters at the beginning and end of a string.
	 *
	 * @param str		string to operate with
	 * @param count		number of characters to trim
	 * @return			new string, empty or the same string
	 */
	public static String trim(String str, Integer count) {
		if (ObjectUtils.anyNull(str, count)
				|| count > str.length() / 2
		) {
			return "";
		}

		if (count <= 0) {
			return str;
		}

		return str.substring(count, str.length() - count);
	}

	private UwString() {
		throw new UnsupportedOperationException();
	}
}
