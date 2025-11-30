package com.NebulaDemo.utils.actions;

import com.NebulaDemo.utils.WaitManger;
import com.NebulaDemo.utils.logs.LogsManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;

public class ElementActions {
    private final WebDriver driver;
    private WaitManger waitManger;

    public ElementActions(WebDriver driver) {
        this.driver = driver;
        this.waitManger = new WaitManger(driver);

    }

    public ElementActions clicing(By locator) {
        waitManger.fluentWait().until(
                d ->

                {
                    try {
                        WebElement element = d.findElement(locator);
                        scrollToElementJS(locator);
                  /*      //wait until element is stable
                        Point initialLocation = element.getLocation();
                        LogsManager.info("Initial location of element: " + initialLocation);
                        Point finalLocation = element.getLocation();
                        LogsManager.info("Final location of element: " + finalLocation);
                        if (!initialLocation.equals(finalLocation)) {
                        return false;
                        }*/
                        element.click();
                        LogsManager.info("Clicked on element: " + locator);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }

                }
        );
        return this;

    }

    public ElementActions typing(By locator, String text) {
        waitManger.fluentWait().until(
                d ->

                {
                    try {
                        WebElement element = d.findElement(locator);
                        scrollToElementJS(locator);
                        element.clear();
                        element.sendKeys(text);
                        LogsManager.info("Typed text: '" + text + "' into element: " + locator);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }

                }

        );
        return this;

    }

    public String getText(By locator) {
        return waitManger.fluentWait().until(
                d ->

                {
                    try {
                        WebElement element = d.findElement(locator);
                        scrollToElementJS(locator);
                        String msg = element.getText();
                        LogsManager.info("Retrieved text: '" + msg + "' from element: " + locator);
                        return !msg.isEmpty() ? msg : null;
                    } catch (Exception e) {
                        return null;
                    }

                }
        );

    }

    //uploding file
    public ElementActions uploadFile(By locator, String filePath) {
String fileAbsolute = System.getProperty("user.dir") + File.separator + filePath;

        waitManger.fluentWait().until(
                d ->

                {
                    try {
                        WebElement element = d.findElement(locator);
                        scrollToElementJS(locator);
                        element.sendKeys(fileAbsolute);
                        LogsManager.info("Uploaded file: '" + filePath + "' to element: " + locator);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }

                }
        );
return this;
    }
    //find an element and return it
    public WebElement findElement(By locator) {

        return driver.findElement(locator);


    }

    // function to scroll to element using js
    public void scrollToElementJS(By locator) {


        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("""
                        arguments[0].scrollIntoView({behavior:"auto",block:"center",inline:"center"});""",
                        findElement(locator));


    }
    //select from dropdown
    public ElementActions selectFromDropdown(By locator, String value) {
        waitManger.fluentWait().until(
                d ->

                {
                    try {
                        WebElement element = d.findElement(locator);
                        scrollToElementJS(locator);
                         Select select = new Select(element);
                        select.selectByVisibleText(value);
                        LogsManager.info("Selected value: '" + value + "' from dropdown: " + locator);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }

                }
        );
return this;
    }
    //Hovering
    public ElementActions hovering(By locator) {
        waitManger.fluentWait().until(
                d ->

                {
                    try {
                        WebElement element = d.findElement(locator);
                        scrollToElementJS(locator);
                        new Actions(d).moveToElement(element).perform();
                        LogsManager.info("Hovered over element: " + locator);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }

                }
        );
        return this;

    }

}
