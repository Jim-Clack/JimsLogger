package com.ablestrategies.logger;

/**
 * Note1: Name of each Appender should end with "Appender"
 * Note2: You'll need to specify the entire package name in jlogger.appenders.list
 * Note3: ctor will be passed one argument: the IConfiguration instance
 */
public interface IAppender {

    void append(LogEvent logEvent);

}
