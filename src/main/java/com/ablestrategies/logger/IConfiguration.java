package com.ablestrategies.logger;

/**
 * IConfiguration - Interface for a configuration provider<p/>
 * Expected Configuration Settings...<ul>
 * <li> jlogger.appenders.list  "ConsoleAppender"     Comma-delimited list </li>
 * <li> jlogger.console.prefix  "@X [@L] @C: "        See XxxFormatters.java </li>
 * <li> jlogger.default.level   "Warn"                See LogLevel.java </li>
 * </ul>
 */
public interface IConfiguration {

    /**
     * Get a named string value from the configuration
     * @param key lookup key
     * @param defaultValue default value to be used if the key or value is missing
     * @return the value
     */
    String getString(String key, String defaultValue);

    /**
     * Get a named integer value from the configuration
     * @param key lookup key
     * @param defaultValue default value to be used if the key or value is missing
     * @return the value
     */
    int getInteger(String key, int defaultValue);

}
