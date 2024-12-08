package com.codephantom.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FileUtils class.
 */
class FileUtilsTest {

    private static final String TEST_DIRECTORY = "test_files";
    private static final String TEST_FILE = TEST_DIRECTORY + "/test.txt";

    @BeforeEach
    void setUp() throws IOException {
        // Create test directory and file
        Files.createDirectories(Path.of(TEST_DIRECTORY));
        Files.writeString(Path.of(TEST_FILE), "Sample content for testing.");
    }

    @AfterEach
    void tearDown() throws IOException {
        // Cleanup test directory and file
        Files.walk(Path.of(TEST_DIRECTORY))
                .map(Path::toFile)
                .forEach(File::delete);
        Files.deleteIfExists(Path.of(TEST_DIRECTORY));
    }

    @Test
    void testReadFileToString() throws IOException {
        String content = FileUtils.readFileToString(TEST_FILE);
        assertNotNull(content, "File content should not be null");
        assertEquals("Sample content for testing.", content, "File content does not match expected value");
    }

    @Test
    void testWriteStringToFile() throws IOException {
        String newContent = "Updated content for testing.";
        FileUtils.writeStringToFile(TEST_FILE, newContent);

        String content = Files.readString(Path.of(TEST_FILE));
        assertNotNull(content, "File content should not be null after writing");
        assertEquals(newContent, content, "Written content does not match expected value");
    }

    @Test
    void testFileExists() {
        assertTrue(FileUtils.fileExists(TEST_FILE), "File should exist");
        assertFalse(FileUtils.fileExists("nonexistent.txt"), "Non-existent file should not exist");
    }

    @Test
    void testDeleteFile() {
        assertTrue(FileUtils.fileExists(TEST_FILE), "File should exist before deletion");
        assertTrue(FileUtils.deleteFile(TEST_FILE), "File deletion should return true");
        assertFalse(FileUtils.fileExists(TEST_FILE), "File should not exist after deletion");
    }

    @Test
    void testGetFileSize() {
        long size = FileUtils.getFileSize(TEST_FILE);
        assertTrue(size > 0, "File size should be greater than 0");
    }

    @Test
    void testCreateFileIfNotExists() throws IOException {
        String newFile = TEST_DIRECTORY + "/new_test.txt";
        assertFalse(FileUtils.fileExists(newFile), "File should not exist initially");

        FileUtils.createFileIfNotExists(newFile);

        assertTrue(FileUtils.fileExists(newFile), "File should exist after creation");
    }

    @Test
    void testExceptionHandlingForInvalidPaths() {
        assertThrows(IOException.class, () -> FileUtils.readFileToString("invalid_path/invalid_file.txt"),
                "Reading from an invalid path should throw an IOException");

        assertThrows(IOException.class, () -> FileUtils.writeStringToFile("invalid_path/invalid_file.txt", "content"),
                "Writing to an invalid path should throw an IOException");
    }

    @Test
    void testEmptyFileBehavior() throws IOException {
        String emptyFile = TEST_DIRECTORY + "/empty.txt";
        Files.createFile(Path.of(emptyFile));

        String content = FileUtils.readFileToString(emptyFile);
        assertTrue(content.isEmpty(), "Content of empty file should be empty");

        FileUtils.writeStringToFile(emptyFile, "Non-empty content");
        content = FileUtils.readFileToString(emptyFile);
        assertEquals("Non-empty content", content, "File content mismatch for non-empty file");
    }

    @Test
    void testFilePermissions() throws IOException {
        Path filePath = Path.of(TEST_FILE);
        File file = filePath.toFile();
        file.setReadable(false);

        assertThrows(IOException.class, () -> FileUtils.readFileToString(TEST_FILE),
                "Reading from an unreadable file should throw an IOException");

        file.setReadable(true);
    }
}
