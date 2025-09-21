package com.ablestrategies.logger;

/**
 * Configuration Settings
 *   jlogger.appenders.list  "ConsoleJAppender"    Comma-delimited list
 *   jlogger.default.level   "@X [@L] @C: "        See XxxFormatters.java
 *   jlogger.console.prefix  "Warn"                See LogLevel.java
 */
public interface IConfiguration {

    String getString(String key, String defaultValue);

    int getInteger(String key, int defaultValue);

}
