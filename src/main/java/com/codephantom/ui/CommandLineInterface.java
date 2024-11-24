package com.codephantom.ui;

import java.util.Scanner;

public class CommandLineInterface {
    public void start() {
        System.out.println("Welcome to CodePhantom CLI!");
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print(">> ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting CodePhantom CLI. Goodbye!");
                    break;
                }

                handleCommand(input);
            }
        }
    }

    private void handleCommand(String command) {
        // Add logic for handling commands
        System.out.println("Executing: " + command);
    }
}
