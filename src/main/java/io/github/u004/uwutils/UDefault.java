/*
 * Copyright 2023 u004
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
 * A set of default values.
 */
final class UDefault {

	/**
	 * A default class loader instance.
	 */
	public static final ClassLoader CONTEXT = Thread.currentThread()
			.getContextClassLoader();

	/**
	 * A default throw on fail boolean value.
	 */
	public static final boolean THROW_ON_FAIL = true;

	private UDefault() {
		throw new UnsupportedOperationException();
	}
}
