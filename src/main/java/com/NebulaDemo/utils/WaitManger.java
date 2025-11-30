package com.NebulaDemo.utils;

import com.NebulaDemo.utils.dataReader.PropertyReader;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class WaitManger {
    private WebDriver driver;
    public WaitManger(WebDriver driver) {
        this.driver = driver;
    }
    public FluentWait<WebDriver> fluentWait() {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(Long.parseLong(PropertyReader.getProperty("WAIT_DEFAULT"))))
                .pollingEvery(Duration.ofMillis(100))
                .ignoreAll(getExption());

    }
    private ArrayList<Class<?extends Exception>> getExption()
    {
     ArrayList<Class<? extends Exception>> exceptions = new ArrayList<>();
     exceptions.add(NoSuchElementException.class);
     exceptions.add(StaleElementReferenceException.class);
     exceptions.add(ElementNotInteractableException.class);
     exceptions.add(ElementClickInterceptedException.class);
     return exceptions;
    }

}
