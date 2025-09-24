package com.ablestrategies.logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class LogEventStringGetterTest {

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
        LogEvent event = new LogEvent(Level.Diag, "ABCDEFG @1e @2s @3h",
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
        System.out.println("Testing LogEventStringGetter.getThrowableMessage (" + str + ")");
        assertNotNull(str);
        assertEquals("EXC", str);
    }

    @Test
    void testGetThrowableStackDump() {
        String str = getter.getThrowableStackDump();
        System.out.println("Testing LogEventStringGetter.getThrowableStackDump (" + str.substring(0, 30) + ")");
        assertNotNull(str);
        assertTrue(str.contains("ArithmeticException"));
        assertTrue(str.split("\n").length > 2);
    }

    @Test
    void testGetTimestampLocalDateTimeAsString() {
        String str = getter.getTimestampLocalDateTimeAsString();
        System.out.println("Testing LogEventStringGetter.getTimestampLocalDateTimeAsString (" + str + ")");
        assertNotNull(str);
        assertTrue(str.contains("" + (now.getYear() % 100)));
        assertTrue(str.contains(now.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault())));
        assertTrue(str.contains("" + now.getDayOfMonth()));
        assertTrue(str.contains("" + now.getHour()) || str.contains("" + (now.getHour() % 12)));
        assertTrue(str.contains("" + now.getMinute()));
    }

    @Test
    void testGetTimestampLocalDateAsString() {
        String str = getter.getTimestampLocalDateAsString();
        System.out.println("Testing LogEventStringGetter.getTimestampLocalDateAsString (" + str + ")");
        assertTrue(str.contains("" + (now.getYear() % 100)));
        assertTrue(str.contains(now.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault())));
        assertTrue(str.contains("" + now.getDayOfMonth()));
    }

    @Test
    void testGetTimestampLocalTimeAsString() {
        String str = getter.getTimestampLocalTimeAsString();
        System.out.println("Testing LogEventStringGetter.getTimestampLocalTimeAsString (" + str + ")");
        assertNotNull(str);
        assertTrue(str.contains("" + now.getHour()) || str.contains("" + (now.getHour() % 12)));
        assertTrue(str.contains("" + now.getMinute()));
    }

    @Test
    void testGetTimestampUtcDateTimeAsString() {
        String str = getter.getTimestampUtcDateTimeAsString();
        System.out.println("Testing LogEventStringGetter.getTimestampUtcDateTimeAsString (" + str + ")");
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
        System.out.println("Testing LogEventStringGetter.getTimestampUtcDateAsString (" + str + ")");
        OffsetDateTime utc = now.atOffset(ZoneOffset.UTC);
        assertNotNull(str);
        assertTrue(str.startsWith("" + (utc.getYear())));
        assertTrue(str.contains("" + utc.getMonth().getValue()));
        assertTrue(str.contains("" + utc.getDayOfMonth()));
    }

    @Test
    void testGetTimestampUtcTimeAsString() {
        String str = getter.getTimestampUtcDateTimeAsString();
        System.out.println("Testing LogEventStringGetter.getTimestampUtcTimeAsString (" + str + ")");
        OffsetDateTime utc = now.atOffset(ZoneOffset.UTC);
        assertNotNull(str);
        assertTrue(str.contains("" + utc.getHour()));
        assertTrue(str.contains("" + utc.getMinute()));
    }

    @Test
    void testGetStringArgumentAsString() {
        String str = getter.getStringArgumentAsString(2);
        System.out.println("Testing LogEventStringGetter.getStringArgumentAsString (" + str + ")");
        assertNotNull(str);
        assertEquals("Test", str);
    }

    @Test
    void testGetLongArgumentAsString() {
        String str = getter.getLongArgumentAsString(3);
        System.out.println("Testing LogEventStringGetter.getLongArgumentAsString (" + str + ")");
        assertNotNull(str);
        assertEquals("1024", str);
    }

    @Test
    void testGetDoubleArgumentAsString() {
        String str = getter.getDoubleArgumentAsString(4);
        System.out.println("Testing LogEventStringGetter.getDoubleArgumentAsString (" + str + ")");
        assertNotNull(str);
        assertEquals("-1.234", str);
    }

    @Test
    void testGetBooleanArgumentAsString() {
        String str = getter.getBooleanArgumentAsString(5);
        System.out.println("Testing LogEventStringGetter.getBooleanArgumentAsString (" + str + ")");
        assertNotNull(str);
        assertEquals("true", str);
    }

    @Test
    void testGetHexArgumentAsString() {
        String str = getter.getHexArgumentAsString(3);
        System.out.println("Testing LogEventStringGetter.getHexArgumentAsString (" + str + ")");
        assertNotNull(str);
        assertEquals("400", str);
    }

    @Test
    void testGetLocalDateTimeArgumentAsString() {
        String str = getter.getLocalDateTimeArgumentAsString(7);
        System.out.println("Testing LogEventStringGetter.getLocalDateTimeArgumentAsString (" + str + ")");
        assertNotNull(str);
        assertTrue(str.contains("" + (now.getYear() % 100)));
        assertTrue(str.contains(now.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault())));
        assertTrue(str.contains("" + now.getDayOfMonth()));
        assertTrue(str.contains("" + now.getHour()) || str.contains("" + (now.getHour() % 12)));
        assertTrue(str.contains("" + now.getMinute()));
    }

    @Test
    void testGetUtcDateTimeArgumentAsString() {
        String str = getter.getUtcDateTimeArgumentAsString(7);
        System.out.println("Testing LogEventStringGetter.getUtcDateTimeArgumentAsString (" + str + ")");
        OffsetDateTime utc = now.atOffset(ZoneOffset.UTC);
        assertNotNull(str);
        assertTrue(str.startsWith("" + (utc.getYear())));
        assertTrue(str.contains("" + utc.getMonth().getValue()));
        assertTrue(str.contains("" + utc.getDayOfMonth()));
        assertTrue(str.contains("" + utc.getHour()));
        assertTrue(str.contains("" + utc.getMinute()));
    }

    @Test
    void testGetTimestampNanosAsString() {
        String str = getter.getTimestampNanosAsString();
        System.out.println("Testing LogEventStringGetter.getGetTimestampNanosAsString (" + str + ")");
        long nanos = Long.parseLong(str);
        assertTrue(nanos >= now.getNano());
        assertTrue(nanos <= later.getNano());
    }

    @Test
    void testGetExceptionMessageArgumentAsString() {
        String str = getter.getExceptionMessageArgumentAsString(1);
        System.out.println("Testing LogEventStringGetter.getExceptionMessageArgumentAsString (" + str + ")");
        assertNotNull(str);
        assertEquals("EXC", str);
    }

    @Test
    void testGetExceptionStackDumpArgumentAsString() {
        String str = getter.getExceptionStackDumpArgumentAsString(1);
        System.out.println("Testing LogEventStringGetter.getExceptionStackDumpArgumentAsString (" + str.substring(0, 30) + ")");
        assertNotNull(str);
        assertTrue(str.contains("ArithmeticException"));
        assertTrue(str.split("\n").length > 2);
    }

    @Test
    void testGetToStringArgumentAsString() {
        String str = getter.getToStringArgumentAsString(6);
        System.out.println("Testing LogEventStringGetter.getToStringArgumentAsString (" + str + ")");
        assertNotNull(str);
        assertTrue(str.startsWith("Level"));
        assertTrue(str.contains("Diag"));
    }

    @Test
    void testGetObjectArgumentAsString() {
        String str = getter.getObjectArgumentAsString(6, 0);
        System.out.println("Testing LogEventStringGetter.getObjectArgumentAsString (" + str + ")");
        assertNotNull(str);
        //
        str = getter.getObjectArgumentAsString(6, 1);
        System.out.println("Testing LogEventStringGetter.getObjectArgumentAsString (" + str + ")");
        assertNotNull(str);
        //
    }

}