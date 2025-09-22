package com.ablestrategies.logger;

/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Logger - This is a "named" filter to allow the Log Level to be set hierarchically.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * How to use this.
 *
 * You can call Logger.log() directly, although it is typically easier to use shortcut
 * methods listed at the end of this source file. Each message that you log will have
 * a Level associated with it. (see Level.java) You can set the logLevel hierarchy in
 * LogManager so that some of these messages will be forwarded to the logs and others
 * will be discarded. For instance, if you set the Level to Warn, then only Warn and
 * Error messages will be logged and others will be discarded. (Note that Error is a
 * higher priority than Warn, so it gets logged as well.)
 *
 *   TRACE log messages are filtered by Level Trace
 *   DIAG log messages are filtered by Level Diag
 *   INFO log messages are filtered by Level Info
 *   WARN log messages are filtered by Level Warn
 *   ERROR log messages are filtered by Level Error
 *
 * For example, assume that a class is using a Logger that is set to Level Diag. Here
 * are the results of calling that Logger with various shortcuts.
 *
 *   logger.TRACE("Msg 1");  // will NOT be logged because Level is set to Diag
 *   Logger.DIAG("Msg 2");   // Will bt logged because Level is set to Diag
 *   Logger.INFO("Msg 3");   // Will bt logged because Level Info is higher than Diag
 *   Logger.WARN("Msg 4");   // Will bt logged because Level Warn is higher than Diag
 *   Logger.ERROR("Msg 5");  // Will bt logged because Level Error is higher than Diag
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
public class Logger {

    private final LogManager logManager;

    private final String packageClassName;

    private Level level;

    public Logger(LogManager logManager, String packageClassName, Level level) {
        this.logManager = logManager;
        this.packageClassName = packageClassName;
        this.level = level;
    }

    public void log(Level level, String message) {
        if(level.getValue() >= this.level.getValue()) {
            logManager.write(level, message, null);
        }
    }

    public void log(Level level, String message, Throwable throwable) {
        if(level.getValue() >= this.level.getValue()) {
            logManager.write(level, message, throwable);
        }
    }

    String getPackageClassName() {
        return packageClassName;
    }

    void setLogLevel(Level level) {
        this.level = level;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer("Logger[");
        buffer.append("name=").append(packageClassName).append(", level=")
                .append(level).append("]");
        return buffer.toString();
    }

    //////////////////////////// Shortcut Methods ////////////////////////////

    public void TRACE(String message) { log(Level.Trace, message); }
    public void TRACE(String message, Throwable throwable) { log(Level.Trace, message, throwable); }
    public void DIAG(String message) { log(Level.Diag, message); }
    public void DIAG(String message, Throwable throwable) { log(Level.Diag, message, throwable); }
    public void INFO(String message) { log(Level.Info, message); }
    public void INFO(String message, Throwable throwable) { log(Level.Info, message, throwable); }
    public void WARN(String message) { log(Level.Warn, message); }
    public void WARN(String message, Throwable throwable) { log(Level.Warn, message, throwable); }
    public void ERROR(String message) { log(Level.Error, message); }
    public void ERROR(String message, Throwable throwable) { log(Level.Error, message, throwable); }

}
