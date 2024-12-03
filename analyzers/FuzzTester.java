package com.codephantom.analyzers;

import java.util.Random;

public class FuzzTester {
    public void test(String target, int iterations) {
        System.out.println("Starting fuzz testing on target: " + target);

        for (int i = 0; i < iterations; i++) {
            String randomInput = generateRandomInput();
            System.out.println("Testing with input: " + randomInput);
            // Add logic to send the input to the target and record the result
        }
    }

    private String generateRandomInput() {
        Random random = new Random();
        int length = random.nextInt(10) + 1;
        return random.ints(33, 127)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
