package com.codephantom.api;

import static spark.Spark.*;

public class RESTServer {
    public void start() {
        System.out.println("Starting REST API server...");
        port(8081);

        // Define routes
        get("/api/status", (req, res) -> {
            res.type("application/json");
            return "{\"status\": \"running\"}";
        });

        post("/api/analyze", (req, res) -> {
            String payload = req.body();
            res.type("application/json");
            return "{\"message\": \"Received payload\", \"payload\": \"" + payload + "\"}";
        });
    }

    public void stop() {
        System.out.println("Stopping REST API server...");
        stop();
    }
}
