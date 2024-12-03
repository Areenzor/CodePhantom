package com.codephantom.ui;

import spark.Spark;

public class WebInterface {
    public void start() {
        System.out.println("Starting CodePhantom Web Interface...");
        Spark.port(8080);

        // Define routes
        Spark.get("/", (req, res) -> "Welcome to CodePhantom Web Interface!");

        Spark.get("/status", (req, res) -> {
            res.type("application/json");
            return "{\"status\": \"running\"}";
        });
    }

    public void stop() {
        System.out.println("Stopping CodePhantom Web Interface...");
        Spark.stop();
    }
}
