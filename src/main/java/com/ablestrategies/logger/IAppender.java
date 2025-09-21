package com.ablestrategies.logger;

/**
 * Note1: Name of each Appender must end with "JAppender"
 * Note2: You'll need to specify the entire package name in jlogger.appenders.list
 * Note3: ctor will be passed one argument: the IConfiguration instance
 */
public interface IAppender {

    void append(Event logEvent);

}
