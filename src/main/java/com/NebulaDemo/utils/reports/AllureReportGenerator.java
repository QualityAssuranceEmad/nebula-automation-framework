package com.NebulaDemo.utils.reports;

import com.NebulaDemo.utils.OSUtils;
import com.NebulaDemo.utils.TerminalUtils;
import com.NebulaDemo.utils.TimeManager;
import com.NebulaDemo.utils.logs.LogsManager;
import org.apache.commons.io.FileUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.NebulaDemo.utils.dataReader.PropertyReader.getProperty;
import static com.NebulaDemo.utils.reports.AllureConstants.HISTORY_FOLDER;
import static com.NebulaDemo.utils.reports.AllureConstants.RESULTS_HISTORY_FOLDER;

public class AllureReportGenerator {

    /**
     * Generates the Allure report using the Allure CLI.
     *
     * @param isSingleFile Determines whether the report should be generated
     *                     as a single HTML file or as a full multi-page report.
     *
     * Single file example:
     *   allure generate results -o report --single-file --clean
     *
     * Full report example:
     *   allure generate results -o full-report --clean
     */
    public static void generateReports(boolean isSingleFile) {

        // Determine whether to generate a full report or single-file report output folder
        Path outputFolder = isSingleFile ? AllureConstants.REPORT_PATH : AllureConstants.FULL_REPORT_PATH;

        // Build the Allure CLI command to generate the report
        List<String> command = new ArrayList<>(List.of(
                AllureBinaryManager.getExecutable().toString(),       // Path to allure executable
                "generate",                                           // Allure command
                AllureConstants.RESULTS_FOLDER.toString(),            // Folder containing .json result files
                "-o", outputFolder.toString(),                        // Output folder for the report
                "--clean"                                             // Remove old files before generating
        ));

        // Add --single-file only if requested
        if (isSingleFile)
            command.add("--single-file");

        // Execute the generated command in the terminal
        TerminalUtils.executeTerminalCommand(command.toArray(new String[0]));
    }

    /**
     * Renames the generated Allure single-file report.
     *
     * Example:
     *    From: index.html
     *    To:   AllureReport_20250720_211230.html
     *
     * @return The new file name after renaming.
     */
    public static String renameReport() {

        // Build new name using timestamp
        String newFileName = AllureConstants.REPORT_PREFIX +
                TimeManager.getTimestamp() +
                AllureConstants.REPORT_EXTENSION;

        // Rename the index.html file to the new name
        com.NebulaDemo.FilesUtils.renameFile(
                AllureConstants.REPORT_PATH.resolve(AllureConstants.INDEX_HTML).toString(),
                newFileName
        );

        return newFileName;
    }

    /**
     * Automatically opens the generated Allure report in the default browser.
     *
     * Works only when executionType contains "local".
     *
     * @param reportFileName The final renamed Allure report file.
     */
    public static void openReport(String reportFileName) {

        // Prevent opening the report for remote executions (e.g., CI/CD pipelines)
        if (!getProperty("executionType").toLowerCase().contains("local"))
            return;

        // Get absolute path to the report file
        Path reportPath = AllureConstants.REPORT_PATH.resolve(reportFileName);

        // Open report depending on OS type
        switch (OSUtils.getCurrentOS()) {
            case WINDOWS ->
                    TerminalUtils.executeTerminalCommand("cmd.exe", "/c", "start", reportPath.toString());
            case MAC, LINUX ->
                    TerminalUtils.executeTerminalCommand("open", reportPath.toString());
            default ->
                    LogsManager.warn("Opening Allure Report is not supported on this OS.");
        }
    }

    /**
     * Copies the Allure history folder into the new results folder.
     *
     * Purpose:
     *   Keeps trend data (flaky tests, retries, durations)
     *   so that the report shows historical graphs.
     *
     * Normally copies:
     *   ./allure-report/history  →  ./allure-results/history
     */
    public static void copyHistory()  {
        try {
            FileUtils.copyDirectory(
                    HISTORY_FOLDER.toFile(),
                    RESULTS_HISTORY_FOLDER.toFile()
            );
        } catch (Exception e) {
            LogsManager.error("Error copying history files: " + e.getMessage());
        }
    }
}
