package com.ablestrategies.logmain;

import com.ablestrategies.logger.*;

public class Main {

    public static void main(String[] args) {

        // Test with 2 appenders...
        System.setProperty("jlogger.appenders.list", "com.ablestrategies.logger.ConsoleAppender, com.ablestrategies.logger.LogFileAppender");

        System.out.println("Starting");

        // set our log level to Diag for only this package, not logger or java or anything else
        LogManager.getInstance().setLevel(Level.Diag, "com.ablestrategies.logmain");

        // Get Loggers for (1) Main, (2) our domain, (3) Main, and (4) logger package not including Main
        Logger logger1 = LogManager.getInstance().getLogger();
        Logger logger2 = LogManager.getInstance().getLogger("com.ablestrategies");
        Logger logger3 = LogManager.getInstance().getLogger("com.ablestrategies.logmain.Main");
        Logger logger4 = LogManager.getInstance().getLogger("com.ablestrategies.logger");

        // See if it Logs Diag and if it logs Warn messages on each of those four Loggers
        logger1.DIAG("Logger1 (@1s=@2s) 1a - it should log", logger1.getPackageClassName(), logger1.getLevel().name());
        logger1.WARN("Logger1 (@1s=@2s) 1b - it should log", logger1.getPackageClassName(), logger1.getLevel().name());
        logger2.DIAG("Logger2 (@1s=@2s) 2a - it should NOT log !!!", logger2.getPackageClassName(), logger2.getLevel().name());
        logger2.WARN("Logger2 (@1s=@2s) 2b - it should log", logger2.getPackageClassName(), logger2.getLevel().name());
        logger3.DIAG("Logger3 (@1s=@2s) 3a - it should log", logger3.getPackageClassName(), logger3.getLevel().name());
        logger3.WARN("Logger3 (@1s=@2s) 3b - it should log", logger3.getPackageClassName(), logger3.getLevel().name());
        logger4.DIAG("Logger4 (@1s=@2s) 4a - it should NOT log !!!", logger4.getPackageClassName(), logger4.getLevel().name());
        logger4.WARN("Logger4 (@1s=@2s) 4b - it should log", logger4.getPackageClassName(), logger4.getLevel().name());

        // Now set ALL log levels to Diag
        LogManager.getInstance().setLevel(Level.Diag, null); // all

        // Diag messages should log everywhere
        logger2.DIAG("Logger2 (@1s=@2s) 2a - it should log, even though it did not before", logger2.getPackageClassName(), logger2.getLevel().name());
        logger4.DIAG("Logger4 (@1s=@2s) 4a - it should log, even though it did not before", logger4.getPackageClassName(), logger4.getLevel().name());

        // Create a new Logger to verify that the prior setLevel() calls will still apply to it.
        Logger logger5 = LogManager.getInstance().getLogger(); // this
        logger5.DIAG("Logger5 (@1s=@2s) 5b - it should log", logger5.getPackageClassName(), logger5.getLevel().name());

        System.out.println("Ending");
    }
}