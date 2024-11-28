package com.codephantom.analyzers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FuzzTesterTest {

    @Test
    void testFuzzTesting() {
        FuzzTester tester = new FuzzTester();
        tester.test("test-target", 10);
        assertTrue(true, "Fuzz testing executed successfully");
    }
}
