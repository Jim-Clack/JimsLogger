package com.ablestrategies.logmain;

import com.ablestrategies.logger.*;

public class Main {
    public static void main(String[] args) {
        System.setProperty("jlogger.appenders.list", "com.ablestrategies.logger.ConsoleJAppender");
        System.out.println("Starting");
        Logger logger1 = LogManager.getInstance().getLogger();
        Logger logger2 = LogManager.getInstance().getLogger("com.ablestrategies");
        Logger logger3 = LogManager.getInstance().getLogger("com.ablestrategies.logmain");
        Logger logger4 = LogManager.getInstance().getLogger("com.ablestrategies.logger");
        LogManager.getInstance().setLevel(Level.Diag, "com.ablestrategies.logmain");
        logger1.DIAG("DIAG message 1a - it should log");
        logger1.WARN("WARN message 1b - it should log");
        logger2.DIAG("DIAG message 2a - it should log");
        logger2.WARN("WARN message 2b - it should log");
        logger3.DIAG("DIAG message 3a - it should log");
        logger3.WARN("WARN message 3b - it should log");
        logger4.DIAG("DIAG message 4a - it should NOT log !!!!!!!!!");
        logger4.WARN("WARN message 4b - it should log");
        LogManager.getInstance().setLevel(Level.Diag, "");
        logger4.DIAG("DIAG message 4a - it should log, even though it did not before");
        System.out.println("Ending");
    }
}