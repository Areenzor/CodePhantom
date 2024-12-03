package com.codephantom.security;

public class InputSanitizer {
    public String sanitize(String input) {
        if (input == null) {
            return "";
        }
        return input.replaceAll("[^a-zA-Z0-9]", "_");
    }
}
