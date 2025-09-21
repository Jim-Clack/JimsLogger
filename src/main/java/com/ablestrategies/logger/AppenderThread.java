package com.ablestrategies.logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AppenderThread extends Thread {

    private final Map<String, IAppender> appenders = new HashMap<>();
    private final StringBuffer startupErrors = new StringBuffer();
    private final BlockingQueue<Event> blockingQueue;

    public AppenderThread(IConfiguration config) {
        setName("JLogger-Appenders");
        populateAppenderMap(config);
        blockingQueue = new ArrayBlockingQueue<Event>(128);
    }

    public void appendEvent(Event event) {
        try {
            blockingQueue.put(event);
        } catch (InterruptedException e) {
            System.err.println("\nJLogger Failure: " + e.getMessage() + "\n Event: " + event.toString());
        }
    }

    @Override
    public void run() {
        if(!startupErrors.isEmpty()) {
            Event event = new Event(Level.Warn, startupErrors.toString(), null);
            blockingQueue.add(event);
        }
        while(!interrupted()) {
            try {
                Event event = blockingQueue.take();
                appendEvent(event);
            } catch (InterruptedException e) {
                // ignore timeouts, etc.
            }
        }
        interrupt();
    }

    private void writeToAppenders(Event event) {
        for(Map.Entry<String, IAppender> entry : appenders.entrySet()) {
            IAppender appender = entry.getValue();
            appender.append(event);
        }
    }

    @SuppressWarnings("unchecked")
    private void populateAppenderMap(IConfiguration config) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String appendersString = config.getString("jlogger.appenders.list", "ConsoleJAppender");
        String[] appenderClassNames = appendersString.split("[, ]");
        for(String className : appenderClassNames) {
            try {
                Class<?> clazz = classLoader.loadClass(className);
                Class<?>[] params = new Class[]{IConfiguration.class};
                Constructor<IAppender> ctor = (Constructor<IAppender>) clazz.getConstructor(params);
                IAppender appender = ctor.newInstance(config);
                appenders.put(className, appender);
            } catch (ClassNotFoundException | NullPointerException | NoSuchMethodException |
                     InstantiationException | IllegalAccessException | InvocationTargetException e) {
                startupErrors.append("\n").append(e.getMessage()).append(" ").append(className);
            }
        }
    }

}
