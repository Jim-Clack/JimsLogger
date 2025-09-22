package com.ablestrategies.logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class LogEventGetter {

    private final LogEvent event;

    public LogEventGetter(LogEvent event) {
        this.event = event;
    }

    public Level getLevel() {
        return event.level;
    }

    public String getMessage() {
        return event.message;
    }

    public String getThrowableMessage() {
        if(event.throwable != null) {
            return event.throwable.getMessage();
        }
        return null;
    }

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

    public String getTimestampLocalDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(event.timestamp.toLocalDate());
    }

    public String getTimestampLocalDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(event.timestamp.toLocalDate());
    }

    public String getTimestampLocalTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return formatter.format(event.timestamp.toLocalDate());
    }

    public String getTimestampUTCDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'");
        return formatter.format(event.timestamp.atZone(ZoneOffset.UTC));
    }

    public String getTimestampUTCDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'Z'");
        return formatter.format(event.timestamp.atZone(ZoneOffset.UTC));
    }

    public String getTimestampUTCTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss'Z'");
        return formatter.format(event.timestamp.atZone(ZoneOffset.UTC));
    }

    public String getThreadName() {
        return event.threadName;
    }

    public String getClassName() {
        return event.className;
    }

    public String getMethodName() {
        return event.methodName;
    }

}
