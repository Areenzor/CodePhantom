package com.codephantom.security;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MemorySafetyChecker analyzes code and runtime behavior to detect potential
 * memory safety issues such as buffer overflows, null pointer dereferences,
 * and improper memory access.
 */
public class MemorySafetyChecker {

    private static final Logger LOGGER = Logger.getLogger(MemorySafetyChecker.class.getName());

    /**
     * Performs static memory safety analysis on a given source code.
     *
     * @param sourceCode The source code to analyze.
     * @return A list of detected memory safety issues.
     */
    public List<String> performStaticAnalysis(String sourceCode) {
        LOGGER.info("Starting static memory safety analysis...");
        List<String> issues = new ArrayList<>();

        if (sourceCode == null || sourceCode.isEmpty()) {
            LOGGER.warning("Empty or null source code provided for analysis.");
            return issues;
        }

        // Detect buffer overflow patterns
        if (sourceCode.contains("char buffer[")) {
            if (sourceCode.matches(".*strcpy\\(.*buffer.*,.*\\).*")) {
                issues.add("Potential buffer overflow detected with strcpy usage.");
            }
        }

        // Detect null pointer dereference
        if (sourceCode.matches(".*if\\s*\\(\\s*ptr\\s*==\\s*NULL\\s*\\).*")) {
            issues.add("Potential null pointer dereference detected.");
        }

        // Detect unsafe memory access patterns
        if (sourceCode.contains("malloc") && !sourceCode.contains("free")) {
            issues.add("Potential memory leak: malloc without a corresponding free.");
        }

        LOGGER.info("Static memory safety analysis completed.");
        return issues;
    }

    /**
     * Performs runtime memory safety checks.
     *
     * @param memoryAccessLog A list of memory access operations during runtime.
     * @return A list of detected runtime memory safety issues.
     */
    public List<String> performRuntimeAnalysis(List<String> memoryAccessLog) {
        LOGGER.info("Starting runtime memory safety analysis...");
        List<String> issues = new ArrayList<>();

        if (memoryAccessLog == null || memoryAccessLog.isEmpty()) {
            LOGGER.warning("Empty or null memory access log provided for analysis.");
            return issues;
        }

        Set<String> allocatedMemory = new HashSet<>();

        for (String operation : memoryAccessLog) {
            if (operation.startsWith("ALLOC ")) {
                String address = operation.split(" ")[1];
                allocatedMemory.add(address);
            } else if (operation.startsWith("FREE ")) {
                String address = operation.split(" ")[1];
                if (!allocatedMemory.contains(address)) {
                    issues.add("Invalid free operation detected for address: " + address);
                } else {
                    allocatedMemory.remove(address);
                }
            } else if (operation.startsWith("ACCESS ")) {
                String address = operation.split(" ")[1];
                if (!allocatedMemory.contains(address)) {
                    issues.add("Invalid memory access detected for address: " + address);
                }
            }
        }

        LOGGER.info("Runtime memory safety analysis completed.");
        return issues;
    }

    /**
     * Logs detected memory safety issues.
     *
     * @param issues A list of detected issues.
     */
    public void logIssues(List<String> issues) {
        if (issues.isEmpty()) {
            LOGGER.info("No memory safety issues detected.");
        } else {
            LOGGER.warning("Memory safety issues detected:");
            for (String issue : issues) {
                LOGGER.warning(issue);
            }
        }
    }

    /**
     * Main method for testing the MemorySafetyChecker.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        MemorySafetyChecker checker = new MemorySafetyChecker();

        // Example static analysis
        String sourceCode = """
                char buffer[10];
                strcpy(buffer, input);
                if (ptr == NULL) {
                    *ptr = 5;
                }
                malloc(100);
                """;
        List<String> staticIssues = checker.performStaticAnalysis(sourceCode);
        checker.logIssues(staticIssues);

        // Example runtime analysis
        List<String> memoryAccessLog = Arrays.asList(
                "ALLOC 0x1A2B",
                "ACCESS 0x1A2B",
                "FREE 0x1A2B",
                "FREE 0x1A2B", // Invalid free
                "ACCESS 0x3C4D" // Invalid access
        );
        List<String> runtimeIssues = checker.performRuntimeAnalysis(memoryAccessLog);
        checker.logIssues(runtimeIssues);
    }
}
