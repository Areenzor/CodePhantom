package com.codephantom.analyzers;

import java.util.List;

public class StaticAnalyzer {
    public void analyze(List<String> sourceFiles) {
        System.out.println("Performing static analysis on source files...");
        
        for (String file : sourceFiles) {
            System.out.println("Analyzing: " + file);
            // Add static analysis logic here
        }
    }
}
