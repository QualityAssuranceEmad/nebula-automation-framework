package com.NebulaDemo;

import com.NebulaDemo.utils.dataReader.PropertyReader;
import com.NebulaDemo.utils.logs.LogsManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static org.apache.poi.openxml4j.opc.internal.FileHelper.copyFile;

/**
 * Utility class for common file operations such as renaming, creating directories,
 * deleting, and checking file existence.
 */
public class FilesUtils {

    /** Base project directory (user.dir) obtained from properties */
    private static final String USER_DIR = PropertyReader.getProperty("user.dir") + File.separator;

    // -------------------------
    // File renaming
    // -------------------------

    /**
     * Renames a file to a new name within the same directory.
     * If the new name is the same as the old, it does nothing.
     *
     * @param oldName The current file path.
     * @param newName The desired new file name.
     */
    public static void renameFile(String oldName, String newName) {
        try {
            var targetFile = new File(oldName); // Original file
            String targetDirectory = targetFile.getParentFile().getAbsolutePath(); // Directory path
            File newFile = new File(targetDirectory + File.separator + newName); // New file object

            if (!targetFile.getPath().equals(newFile.getPath())) {
                copyFile(targetFile, newFile); // Copy contents to new file
                FileUtils.deleteQuietly(targetFile); // Delete original quietly
                LogsManager.info("Target File Path: \"" + oldName + "\", file was renamed to \"" + newName + "\".");
            } else {
                LogsManager.info("Target File Path: \"" + oldName + "\", already has the desired name \"" + newName + "\".");
            }
        } catch (IOException e) {
            LogsManager.error(e.getMessage());
        }
    }

    // -------------------------
    // Directory creation
    // -------------------------

    /**
     * Creates a directory (and any necessary parent directories) if it does not exist.
     *
     * @param path Relative path to create inside USER_DIR.
     */
    public static void createDirectory(String path) {
        try {
            File file = new File(USER_DIR + path);
            if (!file.exists()) {
                file.mkdirs(); // Create directory including parents
                LogsManager.info("Directory created successfully at " + path);
            }
        } catch (Exception e) {
            LogsManager.error("Error creating directory " + path, e.getMessage());
        }
    }

    // -------------------------
    // File deletion
    // -------------------------

    /**
     * Forces deletion of a file when JVM exits.
     *
     * @param file File object to delete.
     */
    public static void forceDelete(File file) {
        try {
            FileUtils.forceDeleteOnExit(file); // Schedule file for deletion on exit
            LogsManager.info("File deleted: " + file.getAbsolutePath());
        } catch (IOException e) {
            LogsManager.error("Failed to delete file: " + file.getAbsolutePath(), e.getMessage());
        }
    }

    /**
     * Cleans a directory by deleting its contents quietly.
     *
     * @param file Directory file to clean.
     */
    public static void cleanDirectory(File file) {
        try {
            FileUtils.deleteQuietly(file); // Delete directory contents
        } catch (Exception exception) {
            LogsManager.error("Error cleaning directory " + file.getAbsolutePath(), exception.getMessage());
        }
    }

    // -------------------------
    // File existence check
    // -------------------------

    /**
     * Checks if a file exists in the "downloads" folder under resources.
     *
     * @param fileName Name of the file to check.
     * @return true if the file exists, false otherwise.
     */
    public static boolean isFileExists(String fileName) {
        String filePath = USER_DIR + "/src/test/resources/downloads/";
        File file = new File(filePath + fileName);
        return file.exists();
    }

    // -------------------------
    // Main method for testing
    // -------------------------

    public static void main(String[] args) {
        String oldFileName = "oldFile.txt";
        String newFileName = "newFile.txt";
        renameFile(oldFileName, newFileName); // Example usage of renameFile
    }
}
