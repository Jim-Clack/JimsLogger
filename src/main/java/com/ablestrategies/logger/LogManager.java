package com.ablestrategies.logger;

import java.util.HashMap;
import java.util.Map;

public class LogManager {

    private static LogManager instance = null;

    private final Level defaultLevel;

    private final Map<String, Logger> loggers = new HashMap<>();

    private final Map<String, Level> history = new HashMap<>();

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

    /**
     * Get an existing Logger or create it if it does not exist, named per the calling package and class.
     * @return corresponding Logger
     */
    public Logger getLogger() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int elementIndex =  1;
        while(elementIndex < stackTrace.length-1
                && stackTrace[elementIndex].getClassName().startsWith("com.ablestrategies.logger")) {
            elementIndex++;
        }
        String packageClassName = stackTrace[elementIndex].getClassName();
        return getLogger(packageClassName);
    }

    /**
     * Get an existing Logger or create it if it does not exist.
     * @param packageClass Class to use as the Logger name. ("package.class" as returned by clazz.getName())
     * @return corresponding Logger
     */
    public Logger getLogger(Class<?> packageClass) {
        String packageClassName = packageClass.getName();
        return getLogger(packageClassName);
    }

    /**
     * Get an existing Logger or create it if it does not exist.
     * @param packageClassName partial or full package name, optionally with the class, or arbitrary Logger name
     * @return corresponding Logger
     */
    @SuppressWarnings("all")
    public Logger getLogger(String packageClassName) {
        if (packageClassName == null) {
            return getLogger();
        }
        Logger logger = loggers.get(packageClassName);
        if (logger == null) {
            logger = new Logger(this, packageClassName, defaultLevel);
            applyHistory(logger);
            loggers.put(packageClassName, logger);
        }
        return logger;
    }

    /**
     * Set the log level of certain Loggers specified by their partial or full packageClassName.
     * @param level Level to set the Loggers to.
     * @param packageClassName Partial or full name of target loggers.
     */
    public void setLevel(Level level, String packageClassName) {
        if(packageClassName == null) {
            packageClassName = "";
        }
        final String pkgClassName = packageClassName;
        loggers.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(pkgClassName))
                .forEach(entry -> entry.getValue().setLogLevel(level));
        history.put(packageClassName, level);
    }

    public void write(Level level, String message, Throwable throwable) {
        Event event = new Event(level, message, throwable);
        appenderThread.appendEvent(event);
    }

    private void applyHistory(Logger logger) {
        for(Map.Entry<String, Level> entry : history.entrySet()) {
            if(logger.getPackageClassName().startsWith(entry.getKey())) {
                logger.setLogLevel(entry.getValue());
            }
        }
    }

}
