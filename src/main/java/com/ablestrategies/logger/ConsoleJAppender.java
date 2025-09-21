package com.ablestrategies.logger;

public class ConsoleJAppender implements IAppender {

    private final TextFormatter textFormatter;

    public ConsoleJAppender(IConfiguration config) {
        String prefix = config.getString("jlogger.console.prefix", "@X [@L] @C: ");
        textFormatter = new TextFormatter(prefix);

    }

    public void append(Event logEvent) {
        System.out.println(textFormatter.format(logEvent));
    }

}
