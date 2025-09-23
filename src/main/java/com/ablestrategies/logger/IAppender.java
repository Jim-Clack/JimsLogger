package com.ablestrategies.logger;

/**
 * @apiNote Name of each Appender should end with "Appender"
 * @implNote You'll need to specify the entire package name in jlogger.appenders.list
 * @implSpec Ctor will be passed one argument: the IConfiguration instance
 */
public interface IAppender {

    /**
     * Ctor. (required)
     * @param configuration Source of settings.
     */
    // void XxxxAppender(IConfiguration configuration);

    /**
     * Output a LogEvent to the log.
     * @param logEvent To be formatted and written.
     */
    void append(LogEvent logEvent);

}
