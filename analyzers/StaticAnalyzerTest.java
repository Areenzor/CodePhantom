package com.codephantom.analyzers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Unit tests for StaticAnalyzer class.
 */
class StaticAnalyzerTest {

    private StaticAnalyzer staticAnalyzer;
    private File tempDirectory;

    @BeforeEach
    void setUp() throws IOException {
        staticAnalyzer = new StaticAnalyzer();

        // Create a temporary directory for mock source code files
        tempDirectory = Files.createTempDirectory("static-analyzer-test").toFile();
        tempDirectory.deleteOnExit();
    }

    @Test
    void testAnalyzeSourceCode_withValidFiles() throws IOException {
        // Arrange: Create mock Java source files
        File validFile = createTempJavaFile("ValidFile.java",
                "public class ValidFile {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Hello, World!\");\n" +
                "    }\n" +
                "}");

        // Act
        staticAnalyzer.analyzeSourceCode(tempDirectory.getAbsolutePath());
        List<String> issues = staticAnalyzer.getDetectedIssues();

        // Assert
        assertTrue(issues.isEmpty(), "No issues should be detected in valid files.");
    }

    @Test
    void testAnalyzeSourceCode_withHardcodedSecrets() throws IOException {
        // Arrange: Create a file with hardcoded secrets
        File secretFile = createTempJavaFile("SecretFile.java",
                "public class SecretFile {\n" +
                "    private static final String PASSWORD = \"12345\";\n" +
                "}");

        // Act
        staticAnalyzer.analyzeSourceCode(tempDirectory.getAbsolutePath());
        List<String> issues = staticAnalyzer.getDetectedIssues();

        // Assert
        assertFalse(issues.isEmpty(), "Issues should be detected for hardcoded secrets.");
        assertTrue(issues.get(0).contains("Hardcoded secret found"), "Issue should report hardcoded secrets.");
    }

    @Test
    void testAnalyzeSourceCode_withSQLInjection() throws IOException {
        // Arrange: Create a file with potential SQL injection
        File sqlFile = createTempJavaFile("SQLInjectionFile.java",
                "public class SQLInjectionFile {\n" +
                "    public void executeQuery(String query) {\n" +
                "        Statement stmt = connection.createStatement();\n" +
                "        stmt.execute(query);\n" +
                "    }\n" +
                "}");

        // Act
        staticAnalyzer.analyzeSourceCode(tempDirectory.getAbsolutePath());
        List<String> issues = staticAnalyzer.getDetectedIssues();

        // Assert
        assertFalse(issues.isEmpty(), "Issues should be detected for SQL injection patterns.");
        assertTrue(issues.get(0).contains("Potential SQL injection"), "Issue should report SQL injection patterns.");
    }

    @Test
    void testAnalyzeSourceCode_withDeprecatedFunctions() throws IOException {
        // Arrange: Create a file using deprecated/insecure functions
        File deprecatedFile = createTempJavaFile("DeprecatedFile.java",
                "public class DeprecatedFile {\n" +
                "    public void insecureMethod() {\n" +
                "        Runtime.getRuntime().exec(\"ls\");\n" +
                "    }\n" +
                "}");

        // Act
        staticAnalyzer.analyzeSourceCode(tempDirectory.getAbsolutePath());
        List<String> issues = staticAnalyzer.getDetectedIssues();

        // Assert
        assertFalse(issues.isEmpty(), "Issues should be detected for deprecated functions.");
        assertTrue(issues.get(0).contains("Usage of deprecated/insecure function"), "Issue should report deprecated function usage.");
    }

    @Test
    void testAnalyzeSourceCode_withInvalidDirectory() {
        // Arrange: Use an invalid directory path
        String invalidDirectory = "invalid/directory/path";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            staticAnalyzer.analyzeSourceCode(invalidDirectory);
        }, "Invalid directory should throw IllegalArgumentException.");
    }

    @Test
    void testAnalyzeSourceCode_withEmptyDirectory() {
        // Act
        staticAnalyzer.analyzeSourceCode(tempDirectory.getAbsolutePath());
        List<String> issues = staticAnalyzer.getDetectedIssues();

        // Assert
        assertTrue(issues.isEmpty(), "No issues should be detected in an empty directory.");
    }

    /**
     * Helper method to create a temporary Java source file.
     *
     * @param fileName Name of the file.
     * @param content Content of the file.
     * @return Created file object.
     * @throws IOException if file creation fails.
     */
    private File createTempJavaFile(String fileName, String content) throws IOException {
        File tempFile = new File(tempDirectory, fileName);
        tempFile.deleteOnExit();

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }

        return tempFile;
    }
}
