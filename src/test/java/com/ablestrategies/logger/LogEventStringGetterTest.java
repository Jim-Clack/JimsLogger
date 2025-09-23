package com.ablestrategies.logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class LogEventStringGetterTest {

    private LogEvent event;
    private LogEventStringGetter getter;
    private LocalDateTime now;
    private LocalDateTime later;

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
        event = new LogEvent(Level.Diag, "ABCDEFG @1e @2s @3h",
                new ArithmeticException("EXC"), "Test", 1024, -1.234, true, Level.Diag, now);
        getter = new LogEventStringGetter(event);
        later = now.plusSeconds(3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetThrowableMessage() {
        String str = getter.getThrowableMessage();
        System.out.println("Testing getThrowableMessage (" + str + ")");
        assertNotNull(str);
        assertEquals("EXC", str);
    }

    @Test
    void testGetThrowableStackDump() {
        String str = getter.getThrowableStackDump();
        System.out.println("Testing getThrowableStackDump (" + str.substring(0, 30) + ")");
        assertNotNull(str);
        assertTrue(str.contains("ArithmeticException"));
        assertTrue(str.split("\n").length > 2);
    }

    @Test
    void testGetTimestampLocalDateTimeAsString() {
        String str = getter.getTimestampLocalDateTimeAsString();
        System.out.println("Testing getTimestampLocalDateTimeAsString (" + str + ")");
        assertNotNull(str);
        assertTrue(str.contains("" + (now.getYear() % 100)));
        assertTrue(str.contains("" + now.getMonth().getValue()));
        assertTrue(str.contains("" + now.getDayOfMonth()));
        assertTrue(str.contains("" + now.getHour()) || str.contains("" + (now.getHour() % 12)));
        assertTrue(str.contains("" + now.getMinute()));
    }

    @Test
    void testGetTimestampLocalDateAsString() {
        String str = getter.getTimestampLocalDateAsString();
        System.out.println("Testing getTimestampLocalDateAsString (" + str + ")");
        assertNotNull(str);
        assertTrue(str.contains("" + (now.getYear() % 100)));
        assertTrue(str.contains("" + now.getMonth().getValue()));
        assertTrue(str.contains("" + now.getDayOfMonth()));
    }

    @Test
    void testGetTimestampLocalTimeAsString() {
        String str = getter.getTimestampLocalTimeAsString();
        System.out.println("Testing getTimestampLocalTimeAsString (" + str + ")");
        assertNotNull(str);
        assertTrue(str.contains("" + now.getHour()) || str.contains("" + (now.getHour() % 12)));
        assertTrue(str.contains("" + now.getMinute()));
    }

    @Test
    void testGetTimestampUtcDateTimeAsString() {
        String str = getter.getTimestampUtcDateTimeAsString();
        System.out.println("Testing getTimestampUtcDateTimeAsString (" + str + ")");
        OffsetDateTime utc = now.atOffset(ZoneOffset.UTC);
        assertNotNull(str);
        assertTrue(str.startsWith("" + (utc.getYear())));
        assertTrue(str.contains("" + utc.getMonth().getValue()));
        assertTrue(str.contains("" + utc.getDayOfMonth()));
        assertTrue(str.contains("" + utc.getHour()));
        assertTrue(str.contains("" + utc.getMinute()));
    }

    @Test
    void testGetTimestampUtcDateAsString() {
        String str = getter.getTimestampUtcDateAsString();
        System.out.println("Testing getTimestampUtcDateAsString (" + str + ")");
        OffsetDateTime utc = now.atOffset(ZoneOffset.UTC);
        assertNotNull(str);
        assertTrue(str.startsWith("" + (utc.getYear())));
        assertTrue(str.contains("" + utc.getMonth().getValue()));
        assertTrue(str.contains("" + utc.getDayOfMonth()));
    }

    @Test
    void testGetTimestampUtcTimeAsString() {
        String str = getter.getTimestampUtcDateTimeAsString();
        System.out.println("Testing getTimestampUtcTimeAsString (" + str + ")");
        OffsetDateTime utc = now.atOffset(ZoneOffset.UTC);
        assertNotNull(str);
        assertTrue(str.contains("" + utc.getHour()));
        assertTrue(str.contains("" + utc.getMinute()));
    }

    @Test
    void testGetStringArgumentAsString() {
        String str = getter.getStringArgumentAsString(2);
        System.out.println("Testing getStringArgumentAsString (" + str + ")");
        assertNotNull(str);
        assertEquals("Test", str);
    }

    @Test
    void testGetLongArgumentAsString() {
        String str = getter.getLongArgumentAsString(3);
        System.out.println("Testing getLongArgumentAsString (" + str + ")");
        assertNotNull(str);
        assertEquals("1024", str);
    }

    @Test
    void testGetDoubleArgumentAsString() {
        String str = getter.getDoubleArgumentAsString(4);
        System.out.println("Testing getDoubleArgumentAsString (" + str + ")");
        assertNotNull(str);
        assertEquals("-1.234", str);
    }

    @Test
    void testGetBooleanArgumentAsString() {
        String str = getter.getBooleanArgumentAsString(5);
        System.out.println("Testing getBooleanArgumentAsString (" + str + ")");
        assertNotNull(str);
        assertEquals("true", str);
    }

    @Test
    void testGetHexArgumentAsString() {
        String str = getter.getHexArgumentAsString(3);
        System.out.println("Testing getHexArgumentAsString (" + str + ")");
        assertNotNull(str);
        assertEquals("400", str);
    }

    @Test
    void testGetLocalDateTimeArgumentAsString() {
        String str = getter.getLocalDateTimeArgumentAsString(7);
        System.out.println("Testing getLocalDateTimeArgumentAsString (" + str + ")");
        assertNotNull(str);
        assertTrue(str.contains("" + (now.getYear() % 100)));
        assertTrue(str.contains("" + now.getMonth().getValue()));
        assertTrue(str.contains("" + now.getDayOfMonth()));
        assertTrue(str.contains("" + now.getHour()) || str.contains("" + (now.getHour() % 12)));
        assertTrue(str.contains("" + now.getMinute()));
    }

    @Test
    void testGetUtcDateTimeArgumentAsString() {
        String str = getter.getUtcDateTimeArgumentAsString(7);
        System.out.println("Testing getUtcDateTimeArgumentAsString (" + str + ")");
        OffsetDateTime utc = now.atOffset(ZoneOffset.UTC);
        assertNotNull(str);
        assertTrue(str.startsWith("" + (utc.getYear())));
        assertTrue(str.contains("" + utc.getMonth().getValue()));
        assertTrue(str.contains("" + utc.getDayOfMonth()));
        assertTrue(str.contains("" + utc.getHour()));
        assertTrue(str.contains("" + utc.getMinute()));
    }

    @Test
    void testGetExceptionMessageArgumentAsString() {
        String str = getter.getExceptionMessageArgumentAsString(1);
        System.out.println("Testing getExceptionMessageArgumentAsString (" + str + ")");
        assertNotNull(str);
        assertEquals("EXC", str);
    }

    @Test
    void testGetExceptionStackDumpArgumentAsString() {
        String str = getter.getExceptionStackDumpArgumentAsString(1);
        System.out.println("Testing getExceptionStackDumpArgumentAsString (" + str.substring(0, 30) + ")");
        assertNotNull(str);
        assertTrue(str.contains("ArithmeticException"));
        assertTrue(str.split("\n").length > 2);
    }

    @Test
    void testGetToStringArgumentAsString() {
        String str = getter.getToStringArgumentAsString(6);
        System.out.println("Testing getToStringArgumentAsString (" + str + ")");
        assertNotNull(str);
        assertTrue(str.startsWith("Level"));
        assertTrue(str.contains("Diag"));
    }

    @Test
    void testGetObjectArgumentAsString() {
        String str = getter.getObjectArgumentAsString(6, 0);
        System.out.println("Testing getObjectArgumentAsString (" + str + ")");
        assertNotNull(str);
        //
        str = getter.getObjectArgumentAsString(6, 1);
        System.out.println("Testing getObjectArgumentAsString (" + str + ")");
        assertNotNull(str);
        //
    }

}