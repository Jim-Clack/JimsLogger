package com.ablestrategies.logger;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {

    @org.junit.jupiter.api.Test
    void fromValue() {
        Level level = Level.fromValue(Level.Trace.getValue());
        assertEquals(Level.Trace, level);
        level = Level.fromValue(Level.Diag.getValue());
        assertEquals(Level.Diag, level);
        level = Level.fromValue(Level.Info.getValue());
        assertEquals(Level.Info, level);
        level = Level.fromValue(Level.Warn.getValue());
        assertEquals(Level.Warn, level);
        level = Level.fromValue(Level.Error.getValue());
        assertEquals(Level.Error, level);
    }

    @org.junit.jupiter.api.Test
    void fromName() {
        Level level = Level.fromName(Level.Trace.name());
        assertEquals(Level.Trace, level);
        level = Level.fromName(Level.Diag.name());
        assertEquals(Level.Diag, level);
        level = Level.fromName(Level.Info.name());
        assertEquals(Level.Info, level);
        level = Level.fromName(Level.Warn.name());
        assertEquals(Level.Warn, level);
        level = Level.fromName(Level.Error.name());
        assertEquals(Level.Error, level);
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        String str = Level.Trace.toString();
        assertTrue(str.contains("Trace"));
        assertTrue(str.contains("1"));
        str = Level.Diag.toString();
        assertTrue(str.contains("Diag"));
        assertTrue(str.contains("2"));
        str = Level.Info.toString();
        assertTrue(str.contains("Info"));
        assertTrue(str.contains("3"));
        str = Level.Warn.toString();
        assertTrue(str.contains("Warn"));
        assertTrue(str.contains("4"));
        str = Level.Error.toString();
        assertTrue(str.contains("Error"));
        assertTrue(str.contains("5"));
    }
}