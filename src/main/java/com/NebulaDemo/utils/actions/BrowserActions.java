package com.NebulaDemo.utils.actions;

import com.NebulaDemo.utils.WaitManger;
import com.NebulaDemo.utils.dataReader.PropertyReader;
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
        if (PropertyReader.getProperty("execution").equalsIgnoreCase("enabled")) {
            String currentWindowHandle = driver.getWindowHandle(); //0 1
            waitManager.fluentWait().until(
                    d ->
                    {
                        return d.getWindowHandles().size() > 1; //wait until extension tab is opened
                    }
            );
            for (String windowHandle : driver.getWindowHandles()) //extension tab is opened
            {
                if (!windowHandle.equals(currentWindowHandle))
                    driver.switchTo().window(windowHandle).close(); //close the extension tab
            }
            driver.switchTo().window(currentWindowHandle); //switch back to the main window
            LogsManager.info("Extension tab closed");
        }

    }
}