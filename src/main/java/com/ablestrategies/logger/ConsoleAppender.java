package com.ablestrategies.logger;

/**
 * ConsoleAppender - Log writer that outputs events in textual form to System.err.
 */
public class ConsoleAppender implements IAppender {

    /** We need this to format LogEvents into textual messages. */
    private final TextFormatter textFormatter;

    /**
     * Ctor.
     * @param configuration Source of settings.
     */
    public ConsoleAppender(IConfiguration configuration) {
        String prefix = configuration.getString("jlogger.console.prefix", "@U @c [@L]: ");
        textFormatter = new TextFormatter(prefix);
    }

    /**
     * Append (output) a LogEvent.
     * @param logEvent To be written to the log/err.
     */
    public void append(LogEvent logEvent) {
        System.err.println(textFormatter.format(logEvent));
    }

    /**
     * This will be called after the last log message has been written.
     */
    public void close() {
        // not needed for this
    }

}
