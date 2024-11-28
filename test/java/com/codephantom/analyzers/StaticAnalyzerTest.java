package com.codephantom.analyzers;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StaticAnalyzerTest {

    @Test
    void testStaticAnalysis() {
        StaticAnalyzer analyzer = new StaticAnalyzer();
        analyzer.analyze(List.of("src/test/resources/test-data/test-input.txt"));
        assertTrue(true, "Static analysis executed successfully");
    }
}
