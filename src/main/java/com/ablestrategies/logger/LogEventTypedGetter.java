package com.ablestrategies.logger;

import java.time.LocalDateTime;

/**
 * LogEventTypeGetter - For accessing LogEvent fields as types, not just Strings..
 * @apiNote Intended for use with binary Appenders (db, network)
 * @implNote These getters return a 0 or default value if the arg is missing or null.
 */
public class LogEventTypedGetter extends BaseLogEventGetter {

    /**
     * Ctor.
     * @param event The LogEvent that this object will fetch data from.
     */
    public LogEventTypedGetter(LogEvent event) {
        super(event);
    }

    /**
     * Get a String argument.
     * @param oneBasedIndex argument number (one-based)
     * @return The value of the argument.
     */
    public String getStringArgument(int oneBasedIndex) {
        Object obj = getArgumentAsObject(oneBasedIndex);
        if(obj == null) {
            return "(null)";
        }
        if(obj instanceof String) {
            return (String)obj;
        }
        return "???";
    }

    /**
     * Get an Integer or Long argument.
     * @param oneBasedIndex argument number (one-based)
     * @return The value of the argument.
     */
    public long getLongArgument(int oneBasedIndex) {
        Object obj = getArgumentAsObject(oneBasedIndex);
        return switch (obj) {
            case null -> 0L;
            case Long l -> l;
            case Integer i -> i.longValue();
            default -> 0L;
        };
    }

    /**
     * Get an double or Float argument.
     * @param oneBasedIndex argument number (one-based)
     * @return The value of the argument.
     */
    public double getDoubleArgument(int oneBasedIndex) {
        Object obj = getArgumentAsObject(oneBasedIndex);
        return switch (obj) {
            case null -> 0.0;
            case Double d -> d;
            case Float f -> f.doubleValue();
            default -> 0.0;
        };
    }

    /**
     * Get an boolean argument.
     * @param oneBasedIndex argument number (one-based)
     * @return The value of the argument.
     */
    public boolean getBooleanArgument(int oneBasedIndex) {
        Object obj = getArgumentAsObject(oneBasedIndex);
        return switch (obj) {
            case null -> false;
            case Boolean b -> b;
            default -> false;
        };
    }

    /**
     * Get an LocalDateTime argument.
     * @param oneBasedIndex argument number (one-based)
     * @return The value of the argument.
     */
    public LocalDateTime getLocalDateTimeArgument(int oneBasedIndex) {
        Object obj = getArgumentAsObject(oneBasedIndex);
        return switch (obj) {
            case null -> LocalDateTime.now();
            case LocalDateTime dt -> dt;
            default -> LocalDateTime.now();
        };
    }

}
