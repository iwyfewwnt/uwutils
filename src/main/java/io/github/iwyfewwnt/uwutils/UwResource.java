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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

/**
 * A resource utility.
 *
 * <p>{@code UwResource} is the utility class
 * that provide functionality to operate
 * with resource files.
 */
@SuppressWarnings("unused")
public final class UwResource {

	/**
	 * A class instance of this class.
	 */
	private static final Class<?> CLASS = UwResource.class;

	/**
	 * Safely get a resource file and read it into string or return a default value.
	 *
	 * @param path			path to the resource file
	 * @param charset		charset that is the resource file represented in
	 * @param classLoader	class loader to get the resource file from
	 * @param defaultValue	default value to return on failure
	 * @return				content of the resource file or the default value
	 */
	public static String getAsStringOrElse(String path, Charset charset, ClassLoader classLoader, String defaultValue) {
		if (path == null || path.isEmpty()) {
			return defaultValue;
		}

		classLoader = UwObject.ifNull(classLoader, UwSystem.getContextClassLoader(CLASS));
		if (classLoader == null) {
			return defaultValue;
		}

		URL url = classLoader.getResource(path);
		if (url == null) {
			return defaultValue;
		}

		URI uri = null;
		try {
			uri = url.toURI();
		} catch (URISyntaxException ignored) {
		}

		if (uri == null) {
			return defaultValue;
		}

		Path nioPath = null;
		try {
			nioPath = Paths.get(uri);
		} catch (FileSystemNotFoundException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException ignored) {
		}

		if (nioPath == null) {
			return defaultValue;
		}

		byte[] bytes = null;
		try {
			bytes = Files.readAllBytes(nioPath);
		} catch (OutOfMemoryError | SecurityException e) {
			e.printStackTrace();
		} catch (IOException ignored) {
		}

		if (bytes == null) {
			return defaultValue;
		}

		charset = UwObject.ifNull(charset, UDefault.CHARSET);

		return new String(bytes, charset);
	}

	/**
	 * Safely get a resource file and read it into string or return a default value.
	 *
	 * <p>Wraps {@link #getAsStringOrElse(String, Charset, ClassLoader, String)}
	 * w/ {@code null} as the class loader.
	 *
	 * @param path			path to the resource file
	 * @param charset		charset that is the resource file represented in
	 * @param defaultValue	default value to return on failure
	 * @return				content of the resource file or the default value
	 */
	public static String getAsStringOrElse(String path, Charset charset, String defaultValue) {
		return getAsStringOrElse(path, charset, null, defaultValue);
	}

	/**
	 * Safely get a resource file and read it into string or return a default value.
	 *
	 * <p>Wraps {@link #getAsStringOrElse(String, Charset, ClassLoader, String)}
	 * w/ {@code null} as the charset.
	 *
	 * @param path			path to the resource file
	 * @param classLoader	class loader to get the resource file from
	 * @param defaultValue	default value to return on failure
	 * @return				content of the resource file or the default value
	 */
	public static String getAsStringOrElse(String path, ClassLoader classLoader, String defaultValue) {
		return getAsStringOrElse(path, null, classLoader, defaultValue);
	}

	/**
	 * Safely get a resource file and read it into string or return a default value.
	 *
	 * <p>Wraps {@link #getAsStringOrElse(String, ClassLoader, String)}
	 * w/ {@code null} as the class loader.
	 *
	 * @param path			path to the resource file
	 * @param defaultValue	default value to return on failure
	 * @return				content of the resource file or the default value
	 */
	public static String getAsStringOrElse(String path, String defaultValue) {
		return getAsStringOrElse(path, (ClassLoader) null, defaultValue);
	}

	/**
	 * Safely get a resource file and read it into string or return a default value.
	 *
	 * @param path					path to the resource file
	 * @param charset				charset that is the resource file represented in
	 * @param classLoader			class loader to get the resource file from
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						content of the resource file or the default value
	 */
	public static String getAsStringOrElse(String path, Charset charset, ClassLoader classLoader, Supplier<String> defaultValueSupplier) {
		return UwObject.ifNull(getAsStringOrNull(path, charset, classLoader), defaultValueSupplier);
	}

	/**
	 * Safely get a resource file and read it into string or return a default value.
	 *
	 * @param path					path to the resource file
	 * @param charset				charset that is the resource file represented in
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						content of the resource file or the default value
	 */
	public static String getAsStringOrElse(String path, Charset charset, Supplier<String> defaultValueSupplier) {
		return UwObject.ifNull(getAsStringOrNull(path, charset), defaultValueSupplier);
	}

