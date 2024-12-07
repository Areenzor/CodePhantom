package com.codephantom.security;

import java.util.regex.Pattern;
import java.util.logging.Logger;

/**
 * InputSanitizer provides methods for sanitizing and validating user inputs to
 * prevent injection attacks, ensure data integrity, and maintain application security.
 */
public class InputSanitizer {

    private static final Logger LOGGER = Logger.getLogger(InputSanitizer.class.getName());

    // Common patterns
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(".*([';--]+|\\b(OR|AND)\\b).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern XSS_PATTERN = Pattern.compile("<.*?>|\".*?\"|'.*?|`.*?", Pattern.CASE_INSENSITIVE);
    private static final Pattern ALPHA_NUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    private static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp)://[\\w.-]+(:\\d+)?(/[\\w._%/-]*)?$");

    /**
     * Sanitizes a string to prevent SQL injection.
     *
     * @param input The input string to sanitize.
     * @return A sanitized string safe for use in SQL queries.
     */
    public String sanitizeForSQL(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        if (SQL_INJECTION_PATTERN.matcher(input).matches()) {
            LOGGER.warning("Potential SQL injection attempt detected: " + input);
            return "";
        }
        return input.replace("'", "''"); // Basic escaping for SQL
    }

    /**
     * Sanitizes a string to prevent XSS attacks.
     *
     * @param input The input string to sanitize.
     * @return A sanitized string safe for rendering in HTML.
     */
    public String sanitizeForXSS(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String sanitized = input.replaceAll("<", "&lt;")
                                .replaceAll(">", "&gt;")
                                .replaceAll("\"", "&quot;")
                                .replaceAll("'", "&#x27;")
                                .replaceAll("&", "&amp;");
        if (XSS_PATTERN.matcher(input).matches()) {
            LOGGER.warning("Potential XSS attempt detected: " + input);
        }
        return sanitized;
    }

    /**
     * Validates whether the input matches the alphanumeric pattern.
     *
     * @param input The input string to validate.
     * @return True if the input is alphanumeric, otherwise false.
     */
    public boolean isAlphaNumeric(String input) {
        return input != null && ALPHA_NUMERIC_PATTERN.matcher(input).matches();
    }

    /**
     * Validates whether the input is a valid email address.
     *
     * @param input The email address to validate.
     * @return True if the input is a valid email, otherwise false.
     */
    public boolean isValidEmail(String input) {
        return input != null && EMAIL_PATTERN.matcher(input).matches();
    }

    /**
     * Validates whether the input is a valid URL.
     *
     * @param input The URL to validate.
     * @return True if the input is a valid URL, otherwise false.
     */
    public boolean isValidURL(String input) {
        return input != null && URL_PATTERN.matcher(input).matches();
    }

    /**
     * Trims whitespace from the input string.
     *
     * @param input The input string to trim.
     * @return A trimmed version of the input string.
     */
    public String trimInput(String input) {
        return input == null ? null : input.trim();
    }

    /**
     * Sanitizes inputs based on the context type.
     *
     * @param input The input string to sanitize.
     * @param context The context for sanitization (e.g., "SQL", "XSS").
     * @return A sanitized string based on the context.
     */
    public String sanitizeInput(String input, String context) {
        if (input == null || context == null) {
            return input;
        }
        switch (context.toUpperCase()) {
            case "SQL":
                return sanitizeForSQL(input);
            case "XSS":
                return sanitizeForXSS(input);
            case "TRIM":
                return trimInput(input);
            default:
                LOGGER.warning("Unsupported sanitization context: " + context);
                return input;
        }
    }

    /**
     * Main method for testing the InputSanitizer.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        InputSanitizer sanitizer = new InputSanitizer();

        // Test cases for SQL sanitization
        String sqlInput = "SELECT * FROM users WHERE username = 'admin' --";
        System.out.println("Sanitized SQL: " + sanitizer.sanitizeForSQL(sqlInput));

        // Test cases for XSS sanitization
        String xssInput = "<script>alert('Hacked!');</script>";
        System.out.println("Sanitized XSS: " + sanitizer.sanitizeForXSS(xssInput));

        // Test cases for validation
        System.out.println("Is AlphaNumeric: " + sanitizer.isAlphaNumeric("Test123"));
        System.out.println("Is Valid Email: " + sanitizer.isValidEmail("test@example.com"));
        System.out.println("Is Valid URL: " + sanitizer.isValidURL("https://www.example.com"));

        // Test cases for general sanitization
        System.out.println("Sanitized Trim: " + sanitizer.sanitizeInput("   Hello   ", "TRIM"));
    }
}
