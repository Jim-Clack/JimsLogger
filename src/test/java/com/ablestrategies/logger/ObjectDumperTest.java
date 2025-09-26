package com.ablestrategies.logger;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ObjectDumperTest {

    boolean diag = false;

    ObjectDumper dumper = new ObjectDumper(3, 5, false);

    @Test
    void testStringDump() {
        String str = "Abc";
        String result = dumper.dump(str, "str");
        printDiag(result);
        System.out.println("Testing ObjectFormatter.dump str=Abc (" + result.substring(0, 10) + ")");
        assertTrue(result.startsWith("String str"));
        assertTrue(result.contains("Abc"));
    }

    @Test
    void testNumberDump() {
        int number = 123;
        String result = dumper.dump(number, "number");
        printDiag(result);
        System.out.println("Testing ObjectFormatter.dump number=123 (" + result.substring(0, 10) + ")");
        assertTrue(result.startsWith("Integer nu"));
        assertTrue(result.contains("123"));
    }

    @Test
    void testEventDump() {
        LogEvent event = new LogEvent(Level.Diag, "Test Log Message {} {}", 123, 456);
        String result = dumper.dump(event, "event");
        printDiag(result);
        System.out.println("Testing ObjectFormatter.format event (" + result.substring(0, 10) + ")");
        assertTrue(result.startsWith("LogEvent e"));
        assertTrue(result.contains("2"));
    }

    @Test
    void testArrayDump() {
        int[] array = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 };
        String result = dumper.dump(array, "array");
        printDiag(result);
        System.out.println("Testing ObjectFormatter.dump array (" + result.substring(0, 10) + ")");
        assertTrue(result.startsWith("int[] array;"));
        assertTrue(result.contains(" = 9"));
    }

    @Test
    void testArray2dDump() {
        int[][] array2D = {{1, 2, 3},{1, 4, 9},{1, 8, 27}};
        String result = dumper.dump(array2D, "array2D");
        printDiag(result);
        System.out.println("Testing ObjectFormatter.dump array2D (" + result.substring(0, 10) + ")");
        assertTrue(result.startsWith("int[][] ar"));
        assertTrue(result.contains("[2]"));
    }

    @Test
    void testCollectionDump() {
        String[] strings = {"ab", "cd", "ef", "gh", "ij", "kl", "mn", "op", "qr", "st", "uv"};
        List<String> list = Arrays.asList(strings);
        String result = dumper.dump(list, "list");
        printDiag(result);
        System.out.println("Testing ObjectFormatter.dump list (" + result.substring(0, 10) + ")");
        assertTrue(result.startsWith("ArrayList"));
        assertTrue(result.contains("ij"));
    }

    @Test
    void testMapDump() {
        Map<Integer, String> map = new HashMap<>();
        for(int i = 0; i < 12; i++) {
            map.put(i, "Item" + i);
        }
        String result = dumper.dump(map, "map");
        printDiag(result);
        System.out.println("Testing ObjectFormatter.dump map (" + result.substring(0, 10) + ")");
        assertTrue(result.startsWith("HashMap"));
        assertTrue(result.contains("Item4"));
    }

    @Test
    void testStackTraceElementDump() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String result = dumper.dump(stackTrace, "stackTrace");
        printDiag(result);
        System.out.println("Testing ObjectFormatter.dump stackTrace (" + result.substring(0, 10) + ")");
        assertTrue(result.startsWith("StackTraceElement"));
        assertTrue(result.contains("StackTraceElement [1]"));
        assertTrue(result.contains("java.lang.Thread.getStackTrace"));
    }

    @Test
    void testThrowableDump() {
        Throwable throwableInner = new ArithmeticException("Inner Exception");
        Throwable throwableOuter = new RuntimeException("Outer Exception", throwableInner);
        String result = dumper.dump(throwableOuter, "throwableOuter");
        printDiag(result);
        System.out.println("Testing ObjectFormatter.dump Throwable (" + result.substring(0, 10) + ")");
        assertTrue(result.startsWith("RuntimeException"));
        assertTrue(result.contains("Outer Exception"));
        assertTrue(result.contains("ArithmeticException"));
    }

    private void printDiag(String result) {
        if(diag) {
            System.out.println("\n################");
            System.out.println(result);
            System.out.print("...for: ");
        }
    }

}