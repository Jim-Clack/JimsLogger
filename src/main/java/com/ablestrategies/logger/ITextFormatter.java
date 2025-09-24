package com.ablestrategies.logger;

/**
 * Interface for TextFormatter and a possible future Log4JFormatter.
 */
public interface ITextFormatter {

    /**
     * Ctor. (REquired for dynamic loading)
     * @param prefix The message prefix, or null to accept the default.
     */
    // ITextFormatter(String prefix);

    /**
     * Perfrom the conversion - format a LogEvent as a String.
     * @param logEvent To be formatted as a string message.
     * @return The resultant textual message.
     */
    String format(LogEvent logEvent);

}
