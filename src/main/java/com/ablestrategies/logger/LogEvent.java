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

    public LogEvent(Level level, String message, Throwable throwable) {
        this.level = level;
        this.message = message;
        this.throwable = throwable;
        this.timestamp = LocalDateTime.now();
        this.threadName = Thread.currentThread().getName();
        StackTraceElement stackTraceElement = Support.getCallerStackTraceElement();
        this.className = stackTraceElement.getClassName();
        this.methodName = stackTraceElement.getMethodName();
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer("LogEvent[");
        buffer.append("level=").append(level.name()).append(", class=").append(className)
                .append(", method=").append(methodName).append(", message=").append(message).append("]");
        return buffer.toString();
    }

}
