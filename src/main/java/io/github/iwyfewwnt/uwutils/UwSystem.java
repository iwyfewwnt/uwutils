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
import java.util.Stack;
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
	private static final Map<PrintStream, Stack<Object[]>> BACKUP_MAP = new ConcurrentHashMap<>();

	/**
	 * Setup parallel error output stream for the system.
	 *
	 * @param thread	thread to set up the stream for
	 */
	public static void setupParallelErrorPrint(Thread thread) {
		setupPrint(thread, UwSystem.err, System.err, System::setErr, ERR_STREAM);
	}

	/**
	 * Setup parallel error output stream for the system.
	 *
	 * <p>Wraps {@link UwSystem#setupParallelErrorPrint(Thread)}
	 * w/ {@code null} as the thread.
	 */
	public static void setupParallelErrorPrint() {
		setupParallelErrorPrint(null);
	}

	/**
	 * Backup the error system output stream.
	 *
	 * @param thread	thread to back up the stream for
	 */
	public static void backupSystemErrorPrint(Thread thread) {
		backupPrint(thread, UwSystem.err, System::setErr, ERR_STREAM);
	}

	/**
	 * Backup the error system output stream.
	 *
	 * <p>Wraps {@link UwSystem#backupSystemErrorPrint(Thread)}
	 * w/ {@code null} as the thread.
	 */
	public static void backupSystemErrorPrint() {
		backupSystemErrorPrint(null);
	}

	/**
	 * Setup parallel standard output stream for the system.
	 *
	 * @param thread	thread to set up the stream for
	 */
	public static void setupParallelOutputPrint(Thread thread) {
		setupPrint(thread, UwSystem.out, System.out, System::setOut, OUT_STREAM);
	}

	/**
	 * Setup parallel standard output stream for the system.
	 *
	 * <p>Wraps {@link UwSystem#setupParallelErrorPrint(Thread)}
	 * w/ {@code null} as the thread.
	 */
	public static void setupParallelOutputPrint() {
		setupParallelOutputPrint(null);
	}

	/**
	 * Backup the standard system output stream.
	 *
	 * @param thread	thread to back up the stream for
	 */
	public static void backupSystemOutputPrint(Thread thread) {
		backupPrint(thread, UwSystem.out, System::setOut, OUT_STREAM);
	}

	/**
	 * Backup the standard system output stream.
	 *
	 * <p>Wraps {@link UwSystem#backupSystemOutputPrint(Thread)}
	 * w/ {@code null} as the thread.
	 */
	public static void backupSystemOutputPrint() {
		backupSystemOutputPrint(null);
	}

	/**
	 * Set up the provided print stream for the system.
	 *
	 * @param thread 	thread to set up the stream for
	 * @param key		print stream to set up system for
	 * @param backup	system print stream to backup
	 * @param consumer	consumer that sets up the print stream
	 * @param out 		parallel output stream associated w/ the key
	 */
	private static void setupPrint(Thread thread, PrintStream key, PrintStream backup, Consumer<PrintStream> consumer, ParallelOutputStream out) {
		if (key == null || backup == null
				|| consumer == null) {
			return;
		}

		Stack<Object[]> stack
				= BACKUP_MAP.computeIfAbsent(key, $ -> new Stack<>());

		stack.push(new Object[] { backup, out.isEnabled(thread) });
		consumer.accept(key);
	}

	/**
	 * Backup the system print stream.
	 *
	 * @param thread	thread to back up the stream for
	 * @param key		print stream that was set before
	 * @param consumer	consumer that sets up the print stream
	 * @param out 		parallel output stream associated w/ the key
	 */
	private static void backupPrint(Thread thread, PrintStream key, Consumer<PrintStream> consumer, ParallelOutputStream out) {
		if (key == null
				|| consumer == null) {
			return;
		}

		Stack<Object[]> stack = BACKUP_MAP.get(key);
		if (stack == null || stack.isEmpty()) {
			return;
		}

		Object[] objs = stack.pop();

		consumer.accept((PrintStream) objs[0]);
		out.setEnabled(thread, (boolean) objs[1]);
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
	 * @param <R> 		return type
	 * @return 			return value of the callable call
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
	 * @param <R> 		return type
	 * @return 			return value of the callable call
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
	 * @param <R> 		return type
	 * @return 			return value of the callable call
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
	 * @param <R> 		return type
	 * @return 			return value of the callable call
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
	 * @param <R> 		return type
	 * @return 			return value of the callable call
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
	 * @param <R> 		return type
	 * @return 			return value of the callable call
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
	 * @param <R> 		return type
	 * @return 			return value of the callable call
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
	 * @param <R> 		return type
	 * @return 			return value of the callable call
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
	 * @param thread				thread to suppress the stream for
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

		setupPrint(thread, localPrintStream, backupPrintStream, printStreamConsumer, outputStream);

		outputStreamConsumer.accept(outputStream, thread);

		R returnValue = null;
		Throwable throwable = null;

		try {
			returnValue = callable.call();
		} catch (Exception e) {
			throwable = e;
		}

		backupPrint(thread, localPrintStream, printStreamConsumer, outputStream);

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
			thread = UwObject.getIfNull(thread, Thread.currentThread());

			Boolean isEnabled = UwMap.getOrNull(thread, this.isEnabledMap);

			if (isEnabled == null) {
				this.setEnabled(thread, (isEnabled = true));
			}

			return isEnabled;
		}

		/**
		 * Check if this output stream is enabled for the current thread.
		 *
		 * <p>Wraps {@link ParallelOutputStream#isEnabled(Thread)}
		 * w/ {@code null} as the thread.
		 *
		 * @return	{@code true} if enabled
		 * 			or {@code false} if disabled
		 */
		@SuppressWarnings("BooleanMethodIsAlwaysInverted")
		public boolean isEnabled() {
			return this.isEnabled(null);
		}

		/**
		 * Enable this output stream for the provided thread.
		 *
		 * @param thread	thread to enable the stream for
		 */
		public void enable(Thread thread) {
			this.setEnabled(thread, true);
		}

		/**
		 * Enable this output stream for the current thread.
		 *
		 * <p>Wraps {@link ParallelOutputStream#enable(Thread)}
		 * w/ {@code null} as the thread.
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
			this.setEnabled(thread, false);
		}

		/**
		 * Disable this output stream for the current thread.
		 *
		 * <p>Wraps {@link ParallelOutputStream#disable(Thread)}
		 * w/ {@code null} as the thread.
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
		private void setEnabled(Thread thread, boolean isEnabled) {
			thread = UwObject.getIfNull(thread, Thread.currentThread());

			this.isEnabledMap.put(thread, isEnabled);
		}

		/**
		 * Change this stream state for the current thread.
		 *
		 * <p>Wraps {@link ParallelOutputStream#setEnabled(Thread, boolean)}
		 * w/ {@code null} as the thread.
		 *
		 * @param isEnabled		value to set to the thread
		 */
		private void setEnabled(boolean isEnabled) {
			setEnabled(null, isEnabled);
		}
	}
}
