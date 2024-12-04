package com.codephantom.analyzers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * StaticAnalyzer performs source code analysis to identify potential security issues,
 * bad coding practices, and vulnerabilities.
 */
public class StaticAnalyzer {

    private static final Logger LOGGER = Logger.getLogger(StaticAnalyzer.class.getName());
    private final List<String> detectedIssues = new ArrayList<>();

    public StaticAnalyzer() {
        LOGGER.info("StaticAnalyzer initialized.");
    }

    /**
     * Analyzes the source code for vulnerabilities and security flaws.
     * 
     * @param sourceCodePath Path to the source code directory.
     */
    public void analyzeSourceCode(String sourceCodePath) {
        LOGGER.info("Starting static analysis...");

        File sourceDirectory = new File(sourceCodePath);
        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
            LOGGER.severe("Invalid source code directory: " + sourceCodePath);
            throw new IllegalArgumentException("Invalid directory: " + sourceCodePath);
        }

        // Analyze each file in the directory
        analyzeFilesInDirectory(sourceDirectory);

        LOGGER.info("Static analysis completed.");
        if (detectedIssues.isEmpty()) {
            LOGGER.info("No issues detected.");
        } else {
            LOGGER.warning("Issues detected: " + detectedIssues.size());
        }
    }

    /**
     * Recursively analyzes files in a directory for static issues.
     * 
     * @param directory The directory to analyze.
     */
    private void analyzeFilesInDirectory(File directory) {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                analyzeFilesInDirectory(file);
            } else if (file.getName().endsWith(".java")) {
                analyzeFile(file);
            }
        }
    }

    /**
     * Analyzes a single file for static issues.
     * 
     * @param file The file to analyze.
     */
    private void analyzeFile(File file) {
        LOGGER.info("Analyzing file: " + file.getName());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Check for specific patterns
                checkForHardCodedSecrets(line, file.getName(), lineNumber);
                checkForSQLInjection(line, file.getName(), lineNumber);
                checkForDeprecatedFunctions(line, file.getName(), lineNumber);
                checkForUnsafeFileOperations(line, file.getName(), lineNumber);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading file: " + file.getAbsolutePath(), e);
        }
    }

    /**
     * Checks for hardcoded secrets in the code.
     */
    private void checkForHardCodedSecrets(String line, String fileName, int lineNumber) {
        Pattern secretPattern = Pattern.compile("(password|secret|key)\\s*=\\s*[\"'].*[\"']", Pattern.CASE_INSENSITIVE);
        if (secretPattern.matcher(line).find()) {
            String issue = String.format("Hardcoded secret found in %s at line %d", fileName, lineNumber);
            LOGGER.warning(issue);
            detectedIssues.add(issue);
        }
    }

    /**
     * Checks for potential SQL injection vulnerabilities.
     */
    private void checkForSQLInjection(String line, String fileName, int lineNumber) {
        Pattern sqlPattern = Pattern.compile("Statement\\s*\\.\\s*execute\\s*\\(\\s*[\"'].*[\"']\\s*\\)", Pattern.CASE_INSENSITIVE);
        if (sqlPattern.matcher(line).find()) {
            String issue = String.format("Potential SQL injection in %s at line %d", fileName, lineNumber);
            LOGGER.warning(issue);
            detectedIssues.add(issue);
        }
    }

    /**
     * Checks for usage of deprecated or insecure functions.
     */
    private void checkForDeprecatedFunctions(String line, String fileName, int lineNumber) {
        List<String> deprecatedFunctions = List.of("System.out.print", "Thread.stop", "Runtime.getRuntime().exec");

        for (String func : deprecatedFunctions) {
            if (line.contains(func)) {
                String issue = String.format("Usage of deprecated/insecure function '%s' in %s at line %d", func, fileName, lineNumber);
                LOGGER.warning(issue);
                detectedIssues.add(issue);
            }
        }
    }

    /**
     * Checks for unsafe file operations.
     */
    private void checkForUnsafeFileOperations(String line, String fileName, int lineNumber) {
        Pattern unsafeFilePattern = Pattern.compile("File\\s*\\(\\s*[\"'].*[\"']\\s*\\)", Pattern.CASE_INSENSITIVE);
        if (unsafeFilePattern.matcher(line).find() && !line.contains("Paths.get")) {
            String issue = String.format("Unsafe file operation in %s at line %d", fileName, lineNumber);
            LOGGER.warning(issue);
            detectedIssues.add(issue);
        }
    }

    /**
     * Retrieves the list of detected issues.
     * 
     * @return List of issues found during analysis.
     */
    public List<String> getDetectedIssues() {
        return detectedIssues;
    }

    /**
     * Main method for testing the StaticAnalyzer.
     * 
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            LOGGER.severe("Usage: java StaticAnalyzer <source_code_directory>");
            return;
        }

        StaticAnalyzer analyzer = new StaticAnalyzer();
        analyzer.analyzeSourceCode(args[0]);

        if (!analyzer.getDetectedIssues().isEmpty()) {
            LOGGER.warning("Issues detected during analysis:");
            for (String issue : analyzer.getDetectedIssues()) {
                LOGGER.warning(" - " + issue);
            }
        } else {
            LOGGER.info("No issues detected.");
        }
    }
}
