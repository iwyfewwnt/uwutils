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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Supplier;

/**
 * A bean utility.
 *
 * <p>{@code UwBean} is an alternative
 * to {@link java.util.ServiceLoader} class.
 */
@SuppressWarnings("unused")
public final class UwBean {

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
	 * Find all sub-class types of the specified service provider interface class or return a default value.
	 *
	 * @param clazz			service provider interface class
	 * @param classLoader	class loader to load classes by their names
	 * @param defaultValue	default value to return on failure
	 * @param <T>			interface type
	 * @return				list of sub-class types or the default value
	 */
	public static <T> List<Class<? extends T>> findSpiTypesOrElse(Class<T> clazz, List<Class<? extends T>> defaultValue, ClassLoader classLoader) {
		if (clazz == null) {
			return defaultValue;
		}

		classLoader = UwObject.ifNull(classLoader, UDefault.CLASS_LOADER);

		List<Class<? extends T>> result = new ArrayList<>();

		try {
			Enumeration<URL> urls = classLoader.getResources(String.format(SPI_PATH_FMT, clazz.getName()));

			while (urls.hasMoreElements()) {
				String[] content = read(urls.nextElement())
						.split("\\R");

				for (String className : content) {
					try {
						result.add(classLoader.loadClass(className)
								.asSubclass(clazz)
						);
					} catch (ClassNotFoundException | ClassCastException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException
				| NullPointerException
				| IllegalArgumentException e) {
			e.printStackTrace();
		}

		if (result.isEmpty()) {
			return defaultValue;
		}

		return result;
	}

	/**
	 * Find all sub-class types of the specified service provider interface class or return a default value.
	 *
	 * @param clazz					service provider interface class
	 * @param classLoader			class loader to load classes by their names
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <T>					interface type
	 * @return						list of sub-class types or the default value
	 */
	public static <T> List<Class<? extends T>> findSpiTypesOrElse(Class<T> clazz, Supplier<List<Class<? extends T>>> defaultValueSupplier, ClassLoader classLoader) {
		return UwObject.ifNull(findSpiTypesOrNull(clazz, classLoader), defaultValueSupplier);
	}

	/**
	 * Find all sub-class types of the specified service provider interface class or return {@code null}.
	 *
	 * <p>Wraps {@link #findSpiTypesOrElse(Class, List, ClassLoader)}
	 * w/ {@code null} as the default value.
	 *
	 * @param clazz			service provider interface class
	 * @param classLoader	class loader to load classes by their names
	 * @param <T>			interface type
	 * @return				list of sub-class types or {@code null}
	 */
	public static <T> List<Class<? extends T>> findSpiTypesOrNull(Class<T> clazz, ClassLoader classLoader) {
		return findSpiTypesOrElse(clazz, (List<Class<? extends T>>) null, classLoader);
	}

	/**
	 * Find all sub-class types of the specified service provider interface class or return a default value.
	 *
	 * <p>Wraps {@link #findSpiTypesOrElse(Class, List, ClassLoader)}
	 * w/ {@code null} as the class loader.
	 *
	 * @param clazz			service provider interface class
	 * @param defaultValue	default value to return on failure
	 * @param <T>			interface type
	 * @return				list of sub-class types or the default value
	 */
	public static <T> List<Class<? extends T>> findSpiTypesOrElse(Class<T> clazz, List<Class<? extends T>> defaultValue) {
		return findSpiTypesOrElse(clazz, defaultValue, null);
	}

	/**
	 * Find all sub-class types of the specified service provider interface class or return a default value.
	 *
	 * <p>Wraps {@link #findSpiTypesOrElse(Class, Supplier, ClassLoader)}
	 * w/ {@code null} as the class loader.
	 *
	 * @param clazz					service provider interface class
	 * @param defaultValueSupplier	supplier from which get the default value
	 * @param <T>					interface type
	 * @return						list of sub-class types or the default value
	 */
	public static <T> List<Class<? extends T>> findSpiTypesOrElse(Class<T> clazz, Supplier<List<Class<? extends T>>> defaultValueSupplier) {
		return findSpiTypesOrElse(clazz, defaultValueSupplier, null);
	}

	/**
	 * Find all sub-class types of the specified service provider interface class or return {@code null}.
	 *
	 * <p>Wraps {@link #findSpiTypesOrNull(Class, ClassLoader)}
	 * w/ {@code null} as the class loader.
	 *
	 * @param clazz		service provider interface class
	 * @param <T>		interface type
	 * @return			list of sub-class types or {@code null}
	 */
	public static <T> List<Class<? extends T>> findSpiTypesOrNull(Class<T> clazz) {
		return findSpiTypesOrNull(clazz, null);
	}

	/**
	 * Read content of the provided {@link URL} instance.
	 *
	 * @param url	url from which read the content
	 * @return		content as string instance
	 *
	 * @throws IllegalArgumentException		if URL is {@code null}
	 */
	private static String read(URL url) {
		if (url == null) {
			throw new IllegalArgumentException("URL mustn't be <null>");
		}

		StringBuilder sb = new StringBuilder();

		try {
			try (BufferedInputStream buff = new BufferedInputStream(url.openStream())) {
				int b;
				while ((b = buff.read()) != -1) {
					sb.append((char) b);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	private UwBean() {
		throw new UnsupportedOperationException();
	}
}
