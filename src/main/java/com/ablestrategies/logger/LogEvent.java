package com.ablestrategies.logger;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * LogEvent - Keeps track of the string, timestamp, and other info for a message to be logged.
 * @apiNote There are no getters here, as those are in LogEventXxxGetter classes.
 */
public class LogEvent {

    /** Level of priority/severity for this message. */
    final Level level;

    /** The message including replacement symbols for the args. */
    final String message;

    /** Vararg args to be used in message replacement symbols - index is one-based. */
    final Object[] arguments;

    /** Thrown exception, optionally passed as the first argument, or may be null. */
    final Throwable throwable;

    /** The date and time the Logger was called to process this event. */
    final LocalDateTime timestamp;

    /** The name of the calling thread.*/
    final String threadName;

    /** The dot-delimited package and class where the log request call originated. */
    final String className;

    /** The method that called the Logger. */
    final String methodName;

    /** Used by XxxFormatters to cache results for re-use. */
    private HashMap<String, String> formattersCache;

    /**
     * Ctor.
     * @param level the Level of priority/severity for this message.
     * @param message the message including replacement symbols for the args that follow.
     * @param args vararg args to be used in message replacement symbols.
     */
    public LogEvent(Level level, String message, Object... args) {
        this.level = level;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.threadName = Thread.currentThread().getName();
        this.arguments = args;
        StackTraceElement stackTraceElement = Support.getCallerStackTraceElement();
        this.className = stackTraceElement.getClassName();
        this.methodName = stackTraceElement.getMethodName();
        this.formattersCache = new HashMap<>();
        if(arguments != null && arguments.length > 0 && arguments[0] instanceof Throwable) {
            this.throwable = (Throwable)arguments[0];
        } else {
            this.throwable = null;
        }
    }

    /**
     * Get a previously cached result.
     * @param formatterSignature ITextFormatter name + settings. i.e. "XxxxFormatter;[Prefix @t]:3""
     * @return Previously cached result of same signature.
     */
    String getFormattersCache(String formatterSignature) {
        return formattersCache.get(formatterSignature);
    }

    /**
     * Add an XxxTextFormatter result cache.
     * @param formatterSignature Formatter name + settings. i.e. "XxxxFormatter;[Prefix @t]:3""
     * @param result Result of calling format() with this signature.
     */
    void putFormattersCache(String formatterSignature, String result) {
        formattersCache.put(formatterSignature, result);
    }

    /**
     * Overridden ToString
     * @return descriptive string.
     */
    @Override
    public String toString() {
        return "LogEvent[level=" + level.name() +
                ", class=" + className + ", method=" + methodName + ", message=" + message + "]";
    }

}
