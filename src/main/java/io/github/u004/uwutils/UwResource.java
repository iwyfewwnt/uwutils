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

import org.apache.commons.lang3.ObjectUtils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * A resource utility.
 *
 * <p>{@code UwResource} is the utility class
 * that provide functionality to operate
 * with resources.
 *
 * @since 0.1.3
 */
@SuppressWarnings("unused")
public final class UwResource {

	/**
	 * SPI path format string.
	 *
	 * <p>Arguments in order:
	 * <ul>
	 *     <li>String :: Class name.
	 * </ul>
	 */
	private static final String SPI_PATH_FMT = "META-INF/services/%s";

	/**
	 * Default split content boolean value.
	 *
	 * <p>If {@code true} will split read resource content by end of lines.
	 */
	private static final boolean DEFAULT_SPLIT_CONTENT = false;

	/**
	 * Find content/s for the provided resource/s path.
	 *
	 * @param path				resource path
	 * @param context			class loader, if null default is current thread class loader
	 * @param throwOnFail		if {@code true} will throw exception if the one is occurred, if null default is true
	 * @param splitContent		if {@code true} will split resource content by end of lines, if null default is false
	 * @return					list of resource contents
	 */
	public static List<String> findContent(String path, ClassLoader context, Boolean throwOnFail, Boolean splitContent) {
		if (path == null) {
			return null;
		}

		List<String> result = new ArrayList<>();

		context = ObjectUtils.defaultIfNull(context, UDefault.CONTEXT);
		throwOnFail = ObjectUtils.defaultIfNull(throwOnFail, UDefault.THROW_ON_FAIL);
		splitContent = ObjectUtils.defaultIfNull(splitContent, DEFAULT_SPLIT_CONTENT);

		try {
			Enumeration<URL> urls = context.getResources(path);

			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				String content = read(url, throwOnFail);

				Objects.requireNonNull(content, "Resource content is <null>");

				if (splitContent) {
					result.addAll(Arrays.asList(
							content.split("\\R")
					));
				} else {
					result.add(content);
				}
			}
		} catch (Throwable throwable) {
			if (throwOnFail) {
				throw new RuntimeException(throwable);
			}
		}

		if (result.isEmpty()) {
			return null;
		}

		return result;
	}

	/**
	 * Find content/s for the provided resource/s path.
	 *
	 * <p>Wraps {@link UwResource#findContent(String, ClassLoader, Boolean, Boolean)}.
	 *
	 * @param path				resource path
	 * @param context			class loader, if null default is current thread class loader
	 * @param throwOnFail		if {@code true} will throw exception if the one is occurred, if null default is true
	 * @return					list of resource contents
	 */
	public static List<String> findContent(String path, ClassLoader context, Boolean throwOnFail) {
		return findContent(path, context, throwOnFail, null);
	}

	/**
	 * Find content/s for the provided resource/s path.
	 *
	 * <p>Wraps {@link UwResource#findContent(String, ClassLoader, Boolean)}.
	 *
	 * @param path				resource path
	 * @param context			class loader, if null default is current thread class loader
	 * @return					list of resource contents
	 */
	public static List<String> findContent(String path, ClassLoader context) {
		return findContent(path, context, null);
	}

	/**
	 * Find content/s for the provided resource/s path.
	 *
	 * <p>Wraps {@link UwResource#findContent(String, ClassLoader)}.
	 *
	 * @param path				resource path
	 * @return					list of resource contents
	 */
	public static List<String> findContent(String path) {
		return findContent(path, null);
	}

	/**
	 * Find SPI content/s for the provided class.
	 *
	 * <p>Wraps {@link UwResource#findContent(String, ClassLoader, Boolean, Boolean)}.
	 *
	 * @param clazz				class
	 * @param context			class loader, if null default is current thread class loader
	 * @param throwOnFail		if {@code true} will throw exception if the one is occurred, if null default is true
	 * @return					list of SPI contents
	 */
	public static List<String> findSpiContent(Class<?> clazz, ClassLoader context, Boolean throwOnFail) {
		if (clazz == null) {
			return null;
		}

		return findContent(String.format(SPI_PATH_FMT, clazz.getName()), context, throwOnFail, true);
	}

	/**
	 * Find SPI content/s for the provided class.
	 *
	 * <p>Wraps {@link UwResource#findSpiContent(Class, ClassLoader, Boolean)}.
	 *
	 * @param clazz				class
	 * @param context			class loader, if null default is current thread class loader
	 * @return					list of SPI contents
	 */
	public static List<String> findSpiContent(Class<?> clazz, ClassLoader context) {
		return findSpiContent(clazz, context, null);
	}

	/**
	 * Find SPI content/s for the provided class.
	 *
	 * <p>Wraps {@link UwResource#findSpiContent(Class, ClassLoader)}.
	 *
	 * @param clazz				class
	 * @return					list of SPI contents
	 */
	public static List<String> findSpiContent(Class<?> clazz) {
		return findSpiContent(clazz, null);
	}

	/**
	 * Read content of the provided resource.
	 *
	 * @param url				resource
	 * @param throwOnFail		if {@code true} will throw exception if the one is occurred, if null default is true
	 * @return					resource content
	 */
	public static String read(URL url, Boolean throwOnFail) {
		throwOnFail = ObjectUtils.defaultIfNull(throwOnFail, UDefault.THROW_ON_FAIL);

		StringBuilder sb = new StringBuilder();

		try {
			try (InputStream in = url.openStream();
				 BufferedInputStream buff = new BufferedInputStream(in)
			) {
				int b;
				while ((b = buff.read()) != -1) {
					sb.append((char) b);
				}
			}
		} catch (Throwable throwable) {
			if (throwOnFail) {
				throw new RuntimeException(throwable);
			}
		}

		String str = sb.toString()
				.trim();

		if (str.isEmpty()) {
			return null;
		}

		return str;
	}

	/**
	 * Read content of the provided resource.
	 *
	 * <p>Wraps {@link UwResource#read(URL, Boolean)}.
	 *
	 * @param url				resource
	 * @return					resource content
	 */
	public static String read(URL url) {
		return read(url, null);
	}

	private UwResource() {
		throw new UnsupportedOperationException();
	}
}
