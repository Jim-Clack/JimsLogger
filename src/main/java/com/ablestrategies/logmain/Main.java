package com.ablestrategies.logmain;

import com.ablestrategies.logger.*;

public class Main {

    public static void main(String[] args) {

        // System.setProperty("jlogger.appenders.list", "com.ablestrategies.logger.ConsoleAppender,com.ablestrategies.logger.LogfileAppender");

        System.out.println("Starting");

        LogManager.getInstance().setLevel(Level.Diag, "com.ablestrategies.logmain");

        Logger logger1 = LogManager.getInstance().getLogger(); // this
        Logger logger2 = LogManager.getInstance().getLogger("com.ablestrategies");
        Logger logger3 = LogManager.getInstance().getLogger("com.ablestrategies.logmain.Main"); // this
        Logger logger4 = LogManager.getInstance().getLogger("com.ablestrategies.logger");

        logger1.DIAG("Logger1 (@1s=@2s) 1a - it should log", logger1.getPackageClassName(), logger1.getLevel().name());
        logger1.WARN("Logger1 (@1s=@2s) 1b - it should log", logger1.getPackageClassName(), logger1.getLevel().name());
        logger2.DIAG("Logger2 (@1s=@2s) 2a - it should NOT log !!!", logger2.getPackageClassName(), logger2.getLevel().name());
        logger2.WARN("Logger2 (@1s=@2s) 2b - it should log", logger2.getPackageClassName(), logger2.getLevel().name());
        logger3.DIAG("Logger3 (@1s=@2s) 3a - it should log", logger3.getPackageClassName(), logger3.getLevel().name());
        logger3.WARN("Logger3 (@1s=@2s) 3b - it should log", logger3.getPackageClassName(), logger3.getLevel().name());
        logger4.DIAG("Logger4 (@1s=@2s) 4a - it should NOT log !!!", logger4.getPackageClassName(), logger4.getLevel().name());
        logger4.WARN("Logger4 (@1s=@2s) 4b - it should log", logger4.getPackageClassName(), logger4.getLevel().name());

        LogManager.getInstance().setLevel(Level.Diag, null); // all

        logger2.DIAG("Logger2 (@1s=@2s) 2a - it should log, even though it did not before", logger2.getPackageClassName(), logger2.getLevel().name());
        logger4.DIAG("Logger4 (@1s=@2s) 4a - it should log, even though it did not before", logger4.getPackageClassName(), logger4.getLevel().name());

        Logger logger5 = LogManager.getInstance().getLogger(); // this

        logger5.DIAG("Logger5 (@1s=@2s) 5b - it should log", logger5.getPackageClassName(), logger5.getLevel().name());

        System.out.println("Ending");
    }
}