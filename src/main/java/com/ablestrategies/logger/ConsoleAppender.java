package com.ablestrategies.logger;

public class ConsoleAppender implements IAppender {

    private final TextFormatter textFormatter;

    public ConsoleAppender(IConfiguration config) {
        String prefix = config.getString("jlogger.console.prefix", "@X [@L] @C: ");
        textFormatter = new TextFormatter(prefix);

    }

    public void append(LogEvent logEvent) {
        System.out.println(textFormatter.format(logEvent));
    }

}
