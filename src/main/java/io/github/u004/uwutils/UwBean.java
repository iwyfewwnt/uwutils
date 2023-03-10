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

import java.util.ArrayList;
import java.util.List;

/**
 * A bean utility.
 *
 * <p>{@code UwBean} is an alternative
 * to {@link java.util.ServiceLoader} class.
 *
 * @since 0.1.3
 */
@SuppressWarnings("unused")
public final class UwBean {

	/**
	 * Find all subclasses of service provider interface.
	 *
	 * @param clazz				interface class
	 * @param context			class loader, if null default is current thread class loader
	 * @param throwOnFail		if {@code true} will throw exception if the one is occurred, if null default is true
	 * @param <T>				interface type
	 * @return					list of subclasses or null
	 */
	public static <T> List<Class<? extends T>> findSpiTypes(Class<T> clazz, ClassLoader context, Boolean throwOnFail) {
		context = ObjectUtils.defaultIfNull(context, UDefault.CONTEXT);
		throwOnFail = ObjectUtils.defaultIfNull(throwOnFail, UDefault.THROW_ON_FAIL);

		List<String> strings = UwResource.findSpiContent(clazz);

		if (strings == null) {
			return null;
		}

		List<Class<? extends T>> result = new ArrayList<>();

		for (String str : strings) {
			try {
				result.add(context.loadClass(str).asSubclass(clazz));
			} catch (Throwable throwable) {
				if (throwOnFail) {
					throw new RuntimeException(throwable);
				}
			}
		}

		if (result.isEmpty()) {
			return null;
		}

		return result;
	}

	/**
	 * Find all subclasses of service provider interface.
	 *
	 * <p>Wraps {@link UwBean#findSpiTypes(Class, ClassLoader, Boolean)}.
	 *
	 * @param clazz				interface class
	 * @param context			class loader, if null default is current thread class loader
	 * @param <T>				interface type
	 * @return					list of subclasses or null
	 */
	public static <T> List<Class<? extends T>> findSpiTypes(Class<T> clazz, ClassLoader context) {
		return findSpiTypes(clazz, context, null);
	}

	/**
	 * Find all subclasses of service provider interface.
	 *
	 * <p>Wraps {@link UwBean#findSpiTypes(Class, ClassLoader)}.
	 *
	 * @param clazz				interface class
	 * @param <T>				interface type
	 * @return					list of subclasses
	 */
	public static <T> List<Class<? extends T>> findSpiTypes(Class<T> clazz) {
		return findSpiTypes(clazz, null);
	}

	private UwBean() {
		throw new UnsupportedOperationException();
	}
}
