package com.ablestrategies.logger;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoggingSupportTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCallerStackTraceElement() {
    }

    @Test
    void abbreviate() {
        String s1in = "abc.def.ghi.jkl.mno.pqrs.stuv.";
        String s1out = Support.abbreviate(s1in);
        Assert.assertEquals("a.d.g.j.m.pq.st.", s1out);
    }
}