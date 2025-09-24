package com.ablestrategies.logger;

/**
 * IConfiguration - Interface for a configuration provider<p/>
 * Expected Configuration Settings...<ul>
 * <li> jlogger.appenders.list   "ConsoleAppender"     Comma-delimited list </li>
 * <li> jlogger.default.level    "Warn"                See LogLevel.java </li>
 * <li> jlogger.console.prefix   "@t @c [@L]: "        See TextFormatter.java </li>
 * <li> jlogger.logfile.prefix   "@t @c [@L]: "        See TextFormatter.java </li>
 * <li> jlogger.logfile.name     "jlog"                See FileAppender.java </li>
 * <li> jlogger.logfile.kmaxsize "100"                 See FileAppender.java </li>
 * <li> jlogger.logfile.backups  "10"                  See FileAppender.java </li>
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
     * Get a named long integer value from the configuration
     * @param key lookup key
     * @param defaultValue default value to be used if the key or value is missing
     * @return the value
     */
    long getLong(String key, long defaultValue);

}
