package com.ablestrategies.logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * BaseLogEventXxxGetter - Base class for LogEventXxxGetters that are needed by Formatters.
 */
public class BaseLogEventGetter {

    /** LogEvent to "get" the requested fields from. */
    protected final LogEvent event;

    /**
     * Ctor.
     * @param event LogEvent to "get" the requested fields from.
     */
    public BaseLogEventGetter(LogEvent event) {
        this.event = event;
    }

    /**
     * Get the message level.
     * @return Priority/severity Level
     */
    public Level getLevel() {
        return event.level;
    }

    /**
     * Get the message.
     * @return The message including replacement symbols.
     */
    public String getMessage() {
        return event.message;
    }

    /**
     * Get an argument to be substituted for a replacement symbol in the message.
     * @param oneBasedIndex Varargs index, where the first argument is arg 1.
     * @return The argument as an Object. (primitives will be boxed.)
     */
    protected Object getArgumentAsObject(int oneBasedIndex) {
        if(event.arguments == null || oneBasedIndex > event.arguments.length) {
            return null;
        }
        return event.arguments[oneBasedIndex - 1];
    }

    /**
     * Get the thread name.
     * @return Name of the thread that the Logger was called from.
     */
    public String getThreadName() {
        return event.threadName;
    }

    /**
     * Get the class name.
     * @return Name of the package and class that the Logger was called from.
     */
    public String getClassName() {
        return event.className;
    }

    /**
     * Get the method name.
     * @return Name of the method that the Logger was called from.
     */
    public String getMethodName() {
        return event.methodName;
    }

    /**
     * Get the message from the exception that was thrown, if passed as arg 1.
     * @return Message from the thrown exception, as passed by the caller.
     */
    public String getThrowableMessage() {
        if(event.throwable != null) {
            return event.throwable.getMessage();
        }
        return null;
    }

    /**
     * Get the exception that was thrown, if passed as arg 1.
     * @return The thrown exception, as passed by the caller.
     */
    public String getThrowableStackDump() {
        if(event.throwable != null) {
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (PrintStream printStream = new PrintStream(buffer)) {
                event.throwable.printStackTrace(printStream);
            }
            return buffer.toString();
        }
        return null;
    }

}
