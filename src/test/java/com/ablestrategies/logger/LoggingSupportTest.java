package com.ablestrategies.logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LoggingSupportTest {

    @Test
    void getCallerStackTraceElement() {
        System.out.println("Testing Support.callerStackTraceElement (" + Support.getCallerStackTraceElement().getMethodName() + ")");
        StackTraceElement element = Support.getCallerStackTraceElement();
        assertNotNull(element);
        // Note: Cannot call from outside our package, so this is a makeshift assert
        assertEquals("invoke", element.getMethodName());
    }

    @Test
    void abbreviate() {
        String s1in = "abc.def.ghi.jkl.mno.pqrs.stuv.";
        String s1out = Support.abbreviate(s1in);
        System.out.println("Testing Support.abbreviate (" + s1out + ")");
        assertEquals("a.d.g.j.m.pq.st.", s1out);
    }
}