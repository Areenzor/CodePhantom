package com.codephantom.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class ConfigLoader {
    private final Map<String, Object> config;

    private ConfigLoader(Map<String, Object> config) {
        this.config = config;
    }

    public static ConfigLoader load(String filePath) throws IOException {
        Yaml yaml = new Yaml();
        try {
            Map<String, Object> config = yaml.load(Files.newInputStream(Path.of(filePath)));
            return new ConfigLoader(config);
        } catch (IOException e) {
            throw new IOException("Failed to load configuration from: " + filePath, e);
        }
    }

    public Object get(String key) {
        return config.get(key);
    }

    public String getString(String key) {
        return (String) config.getOrDefault(key, "");
    }

    public int getInt(String key) {
        return (int) config.getOrDefault(key, 0);
    }
}
