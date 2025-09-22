package com.ablestrategies.logmain;

import com.ablestrategies.logger.*;

public class Main {
    public static void main(String[] args) {
        System.setProperty("jlogger.appenders.list", "com.ablestrategies.logger.ConsoleAppender");
        System.out.println("Starting");

        LogManager.getInstance().setLevel(Level.Diag, "com.ablestrategies.logmain");

        Logger logger1 = LogManager.getInstance().getLogger(); // this
        Logger logger2 = LogManager.getInstance().getLogger("com.ablestrategies"); // no
        Logger logger3 = LogManager.getInstance().getLogger("com.ablestrategies.logmain.Main"); // this
        Logger logger4 = LogManager.getInstance().getLogger("com.ablestrategies.logger"); // no

        logger1.DIAG("DIAG Logger1 1a - it should log");
        logger1.WARN("WARN Logger1 1b - it should log");
        logger2.DIAG("DIAG Logger2 2a - it should NOT log !!!");
        logger2.WARN("WARN Logger2 2b - it should log");
        logger3.DIAG("DIAG Logger3 3a - it should log");
        logger3.WARN("WARN Logger3 3b - it should log");
        logger4.DIAG("DIAG Logger4 4a - it should NOT log !!!");
        logger4.WARN("WARN Logger4 4b - it should log");

        LogManager.getInstance().setLevel(Level.Diag, ""); // all

        logger2.DIAG("DIAG Logger2 2a - it should log, even though it did not before");
        logger4.DIAG("DIAG Logger4 4a - it should log, even though it did not before");

        System.out.println("Ending");
    }
}