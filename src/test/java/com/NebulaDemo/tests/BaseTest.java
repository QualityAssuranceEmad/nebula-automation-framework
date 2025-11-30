package com.NebulaDemo.tests;

import com.NebulaDemo.drivers.GuiDrivers;
import com.NebulaDemo.drivers.WebDriverProvider;
import com.NebulaDemo.utils.dataReader.JsonReader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;

public class BaseTest implements WebDriverProvider {
    protected GuiDrivers driver;
    protected JsonReader testData;


    @Override
    public WebDriver getWebDriver() {

        return driver.get();
    }
}
