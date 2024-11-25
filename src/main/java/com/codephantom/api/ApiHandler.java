package com.codephantom.api;

import java.util.Map;

public class ApiHandler {
    public String handleRequest(Map<String, String> params) {
        // Simulate handling request parameters
        System.out.println("Handling API request with params: " + params);
        return "{\"status\": \"success\", \"message\": \"Request processed\"}";
    }
}
