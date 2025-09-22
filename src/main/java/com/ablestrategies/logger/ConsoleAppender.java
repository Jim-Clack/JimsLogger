package com.ablestrategies.logger;

public class ConsoleAppender implements IAppender {

    private final TextFormatter textFormatter;

    public ConsoleAppender(IConfiguration config) {
        String prefix = config.getString("jlogger.console.prefix", "@U @c [@L]: ");
        textFormatter = new TextFormatter(prefix);

    }

    public void append(LogEvent logEvent) {
        System.err.println(textFormatter.format(logEvent));
    }

}
