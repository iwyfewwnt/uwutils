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

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * A system utility.
 *
 * <p>{@code UwSystem} is the utility class
 * that provide functionality to operate
 * with Java's System module.
 */
@SuppressWarnings("unused")
public final class UwSystem {

	/**
	 * A silent print stream that do nothing.
	 */
	private static final PrintStream SILENT_PRINT_STREAM = new PrintStream(new OutputStream() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void write(byte[] b) {
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void write(byte[] b, int off, int len) {
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void flush() {
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void close() {
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void write(int b) {
		}
	});

	/**
	 * A previous {@link System#err} value before silencing the error print.
	 */
	private static volatile PrintStream PREV_ERROR_PRINT_STREAM = null;

	/**
	 * Disable system error print.
	 *
	 * <p>Sets the system error print to the silent output stream
	 * that do nothing.
	 */
	public static void disableErrorPrint() {
		if (PREV_ERROR_PRINT_STREAM != null) {
			return;
		}

		PREV_ERROR_PRINT_STREAM = System.err;

		System.setErr(SILENT_PRINT_STREAM);
	}

	/**
	 * Enable system error print.
	 *
	 * <p>Sets the system error print to the saved backup
	 * of the output stream.
	 */
	public static void enableErrorPrint() {
		if (PREV_ERROR_PRINT_STREAM == null) {
			return;
		}

		System.setErr(PREV_ERROR_PRINT_STREAM);

		PREV_ERROR_PRINT_STREAM = null;
	}

	/**
	 * Suppress system error print for the provided runnable.
	 *
	 * <p>Disables system error print, runs the provided runnable,
	 * enables system error print. If an error occurred while running runnable
	 * it will print a stack trace after enabling system error print back.
	 *
	 * @param runnable	runnable instance
	 */
	public static void suppressErrorPrint(Runnable runnable) {
		if (runnable == null) {
			return;
		}

		disableErrorPrint();

		Throwable throwable = null;

		try {
			runnable.run();
		} catch (Throwable e) {
			throwable = e;
		}

		enableErrorPrint();

		if (throwable != null) {
			throwable.printStackTrace();
		}
	}
}
