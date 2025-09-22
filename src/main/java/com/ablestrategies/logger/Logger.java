package com.ablestrategies.logger;

/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * <p>
 * Logger - This is a "named" filter to allow the Log Level to be set hierarchically.
 * <p>
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * <p>
 * How to use this.
 * <p>
 * You can call Logger.log() directly, although it is typically easier to use shortcut
 * methods listed at the end of this source file. Each message that you log will have
 * a Level associated with it. (see Level.java) You can set the logLevel hierarchy in
 * LogManager so that some of these messages will be forwarded to the logs and others
 * will be discarded. For instance, if you set the Level to Warn, then only Warn and
 * Error messages will be logged and others will be discarded. (Note that Error is a
 * higher priority than Warn, so it gets logged as well.)
 * <p>
 *   TRACE log messages are filtered by Level Trace
 *   DIAG log messages are filtered by Level Diag
 *   INFO log messages are filtered by Level Info
 *   WARN log messages are filtered by Level Warn
 *   ERROR log messages are filtered by Level Error
 * <p>
 * For example, assume that a class is using a Logger that is set to Level Diag. Here
 * are the results of calling that Logger with various shortcuts.
 * <p>
 *   logger.TRACE("Msg 1");  // will NOT be logged because Level is set to Diag
 *   Logger.DIAG("Msg 2");   // Will bt logged because Level is set to Diag
 *   Logger.INFO("Msg 3");   // Will bt logged because Level Info is higher than Diag
 *   Logger.WARN("Msg 4");   // Will bt logged because Level Warn is higher than Diag
 *   Logger.ERROR("Msg 5");  // Will bt logged because Level Error is higher than Diag
 * <p>
 * There are 3 ways of calling each log() or shortcut method:
 * <p>
 *   1. log(Level.Xxx, message);                 -OR-     XXXX(message);
 *   2. log(Level.Xxx, message, exception);      -OR-     XXXX(message, exception);
 *   3. log(Level.Xxx, message, arg1, argN..);   -OR-     XXXX(message, arg1, argN..);
 * <p>
 * Note that you pass an exception as arg1 using form (3) above, but consider that the
 * exception will be argument number 1 so the first argument following that will be 2.
 * <p>
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

    public void log(Level level, String message, Object... args) {
        if(level.getValue() >= this.level.getValue()) {
            logManager.write(level, message, args);
        }
    }

    public String getPackageClassName() {
        return packageClassName;
    }

    public Level getLogLevel() {
        return level;
    }

    void setLogLevel(Level level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Logger[name=" + packageClassName + ", level=" + level + "]";
    }

    //////////////////////////// Shortcut Methods ////////////////////////////

    public void TRACE(String message) { log(Level.Trace, message); }
    public void TRACE(String message, Object... args) { log(Level.Trace, message, args); }
    public void DIAG(String message) { log(Level.Diag, message); }
    public void DIAG(String message, Object... args) { log(Level.Diag, message, args); }
    public void INFO(String message) { log(Level.Info, message); }
    public void INFO(String message, Object... args) { log(Level.Info, message, args); }
    public void WARN(String message) { log(Level.Warn, message); }
    public void WARN(String message, Object... args) { log(Level.Warn, message, args); }
    public void ERROR(String message) { log(Level.Error, message); }
    public void ERROR(String message, Object... args) { log(Level.Error, message, args); }

}
