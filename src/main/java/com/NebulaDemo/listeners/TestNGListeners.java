package com.NebulaDemo.listeners;

import com.NebulaDemo.FilesUtils;
import com.NebulaDemo.drivers.UiTest;
import com.NebulaDemo.drivers.WebDriverProvider;
import com.NebulaDemo.media.ScreenRecordManager;
import com.NebulaDemo.media.ScreenshotsManager;
import com.NebulaDemo.utils.dataReader.PropertyReader;
import com.NebulaDemo.utils.logs.LogsManager;
import com.NebulaDemo.utils.reports.*;
import com.NebulaDemo.validations.Validation;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Custom TestNG listeners for managing test execution, reporting, and attachments.
 * Implements multiple TestNG listener interfaces:
 * - ISuiteListener: Suite-level events
 * - IExecutionListener: Overall execution start/finish
 * - IInvokedMethodListener: Before/After each method invocation
 * - ITestListener: Test case-level events
 */
public class TestNGListeners implements ISuiteListener, IExecutionListener, IInvokedMethodListener, ITestListener {

    // -------------------------
    // Suite-level listener
    // -------------------------

    @Override
    public void onStart(ISuite suite) {
        // Set a custom name for the TestNG suite
        suite.getXmlSuite().setName("Automation Exercise");
    }

    // -------------------------
    // Execution-level listeners
    // -------------------------

    @Override
    public void onExecutionStart() {
        LogsManager.info("Test Execution started");

        try {
            cleanTestOutputDirectories(); // Clean screenshots, recordings, logs
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LogsManager.info("Directories cleaned");

        createTestOutputDirectories(); // Recreate necessary directories
        LogsManager.info("Directories created");

        PropertyReader.loadProperties(); // Load configuration properties
        LogsManager.info("Properties loaded");
        com.NebulaDemo.utils.report.AllureEnvironmentManager.setEnvironmentVariables(); // Set Allure environment info
        LogsManager.info("Allure environment set");
    }

    @Override
    public void onExecutionFinish() {
        // Generate reports and copy history
        AllureReportGenerator.generateReports(false); // First generate without overwriting history
        AllureReportGenerator.copyHistory();          // Copy history to results
        AllureReportGenerator.generateReports(true);  // Regenerate full report with history
        AllureReportGenerator.openReport(AllureReportGenerator.renameReport()); // Open report in browser
        LogsManager.info("Test Execution Finished");
    }

    // -------------------------
    // Method invocation listeners
    // -------------------------

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            // Start recording only for UI tests
            if (testResult.getInstance().getClass().isAnnotationPresent(UiTest.class)) {
                ScreenRecordManager.startRecording();
            }

            LogsManager.info("Test Case " + testResult.getName() + " started");
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (!method.isTestMethod()) return;

        WebDriver driver = null;

        // Retrieve WebDriver from test class if it implements WebDriverProvider
        if (testResult.getInstance() instanceof WebDriverProvider provider) {
            driver = provider.getWebDriver();
        }

        // Stop recording if UI test
        if (testResult.getInstance().getClass().isAnnotationPresent(UiTest.class)) {
            ScreenRecordManager.stopRecording(testResult.getName());
            AllureAttachmentManager.attachRecords(testResult.getName());
        }

        // Take screenshots based on test status
        if (driver != null) {
            switch (testResult.getStatus()) {
                case ITestResult.SUCCESS ->
                        ScreenshotsManager.takeFullPageScreenshot(driver, "passed-" + testResult.getName());
                case ITestResult.FAILURE ->
                        ScreenshotsManager.takeFullPageScreenshot(driver, "failed-" + testResult.getName());
                case ITestResult.SKIP ->
                        ScreenshotsManager.takeFullPageScreenshot(driver, "skipped-" + testResult.getName());
            }
        } else {
            LogsManager.warn("No WebDriver available for screenshots in: " + testResult.getName());
        }

        // Run all soft assertions for the test
        Validation.assertAll(testResult);

        // Attach logs to Allure report
        AllureAttachmentManager.attachLogs();
    }

    // -------------------------
    // Test-level listeners
    // -------------------------

    @Override
    public void onTestSuccess(ITestResult result) {
        LogsManager.info("Test Case " + result.getName() + " passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LogsManager.info("Test Case " + result.getName() + " failed");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LogsManager.info("Test Case " + result.getName() + " skipped");
    }

    // -------------------------
    // Helper methods
    // -------------------------

    /**
     * Cleans previous test output directories: Allure results (except history),
     * screenshots, recordings, and logs.
     */
    private void cleanTestOutputDirectories() throws IOException {
        File resultsDir = AllureConstants.RESULTS_FOLDER.toFile();
        if (resultsDir.exists()) {
            for (File file : resultsDir.listFiles()) {
                if (!file.getName().equals("history")) {
                    FileUtils.deleteQuietly(file);
                }
            }
        }

        FilesUtils.cleanDirectory(new File(ScreenshotsManager.SCREENSHOTS_PATH));
        FilesUtils.cleanDirectory(new File(ScreenRecordManager.RECORDINGS_PATH));
        FilesUtils.cleanDirectory(new File(LogsManager.LOGS_PATH));
      //  FilesUtils.forceDelete(new File(LogsManager.LOGS_PATH + File.separator + "logs.log"));
        new PrintWriter(LogsManager.LOGS_PATH + File.separator + "logs.log").close();

    }

    /**
     * Creates necessary directories for screenshots and recordings if they do not exist.
     */
    private void createTestOutputDirectories() {
        FilesUtils.createDirectory(ScreenshotsManager.SCREENSHOTS_PATH);
        FilesUtils.createDirectory(ScreenRecordManager.RECORDINGS_PATH);
    }
}
