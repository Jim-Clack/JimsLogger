package com.ablestrategies.logger;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {

    @org.junit.jupiter.api.Test
    void fromValue() {
        Level level = Level.fromValue(Level.Trace.getValue());
        System.out.println("Testing Level.fromValue Trace (" + level + ")");
        assertEquals(Level.Trace, level);
        level = Level.fromValue(Level.Diag.getValue());
        System.out.println("Testing Level.fromValue Diag (" + level + ")");
        assertEquals(Level.Diag, level);
        level = Level.fromValue(Level.Info.getValue());
        System.out.println("Testing Level.fromValue Info (" + level + ")");
        assertEquals(Level.Info, level);
        level = Level.fromValue(Level.Warn.getValue());
        System.out.println("Testing Level.fromValue Warn (" + level + ")");
        assertEquals(Level.Warn, level);
        level = Level.fromValue(Level.Error.getValue());
        System.out.println("Testing Level.fromValue Error (" + level + ")");
        assertEquals(Level.Error, level);
    }

    @org.junit.jupiter.api.Test
    void fromName() {
        Level level = Level.fromName(Level.Trace.name());
        System.out.println("Testing Level.fromName Trace (" + level + ")");
        assertEquals(Level.Trace, level);
        level = Level.fromName(Level.Diag.name());
        System.out.println("Testing Level.fromName Diag (" + level + ")");
        assertEquals(Level.Diag, level);
        level = Level.fromName(Level.Info.name());
        System.out.println("Testing Level.fromName Info (" + level + ")");
        assertEquals(Level.Info, level);
        level = Level.fromName(Level.Warn.name());
        System.out.println("Testing Level.fromName Warn (" + level + ")");
        assertEquals(Level.Warn, level);
        level = Level.fromName(Level.Error.name());
        System.out.println("Testing Level.fromName Error (" + level + ")");
        assertEquals(Level.Error, level);
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        String str = Level.Trace.toString();
        System.out.println("Testing Level.toString Trace (" + str + ")");
        assertTrue(str.contains("Trace"));
        assertTrue(str.contains("1"));
        str = Level.Diag.toString();
        System.out.println("Testing Level.toString Diag (" + str + ")");
        assertTrue(str.contains("Diag"));
        assertTrue(str.contains("2"));
        str = Level.Info.toString();
        System.out.println("Testing Level.toString Info (" + str + ")");
        assertTrue(str.contains("Info"));
        assertTrue(str.contains("3"));
        str = Level.Warn.toString();
        System.out.println("Testing Level.toString Warn (" + str + ")");
        assertTrue(str.contains("Warn"));
        assertTrue(str.contains("4"));
        str = Level.Error.toString();
        System.out.println("Testing Level.toString Error (" + str + ")");
        assertTrue(str.contains("Error"));
        assertTrue(str.contains("5"));
    }
}