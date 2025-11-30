package com.NebulaDemo.drivers;

import com.NebulaDemo.utils.actions.AllertActions;
import com.NebulaDemo.utils.actions.BrowserActions;
import com.NebulaDemo.utils.actions.ElementActions;
import com.NebulaDemo.utils.actions.FrameActions;
import com.NebulaDemo.utils.dataReader.PropertyReader;
import com.NebulaDemo.utils.logs.LogsManager;
import com.NebulaDemo.validations.Validation;
import com.NebulaDemo.validations.Verification;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ThreadGuard;

import java.util.List;

public class GuiDrivers {
    private final  String browser = PropertyReader.getProperty("browserType");

    private final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
public GuiDrivers() {
    LogsManager.info("Setting up WebDriver for browser: " + browser);
    Browser browserType = Browser.valueOf(browser.toUpperCase());
    // Initialize the driver for the specified browser type
    LogsManager.info("Initializing WebDriver for browser: " + browserType);
    AbstractDriver abstractDriver = browserType.getDriverFactory(); //local
    WebDriver driver = ThreadGuard.protect(abstractDriver.createDriver());
    driverThreadLocal.set(driver);
    }

    //safari > SAFARI
    private  WebDriver getDriver() {

        Browser browserType = Browser.valueOf(browser.toUpperCase());
        AbstractDriver abstractDriver = browserType.getDriverFactory(); //local
        return abstractDriver.createDriver();
    }

public ElementActions element() {
        return new ElementActions(get());
    }
    public BrowserActions browser() {
        return new BrowserActions(get());
    }
    public FrameActions frame() {
        return new FrameActions(get());
    }
    public AllertActions alert() {
        return new AllertActions(get());
    }
    public Validation validation() {
        return new Validation(get());
    }
    public Verification verification() {
        return new Verification(get());
    }




    public  WebDriver get() {
        return driverThreadLocal.get();
    }

    public  void quitDriver() {
        driverThreadLocal.get().quit();
    }


    public List<WebElement> findElements(By by) {
        return get().findElements(by);
    }
}
