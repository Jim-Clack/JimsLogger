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
        prevMax = System.getProperty("jlogger.max.filesize", "");
        // test values
        System.setProperty("jlogger.appenders.list", "appender1,appender2");
        System.setProperty("jlogger.console.prefix", "[prefix]");
        System.setProperty("jlogger.default.level", "Info");
        System.setProperty("jlogger.max.filesize", "12345");
        // do it...
        configuration = new PropsConfiguration();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        // restore originals
        System.setProperty("jlogger.appenders.list", prevAppenders);
        System.setProperty("jlogger.console.prefix", prevPrefix);
        System.setProperty("jlogger.default.level", prevLevel);
        System.setProperty("jlogger.max.filesize", prevMax);
    }

    @org.junit.jupiter.api.Test
    void getString() {
        assertEquals("appender1,appender2", configuration.getString("jlogger.appenders.list", ""));
        assertEquals("[prefix]", configuration.getString("jlogger.console.prefix", ""));
        assertEquals("Info", configuration.getString("jlogger.default.level", ""));
        assertEquals(12345, configuration.getInteger("jlogger.max.filesize", 0));
    }

    @org.junit.jupiter.api.Test
    void getInteger() {
    }
}