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

import io.github.iwyfewwnt.uwutils.interfaces.UwCallable;

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
	 * Setup parallel error output stream for the system.
	 *
	 * @param thread	thread to set up the stream for
	 */
	public static void setupParallelErrorPrint(Thread thread) {
		setupPrint(thread, ERR_STREAM, System.err, UwSystem.err, System::setErr);
	}

	/**
	 * Setup parallel error output stream for the system.
	 *
	 * <p>Wraps {@link #setupParallelErrorPrint(Thread)}
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
		backupPrint(thread, ERR_STREAM, System::setErr);
	}

	/**
	 * Backup the error system output stream.
	 *
	 * <p>Wraps {@link #backupSystemErrorPrint(Thread)}
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
		setupPrint(thread, OUT_STREAM, System.out, UwSystem.out, System::setOut);
	}

	/**
	 * Setup parallel standard output stream for the system.
	 *
	 * <p>Wraps {@link #setupParallelErrorPrint(Thread)}
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
		backupPrint(thread, OUT_STREAM, System::setOut);
	}

	/**
	 * Backup the standard system output stream.
	 *
	 * <p>Wraps {@link #backupSystemOutputPrint(Thread)}
	 * w/ {@code null} as the thread.
	 */
	public static void backupSystemOutputPrint() {
		backupSystemOutputPrint(null);
	}

	/**
	 * Set up a new print stream context for the provided thread.
	 *
	 * @param thread					thread to set up the context for
	 * @param parallelOutputStream		parallel output stream to switch the context for
	 * @param currPrintStream 			current print stream to switch from
	 * @param nextPrintStream			next print stream to switch to
	 * @param nextPrintStreamConsumer   consumer to call after the setup
	 */
	private static void setupPrint(Thread thread, ParallelOutputStream parallelOutputStream, PrintStream currPrintStream, PrintStream nextPrintStream, Consumer<PrintStream> nextPrintStreamConsumer) {
		if (parallelOutputStream == null || currPrintStream == null
				|| nextPrintStream == null || nextPrintStreamConsumer == null) {
			return;
		}

		parallelOutputStream.setup(thread);

		if (currPrintStream == nextPrintStream) {
			return;
		}

		nextPrintStreamConsumer.accept(nextPrintStream);
	}

	/**
	 * Backup previous print stream context of the provided thread.
	 *
	 * @param thread				thread to set up the context for
	 * @param parallelOutputStream	parallel output stream to switch the context for
	 * @param printStreamConsumer	consumer to call after the backup if context is empty
	 */
	private static void backupPrint(Thread thread, ParallelOutputStream parallelOutputStream, Consumer<PrintStream> printStreamConsumer) {
		if (parallelOutputStream == null || printStreamConsumer == null) {
			return;
		}

		parallelOutputStream.backup(thread);
		if (!parallelOutputStream.isEmpty()) {
			return;
		}

		OutputStream defaultOutputStream = parallelOutputStream.getDefaultOutputStream();
		if (!(defaultOutputStream instanceof PrintStream)) {
			throw new IllegalStateException("Unable to cast to the <PrintStream> type");
		}

		printStreamConsumer.accept((PrintStream) defaultOutputStream);
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
		return isErrorPrintEnabled(null);
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
	 * Enable the error output stream for the provided thread.
	 *
	 * <p>Calls the provided callable after enabling the stream
	 * for the thread and backups an initial stream state after
	 * finishing the run.
	 *
	 * @param thread	thread to enable the stream for
	 * @param callable 	callable to call after the switch
	 */
	public static void enableErrorPrint(Thread thread, UwCallable callable) {
		enableErrorPrint(thread, (Callable<?>) callable);
	}

	/**
	 * Enable the error output stream for the current thread.
	 */
	public static void enableErrorPrint() {
		enableErrorPrint((Thread) null);
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
	 * Enable the error output stream for the current thread.
	 *
	 * <p>Calls the provided callable after enabling the stream
	 * for the thread and backups an initial stream state after
	 * finishing the run.
	 *
	 * @param callable 	callable to call after the switch
	 */
	public static void enableErrorPrint(UwCallable callable) {
		enableErrorPrint((Callable<?>) callable);
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
	 * Disable the error output stream for the provided thread.
	 *
	 * <p>Calls the provided callable after disabling the stream
	 * for the thread and backups an initial stream state after
	 * finishing the run.
	 *
	 * @param thread	thread to enable the stream for
	 * @param callable 	callable to call after the switch
	 */
	public static void disableErrorPrint(Thread thread, UwCallable callable) {
		disableErrorPrint(thread, (Callable<?>) callable);
	}

	/**
	 * Disable the error output stream for the current thread.
	 */
	public static void disableErrorPrint() {
		disableErrorPrint((Thread) null);
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
	 * Disable the error output stream for the current thread.
	 *
	 * <p>Calls the provided callable after disabling the stream
	 * for the thread and backups an initial stream state after
	 * finishing the run.
	 *
	 * @param callable 	callable to call after the switch
	 */
	public static void disableErrorPrint(UwCallable callable) {
		disableErrorPrint((Callable<?>) callable);
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
		return isOutputPrintEnabled(null);
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
	 * Enable the standard output stream for the provided thread.
	 *
	 * <p>Calls the provided callable after enabling the stream
	 * for the thread and backups an initial stream state after
	 * finishing the run.
	 *
	 * @param thread	thread to enable the stream for
	 * @param callable 	callable to call after the switch
	 */
	public static void enableOutputPrint(Thread thread, UwCallable callable) {
		enableOutputPrint(thread, (Callable<?>) callable);
	}

	/**
	 * Enable the standard output stream for the current thread.
	 */
	public static void enableOutputPrint() {
		enableOutputPrint((Thread) null);
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
	 * Enable the standard output stream for the current thread.
	 *
	 * <p>Calls the provided callable after enabling the stream
	 * for the thread and backups an initial stream state after
	 * finishing the run.
	 *
	 * @param callable 	callable to call after the switch
	 */
	public static void enableOutputPrint(UwCallable callable) {
		enableOutputPrint((Callable<?>) callable);
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
	 * Disable the standard output stream for the provided thread.
	 *
	 * <p>Calls the provided callable after disabling the stream
	 * for the thread and backups an initial stream state after
	 * finishing the run.
	 *
	 * @param thread	thread to enable the stream for
	 * @param callable 	callable to call after the switch
	 */
	public static void disableOutputPrint(Thread thread, UwCallable callable) {
		disableOutputPrint(thread, (Callable<?>) callable);
	}

	/**
	 * Disable the standard output stream for the current thread.
	 */
	public static void disableOutputPrint() {
		disableOutputPrint((Thread) null);
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
	 * Disable the standard output stream for the current thread.
	 *
	 * <p>Calls the provided callable after disabling the stream
	 * for the thread and backups an initial stream state after
	 * finishing the run.
	 *
	 * @param callable 	callable to call after the switch
	 */
	public static void disableOutputPrint(UwCallable callable) {
		disableOutputPrint((Callable<?>) callable);
	}

	/**
	 * Suppress the error output stream for the provided thread.
	 *
	 * <p>Calls the provided callable after calling a state switch
	 * method for the thread stream and backups an initial stream
	 * state after finishing the run.
	 *
	 * @param thread	thread to suppress the stream for
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
	 * @param thread	thread to suppress the stream for
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
	 * @param thread						thread to suppress the stream for
	 * @param callable 						callable to call after the switch
	 * @param currPrintStream 				current print stream to switch from
	 * @param nextPrintStream				new print stream to switch to
	 * @param printStreamConsumer			reference to the system state switch method
	 * @param parallelOutputStream			parallel output stream to switch the state for
	 * @param parallelOutputStreamConsumer	reference to the output stream state switch method
	 */
	private static <R> R suppress(Thread thread, Callable<R> callable, PrintStream currPrintStream, PrintStream nextPrintStream, Consumer<PrintStream> printStreamConsumer, ParallelOutputStream parallelOutputStream, BiConsumer<ParallelOutputStream, Thread> parallelOutputStreamConsumer) {
		if (callable == null
				|| currPrintStream == null
				|| nextPrintStream == null
				|| printStreamConsumer == null
				|| parallelOutputStream == null
				|| parallelOutputStreamConsumer == null) {
			return null;
		}

		setupPrint(thread, parallelOutputStream, currPrintStream, nextPrintStream, printStreamConsumer);
		parallelOutputStreamConsumer.accept(parallelOutputStream, thread);

		R returnValue = null;
		Throwable throwable = null;

		try {
			returnValue = callable.call();
		} catch (Exception e) {
			throwable = e;
		}

		backupPrint(thread, parallelOutputStream, printStreamConsumer);

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
		 * A default isEnabled boolean value of a thread.
		 */
		private static final boolean DEFAULT_ENABLED = true;

		/**
		 * A default output stream to use when there are no contexts.
		 */
		private final OutputStream defaultOutputStream;

		/**
		 * A map to individually track the stream of threads.
		 */
		private final Map<Thread, OutputStream> streamMap;

		/**
		 * A map to individually track the state of threads.
		 */
		private final Map<Thread, Boolean> stateMap;

		/**
		 * A map to individually track the context of threads.
		 */
		private final Map<Thread, Stack<Object[]>> contextMap;

		/**
		 * Initialize a {@link ParallelOutputStream} instance.
		 *
		 * @param defaultOutputStream 	default output stream to use when there are no contexts
		 *
		 * @throws IllegalArgumentException		if default output stream is {@code null}
		 */
		public ParallelOutputStream(OutputStream defaultOutputStream) {
			if (defaultOutputStream == null) {
				throw new IllegalArgumentException("Default output stream mustn't be <null>");
			}

			this.defaultOutputStream = defaultOutputStream;

			this.streamMap = new ConcurrentHashMap<>();
			this.stateMap = new ConcurrentHashMap<>();
			this.contextMap = new ConcurrentHashMap<>();
		}

		/**
		 * Get this default output stream.
		 *
		 * @return	default output stream
		 */
		public OutputStream getDefaultOutputStream() {
			return this.defaultOutputStream;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void write(int b) throws IOException {
			Thread thread = Thread.currentThread();

			OutputStream currentOutputStream
					= this.streamMap.getOrDefault(thread, this.defaultOutputStream);

			if (!this.isEnabled(thread)) {
				return;
			}

			currentOutputStream.write(b);
		}

		/**
		 * Set up a new output stream context for the provided thread.
		 *
		 * @param thread	thread to set up the context for
		 */
		public void setup(Thread thread) {
			thread = UwObject.ifNull(thread, Thread.currentThread());

			Stack<Object[]> contextStack
					= this.contextMap.computeIfAbsent(thread, unused -> new Stack<>());

			OutputStream currentOutputStream
					= this.streamMap.getOrDefault(thread, this.defaultOutputStream);


			contextStack.push(new Object[] {
					currentOutputStream,
					this.isEnabled(thread)
			});

			this.streamMap.put(thread, this.defaultOutputStream);
		}

		/**
		 * Backup previous context of the provided thread.
		 *
		 * @param thread	thread to back up the context for
		 */
		public void backup(Thread thread) {
			thread = UwObject.ifNull(thread, Thread.currentThread());

			Stack<Object[]> contextStack = this.contextMap.get(thread);
			if (contextStack == null || contextStack.isEmpty()) {
				return;
			}

			Object[] context = contextStack.pop();

			this.streamMap.put(thread, (OutputStream) context[0]);
			this.stateMap.put(thread, (boolean) context[1]);
		}

		/**
		 * Check if this output stream is enabled for the provided thread.
		 *
		 * @param thread	thread to check the stream for
		 * @return			{@code true} if enabled
		 * 					or {@code false} if disabled
		 */
		public boolean isEnabled(Thread thread) {
			thread = UwObject.ifNull(thread, Thread.currentThread());

			return this.stateMap.computeIfAbsent(thread, unused -> DEFAULT_ENABLED);
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
		 * Disable this output stream for the provided thread.
		 *
		 * @param thread	thread to disable the stream for
		 */
		public void disable(Thread thread) {
			this.setEnabled(thread, false);
		}

		/**
		 * Change this output stream for the provided thread.
		 *
		 * @param thread	thread to set the value for
		 * @param stream	value to associate w/ the thread
		 */
		public void setStream(Thread thread, OutputStream stream) {
			thread = UwObject.ifNull(thread, Thread.currentThread());

			this.streamMap.put(thread, stream);
		}

		/**
		 * Change this stream state for the provided thread.
		 *
		 * @param thread		thread to set the value for
		 * @param isEnabled		value to associate w/ the thread
		 */
		public void setEnabled(Thread thread, boolean isEnabled) {
			thread = UwObject.ifNull(thread, Thread.currentThread());

			this.stateMap.put(thread, isEnabled);
		}

		/**
		 * Check if all reserved contexts were backed up.
		 *
		 * @return	{@code true} if there are no other contexts
		 * 			or {@code false} if there are other contexts
		 */
		public boolean isEmpty() {
			for (Stack<Object[]> context : this.contextMap.values()) {
				if (context != null && !context.isEmpty()) {
					return false;
				}
			}

			return true;
		}
	}

	/**
	 * A name of this class.
	 */
	private static final String CLASS_NAME = UwSystem.class.getName();

	/**
	 * A default stack trace offset.
	 */
	private static final int DEFAULT_STACK_TRACE_OFFSET = 0;

	/**
	 * Get current stack trace element that points to the caller.
	 *
	 * @param offset	stack trace offset
	 * @return			stack trace element
	 */
	public static StackTraceElement getCurrentStackTraceElement(Integer offset) {
		offset = UwObject.ifNull(offset, DEFAULT_STACK_TRACE_OFFSET);

		StackTraceElement[] stackTraceElements = Thread.currentThread()
				.getStackTrace();

		int i = 1 + offset;
		StackTraceElement returnValue
				= UwArray.getOrNull(i, stackTraceElements);

		for (i++; i < stackTraceElements.length; i++) {
			StackTraceElement stackTraceElement = stackTraceElements[i];

			String className = stackTraceElement.getClassName();
			if (className.equals(CLASS_NAME)) {
				continue;
			}

			returnValue = stackTraceElement;
			break;
		}

		return returnValue;
	}

	/**
	 * Get current stack trace element that points to the caller.
	 *
	 * <p>Wraps {@link #getCurrentStackTraceElement(Integer)}
	 * w/ {@code null} as the stack trace offset.
	 *
	 * @return	stack trace element
	 */
	public static StackTraceElement getCurrentStackTraceElement() {
		return getCurrentStackTraceElement(null);
	}
}
