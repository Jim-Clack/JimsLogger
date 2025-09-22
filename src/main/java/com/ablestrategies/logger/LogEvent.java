package com.ablestrategies.logger;

import java.time.LocalDateTime;

public class LogEvent {

    final Level level;
    final String message;
    final Throwable throwable;
    final LocalDateTime timestamp;
    final String threadName;
    final String className;
    final String methodName;
    final Object[] arguments;

    public LogEvent(Level level, String message, Object... args) {
        this.level = level;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.threadName = Thread.currentThread().getName();
        this.arguments = args;
        StackTraceElement stackTraceElement = Support.getCallerStackTraceElement();
        this.className = stackTraceElement.getClassName();
        this.methodName = stackTraceElement.getMethodName();
        if(arguments != null && arguments.length > 0 && arguments[0] instanceof Throwable) {
            this.throwable = (Throwable)arguments[0];
        } else {
            this.throwable = null;
        }
    }

    @Override
    public String toString() {
        return "LogEvent[level=" + level.name() +
                ", class=" + className + ", method=" + methodName + ", message=" + message + "]";
    }

}
