package com.NebulaDemo.utils.reports;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.NebulaDemo.utils.dataReader.PropertyReader.getProperty;

/**
 * This class contains constants related to Allure report paths and configuration.
 * All paths are defined as static final to make them globally accessible and immutable.
 */
public class AllureConstants {

    // -------------------------
    // User directories
    // -------------------------

    /** Path to the current project directory (user.dir) */
    public static final Path USER_DIR = Paths.get(getProperty("user.dir"), File.separator);

    /** Path to the user's home directory (user.home) */
    public static final Path USER_HOME = Paths.get(getProperty("user.home"), File.separator);

    // -------------------------
    // Allure output folders
    // -------------------------

    /** Path where Allure stores raw results before report generation */
    public static final Path RESULTS_FOLDER = Paths.get(String.valueOf(USER_DIR), "test-output", "allure-results", File.separator);

    /** Path for the single HTML report after generation */
    public static final Path REPORT_PATH = Paths.get(String.valueOf(USER_DIR), "test-output", "reports", File.separator);

    /** Path for the full Allure report (includes history, attachments, etc.) */
    public static final Path FULL_REPORT_PATH = Paths.get(String.valueOf(USER_DIR), "test-output", "full-report", File.separator);

    // -------------------------
    // History folders
    // -------------------------

    /** Folder inside full report to store historical data (used for trend graphs) */
    public static final Path HISTORY_FOLDER = Paths.get(FULL_REPORT_PATH.toString(), "history", File.separator);

    /** Folder inside results to copy generated history (before regenerating report) */
    public static final Path RESULTS_HISTORY_FOLDER = Paths.get(RESULTS_FOLDER.toString(), "history", File.separator);

    // -------------------------
    // Report naming
    // -------------------------

    /** Default name of the main HTML page */
    public static final String INDEX_HTML = "index.html";

    /** Prefix to use for generated reports (e.g., AllureReport_2025-11-29.html) */
    public static final String REPORT_PREFIX = "AllureReport_";

    /** File extension for the report */
    public static final String REPORT_EXTENSION = ".html";

    // -------------------------
    // Allure command-line download URL
    // -------------------------

    /** Base URL for downloading Allure Commandline from Maven Central */
    public static final String ALLURE_ZIP_BASE_URL = "https://repo.maven.apache.org/maven2/io/qameta/allure/allure-commandline/";

    // -------------------------
    // Extraction folder for Allure binaries
    // -------------------------

    /** Directory where Allure binaries are extracted (typically inside user home) */
    public static final Path EXTRACTION_DIR = Paths.get(String.valueOf(USER_HOME), "Administrator/allure", File.separator);
}
