package com.ablestrategies.logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * LogEventGetter - For accessing LogEvent fields and converting them as needed.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
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

    public String getThreadName() {
        return event.threadName;
    }

    public String getClassName() {
        return event.className;
    }

    public String getMethodName() {
        return event.methodName;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(Locale.US);
        return event.timestamp.format(formatter);
    }

    public String getTimestampLocalDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                .withLocale(Locale.US);
        return event.timestamp.format(formatter);
    }

    public String getTimestampLocalTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                .withLocale(Locale.US);
        return event.timestamp.format(formatter);
    }

    public String getTimestampUTCDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        return event.timestamp.atOffset(ZoneOffset.UTC).format(formatter);
    }

    public String getTimestampUTCDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'X");
        return event.timestamp.atOffset(ZoneOffset.UTC).format(formatter);
    }

    public String getTimestampUTCTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSX");
        return event.timestamp.atOffset(ZoneOffset.UTC).format(formatter);
    }

    public Object getArgument(int oneBasedIndex) {
        if(event.arguments == null || oneBasedIndex > event.arguments.length) {
            return null;
        }
        return event.arguments[oneBasedIndex - 1];
    }

    public String getArgumentAsString(int oneBasedIndex) {
        Object arg = getArgument(oneBasedIndex);
        String result = "(null)";
        if(arg != null && String.class.isAssignableFrom(arg.getClass())) {
            result = (String)arg;
        }
        return result;
    }

    public String getArgumentAsLong(int oneBasedIndex) {
        Object arg = getArgument(oneBasedIndex);
        String result = "(null)";
        if(arg != null && Long.class.isAssignableFrom(arg.getClass())) {
            result = ((Long)arg).toString();
        }else if(arg != null && Integer.class.isAssignableFrom(arg.getClass())) {
            result = ((Integer)arg).toString();
        }
        return result;
    }

    public String getArgumentAsDouble(int oneBasedIndex) {
        Object arg = getArgument(oneBasedIndex);
        String result = "(null)";
        if(arg != null && Double.class.isAssignableFrom(arg.getClass())) {
            result = ((Double)arg).toString();
        } else if(arg != null && Float.class.isAssignableFrom(arg.getClass())) {
            result = ((Float)arg).toString();
        }
        return result;
    }

    public String getArgumentAsBoolean(int oneBasedIndex) {
        Object arg = getArgument(oneBasedIndex);
        String result = "(null)";
        if(arg != null && Boolean.class.isAssignableFrom(arg.getClass())) {
            result = ((Boolean)arg).toString();
        }
        return result;

    }

    public String getArgumentAsHex(int oneBasedIndex) {
        Object arg = getArgument(oneBasedIndex);
        String result = "(null)";
        if(arg != null && Long.class.isAssignableFrom(arg.getClass())) {
            result = Long.toHexString((Long)arg);
        } else if(arg != null && Integer.class.isAssignableFrom(arg.getClass())) {
            result = Integer.toHexString((Integer)arg);
        }
        return result;
    }

    public String getArgumentAsLocalDateTime(int oneBasedIndex) {
        Object arg = getArgument(oneBasedIndex);
        String result = "(null)";
        if(arg != null && LocalDateTime.class.isAssignableFrom(arg.getClass())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                    .withLocale(Locale.US);
            result = ((LocalDateTime) arg).format(formatter);
        }
        return result;
    }

    public String getArgumentAsUTCDateTime(int oneBasedIndex) {
        Object arg = getArgument(oneBasedIndex);
        String result = "(null)";
        if (arg != null && LocalDateTime.class.isAssignableFrom(arg.getClass())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            result = ((LocalDateTime) arg).atOffset(ZoneOffset.UTC).format(formatter);
        }
        return result;
    }

    public String getArgumentAsExceptionMessage(int oneBasedIndex) {
        Object arg = getArgument(oneBasedIndex);
        String result = "(null)";
        if(arg != null && Throwable.class.isAssignableFrom(arg.getClass())) {
            result = ((Throwable)arg).getMessage();
        }
        return result;
    }

    public String getArgumentAsExceptionStackDump(int oneBasedIndex) {
        Object arg = getArgument(oneBasedIndex);
        String result = "(null)";
        if(arg != null && Throwable.class.isAssignableFrom(arg.getClass())) {
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (PrintStream printStream = new PrintStream(buffer)) {
                ((Throwable)arg).printStackTrace(printStream);
            }
            result = buffer.toString();
        }
        return result;
    }

    public String getArgumentAsPointer(int oneBasedIndex) {
        Object arg = getArgument(oneBasedIndex);
        String result = "(null)";
        if(arg != null) {
            result = arg.toString();
        }
        return result;
    }

    public String getArgumentAsObjectOrArray(int oneBasedIndex) {

        // TODO

        return getArgumentAsPointer(oneBasedIndex);
    }

}
