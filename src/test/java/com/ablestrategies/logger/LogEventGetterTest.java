package com.ablestrategies.logger;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

import static org.junit.jupiter.api.Assertions.*;

class LogEventGetterTest {

    private LogEvent event;
    private LogEventGetter getter;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        // if it's just before the minute changes, wait a few seconds first...
        LocalDateTime now;
        do {
            now = LocalDateTime.now();
        } while(now.get(ChronoField.SECOND_OF_MINUTE) > 57);
        // create the event now
        event = new LogEvent(Level.Diag, "ABCDEFG", new ArithmeticException("EXC"));
        getter = new LogEventGetter(event);
    }

    @org.junit.jupiter.api.Test
    void getTimestampLocalDateTime() {
    }

    @org.junit.jupiter.api.Test
    void getTimestampLocalDate() {
    }

    @org.junit.jupiter.api.Test
    void getTimestampLocalTime() {
    }

    @org.junit.jupiter.api.Test
    void getTimestampUTCDateTime() {
    }

    @org.junit.jupiter.api.Test
    void getTimestampUTCDate() {
    }

    @org.junit.jupiter.api.Test
    void getTimestampUTCTime() {
    }

    @org.junit.jupiter.api.Test
    void getThreadName() {
    }

    @org.junit.jupiter.api.Test
    void getClassName() {
    }

    @org.junit.jupiter.api.Test
    void getMethodName() {
    }
}