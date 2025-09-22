package com.ablestrategies.logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

import static org.junit.jupiter.api.Assertions.*;

class LogEventTest {

    private LogEvent event;

    @BeforeEach
    void setUp() {
        // if it's just before the minute changes, wait a few seconds first...
        LocalDateTime now;
        do {
            now = LocalDateTime.now();
        } while (now.get(ChronoField.SECOND_OF_MINUTE) > 57);
        // create the event now
        event = new LogEvent(Level.Diag, "ABCDEFG", new ArithmeticException("EXC"));
    }

    @org.junit.jupiter.api.Test
    void getLevel() {
        assertEquals(Level.Diag.name(), event.level.name());
    }

    @org.junit.jupiter.api.Test
    void getMessage() {
        assertEquals("ABCDEFG", event.message);
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        assertTrue(event.toString().contains("Diag"));
        assertTrue(event.toString().contains("ABCDEFG"));
    }

    @org.junit.jupiter.api.Test
    void getThrowableMessage() {
        assertEquals("EXC", event.throwable.getMessage());
        assertEquals("java.lang.ArithmeticException", event.throwable.getClass().getName());
    }

    @org.junit.jupiter.api.Test
    void getThrowableStackDump() {
        StackTraceElement[] stackTrace = event.throwable.getStackTrace();
        assertEquals("setUp", stackTrace[0].getMethodName());
    }

}