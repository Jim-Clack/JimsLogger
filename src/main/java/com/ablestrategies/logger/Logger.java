package com.ablestrategies.logger;

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

    public void TRACE(String message) { log(Level.Trace, message); }
    public void TRACE(String message, Throwable throwable) { log(Level.Trace, message,throwable); }
    public void DIAG(String message) { log(Level.Diag, message); }
    public void DIAG(String message, Throwable throwable) { log(Level.Diag, message,throwable); }
    public void INFO(String message) { log(Level.Info, message); }
    public void INFO(String message, Throwable throwable) { log(Level.Info, message,throwable); }
    public void WARN(String message) { log(Level.Warn, message); }
    public void WARN(String message, Throwable throwable) { log(Level.Warn, message,throwable); }
    public void ERROR(String message) { log(Level.Error, message); }
    public void ERROR(String message, Throwable throwable) { log(Level.Error, message,throwable); }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer("Logger[");
        buffer.append("name=").append(packageClassName).append(", level=")
                .append(level).append("]");
        return buffer.toString();
    }

    String getPackageClassName() {
        return packageClassName;
    }

    void setLogLevel(Level level) {
        this.level = level;
    }

}
