package com.ablestrategies.logger;

/**
 * LogEventTypeGetter - For accessing LogEvent fields as types, not just Strings..
 * @apiNote Intended for use with binary Appenders (db, network)
 */
public class LogEventTypedGetter extends LogEventBaseGetter {

    /**
     * Ctor.
     * @param event The LogEvent that this object will fetch data from.
     */
    public LogEventTypedGetter(LogEvent event) {
        super(event);
    }

    // TODO

}
