package com.ablestrategies.logger;

/**
 * Logger - This is a "named" filter to allow the Log Level to be set hierarchically.
 * <h4> How to use this: </h4>
 * You can call Logger.log() directly, although it is typically easier to use shortcut
 * methods listed at the end of this source file. Each message that you log will have
 * a Level associated with it. (see Level.java) You can set the logLevel hierarchy in
 * LogManager so that some of these messages will be forwarded to the logs and others
 * will be discarded. For instance, if you set the Level to Warn, then only Warn and
 * Error messages will be logged and others will be discarded. (Note that Error is a
 * higher priority than Warn, so it gets logged as well.)
 * <ul>
 *  <li> TRACE log messages are filtered by Level Trace </li>
 *  <li> DIAG log messages are filtered by Level Diag </li>
 *  <li> INFO log messages are filtered by Level Info </li>
 *  <li> WARN log messages are filtered by Level Warn </li>
 *  <li> ERROR log messages are filtered by Level Error </li>
 * </ul>
 * For example, assume that a class is using a Logger that is set to Level Diag. Here
 * are the results of calling that Logger with various shortcuts.
 * <ul>
 *  <li> logger.TRACE("Msg 1");  // will NOT be logged because Level is set to Diag </li>
 *  <li> Logger.DIAG("Msg 2");   // Will bt logged because Level is set to Diag </li>
 *  <li> Logger.INFO("Msg 3");   // Will bt logged because Level Info is higher than Diag </li>
 *  <li> Logger.WARN("Msg 4");   // Will bt logged because Level Warn is higher than Diag </li>
 *  <li> Logger.ERROR("Msg 5");  // Will bt logged because Level Error is higher than Diag </li>
 * </ul>
 * <h4> There are 3 ways of calling each log() or shortcut method: </h4>
 * <ul>
 *  <li> 1. log(Level.Xxx, message);                 -OR-     XXXX(message); </li>
 *  <li> 2. log(Level.Xxx, message, exception);      -OR-     XXXX(message, exception); </li>
 *  <li> 3. log(Level.Xxx, message, arg1, argN..);   -OR-     XXXX(message, arg1, argN..); </li>
 * </ul>
 * @apiNote You can pass an exception as arg1 as in (3) above, but please consider that
 * the exception will be arg 1 so the first argument following that will be arg 2.
 */
public class Logger {

    /** The LogManager that created this Logger. */
    private final LogManager logManager;

    /** The name of this Logger, typically a dot-delimited package and class name. */
    private final String packageClassName;

    /** The current priority/severity level for filtering/discarding messages of lower levels. */
    private Level level;

    /**
     * Ctor.
     * @param logManager The LogManager that created this Logger.
     * @param packageClassName The name of this Logger, typically a dot-delimited package and class name.
     * @param level Initial/default Level.
     */
    public Logger(LogManager logManager, String packageClassName, Level level) {
        this.logManager = logManager;
        this.packageClassName = packageClassName;
        this.level = level;
    }

    /**
     * Write a log message. (actually, enqueue it so that it will be dequeued and sent to Appenders)
     * @param level Priority/severity level. (Some Loggers may discard this message based on this.)
     * @param message The message to be logged.
     */
    public void log(Level level, String message) {
        if(level.getValue() >= this.level.getValue()) {
            logManager.write(level, message, null);
        }
    }

    /**
     * Write a log message. (actually, enqueue it so that it will be dequeued and sent to Appenders)
     * @param level Priority/severity level. (Some Loggers may discard this message based on this.)
     * @param message The message to be logged, wiht replacement symbols that reference varargs.
     * @param args The arguments to be substituted into the message based on replacement symbols.
     */
    public void log(Level level, String message, Object... args) {
        if(level.getValue() >= this.level.getValue()) {
            logManager.write(level, message, args);
        }
    }

    /**
     * Get our package/class name.
     * @return The dot-delimited package and class as specified by the programmer.
     */
    public String getPackageClassName() {
        return packageClassName;
    }

    /**
     * Get the priority/severity level.
     * @return The level.
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Set the priority/severity level.
     * @param level Passed in by LogManager according to the PackageClassName.
     */
    void setLevel(Level level) {
        this.level = level;
    }

    /**
     * Override ToString().
     * @return Description of this Logger.
     */
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
