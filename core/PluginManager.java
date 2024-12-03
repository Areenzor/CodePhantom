package com.codephantom.core;

import java.util.ServiceLoader;

public class PluginManager {
    private final ConfigLoader config;

    public PluginManager(ConfigLoader config) {
        this.config = config;
    }

    public void initializePlugins() {
        ServiceLoader<Plugin> plugins = ServiceLoader.load(Plugin.class);
        for (Plugin plugin : plugins) {
            System.out.println("Initializing plugin: " + plugin.getName());
            plugin.initialize(config);
        }
    }
}
