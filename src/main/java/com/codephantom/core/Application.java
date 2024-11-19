package com.codephantom.core;

public class Application {
    public static void main(String[] args) {
        System.out.println("Initializing CodePhantom...");
        
        try {
            // Load configuration
            ConfigLoader config = ConfigLoader.load("config/app-config.yaml");
            
            // Initialize plugin manager
            PluginManager pluginManager = new PluginManager(config);
            pluginManager.initializePlugins();
            
            System.out.println("CodePhantom is ready!");
        } catch (Exception e) {
            System.err.println("Error initializing application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
