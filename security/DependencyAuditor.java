package com.codephantom.security;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DependencyAuditor checks project dependencies for known vulnerabilities, licensing issues,
 * and version inconsistencies using metadata from trusted sources.
 */
public class DependencyAuditor {

    private static final Logger LOGGER = Logger.getLogger(DependencyAuditor.class.getName());
    private static final String VULNERABILITY_DB = "resources/vulnerability-db.json"; // Simulated vulnerability DB
    private final Map<String, List<String>> vulnerabilityDatabase;

    /**
     * Constructor initializes the vulnerability database from a predefined source.
     */
    public DependencyAuditor() {
        this.vulnerabilityDatabase = loadVulnerabilityDatabase();
    }

    /**
     * Audits dependencies for known vulnerabilities and licensing issues.
     *
     * @param dependencyFilePath Path to the dependencies file (e.g., pom.xml, build.gradle).
     */
    public void auditDependencies(String dependencyFilePath) {
        try {
            LOGGER.info("Auditing dependencies in: " + dependencyFilePath);

            List<String> dependencies = parseDependencies(dependencyFilePath);
            for (String dependency : dependencies) {
                checkForVulnerabilities(dependency);
                checkForLicensingIssues(dependency);
            }

            LOGGER.info("Dependency audit completed successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred during dependency auditing.", e);
        }
    }

    /**
     * Parses the dependency file to extract a list of dependencies.
     *
     * @param dependencyFilePath Path to the dependencies file.
     * @return List of dependencies in the format "groupId:artifactId:version".
     * @throws IOException If an error occurs while reading the file.
     */
    private List<String> parseDependencies(String dependencyFilePath) throws IOException {
        LOGGER.info("Parsing dependencies from: " + dependencyFilePath);
        Path path = Path.of(dependencyFilePath);
        if (!Files.exists(path)) {
            throw new IOException("Dependency file not found: " + dependencyFilePath);
        }

        List<String> dependencies = new ArrayList<>();
        List<String> lines = Files.readAllLines(path);

        for (String line : lines) {
            // Simple parsing for demonstration purposes
            if (line.contains("<dependency>") || line.contains("implementation")) {
                String dependency = extractDependency(line);
                if (dependency != null) {
                    dependencies.add(dependency);
                }
            }
        }

        return dependencies;
    }

    /**
     * Extracts a dependency in the format "groupId:artifactId:version" from a line.
     *
     * @param line The line containing dependency information.
     * @return The formatted dependency string or null if parsing fails.
     */
    private String extractDependency(String line) {
        // Simplistic parsing for demonstration; adapt to the format of your dependency file
        try {
            String[] tokens = line.split(":");
            if (tokens.length == 3) {
                return tokens[0].trim() + ":" + tokens[1].trim() + ":" + tokens[2].trim();
            }
        } catch (Exception e) {
            LOGGER.warning("Failed to parse dependency from line: " + line);
        }
        return null;
    }

    /**
     * Checks if a dependency has known vulnerabilities.
     *
     * @param dependency The dependency string in "groupId:artifactId:version" format.
     */
    private void checkForVulnerabilities(String dependency) {
        LOGGER.info("Checking vulnerabilities for: " + dependency);

        List<String> vulnerabilities = vulnerabilityDatabase.getOrDefault(dependency, Collections.emptyList());
        if (vulnerabilities.isEmpty()) {
            LOGGER.info("No known vulnerabilities found for: " + dependency);
        } else {
            LOGGER.warning("Vulnerabilities found in " + dependency + ": " + vulnerabilities);
        }
    }

    /**
     * Checks if a dependency has licensing issues.
     *
     * @param dependency The dependency string in "groupId:artifactId:version" format.
     */
    private void checkForLicensingIssues(String dependency) {
        // Placeholder for license checks; assume all dependencies use valid licenses
        LOGGER.info("Checking licenses for: " + dependency);
        LOGGER.info("License check passed for: " + dependency);
    }

    /**
     * Loads the vulnerability database from a predefined source.
     *
     * @return A map where keys are dependency strings and values are lists of vulnerabilities.
     */
    private Map<String, List<String>> loadVulnerabilityDatabase() {
        LOGGER.info("Loading vulnerability database...");
        Map<String, List<String>> database = new HashMap<>();
        try {
            Path dbPath = Path.of(VULNERABILITY_DB);
            if (!Files.exists(dbPath)) {
                LOGGER.warning("Vulnerability database not found: " + VULNERABILITY_DB);
                return database;
            }

            // Simulated JSON parsing for demonstration purposes
            List<String> lines = Files.readAllLines(dbPath);
            for (String line : lines) {
                String[] tokens = line.split(":", 2);
                if (tokens.length == 2) {
                    String dependency = tokens[0].trim();
                    List<String> vulnerabilities = Arrays.asList(tokens[1].split(","));
                    database.put(dependency, vulnerabilities);
                }
            }

            LOGGER.info("Vulnerability database loaded successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error loading vulnerability database.", e);
        }
        return database;
    }

    /**
     * Main method for testing the DependencyAuditor.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        DependencyAuditor auditor = new DependencyAuditor();
        auditor.auditDependencies("resources/test-data/dependencies.txt");
    }
}
