package com.ablestrategies.logger;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * <p>
 * AppenderThread - Background thread that dequeues events and calls the Appenders.
 * <p>
 * Note: Some of these methods run on the background thread and some on the Main thread.
 * <p>
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * <p>
 * To add appenders, put the XxxAppender.class file into a folder referenced by the
 * classpath, then add its full name (dotted package and class name without ".class")
 * to the list of appenders in the configuration. Some appenders need more configuration
 * settings as well, such as a message prefix, so refer to the documentation for the
 * corresponding appender.
 * <p>
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
public class AppenderThread extends Thread {

    public static int MAX_ERRORS = 1000;

    private final Map<String, IAppender> appenders = new HashMap<>();
    private final StringBuffer startupErrors = new StringBuffer();
    private final BlockingQueue<LogEvent> blockingQueue;
    private int errorCount = 0;

    /**
     * Ctor.
     * @param configuration Source of settings.
     */
    public AppenderThread(IConfiguration configuration) {
        setName("JLogger-Appenders");
        populateAppenderMap(configuration);
        blockingQueue = new ArrayBlockingQueue<LogEvent>(128);
    }

    /**
     * Background thread that dequeues events and writes them to the appenders.
     */
    @Override
    public void run() {
        if(!startupErrors.isEmpty()) {
            LogEvent event = new LogEvent(Level.Warn, startupErrors.toString(), null);
            blockingQueue.add(event);
        }
        while(!interrupted()) {
            try {
                LogEvent event = blockingQueue.take();
                writeToAppenders(event);
            } catch (InterruptedException e) {
                if(++errorCount >= MAX_ERRORS) {
                    System.err.println("\n\nJLogger AppenderThread Failure\n");
                    errorCount = 0;
                }
            }
        }
        interrupt();
    }

    /**
     * Do the actual logging by calling the appenders.
     * @param event to be logged.
     */
    private void writeToAppenders(LogEvent event) {
        for(Map.Entry<String, IAppender> entry : appenders.entrySet()) {
            IAppender appender = entry.getValue();
            appender.append(event);
        }
    }

    /**
     * Append an event to the queue. Note: Runs on Main thread, not Appender thread.
     * @param event To be enqueued.
     */
    public void appendEvent(LogEvent event) {
        try {
            blockingQueue.put(event);
        } catch (InterruptedException e) {
            if(++errorCount >= MAX_ERRORS) {
                System.err.println("\n\nJLogger AppenderThread Failure\n");
                errorCount = 0;
            }
        }
    }

    /**
     * Populate the AppenderMap with the appenders to be called. Note: Runs on Main thread, not Appender thread.
     * @param configuration Provided settings.
     */
    private void populateAppenderMap(IConfiguration configuration) {
        String appendersString = configuration.getString("jlogger.appenders.list", "ConsoleAppender");
        String[] appenderClassNames = appendersString.split("[, ]");
        for(String className : appenderClassNames) {
            try {
                addAppenderToMap(configuration, className);
            } catch (Exception e) {
                if(className.equals("ConsoleAppender")) {
                    appenders.put(className, new ConsoleAppender(configuration)); // in case the naked .class is missing
                    continue;
                }
                startupErrors.append("\n").append(e.getMessage()).append(" ").append(className);
            }
        }
    }

    /**
     * Add an Appender to the appenders map. Note: Runs on Main thread, not Appender thread.
     * @param configuration Provided settings.
     * @param className Name of Appender class to be added.
     * @throws Exception If appender cannot be found, we lack premission, or other problem.
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

}
