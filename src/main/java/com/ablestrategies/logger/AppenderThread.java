package com.ablestrategies.logger;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * AppenderThread - Background thread that dequeues events and calls the Appenders.<br/>
 * <br/>
 * To add appenders, put the XxxAppender.class file into a folder referenced by the
 * classpath, then add its full name (dotted package and class name without ".class")
 * to the list of appenders in the configuration. Some appenders need more configuration
 * settings as well, such as a message prefix, so refer to the documentation for the
 * corresponding appender.
 * @apiNote Some of these methods run on the background thread and some on the Main thread.<br/>
 */
public class AppenderThread extends Thread {

    /** If lost-message errors occur, report the first one, then every this many, */
    public static int ROLLUP_ERRORS = 1000;

    /** OUt own thread. */
    private AppenderThread appenderThread = null;

    /** Map of all Appenders to log to. */
    private final Map<String, IAppender> appenders = new HashMap<>();

    /** Pending statup errors to report once the logger is up-and-running. */
    private final StringBuffer startupErrors = new StringBuffer();

    /** Here's a holding-tank for log events passed to this thread. */
    private final BlockingQueue<LogEvent> blockingQueue;

    /** Keep track of the count of lost-message errors. */
    private int errorCount = ROLLUP_ERRORS - 1;

    /** Used for controlled shut-down. */
    private boolean exitThread = false;

    /**
     * Ctor.
     * @param configuration Source of settings.
     */
    public AppenderThread(IConfiguration configuration) {
        setName("JLogger-Appenders");
        populateAppenderMap(configuration);
        blockingQueue = new ArrayBlockingQueue<LogEvent>(128);
        appenderThread = this;
    }

    /**
     * Main background loop.
     * Background thread that dequeues events and writes them to the appenders.
     */
    @Override
    public void run() {
        // First, log any errors that occurred during startup.
        appenderThread = this;
        if(!startupErrors.isEmpty()) {
            LogEvent event = new LogEvent(Level.Warn, startupErrors.toString());
            blockingQueue.add(event);
        }
        hookShutdown();
        // Appender loop
        while(!interrupted()) {
            try {
                LogEvent event = blockingQueue.take();
                writeToAppenders(event);
            } catch (InterruptedException e) {
                if(exitThread) {
                    break;
                }
                if(++errorCount >= ROLLUP_ERRORS) {
                    System.err.println("\n\nJLogger AppenderThread Failure\n");
                    errorCount = 0;
                }
            }
        }
        interrupt();
    }

    /**
     * Do the actual logging by calling the appenders, runs on Appender thread.
     * @param event to be logged.
     * @implNote Called only by the Appender thread.
     */
    private void writeToAppenders(LogEvent event) {
        for(Map.Entry<String, IAppender> entry : appenders.entrySet()) {
            IAppender appender = entry.getValue();
            appender.append(event);
        }
    }

    //////////////////// The following methods run on other threads /////////////////////

    /**
     * Append an event to the queue.
     * @param event To be enqueued.
     * @implNote Called by other threads, not the Appender thread.
     */
    public void appendEvent(LogEvent event) {
        try {
            blockingQueue.put(event);
        } catch (InterruptedException e) {
            if(++errorCount >= ROLLUP_ERRORS) {
                System.err.println("\n\nJLogger AppenderThread Failure\n");
                errorCount = 0;
            }
        }
    }

    public void triggerExit() {
        this.exitThread = true;
    }

    /**
     * Populate the AppenderMap with the appenders to be called.
     * @param configuration Provided settings.
     * @implNote Called by other threads, not the Appender thread.
     */
    private void populateAppenderMap(IConfiguration configuration) {
        String appendersString = configuration.getString("jlogger.appenders.list", "ConsoleAppender");
        String[] appenderClassNames = appendersString.split("[, ]");
        for(String className : appenderClassNames) {
            try {
                addAppenderToMap(configuration, className);
            } catch (Exception e) {
                if(className.endsWith("ConsoleAppender")) { // in case it is specified but cannot be found
                    appenders.put(className, new ConsoleAppender(configuration));
                    continue;
                }
                startupErrors.append("\n").append(e.getMessage()).append(" ").append(className);
            }
        }
    }

    /**
     * Add an Appender to the appenders map.
     * @param configuration Provided settings.
     * @param className Name of Appender class to be added.
     * @throws Exception If appender cannot be found, we lack permission, or other problem.
     * @implNote Called by other threads, not the Appender thread.
     */
    @SuppressWarnings("unchecked")
    private void addAppenderToMap(IConfiguration configuration, String className)
            throws Exception {
        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
        Class<?>[] params = new Class[]{IConfiguration.class};
        Constructor<IAppender> ctor = (Constructor<IAppender>) clazz.getConstructor(params);
        IAppender appender = ctor.newInstance(configuration);
        appenders.put(className, appender);
    }

    /**
     * Shutdown Hook
     * Flush the queue - write events to Appenders before allowing app to exit.
     */
    private void hookShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            appenderThread.triggerExit();
            appenderThread.interrupt();
            appendEvent(new LogEvent(Level.Info,
                    "Logger Shutting Down as queue lgt=@1i", blockingQueue.size()));
            while(!blockingQueue.isEmpty()) {
                try {
                    LogEvent event = blockingQueue.take(); // flush queue
                    writeToAppenders(event);
                } catch (InterruptedException e) {
                    // ignore, as we are shutting down
                }
            }
        }));
    }

}
