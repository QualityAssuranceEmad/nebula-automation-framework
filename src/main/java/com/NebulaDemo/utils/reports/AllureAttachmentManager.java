package com.NebulaDemo.utils.reports;

import com.NebulaDemo.media.ScreenRecordManager;
import com.NebulaDemo.utils.logs.LogsManager;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.NebulaDemo.utils.dataReader.PropertyReader.getProperty;

/**
 * Utility class to manage attachments for Allure reports.
 * It supports attaching screenshots, logs, video recordings, and product lists.
 */
public class AllureAttachmentManager {

    /**
     * Attaches a screenshot to the Allure report.
     *
     * @param name The name of the attachment in the report.
     * @param path The path to the screenshot file.
     */
    public static void attachScreenshot(String name, String path) {
        try {
            Path screenshot = Path.of(path);
            if (Files.exists(screenshot)) {
                Allure.addAttachment(name, Files.newInputStream(screenshot));
            } else {
                LogsManager.error("Screenshot not found: " + path); // Log error if file doesn't exist
            }
        } catch (Exception e) {
            LogsManager.error("Error attaching screenshot", e.getMessage()); // Catch any exception during attachment
        }
    }

    /**
     * Attaches the current log file to the Allure report.
     * This method shuts down and reconfigures the logger to make sure logs are up-to-date.
     */
    public static void attachLogs() {
        try {
            LogManager.shutdown(); // Ensure logging resources are released
            File logFile = new File(LogsManager.LOGS_PATH + "logs.log"); // Path to the log file
            ((LoggerContext) LogManager.getContext(false)).reconfigure(); // Reconfigure logger context
            if (logFile.exists()) {
                Allure.attachment("logs.log", Files.readString(logFile.toPath())); // Add log content to Allure
            }
        } catch (Exception e) {
            LogsManager.error("Error attaching logs", e.getMessage());
        }
    }

    /**
     * Attaches a video recording of a test to the Allure report if recording is enabled.
     *
     * @param testMethodName The name of the test method used to locate the recording.
     */
    public static void attachRecords(String testMethodName) {
        if (getProperty("recordTests").equalsIgnoreCase("true")) { // Check if recording is enabled in properties
            try {
                File record = new File(ScreenRecordManager.RECORDINGS_PATH + testMethodName);
                if (record != null && record.getName().endsWith(".mp4")) {
                    Allure.addAttachment(testMethodName, "video/mp4", Files.newInputStream(record.toPath()), ".mp4"); // Attach video
                }
            } catch (Exception e) {
                LogsManager.error("Error attaching records", e.getMessage());
            }
        }
    }

    /**
     * Attaches a list of product names to the Allure report.
     * Note: This method is not part of the main test framework reporting.
     *
     * @param title        The title of the attachment.
     * @param productNames List of product names to attach.
     */
    public static void attachProductNames(String title, java.util.List<String> productNames) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Total products: ").append(productNames.size()).append("\n\n"); // Add total count
            for (String name : productNames) {
                sb.append(name).append("\n"); // Add each product name
            }

            Allure.addAttachment(title, "text/plain", sb.toString()); // Attach as plain text
        } catch (Exception e) {
            LogsManager.error("Error attaching product names", e.getMessage());
        }
    }
}
