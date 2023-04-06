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
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
	 * A error parallel output stream.
	 */
	private static final ParallelOutputStream ERR_STREAM = new ParallelOutputStream(System.err);

	/**
	 * A standard parallel output stream.
	 */
	private static final ParallelOutputStream OUT_STREAM = new ParallelOutputStream(System.out);

	/**
	 * A error parallel print stream.
	 */
	public static final PrintStream err = new PrintStream(ERR_STREAM);

	/**
	 * A standard parallel print stream.
	 */
	public static final PrintStream out = new PrintStream(OUT_STREAM);

	/**
	 * A system output stream backup map.
	 */
	private static final Map<PrintStream, PrintStream> BACKUP_MAP = new ConcurrentHashMap<>();

	/**
	 * Setup parallel error output stream for the system.
	 */
	public static void setupParallelErrorPrint() {
		setupPrint(UwSystem.err, System.err, System::setErr);
	}

	/**
	 * Backup the error system output stream.
	 */
	public static void backupSystemErrorPrint() {
		backupPrint(UwSystem.err, System::setErr);
	}

	/**
	 * Setup parallel standard output stream for the system.
	 */
	public static void setupParallelOutputPrint() {
		setupPrint(UwSystem.out, System.out, System::setOut);
	}

	/**
	 * Backup the standard system output stream.
	 */
	public static void backupSystemOutputPrint() {
		backupPrint(UwSystem.out, System::setOut);
	}

	/**
	 * Set up the provided print stream for the system.
	 *
	 * @param key		print stream to set up system for
	 * @param backup	system print stream to backup
	 * @param consumer	consumer that sets up the print stream
	 */
	private static void setupPrint(PrintStream key, PrintStream backup, Consumer<PrintStream> consumer) {
		if (key == null || backup == null
				|| consumer == null) {
			return;
		}

		if (BACKUP_MAP.get(key) != null) {
			return;
		}

		BACKUP_MAP.put(key, backup);
		consumer.accept(key);
	}

	/**
	 * Backup the system print stream.
	 *
	 * @param key		print stream that was set before
	 * @param consumer	consumer that sets up the print stream
	 */
	private static void backupPrint(PrintStream key, Consumer<PrintStream> consumer) {
		if (key == null
				|| consumer == null) {
			return;
		}

		PrintStream backup = BACKUP_MAP.get(key);
		if (backup == null) {
			return;
		}

		consumer.accept(backup);
		BACKUP_MAP.remove(key);
	}

	/**
	 * Check if the error output stream is enabled for the provided thread.
	 *
	 * @param thread	thread to check the stream for
	 * @return			{@code true} if enabled
	 * 					or {@code false} if disabled
	 */
	public static boolean isErrorPrintEnabled(Thread thread) {
		return ERR_STREAM.isEnabled(thread);
	}

	/**
	 * Check if the error output stream is enabled for the current thread.
	 *
	 * @return	{@code true} if enabled
	 * 			or {@code false} if disabled
	 */
	public static boolean isErrorPrintEnabled() {
		return ERR_STREAM.isEnabled();
	}

	/**
	 * Enable the error output stream for the provided thread.
	 *
	 * @param thread	thread to enable the stream for
	 */
	public static void enableErrorPrint(Thread thread) {
		ERR_STREAM.enable(thread);
	}

	/**
	 * Enable the error output stream for the provided thread.
	 *
	 * <p>Calls the provided callable after enabling the stream
	 * for the thread and backups an initial stream state after
	 * finishing the run.
	 *
	 * @param thread	thread to enable the stream for
	 * @param callable 	callable to call after the switch
	 */
	@SuppressWarnings("UnusedReturnValue")
	public static <R> R enableErrorPrint(Thread thread, Callable<R> callable) {
		return suppressErrorPrint(thread, callable, ParallelOutputStream::enable);
	}

	/**
	 * Enable the error output stream for the current thread.
	 */
	public static void enableErrorPrint() {
		ERR_STREAM.enable();
	}

	/**
	 * Enable the error output stream for the current thread.
	 *
	 * <p>Calls the provided callable after enabling the stream
	 * for the thread and backups an initial stream state after
	 * finishing the run.
	 *
	 * @param callable 	callable to call after the switch
	 */
	@SuppressWarnings("UnusedReturnValue")
	public static <R> R enableErrorPrint(Callable<R> callable) {
		return enableErrorPrint(null, callable);
	}

	/**
	 * Disable the error output stream for the provided thread.
	 *
	 * @param thread	thread to disable the stream for
	 */
	public static void disableErrorPrint(Thread thread) {
		ERR_STREAM.disable(thread);
	}

	/**
	 * Disable the error output stream for the provided thread.
	 *
	 * <p>Calls the provided callable after disabling the stream
	 * for the thread and backups an initial stream state after
	 * finishing the run.
	 *
	 * @param thread	thread to enable the stream for
	 * @param callable 	callable to call after the switch
	 */
	@SuppressWarnings("UnusedReturnValue")
	public static <R> R disableErrorPrint(Thread thread, Callable<R> callable) {
		return suppressErrorPrint(thread, callable, ParallelOutputStream::disable);
	}

	/**
	 * Disable the error output stream for the current thread.
	 */
	public static void disableErrorPrint() {
		ERR_STREAM.disable();
	}

	/**
	 * Disable the error output stream for the current thread.
	 *
	 * <p>Calls the provided callable after disabling the stream
	 * for the thread and backups an initial stream state after
	 * finishing the run.
	 *
	 * @param callable 	callable to call after the switch
	 */
	@SuppressWarnings("UnusedReturnValue")
	public static <R> R disableErrorPrint(Callable<R> callable) {
		return disableErrorPrint(null, callable);
	}

	/**
	 * Check if the standard output stream is enabled for the provided thread.
	 *
	 * @param thread	thread to check the stream for
	 * @return			{@code true} if enabled
	 * 					or {@code false} if disabled
	 */
	public static boolean isOutputPrintEnabled(Thread thread) {
		return OUT_STREAM.isEnabled(thread);
	}

	/**
	 * Check if the standard output stream is enabled for the current thread.
	 *
	 * @return	{@code true} if enabled
	 * 			or {@code false} if disabled
	 */
	public static boolean isOutputPrintEnabled() {
		return OUT_STREAM.isEnabled();
	}

	/**
	 * Enable the standard output stream for the provided thread.
	 *
	 * @param thread	thread to enable the stream for
	 */
	public static void enableOutputPrint(Thread thread) {
		OUT_STREAM.enable(thread);
	}

	/**
	 * Enable the standard output stream for the provided thread.
	 *
	 * <p>Calls the provided callable after enabling the stream
	 * for the thread and backups an initial stream state after
	 * finishing the run.
	 *
	 * @param thread	thread to enable the stream for
	 * @param callable 	callable to call after the switch
	 */
	@SuppressWarnings("UnusedReturnValue")
	public static <R> R enableOutputPrint(Thread thread, Callable<R> callable) {
		return suppressOutputPrint(thread, callable, ParallelOutputStream::enable);
	}

	/**
	 * Enable the standard output stream for the current thread.
	 */
	public static void enableOutputPrint() {
		OUT_STREAM.enable();
	}

	/**
	 * Enable the standard output stream for the current thread.
	 *
	 * <p>Calls the provided callable after enabling the stream
	 * for the thread and backups an initial stream state after
	 * finishing the run.
	 *
	 * @param callable 	callable to call after the switch
	 */
	@SuppressWarnings("UnusedReturnValue")
	public static <R> R enableOutputPrint(Callable<R> callable) {
		return enableOutputPrint(null, callable);
	}

	/**
	 * Disable the standard output stream for the provided thread.
	 *
	 * @param thread	thread to disable the stream for
	 */
	public static void disableOutputPrint(Thread thread) {
		OUT_STREAM.disable(thread);
	}

	/**
	 * Disable the standard output stream for the provided thread.
	 *
	 * <p>Calls the provided callable after disabling the stream
	 * for the thread and backups an initial stream state after
	 * finishing the run.
	 *
	 * @param thread	thread to enable the stream for
	 * @param callable 	callable to call after the switch
	 */
	@SuppressWarnings("UnusedReturnValue")
	public static <R> R disableOutputPrint(Thread thread, Callable<R> callable) {
		return suppressOutputPrint(thread, callable, ParallelOutputStream::disable);
	}

	/**
	 * Disable the standard output stream for the current thread.
	 */
	public static void disableOutputPrint() {
		OUT_STREAM.disable();
	}

	/**
	 * Disable the standard output stream for the current thread.
	 *
	 * <p>Calls the provided callable after disabling the stream
	 * for the thread and backups an initial stream state after
	 * finishing the run.
	 *
	 * @param callable 	callable to call after the switch
	 */
	@SuppressWarnings("UnusedReturnValue")
	public static <R> R disableOutputPrint(Callable<R> callable) {
		return disableOutputPrint(null, callable);
	}

	/**
	 * Suppress the error output stream for the provided thread.
	 *
	 * <p>Calls the provided callable after calling a state switch
	 * method for the thread stream and backups an initial stream
	 * state after finishing the run.
	 *
	 * <p>Synchronizes the {@link UwSystem#suppress(Thread, Callable, PrintStream, PrintStream, Consumer, ParallelOutputStream, BiConsumer)}
	 * method call on the {@link System#err} object.
	 *
	 * @param thread	thread to enable the stream for
	 * @param callable 	callable to call after the switch
	 * @param consumer 	reference to the state switch method
	 */
	private static <R> R suppressErrorPrint(Thread thread, Callable<R> callable, BiConsumer<ParallelOutputStream, Thread> consumer) {
		return suppress(thread, callable, System.err, UwSystem.err, System::setErr, ERR_STREAM, consumer);
	}

	/**
	 * Suppress the standard output stream for the provided thread.
	 *
	 * <p>Calls the provided callable after calling a state switch
	 * method for the thread stream and backups an initial stream
	 * state after finishing the run.
	 *
	 * <p>Synchronizes {@link UwSystem#suppress(Thread, Callable, PrintStream, PrintStream, Consumer, ParallelOutputStream, BiConsumer)}
	 * method on the {@link System#err} object.
	 *
	 * @param thread	thread to enable the stream for
	 * @param callable 	callable to call after the switch
	 * @param consumer 	reference to the state switch method
	 */
	private static <R> R suppressOutputPrint(Thread thread, Callable<R> callable, BiConsumer<ParallelOutputStream, Thread> consumer) {
		return suppress(thread, callable, System.out, UwSystem.out, System::setOut, OUT_STREAM, consumer);
	}

	/**
	 * Suppress the provided output stream for the provided thread.
	 *
	 * <p>Calls the provided callable after calling a state switch
	 * method for the thread stream and backups an initial stream
	 * state after finishing the run.
	 *
	 * @param thread				thread to enable the stream for
	 * @param callable 				callable to call after the switch
	 * @param backupPrintStream 	current print stream to switch from
	 * @param localPrintStream		new print stream to switch to
	 * @param printStreamConsumer	reference to the system state switch method
	 * @param outputStream			parallel output stream to switch the state for
	 * @param outputStreamConsumer	reference to the output stream state switch method
	 */
	private static <R> R suppress(Thread thread, Callable<R> callable, PrintStream backupPrintStream, PrintStream localPrintStream, Consumer<PrintStream> printStreamConsumer, ParallelOutputStream outputStream, BiConsumer<ParallelOutputStream, Thread> outputStreamConsumer) {
		if (callable == null
				|| backupPrintStream == null
				|| localPrintStream == null
				|| printStreamConsumer == null
				|| outputStream == null
				|| outputStreamConsumer == null) {
			return null;
		}

		thread = UwObject.getIfNull(thread, Thread.currentThread());

		printStreamConsumer.accept(localPrintStream);

		boolean isEnabled = outputStream.isEnabled(thread);
		outputStreamConsumer.accept(outputStream, thread);

		R returnValue = null;
		Throwable throwable = null;

		try {
			returnValue = callable.call();
		} catch (Exception e) {
			throwable = e;
		}

		printStreamConsumer.accept(backupPrintStream);
		outputStream.setIsEnabled(thread, isEnabled);

		if (throwable != null) {
			throwable.printStackTrace();
		}

		return returnValue;
	}

	/**
	 * A parallel output stream.
	 */
	private static final class ParallelOutputStream extends OutputStream {

		/**
		 * An output stream to delegate methods from.
		 */
		private final OutputStream out;

		/**
		 * A map to individually track the state of this stream across the threads.
		 */
		private final Map<Thread, Boolean> isEnabledMap;

		/**
		 * Initialize a {@link ParallelOutputStream} instance.
		 *
		 * @param out	output stream to delegate methods from
		 *
		 * @throws UnsupportedOperationException	if the provided output stream is instance of this class
		 */
		public ParallelOutputStream(OutputStream out) throws UnsupportedOperationException {
			if (out instanceof ParallelOutputStream) {
				throw new UnsupportedOperationException("Nested parallel output streams aren't supported");
			}

			this.out = out;

			this.isEnabledMap = new ConcurrentHashMap<>();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void write(int b) throws IOException {
			if (!this.isEnabled()) {
				return;
			}

			this.out.write(b);
		}

		/**
		 * Check if this output stream is enabled for the provided thread.
		 *
		 * @param thread	thread to check the stream for
		 * @return			{@code true} if enabled
		 * 					or {@code false} if disabled
		 */
		public boolean isEnabled(Thread thread) {
			Boolean isEnabled = UwMap.getOrNull(thread, this.isEnabledMap);

			if (isEnabled == null) {
				this.setIsEnabled(thread, (isEnabled = true));
			}

			return isEnabled;
		}

		/**
		 * Check if this output stream is enabled for the current thread.
		 *
		 * <p>Wraps {@link ParallelOutputStream#isEnabled(Thread)}
		 * w/ {@link Thread#currentThread()}.
		 *
		 * @return	{@code true} if enabled
		 * 			or {@code false} if disabled
		 */
		@SuppressWarnings("BooleanMethodIsAlwaysInverted")
		public boolean isEnabled() {
			return this.isEnabled(Thread.currentThread());
		}

		/**
		 * Enable this output stream for the provided thread.
		 *
		 * @param thread	thread to enable the stream for
		 */
		public void enable(Thread thread) {
			this.setIsEnabled(thread, true);
		}

		/**
		 * Enable this output stream for the current thread.
		 */
		public void enable() {
			this.enable(null);
		}

		/**
		 * Disable this output stream for the provided thread.
		 *
		 * @param thread	thread to disable the stream for
		 */
		public void disable(Thread thread) {
			this.setIsEnabled(thread, false);
		}

		/**
		 * Disable this output stream for the current thread.
		 */
		public void disable() {
			this.disable(null);
		}

		/**
		 * Change this stream state for the provided thread.
		 *
		 * @param thread		thread to set the value for
		 * @param isEnabled		value to set to the thread
		 */
		private void setIsEnabled(Thread thread, boolean isEnabled) {
			this.isEnabledMap.put(UwObject.getIfNull(thread, Thread.currentThread()), isEnabled);
		}
	}
}
