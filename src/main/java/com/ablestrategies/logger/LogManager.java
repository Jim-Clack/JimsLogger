package com.ablestrategies.logger;

import java.util.HashMap;
import java.util.Map;

public class LogManager {

    private static LogManager instance = null;

    private final Level defaultLevel;

    private Map<String, Logger> loggers = new HashMap<>();

    private final AppenderThread appenderThread;

    private LogManager() {
        IConfiguration configuration = new PropsConfiguration();
        String level = configuration.getString("jlogger.default.level", "Warn");
        defaultLevel = Level.fromName(level);
        this.appenderThread = new AppenderThread(configuration);
        this.appenderThread.start();
    }

    public static LogManager getInstance() {
        if (instance == null) {
            instance = new LogManager();
        }
        return instance;
    }

    public Logger getLogger() {
        String packageClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        return getLogger(packageClassName);
    }

    public Logger getLogger(Class<?> packageClass) {
        String packageClassName = packageClass.getName();
        return getLogger(packageClassName);
    }

    public Logger getLogger(String packageClassName) {
        if (packageClassName == null) {
            packageClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        }
        Logger logger = new Logger(this, packageClassName, defaultLevel);
        loggers.put(packageClassName, logger);
        return logger;
    }

    public void setLevel(Level level, String packageClassName) {
        if(packageClassName == null) {
            packageClassName = "";
        }
        final String pkgClassName = packageClassName;
        loggers.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(pkgClassName))
                .forEach(entry -> entry.getValue().setLogLevel(level));
    }

    public void write(Level level, String message, Throwable throwable) {
        Event event = new Event(level, message, throwable);
        appenderThread.appendEvent(event);
    }

}
