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
        now = now.minusSeconds(1);
        textFormatter = new TextFormatter("[PREFIX] ");
    }

    @org.junit.jupiter.api.Test
    void testAtSymbols() {
        String message = textFormatter.format(event);
        System.out.println("Testing TextFormatter.format testAtSymbols (" + message + ")");
        assertTrue(message.startsWith(
            "[PREFIX] ABC EXC Test 1024 -1.234 T Level[name=Diag, value=2] 20"));
    }

    @org.junit.jupiter.api.Test
    void testBracesSymbols() {
        LogEvent event2 = new LogEvent(Level.Diag, "{} {} {} {} {} {7} {6}",
                "Xyz", 123, 7.5, Level.Diag, false, new ArithmeticException("WWW"), 33);
        String message = textFormatter.format(event2);
        System.out.println("Testing TextFormatter.format testBracesSymbol (" + message.substring(0, 20) + ")");
        assertTrue(message.startsWith(
                "[PREFIX] Xyz 123 7.5 Level[name=Diag, value=2] false 33 java.lang.Arithmetic"));
    }

}