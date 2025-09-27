package com.ablestrategies.logger;

import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LogEventTest {

    private LogEvent event;

    @BeforeEach
    void setUp() {
        // if it's just before the minute changes, wait a few seconds first...
        LocalDateTime now;
        do {
            now = LocalDateTime.now();
        } while (now.getSecond() > 57);
        // create the event now
        event = new LogEvent(Level.Diag, "ABCDEFG", new ArithmeticException("EXC"));
    }

    @org.junit.jupiter.api.Test
    void getLevel() {
        System.out.println("Testing LogEvent.Level (" + event.level.name() + ")");
        assertEquals(Level.Diag.name(), event.level.name());
    }

    @org.junit.jupiter.api.Test
    void getMessage() {
        System.out.println("Testing LogEvent.Message (" + event.message + ")");
        assertEquals("ABCDEFG", event.message);
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        System.out.println("Testing LogEvent.toString (" + event.toString() + ")");
        assertTrue(event.toString().contains("Diag"));
        assertTrue(event.toString().contains("ABCDEFG"));
    }

    @org.junit.jupiter.api.Test
    void getThrowableMessage() {
        assertNotNull(event.throwable);
        System.out.println("Testing LogEvent.Throwable (" + event.throwable.getMessage() + ")");
        assertEquals("EXC", event.throwable.getMessage());
        assertEquals("java.lang.ArithmeticException", event.throwable.getClass().getName());
    }

    @org.junit.jupiter.api.Test
    void getThrowableStackDump() {
        assertNotNull(event.throwable);
        System.out.println("Testing LogEvent.Throwable (" + event.throwable.getStackTrace()[0].getMethodName() + ")");
        StackTraceElement[] stackTrace = event.throwable.getStackTrace();
        assertEquals("setUp", stackTrace[0].getMethodName());
    }

}