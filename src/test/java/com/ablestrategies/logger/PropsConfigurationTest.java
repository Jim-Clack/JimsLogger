package com.ablestrategies.logger;

import static org.junit.jupiter.api.Assertions.*;

class PropsConfigurationTest {

    IConfiguration configuration;

    String prevAppenders;
    String prevPrefix;
    String prevLevel;
    String prevMax;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        // preserve originals
        prevAppenders = System.getProperty("jlogger.appenders.list", "");
        prevPrefix = System.getProperty("jlogger.console.prefix", "");
        prevLevel = System.getProperty("jlogger.default.level", "");
        prevMax = System.getProperty("jlogger.logfile.kmaxsize", "");
        // test values
        System.setProperty("jlogger.appenders.list", "appender1,appender2");
        System.setProperty("jlogger.console.prefix", "[prefix]");
        System.setProperty("jlogger.default.level", "Info");
        System.setProperty("jlogger.logfile.kmaxsize", "100");
        // do it...
        configuration = new PropsConfiguration();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        // restore originals
        System.setProperty("jlogger.appenders.list", prevAppenders);
        System.setProperty("jlogger.console.prefix", prevPrefix);
        System.setProperty("jlogger.default.level", prevLevel);
        System.setProperty("jlogger.logfile.kmaxsize", prevMax);
    }

    @org.junit.jupiter.api.Test
    void getString() {
        String str = configuration.getString("jlogger.appenders.list", "");
        System.out.println("Testing PropsConfiguration.getString jlogger.appenders.list (" + str + ")");
        assertEquals("appender1,appender2", str);
        str = configuration.getString("jlogger.console.prefix", "");
        System.out.println("Testing PropsConfiguration.getString jlogger.console.prefix (" + str + ")");
        assertEquals("[prefix]", str);
        str = configuration.getString("jlogger.default.level", "");
        System.out.println("Testing PropsConfiguration.getString jlogger.default.level (" + str + ")");
        assertEquals("Info", str);
    }

    @org.junit.jupiter.api.Test
    void getLong() {
        long i = configuration.getLong("jlogger.logfile.kmaxsize", 0);
        System.out.println("Testing PropsConfiguration.getString jlogger.logfile.kmaxsize (" + i + ")");
        assertEquals(100, i);
    }

}