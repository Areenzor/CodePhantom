package com.codephantom.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    @Test
    void testReadFile() throws IOException {
        String content = FileUtils.readFile("src/test/resources/test-data/test-input.txt");
        assertNotNull(content, "File content should not be null");
    }

    @Test
    void testWriteFile() throws IOException {
        Path testFile = Path.of("src/test/resources/test-data/output.txt");
        FileUtils.writeFile(testFile.toString(), "Test Content");
        assertTrue(Files.exists(testFile), "Output file should exist");
        Files.delete(testFile);
    }

    @Test
    void testValidateFilePath() {
        boolean isValid = FileUtils.validateFilePath("src/test/resources/test-data/test-input.txt");
        assertTrue(isValid, "File path should be valid");
    }
}
