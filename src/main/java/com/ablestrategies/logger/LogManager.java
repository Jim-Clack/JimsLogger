package com.ablestrategies.logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * LogManager - Provides core functionality.
 * <h4> About Loggers and their usage. </h4>
 * <p/>
 * Do not instantiate Loggers yourself, as they will not function properly. To create one,
 * call LogManager.getLogger() in one of the following ways:
 * <ul>
 *  <li> LogManager.getLogger();                    Gets the Logger for the current class </li>
 *  <li> LogManager.getLogger(this.getClass());     Gets the Logger for the current class </li>
 *  <li> LogManager.getLogger(Main.getClass());     Gets the Logger for the Main class </li>
 *  <li> LogManager.getLogger("com.xyz.DBO");       Gets the Logger for class "com.xyz.DBO" </li>
 *  <li> LogManager.getLogger("com.xyz");           Gets the Logger for classes in "com.xyz" </li>
 *  <li> LogManager.getLogger("Issue231");          Gets the Logger for tracking issue231 </li>
 * </ul>
 * The Logger that is returned from that call (above) is to be used for logging in that
 * class. However, you can create multiple Loggers, if desired, for one class, or you can
 * create a Logger to use for multiple classes. The name of the Logger is not required to
 * be the same as the class. That is merely a convention to help you to track log events
 * from the log output back to the class where they originated. (hence "Issue231" above)
 * <h4> Hierarchy of LogManager.setLevel() calls. </h4>
 * <p/>
 * The setLevel() calls are apply in a temporal hierarchy - one after the other in the
 * order in which they are called. For instance, you may have classes named com.xyz.Main,
 * com.xyz.support.Reports, and com.xyz.support.DBO. If you wanted to track only Warnings
 * and Errors in the libraries, but wanted to log Info for Main and Reports, but needed
 * Diag level logging in DBO, you could put this code into Main:
 * <ul>
 *  <li> LogManager.setLevel(LogLevel.Warn, ""); </li>
 *  <li> LogManager.setLevel(LogLevel.Info, "com.xyz"); </li>
 *  <li> LogManager.setLevel(LogLevel.Diag, "com.xyz.DBO"); </li>
 * </ul>
 * Of course, this assumes that you used the default (no argument) LogManager.getLogger()
 * to get your Loggers, so they would be named according to their package and class names.
 * <h4> Description of setLevel() and related methods. </h4>
 * <p>
 * An elegant way to apply the hierarchy of setLevel() calls is to maintain a directed graph
 * that can be used dynamically by Logger.log(). The problem with that approach, however, is
 * that the evaluation will occur during the Logger.log() call and that is the most time
 * critical method in the package. Please understand that setLevel() and get getLogger() are
 * called very occasionally, but Logger.log() is called frequently and sometimes in a loop.
 * Moreover, the user has an expectation that Logger.log() performance will not drastically
 * impact the timing of his application, else it would be useless for concurrent debugging.
 * <br/><br/>
 * With all of that in mind, a LESS elegant approach was chosen for setLevel(). This allows
 * the Logger.log() calls to retain a "current Level" that does not need to be evaluated at
 * the time of the call. Here is how it works...
 * <ul>
 *  <li> 1. When setLevel() is called, it sets the current LogLevel of every existing Logger. </li>
 *  <li> 2. When a new Logger is instantiated, it will then be configured with the history of
 *     previous setLevel() calls. </li>
 * </ul>
 * In other words, setLevel() works on two fronts. When it is called, it sets the level
 * of every existing Logger and keeps track of this in a Map named historyOfSetLevel.
 * Then, when a new Logger is instantiated, that historyOfSetLevel will be re-evaluated
 * for that new Logger only.
 * <br/><br/>
 * Maybe you can think of a better way to accomplish this without adversely impacting the
 * performance of Logger.log() or its shortcuts.
 */
public class LogManager {

    /** Singleton, at least for now. */
    private static LogManager instance = null;

    /** Default level for new Loggers. */
    private final Level defaultLevel;

    /** A map of all known Loggers. */
    private final Map<String, Logger> allLoggers = new HashMap<>();

    /** A map of the setLevel() history. */
    private final List<KeyValuePair<String, Level>> historyOfSetLevel = new LinkedList<>();

    /** Background appender thread. */
    private final AppenderThread appenderThread;

    /**
     * Ctor.
     * @param dummy Placeholder, not used.
     */
    @SuppressWarnings("all")
    private LogManager(int dummy) {
        IConfiguration configuration = new PropsConfiguration();
        String level = configuration.getString("jlogger.default.level", "Warn");
        defaultLevel = Level.fromName(level);
        this.appenderThread = new AppenderThread(configuration);
        this.appenderThread.setDaemon(true);
        this.appenderThread.start();
    }

    /**
     * Ctor.
     * @apiNote LogManager is intended as a singleton, but the ctor is permitted for testing, etc.
     */
    @SuppressWarnings("unused")
    public LogManager() {
        this(0);
        System.err.println("LogManager is a singleton, you should not call the ctor directly!");
    }

    /**
     * Get our singleton.
     * @return Existing LogManager.
     */
    public static LogManager getInstance() {
        if (instance == null) {
            instance = new LogManager(1);
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
    @SuppressWarnings("unused")
    public Logger getLogger(Class<?> packageClass) {
        String packageClassName = packageClass.getName();
        return getLogger(packageClassName);
    }

    /**
     * Get an existing Logger or create it if it does not exist.
     * @param packageClassName partial or full package + class name, or arbitrary/custom Logger name.
     * @return corresponding Logger.
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
     * @param packageClassName Partial or full name of target loggers. For "all" loggers pass null.
     */
    public void setLevel(Level level, String packageClassName) {
        if(packageClassName == null) {
            packageClassName = "";
        }
        final String pkgClassName = packageClassName;
        allLoggers.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(pkgClassName))
                .forEach(entry -> entry.getValue().setLevel(level));
        historyOfSetLevel.add(new KeyValuePair<>(packageClassName, level));
    }

    /**
     * Put the log message into a LogEvent and enqueue it to send to AppenderThread.
     * @param level The priority/severity of the message.
     * @param message The message, optionally containing replacement symbols.
     * @param args Vararg values to substitute for certain replacement symbols.
     */
    public void write(Level level, String message, Object... args) {
        LogEvent event = new LogEvent(level, message, args);
        appenderThread.appendEvent(event);
    }

    /**
     * Call this to bring a new Logger up-to-date with setLevel() calls.
     * @param logger To be brought up-to-date.
     */
    private void applyHistoryOfSetLevel(Logger logger) {
        for(KeyValuePair<String, Level> entry : historyOfSetLevel) {
            if(logger.getPackageClassName().startsWith(entry.getKey())) {
                logger.setLevel(entry.getValue());
            }
        }
    }

}
