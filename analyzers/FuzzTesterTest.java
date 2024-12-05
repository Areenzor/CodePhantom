package com.codephantom.analyzers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Unit tests for the FuzzTester class.
 */
class FuzzTesterTest {

    private FuzzTester fuzzTester;
    private FuzzTester.TargetFunction mockTargetFunction;

    @BeforeEach
    void setUp() {
        fuzzTester = new FuzzTester();
        mockTargetFunction = mock(FuzzTester.TargetFunction.class);
    }

    @Test
    void testRunFuzzTests_withEdgeCases() {
        // Arrange
        doNothing().when(mockTargetFunction).execute(anyString());

        // Act
        fuzzTester.runFuzzTests(mockTargetFunction);

        // Assert
        verify(mockTargetFunction, atLeastOnce()).execute(anyString());
    }

    @Test
    void testRunFuzzTests_handlesExceptionsGracefully() {
        // Arrange: Simulate exceptions for specific inputs
        doThrow(new IllegalArgumentException("Explosive input detected!"))
                .when(mockTargetFunction)
                .execute("💥🔥💡");
        doThrow(new SecurityException("Potential XSS detected!"))
                .when(mockTargetFunction)
                .execute("<script>alert('xss')</script>");

        // Act
        fuzzTester.runFuzzTests(mockTargetFunction);

        // Assert
        verify(mockTargetFunction, atLeast(1)).execute("💥🔥💡");
        verify(mockTargetFunction, atLeast(1)).execute("<script>alert('xss')</script>");
    }

    @Test
    void testRunFuzzTests_generatesRandomInputs() {
        // Arrange
        Logger logger = Logger.getLogger(FuzzTester.class.getName());
        logger.setLevel(Level.OFF); // Suppress logs for cleaner test output
        doNothing().when(mockTargetFunction).execute(anyString());

        // Act
        fuzzTester.runFuzzTests(mockTargetFunction);

        // Assert
        verify(mockTargetFunction, atLeast(1)).execute(anyString());
    }

    @Test
    void testRunFuzzTests_withEmptyEdgeCase() {
        // Arrange
        doThrow(new IllegalArgumentException("Empty input is invalid"))
                .when(mockTargetFunction)
                .execute("");

        // Act
        fuzzTester.runFuzzTests(mockTargetFunction);

        // Assert
        verify(mockTargetFunction, atLeast(1)).execute("");
    }

    @Test
    void testRunFuzzTests_logsDetectedIssues() {
        // Arrange
        Logger testLogger = mock(Logger.class);
        doThrow(new RuntimeException("Test runtime exception"))
                .when(mockTargetFunction)
                .execute("test_case");

        // Inject mock logger into FuzzTester for validation
        Logger originalLogger = Logger.getLogger(FuzzTester.class.getName());
        FuzzTesterTestUtils.setLogger(testLogger); // Utility to inject mock logger

        // Act
        fuzzTester.runFuzzTests(mockTargetFunction);

        // Assert
        verify(testLogger, atLeastOnce())
                .log(eq(Level.SEVERE), contains("Issue detected during Edge case test"));
        verify(testLogger, atLeastOnce())
                .log(eq(Level.SEVERE), any(String.class));
    }

    @Test
    void testRunFuzzTests_usesCustomTestCases() {
        // Arrange
        FuzzTester customFuzzTester = new FuzzTester() {
            @Override
            protected String generateRandomInput() {
                return "CUSTOM_TEST_INPUT";
            }
        };
        doNothing().when(mockTargetFunction).execute("CUSTOM_TEST_INPUT");

        // Act
        customFuzzTester.runFuzzTests(mockTargetFunction);

        // Assert
        verify(mockTargetFunction, atLeastOnce()).execute("CUSTOM_TEST_INPUT");
    }

    /**
     * Utility class to inject a custom logger into the FuzzTester class for testing purposes.
     */
    private static class FuzzTesterTestUtils {
        public static void setLogger(Logger customLogger) {
            try {
                var loggerField = FuzzTester.class.getDeclaredField("LOGGER");
                loggerField.setAccessible(true);
                loggerField.set(null, customLogger);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Failed to inject custom logger into FuzzTester", e);
            }
        }
    }
}
