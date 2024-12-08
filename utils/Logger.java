package com.codephantom.utils;

import java.io.IOException;
import java.util.logging.*;

/**
 * Logger is a centralized utility for managing application logging.
 * It configures log levels, formats, and output streams to ensure
 * consistent and meaningful logs throughout the application.
 */
public class Logger {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("CodePhantomLogger");

    static {
        configureLogger();
    }

    /**
     * Configures the logger with custom settings including:
     * - Console output with a custom format.
     * - File output for persistent logs.
     * - Dynamic log level control via configuration.
     */
    private static void configureLogger() {
        try {
            // Remove default handlers
            LogManager.getLogManager().reset();
            
            // Console handler setup
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            consoleHandler.setFormatter(new CustomLogFormatter());
            LOGGER.addHandler(consoleHandler);

            // File handler setup
            FileHandler fileHandler = new FileHandler("logs/app.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new CustomLogFormatter());
            LOGGER.addHandler(fileHandler);

            // Set global log level (modifiable via configuration)
            LOGGER.setLevel(Level.INFO);

        } catch (IOException e) {
            System.err.println("Failed to configure logger: " + e.getMessage());
        }
    }

    /**
     * Logs a message at the INFO level.
     *
     * @param message The message to log.
     */
    public static void info(String message) {
        LOGGER.info(message);
    }

    /**
     * Logs a message at the WARNING level.
     *
     * @param message The message to log.
     */
    public static void warning(String message) {
        LOGGER.warning(message);
    }

    /**
     * Logs a message at the SEVERE level.
     *
     * @param message The message to log.
     */
    public static void error(String message) {
        LOGGER.severe(message);
    }

    /**
     * Logs a message at the DEBUG level (FINE).
     *
     * @param message The message to log.
     */
    public static void debug(String message) {
        LOGGER.fine(message);
    }

    /**
     * Logs an exception with an optional custom message.
     *
     * @param message   Custom message to log (can be null or empty).
     * @param throwable The exception to log.
     */
    public static void logException(String message, Throwable throwable) {
        if (message == null || message.isEmpty()) {
            LOGGER.log(Level.SEVERE, "An exception occurred", throwable);
        } else {
            LOGGER.log(Level.SEVERE, message, throwable);
        }
    }

    /**
     * Custom formatter for log messages.
     */
    private static class CustomLogFormatter extends Formatter {

        @Override
        public String format(LogRecord record) {
            return String.format("[%1$tF %1$tT] [%2$s] %3$s - %4$s %n",
                    record.getMillis(),
                    record.getLevel(),
                    record.getLoggerName(),
                    record.getMessage());
        }
    }

    /**
     * Example main method for testing the Logger utility.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Logger.info("Application started.");
        Logger.debug("Debugging application flow.");
        Logger.warning("This is a warning message.");
        Logger.error("This is an error message.");
        Logger.logException("An example exception occurred.", new Exception("Test Exception"));
    }
}
