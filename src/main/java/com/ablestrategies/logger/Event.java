package com.ablestrategies.logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Event {

    private final Level level;
    private final String message;
    private final Throwable throwable;
    private final LocalDateTime timestamp;
    private final String threadName;
    private final String className;
    private final String methodName;

    public Event(Level level, String message, Throwable throwable) {
        this.level = level;
        this.message = message;
        this.throwable = throwable;
        this.timestamp = LocalDateTime.now();
        this.threadName = Thread.currentThread().getName();
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
        this.className = stackTraceElement.getClassName();
        this.methodName = stackTraceElement.getMethodName();
    }

    public Level getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer("LogEvent[");
        buffer.append("level=").append(level.name()).append(", class=").append(className)
                .append(", method=").append(methodName).append(", message=").append(message).append("]");
        return buffer.toString();
    }

    //////////////////////////// Getters Galore ///////////////////////////////

    public String getThrowableMessage() {
        if(throwable != null) {
            return throwable.getMessage();
        }
        return null;
    }

    public String getThrowableStackDump() {
        if(throwable != null) {
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (PrintStream printStream = new PrintStream(buffer)) {
                throwable.printStackTrace(printStream);
            }
            return buffer.toString();
        }
        return null;
    }

    public String getTimestampLocalDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(timestamp.toLocalDate());
    }

    public String getTimestampLocalDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(timestamp.toLocalDate());
    }

    public String getTimestampLocalTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return formatter.format(timestamp.toLocalDate());
    }

    public String getTimestampUTCDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'");
        return formatter.format(timestamp.atZone(ZoneOffset.UTC));
    }

    public String getTimestampUTCDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'Z'");
        return formatter.format(timestamp.atZone(ZoneOffset.UTC));
    }

    public String getTimestampUTCTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss'Z'");
        return formatter.format(timestamp.atZone(ZoneOffset.UTC));
    }

    public String getThreadName() {
        return threadName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

}
