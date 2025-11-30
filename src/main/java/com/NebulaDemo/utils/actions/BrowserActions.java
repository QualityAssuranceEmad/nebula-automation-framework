package com.NebulaDemo.utils.actions;

import com.NebulaDemo.utils.WaitManger;
import com.NebulaDemo.utils.logs.LogsManager;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;

import java.time.Duration;
import java.util.Set;

public class BrowserActions {
    private final WebDriver driver;
    private final WaitManger waitManager;
    public BrowserActions(WebDriver driver) {
        this.driver = driver;
        this.waitManager = new WaitManger(driver);
    }
    // method maximize the browser window
    public void maximizeWindow() {
        driver.manage().window().maximize();
    }
    // method to get the current URL of the browser
    public String getCurrentUrl() {
        String currentUrl = driver.getCurrentUrl();
        LogsManager.info("Current URL: " + currentUrl);
        return currentUrl;
    }
    // method to navigate to a specific URL
    public void navigateTo(String url) {

        driver.get(url);
        LogsManager.info("Navigated to URL: " + url);
    }
    // method to refresh the current page
    public void refreshPage() {
        driver.navigate().refresh();
    }
    // method to navigate back to the previous page
    public void navigateBack() {
        driver.navigate().back();
    }
    // method to navigate forward to the next page
    public void navigateForward() {
        driver.navigate().forward();
    }
    // method to close the current browser window
    public void closeBrowser() {
        driver.close();
    }
    // method to quit the browser session
    public void quitBrowser() {
        driver.quit();
    }
    //method to switch to a new window
    public void switchToNewWindow() {
        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
        }
    }
    //method to open new window
    public void openNewWindow() {
        driver.switchTo().newWindow(WindowType.WINDOW);
    }

    //close extension tab
    public void closeExtensionTab() {
        try {
            String currentWindowHandle = driver.getWindowHandle();
            Set<String> initialHandles = driver.getWindowHandles();

            // If there's only one window, no extension tabs to close
            if (initialHandles.size() <= 1) {
                LogsManager.info("No extension tabs found - only one window is open");
                return;
            }

            // Wait for potential extension tab (with shorter timeout and better condition)
            try {
                waitManager.fluentWait().withTimeout(Duration.ofSeconds(5)) // Shorter timeout
                        .until(d -> d.getWindowHandles().size() > initialHandles.size());
            } catch (TimeoutException e) {
                // No new tabs opened within timeout, check if we have multiple tabs already
                if (driver.getWindowHandles().size() > 1) {
                    LogsManager.info("Multiple tabs found but no new ones opened recently");
                } else {
                    LogsManager.info("No extension tabs detected within timeout period");
                    return;
                }
            }

            // Close any non-original windows
            Set<String> allHandles = driver.getWindowHandles();
            for (String windowHandle : allHandles) {
                if (!windowHandle.equals(currentWindowHandle)) {
                    driver.switchTo().window(windowHandle);
                    driver.close();
                    LogsManager.info("Closed additional tab: " + driver.getTitle());
                }
            }

            // Switch back to the original window
            driver.switchTo().window(currentWindowHandle);
            LogsManager.info("Extension tab handling completed");

        } catch (Exception e) {
            LogsManager.warn("Failed to handle extension tabs: " + e.getMessage());
            // Don't throw exception, just log and continue
        }
    }


}
