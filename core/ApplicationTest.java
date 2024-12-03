package com.codephantom.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ApplicationTest {

    @Test
    void testApplicationInitialization() {
        assertDoesNotThrow(() -> Application.main(new String[]{}));
    }
}
