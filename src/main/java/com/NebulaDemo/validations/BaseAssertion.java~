package com.automationExercise.validations;

import com.automationExercise.FilesUtils;
import com.automationExercise.utils.WaitManger;
import com.automationExercise.utils.actions.ElementActions;
import org.apache.logging.log4j.core.util.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class BaseAssertion {
    protected WebDriver driver;
    protected BaseAssertion()
    {

    }
    protected WaitManger waitManager;
    protected ElementActions elementActions;

    protected BaseAssertion(WebDriver driver) {
        this.driver = driver;
        this.waitManager = new WaitManger(driver);
        this.elementActions = new ElementActions(driver);

    }

    protected abstract void assertTrue(boolean condition, String message);

    protected abstract void assertFalse(boolean condition, String message);

    protected abstract void assertEquals(Object actual, Object expected, String message);


    public void Equals(String actual, String expected, String message) {
        assertEquals(actual, expected, message);
    }

    public void isElementVisible(By locator) {
       boolean flag= waitManager.fluentWait().until(driver1 ->
        {
            try {
                driver1.findElement(locator).isDisplayed();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
        assertTrue(flag,
                "Element is not visible: " + locator);

    }
    // verify page url
    public void assertPageUrl(String expectedUrl) {
        String actualUrl = driver.getCurrentUrl();
        assertEquals(actualUrl, expectedUrl, "URL does not match. Expected: " + expectedUrl + ", Actual: " + actualUrl);
    }

    // verify page title
    public void assertPageTitle(String expectedTitle) {
        String actualTitle = driver.getTitle();
        assertEquals(actualTitle, expectedTitle, "Title does not match. Expected: " + expectedTitle + ", Actual: " + actualTitle);
    }

    //verify that file exists
    public void assertFileExists(String fileName, String message) {
        waitManager.fluentWait().until(
                d -> FilesUtils.isFileExists(fileName)
        );
        assertTrue(FilesUtils.isFileExists(fileName), message);
    }
}