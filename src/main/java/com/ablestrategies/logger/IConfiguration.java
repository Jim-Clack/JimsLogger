package com.ablestrategies.logger;

/**
 * Configuration Settings.
 *   jlogger.appenders.list  "ConsoleAppender"     Comma-delimited list
 *   jlogger.console.prefix  "@X [@L] @C: "        See XxxFormatters.java
 *   jlogger.default.level   "Warn"                See LogLevel.java
 */
public interface IConfiguration {

    String getString(String key, String defaultValue);

    int getInteger(String key, int defaultValue);

}
