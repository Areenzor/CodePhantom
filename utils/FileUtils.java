package com.codephantom.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * FileUtils provides utility methods for file operations such as reading, writing,
 * copying, moving, deleting files, and directory management.
 */
public class FileUtils {

    private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());

    /**
     * Reads the content of a file into a single String.
     *
     * @param filePath Path to the file to be read.
     * @return Content of the file as a String.
     * @throws IOException If an I/O error occurs.
     */
    public static String readFileAsString(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        LOGGER.info("Reading file: " + filePath);
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    /**
     * Reads the content of a file line by line into a List.
     *
     * @param filePath Path to the file to be read.
     * @return List of lines from the file.
     * @throws IOException If an I/O error occurs.
     */
    public static List<String> readFileAsLines(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        LOGGER.info("Reading file line by line: " + filePath);
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    /**
     * Writes a String content to a file.
     *
     * @param filePath Path to the file to write.
     * @param content  The content to write.
     * @param append   If true, appends to the file; otherwise, overwrites.
     * @throws IOException If an I/O error occurs.
     */
    public static void writeFile(String filePath, String content, boolean append) throws IOException {
        Path path = Paths.get(filePath);
        LOGGER.info((append ? "Appending to" : "Writing to") + " file: " + filePath);
        if (append) {
            Files.writeString(path, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } else {
            Files.writeString(path, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    /**
     * Copies a file from one location to another.
     *
     * @param sourcePath      Path to the source file.
     * @param destinationPath Path to the destination file.
     * @throws IOException If an I/O error occurs.
     */
    public static void copyFile(String sourcePath, String destinationPath) throws IOException {
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath);
        LOGGER.info("Copying file from " + sourcePath + " to " + destinationPath);
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Moves a file from one location to another.
     *
     * @param sourcePath      Path to the source file.
     * @param destinationPath Path to the destination file.
     * @throws IOException If an I/O error occurs.
     */
    public static void moveFile(String sourcePath, String destinationPath) throws IOException {
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath);
        LOGGER.info("Moving file from " + sourcePath + " to " + destinationPath);
        Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Deletes a file or directory. If the path is a directory, it deletes all contents recursively.
     *
     * @param path Path to the file or directory to delete.
     * @throws IOException If an I/O error occurs.
     */
    public static void deleteFileOrDirectory(String path) throws IOException {
        Path target = Paths.get(path);
        LOGGER.info("Deleting: " + path);
        if (Files.isDirectory(target)) {
            Files.walk(target)
                    .sorted((p1, p2) -> p2.compareTo(p1)) // Delete children before parent
                    .forEach(FileUtils::deleteSafely);
        } else {
            deleteSafely(target);
        }
    }

    /**
     * Safely deletes a file or directory without throwing exceptions.
     *
     * @param path Path to the file or directory.
     */
    private static void deleteSafely(Path path) {
        try {
            Files.delete(path);
            LOGGER.info("Deleted: " + path);
        } catch (IOException e) {
            LOGGER.warning("Failed to delete: " + path + " - " + e.getMessage());
        }
    }

    /**
     * Lists all files in a directory.
     *
     * @param directoryPath Path to the directory.
     * @return List of file paths in the directory.
     * @throws IOException If an I/O error occurs.
     */
    public static List<String> listFilesInDirectory(String directoryPath) throws IOException {
        Path dir = Paths.get(directoryPath);
        LOGGER.info("Listing files in directory: " + directoryPath);
        List<String> filePaths = new ArrayList<>();
        Files.walk(dir, 1) // 1 level depth
                .filter(Files::isRegularFile)
                .forEach(path -> filePaths.add(path.toString()));
        return filePaths;
    }

    /**
     * Ensures the parent directories for a file exist. Creates them if they don't.
     *
     * @param filePath Path to the file.
     * @throws IOException If an I/O error occurs.
     */
    public static void ensureParentDirectories(String filePath) throws IOException {
        Path path = Paths.get(filePath).getParent();
        if (path != null && !Files.exists(path)) {
            LOGGER.info("Creating parent directories for: " + filePath);
            Files.createDirectories(path);
        }
    }

    /**
     * Main method for testing the FileUtils.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        try {
            // Test file operations
            String testFilePath = "test.txt";
            String testContent = "Hello, FileUtils!";

            // Write file
            writeFile(testFilePath, testContent, false);
            System.out.println("File written: " + testFilePath);

            // Read file
            String content = readFileAsString(testFilePath);
            System.out.println("File content: " + content);

            // List files
            String testDir = ".";
            List<String> files = listFilesInDirectory(testDir);
            System.out.println("Files in directory: " + files);

            // Delete file
            deleteFileOrDirectory(testFilePath);
            System.out.println("File deleted: " + testFilePath);

        } catch (IOException e) {
            LOGGER.severe("Error in file operations: " + e.getMessage());
        }
    }
}
