package com.ablestrategies.logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * LogEventStringGetter - For accessing LogEvent fields and converting them as needed.
 * @apiNote Intended for use with text-based Appenders (console, file, json, xml)
 */
public class LogEventStringGetter extends LogEventTypedGetter {

    /**
     * Ctor.
     * @param event The LogEvent that this object will fetch data from.
     */
    public LogEventStringGetter(LogEvent event) {
        super(event);
    }

    /**
     * Get the timestamp in short local form.
     * @return Example: "9/23/25, 1:26PM" (depending on locale)
     */
    public String getTimestampLocalDateTimeAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(Locale.US);
        return event.timestamp.format(formatter);
    }

    /**
     * Get the timestamp date in short local form.
     * @return Example: "9/23/25" (depending on locale)
     */
    public String getTimestampLocalDateAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                .withLocale(Locale.US);
        return event.timestamp.format(formatter);
    }

    /**
     * Get the timestamp time in short local form.
     * @return Example: "1:26PM" (depending on locale)
     */
    public String getTimestampLocalTimeAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                .withLocale(Locale.US);
        return event.timestamp.format(formatter);
    }

    /**
     * Get the timestamp in UTC form.
     * @return Example: "2025-09-23T13:26:44.736Z"
     */
    public String getTimestampUtcDateTimeAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return event.timestamp.atOffset(ZoneOffset.UTC).format(formatter);
    }

    /**
     * Get the timestamp date in UTC form.
     * @return Example: "2025-09-23"
     */
    public String getTimestampUtcDateAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return event.timestamp.atOffset(ZoneOffset.UTC).format(formatter);
    }

    /**
     * Get the timestamp date in UTC form.
     * @return Example: "T13:26:44.736Z"
     */
    public String getTimestampUtcTimeAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'T'HH:mm:ss'Z'");
        return event.timestamp.atOffset(ZoneOffset.UTC).format(formatter);
    }

    /**
     * Get the value of an argument that is a String.
     * @param oneBasedIndex argument number (one-based)
     * @return The String representation of the argument.
     */
    public String getStringArgumentAsString(int oneBasedIndex) {
        Object arg = getArgumentAsObject(oneBasedIndex);
        String result = "(null)";
        if(arg != null && String.class.isAssignableFrom(arg.getClass())) {
            result = (String)arg;
        }
        return result;
    }

    /**
     * Get the String representation of an argument that is a Long, Integer, long, or int.
     * @param oneBasedIndex argument number (one-based)
     * @return The String representation of the argument.
     */
    public String getLongArgumentAsString(int oneBasedIndex) {
        Object arg = getArgumentAsObject(oneBasedIndex);
        String result = "(null)";
        if(arg != null && Long.class.isAssignableFrom(arg.getClass())) {
            result = ((Long)arg).toString();
        }else if(arg != null && Integer.class.isAssignableFrom(arg.getClass())) {
            result = ((Integer)arg).toString();
        }
        return result;
    }

    /**
     * Get the String representation of an argument that is a Double, double, Float, or float.
     * @param oneBasedIndex argument number (one-based)
     * @return The String representation of the argument.
     */
    public String getDoubleArgumentAsString(int oneBasedIndex) {
        Object arg = getArgumentAsObject(oneBasedIndex);
        String result = "(null)";
        if(arg != null && Double.class.isAssignableFrom(arg.getClass())) {
            result = ((Double)arg).toString();
        } else if(arg != null && Float.class.isAssignableFrom(arg.getClass())) {
            result = ((Float)arg).toString();
        }
        return result;
    }

    /**
     * Get the String representation of an argument that is a Boolean or boolean.
     * @param oneBasedIndex argument number (one-based)
     * @return The String representation of the argument.
     */
    public String getBooleanArgumentAsString(int oneBasedIndex) {
        Object arg = getArgumentAsObject(oneBasedIndex);
        String result = "(null)";
        if(arg != null && Boolean.class.isAssignableFrom(arg.getClass())) {
            result = ((Boolean)arg).toString();
        }
        return result;

    }

    /**
     * Get the String hexadecimal representation of an argument that is a Long, Integer, long, or int.
     * @param oneBasedIndex argument number (one-based)
     * @return The String representation of the argument.
     */
    public String getHexArgumentAsString(int oneBasedIndex) {
        Object arg = getArgumentAsObject(oneBasedIndex);
        String result = "(null)";
        if(arg != null && Long.class.isAssignableFrom(arg.getClass())) {
            result = Long.toHexString((Long)arg);
        } else if(arg != null && Integer.class.isAssignableFrom(arg.getClass())) {
            result = Integer.toHexString((Integer)arg);
        }
        return result;
    }

    /**
     * Get the String (short/local) representation of an argument that is a LocalDateTime.
     * @param oneBasedIndex argument number (one-based)
     * @return The String representation of the argument.
     */
    public String getLocalDateTimeArgumentAsString(int oneBasedIndex) {
        Object arg = getArgumentAsObject(oneBasedIndex);
        String result = "(null)";
        if(arg != null && LocalDateTime.class.isAssignableFrom(arg.getClass())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                    .withLocale(Locale.US);
            result = ((LocalDateTime) arg).format(formatter);
        }
        return result;
    }

    /**
     * Get the String (UTC) representation of an argument that is a LocalDateTime.
     * @param oneBasedIndex argument number (one-based)
     * @return The String representation of the argument.
     */
    public String getUtcDateTimeArgumentAsString(int oneBasedIndex) {
        Object arg = getArgumentAsObject(oneBasedIndex);
        String result = "(null)";
        if (arg != null && LocalDateTime.class.isAssignableFrom(arg.getClass())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            result = ((LocalDateTime) arg).atOffset(ZoneOffset.UTC).format(formatter);
        }
        return result;
    }

    /**
     * Get the Message from am argument that is a Throwable/Exception.
     * @param oneBasedIndex argument number (one-based)
     * @return The String representation of the argument.
     */
    public String getExceptionMessageArgumentAsString(int oneBasedIndex) {
        Object arg = getArgumentAsObject(oneBasedIndex);
        String result = "(null)";
        if(arg != null && Throwable.class.isAssignableFrom(arg.getClass())) {
            result = ((Throwable)arg).getMessage();
        }
        return result;
    }

    /**
     * Get a StackDump from am argument that is a Throwable/Exception.
     * @param oneBasedIndex argument number (one-based)
     * @return The String representation of the argument.
     */
    public String getExceptionStackDumpArgumentAsString(int oneBasedIndex) {
        Object arg = getArgumentAsObject(oneBasedIndex);
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

    /**
     * Get the ToString() output from am argument.
     * @param oneBasedIndex argument number (one-based)
     * @return The String representation of the argument.
     */
    public String getToStringArgumentAsString(int oneBasedIndex) {
        Object arg = getArgumentAsObject(oneBasedIndex);
        String result = "(null)";
        if(arg != null) {
            result = arg.toString();
        }
        return result;
    }

    /**
     * Get a varargs argument that is a String, Array, Collection, or other Object.
     * @param oneBasedIndex argument number (one-based)
     * @param depth 0=simple one-line, 1=shallow dump, 2=future
     * @return a String representation of the object
     */
    public String getObjectArgumentAsString(int oneBasedIndex, int depth) {

        // TODO - must handle Strings, Arrays, Collections, other Objects

        return getToStringArgumentAsString(oneBasedIndex);
    }

}
