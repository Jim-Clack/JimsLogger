package com.ablestrategies.logger;

import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TextFormatterTest {

    private TextFormatter textFormatter;
    private LogEvent event;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        // if it's just before the minute changes, wait a few seconds first...
        boolean tryAgain = true;
        while(tryAgain) {
            tryAgain = false;
            now = LocalDateTime.now();
            if (now.getSecond() > 56) {
                Thread.yield();
                tryAgain = true; // wait, if just before a rollover
            }
        }
        // create the event now
        event = new LogEvent(Level.Diag, "ABC @1e @2s @3i @4f @5b @6t @7D",
                new ArithmeticException("EXC"), "Test", 1024, -1.234, true, Level.Diag, now);
        getter = new LogEventStringGetter(event);
        later = now.plusSeconds(3);
        textFormatter = new TextFormatter("[PREFIX] ");
    }

    @org.junit.jupiter.api.Test
    void testFormat() {
        String message = textFormatter.format(event);
        System.out.println("Testing TextFormatter.format (" + message + ")");
        assertTrue(message.startsWith(
            "[PREFIX] ABC EXC Test 1024 -1.234 T Level[name=Diag, value=2] 20"));
    }

}