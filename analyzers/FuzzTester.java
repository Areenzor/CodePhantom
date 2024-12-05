package com.codephantom.analyzers;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FuzzTester performs dynamic fuzz testing by sending random and edge-case inputs
 * to the system under test to identify potential vulnerabilities and crashes.
 */
public class FuzzTester {

    private static final Logger LOGGER = Logger.getLogger(FuzzTester.class.getName());
    private static final int DEFAULT_TEST_CASES = 100; // Number of test cases to generate by default
    private static final List<String> COMMON_EDGE_CASES = List.of(
            "", // Empty string
            " ", // Single space
            "null", // Literal null
            "\n", // Newline
            "\0", // Null character
            "💥🔥💡", // Unicode input
            "A".repeat(1024 * 1024), // 1MB string
            "<script>alert('xss')</script>", // XSS payload
            "' OR '1'='1", // SQL Injection payload
            "../../../etc/passwd" // Path traversal payload
    );

    /**
     * Conducts fuzz testing on a target function.
     *
     * @param targetFunction A callback representing the function under test.
     */
    public void runFuzzTests(TargetFunction targetFunction) {
        LOGGER.info("Starting fuzz tests...");

        // Test with common edge cases
        for (String edgeCase : COMMON_EDGE_CASES) {
            LOGGER.info("Testing edge case: " + edgeCase);
            try {
                targetFunction.execute(edgeCase);
            } catch (Exception e) {
                logIssue("Edge case", edgeCase, e);
            }
        }

        // Test with random inputs
        for (int i = 0; i < DEFAULT_TEST_CASES; i++) {
            String randomInput = generateRandomInput();
            LOGGER.info("Testing random input: " + randomInput);
            try {
                targetFunction.execute(randomInput);
            } catch (Exception e) {
                logIssue("Random input", randomInput, e);
            }
        }

        LOGGER.info("Fuzz testing completed.");
    }

    /**
     * Generates random input strings for fuzz testing.
     *
     * @return A randomly generated input string.
     */
    private String generateRandomInput() {
        Random random = new Random();
        int length = random.nextInt(100) + 1; // Random length between 1 and 100
        StringBuilder input = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // Append random printable ASCII character
            char randomChar = (char) (random.nextInt(94) + 32); // ASCII range 32 to 126
            input.append(randomChar);
        }

        return input.toString();
    }

    /**
     * Logs issues detected during fuzz testing.
     *
     * @param testType Type of the test (e.g., Edge case, Random input).
     * @param input    Input that caused the issue.
     * @param exception The exception raised by the target function.
     */
    private void logIssue(String testType, String input, Exception exception) {
        LOGGER.log(Level.SEVERE, String.format("Issue detected during %s test:\nInput: %s\nException: %s",
                testType, input, exception.toString()));
    }

    /**
     * Functional interface representing the function to be fuzz-tested.
     */
    @FunctionalInterface
    public interface TargetFunction {
        void execute(String input) throws Exception;
    }

    /**
     * Main method for testing the FuzzTester.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        FuzzTester fuzzTester = new FuzzTester();

        // Example target function for demonstration
        TargetFunction targetFunction = input -> {
            if (input.contains("💥")) throw new IllegalArgumentException("Explosive input detected!");
            if (input.length() > 512) throw new RuntimeException("Input exceeds maximum length.");
            if (input.matches("<script>.*</script>")) throw new SecurityException("Potential XSS detected.");
            // Simulate processing the input
            System.out.println("Processed input: " + input);
        };

        fuzzTester.runFuzzTests(targetFunction);
    }
}
