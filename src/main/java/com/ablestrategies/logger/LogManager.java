package com.ablestrategies.logger;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Description of setLevel() and related methods.
 *
 * An elegant way to apply the hierarchy of setLevel() calls is to maintain a directed graph
 * that can be used dynamically by Logger.log(). The problem with that approach, however, is
 * that the evaluation will occur during the Logger.log() call and that is the most time-
 * critical method in the package. Please understand that setLevel() and get getLogger() are
 * called very occasionally, but Logger.log() is called frequently and sometimes in a loop.
 * Moreover, the user has an expectation that Logger.log() performance will not drastically
 * impact the timing of his application, else it would be useless for concurrent debugging.
 *
 * With all of that in mind, a less elegant approach was chosen for setLevel(). This allows
 * the Logger.log() calls to retain a "current Level" that does not need to be evaluated at
 * the time of the call. Here is how it works...
 *
 *  1. When setLevel() is called, it sets the current LogLevel of every existing Logger.
 *
 *  2. When a new Logger is instantiated, it will then be configured with the history of
 *     previous setLevel() calls.
 *
 * In other words, setLevel() works on two fronts. When it is called, it sets the level
 * of every existing Logger and keeps track of this in a map named historyOfSetLevel.
 * Then, when a new Logger is instantiated, that historyOfSetLevel will be re-evaluated
 * for that new Logger only.
 *
 *
 */
public class LogManager {

    private static LogManager instance = null;

    private final Level defaultLevel;

    private final Map<String, Logger> allLoggers = new HashMap<>();

    private final Map<String, Level> historyOfSetLevel = new HashMap<>();

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
        String packageClassName = Support.getCallerStackTraceElement().getClassName();
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
        Logger logger = allLoggers.get(packageClassName);
        if (logger == null) {
            logger = new Logger(this, packageClassName, defaultLevel);
            applyHistoryOfSetLevel(logger);
            allLoggers.put(packageClassName, logger);
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
        allLoggers.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(pkgClassName))
                .forEach(entry -> entry.getValue().setLogLevel(level));
        historyOfSetLevel.put(packageClassName, level);
    }

    public void write(Level level, String message, Throwable throwable) {
        LogEvent event = new LogEvent(level, message, throwable);
        appenderThread.appendEvent(event);
    }

    private void applyHistoryOfSetLevel(Logger logger) {
        for(Map.Entry<String, Level> entry : historyOfSetLevel.entrySet()) {
            if(logger.getPackageClassName().startsWith(entry.getKey())) {
                logger.setLogLevel(entry.getValue());
            }
        }
    }

}
