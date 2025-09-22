package com.ablestrategies.logger;

import org.junit.Assert;

import static org.junit.jupiter.api.Assertions.*;

class TextFormatterTest {

    private TextFormatter textFormatter;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        textFormatter = new TextFormatter("[PREFIX]");
    }

    @org.junit.jupiter.api.Test
    void testFormat() {
    }

}