	/**
	 * Safely get a resource file and read it into string or return a default value.
	 *
	 * @param path					path to the resource file
	 * @param classLoader			class loader to get the resource file from
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						content of the resource file or the default value
	 */
	public static String getAsStringOrElse(String path, ClassLoader classLoader, Supplier<String> defaultValueSupplier) {
		return UwObject.ifNull(getAsStringOrNull(path, classLoader), defaultValueSupplier);
	}

	/**
	 * Safely get a resource file and read it into string or return a default value.
	 *
	 * @param path					path to the resource file
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @return						content of the resource file or the default value
	 */
	public static String getAsStringOrElse(String path, Supplier<String> defaultValueSupplier) {
		return UwObject.ifNull(getAsStringOrNull(path), defaultValueSupplier);
	}

	/**
	 * Safely get a resource file and read it into string or return an empty string.
	 *
	 * <p>Wraps {@link #getAsStringOrElse(String, Charset, ClassLoader, String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @param path			path to the resource file
	 * @param charset		charset that is the resource file represented in
	 * @param classLoader	class loader to get the resource file from
	 * @return				content of the resource file or the empty string
	 */
	public static String getAsStringOrEmpty(String path, Charset charset, ClassLoader classLoader) {
		return getAsStringOrElse(path, charset, classLoader, UwString.EMPTY);
	}

	/**
	 * Safely get a resource file and read it into string or return an empty string.
	 *
	 * <p>Wraps {@link #getAsStringOrElse(String, Charset, String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @param path		path to the resource file
	 * @param charset	charset that is the resource file represented in
	 * @return			content of the resource file or the empty string
	 */
	public static String getAsStringOrEmpty(String path, Charset charset) {
		return getAsStringOrElse(path, charset, UwString.EMPTY);
	}

	/**
	 * Safely get a resource file and read it into string or return an empty string.
	 *
	 * <p>Wraps {@link #getAsStringOrElse(String, ClassLoader, String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @param path			path to the resource file
	 * @param classLoader	class loader to get the resource file from
	 * @return				content of the resource file or the empty string
	 */
	public static String getAsStringOrEmpty(String path, ClassLoader classLoader) {
		return getAsStringOrElse(path, classLoader, UwString.EMPTY);
	}

	/**
	 * Safely get a resource file and read it into string or return an empty string.
	 *
	 * <p>Wraps {@link #getAsStringOrElse(String, String)}
	 * w/ {@link UwString#EMPTY} as the default value.
	 *
	 * @param path	path to the resource file
	 * @return		content of the resource file or the empty string
	 */
	public static String getAsStringOrEmpty(String path) {
		return getAsStringOrElse(path, UwString.EMPTY);
	}

	/**
	 * Safely get a resource file and read it into string or return {@code null}.
	 *
	 * <p>Wraps {@link #getAsStringOrElse(String, Charset, ClassLoader, String)}
	 * w/ {@code null} as the default value.
	 *
	 * @param path			path to the resource file
	 * @param charset		charset that is the resource file represented in
	 * @param classLoader	class loader to get the resource file from
	 * @return				content of the resource file or {@code null}
	 */
	public static String getAsStringOrNull(String path, Charset charset, ClassLoader classLoader) {
		return getAsStringOrElse(path, charset, classLoader, (String) null);
	}

	/**
	 * Safely get a resource file and read it into string or return {@code null}.
	 *
	 * <p>Wraps {@link #getAsStringOrElse(String, Charset, String)}
	 * w/ {@code null} as the default value.
	 *
	 * @param path		path to the resource file
	 * @param charset	charset that is the resource file represented in
	 * @return			content of the resource file or {@code null}
	 */
	public static String getAsStringOrNull(String path, Charset charset) {
		return getAsStringOrElse(path, charset, (String) null);
	}

	/**
	 * Safely get a resource file and read it into string or return {@code null}.
	 *
	 * <p>Wraps {@link #getAsStringOrElse(String, ClassLoader, String)}
	 * w/ {@code null} as the default value.
	 *
	 * @param path			path to the resource file
	 * @param classLoader	class loader to get the resource file from
	 * @return				content of the resource file or {@code null}
	 */
	public static String getAsStringOrNull(String path, ClassLoader classLoader) {
		return getAsStringOrElse(path, classLoader, (String) null);
	}

	/**
	 * Safely get a resource file and read it into string or return {@code null}.
	 *
	 * <p>Wraps {@link #getAsStringOrElse(String, String)}
	 * w/ {@code null} as the default value.
	 *
	 * @param path	path to the resource file
	 * @return		content of the resource file or {@code null}
	 */
	public static String getAsStringOrNull(String path) {
		return getAsStringOrElse(path, (String) null);
	}

	private UwResource() {
		throw new UnsupportedOperationException();
	}
}
