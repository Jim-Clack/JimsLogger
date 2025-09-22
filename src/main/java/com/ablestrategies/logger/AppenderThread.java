package com.ablestrategies.logger;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AppenderThread extends Thread {

    public static int MAX_ERRORS = 1000;

    private final Map<String, IAppender> appenders = new HashMap<>();
    private final StringBuffer startupErrors = new StringBuffer();
    private final BlockingQueue<LogEvent> blockingQueue;
    private int errorCount = 0;

    public AppenderThread(IConfiguration config) {
        setName("JLogger-Appenders");
        populateAppenderMap(config);
        blockingQueue = new ArrayBlockingQueue<LogEvent>(128);
    }

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

    private void writeToAppenders(LogEvent event) {
        for(Map.Entry<String, IAppender> entry : appenders.entrySet()) {
            IAppender appender = entry.getValue();
            appender.append(event);
        }
    }

    private void populateAppenderMap(IConfiguration config) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String appendersString = config.getString("jlogger.appenders.list", "ConsoleAppender");
        String[] appenderClassNames = appendersString.split("[, ]");
        for(String className : appenderClassNames) {
            try {
                addAppenderToMap(config, className, classLoader);
            } catch (Exception e) {
                if(className.equals("ConsoleAppender")) {
                    appenders.put(className, new ConsoleAppender(config)); // in case the naked .class is missing
                    continue;
                }
                startupErrors.append("\n").append(e.getMessage()).append(" ").append(className);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void addAppenderToMap(IConfiguration config, String className, ClassLoader classLoader)
            throws Exception {
        Class<?> clazz = classLoader.loadClass(className);
        Class<?>[] params = new Class[]{IConfiguration.class};
        Constructor<IAppender> ctor = (Constructor<IAppender>) clazz.getConstructor(params);
        IAppender appender = ctor.newInstance(config);
        appenders.put(className, appender);
    }

}
