package com.codephantom.utils;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Logger {
    private static final Logger logger = LoggerFactory.getLogger("CodePhantom");

    public static void info(String message) {
        logger.info(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }

    public static void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public static void debug(String message) {
        logger.debug(message);
    }
}
