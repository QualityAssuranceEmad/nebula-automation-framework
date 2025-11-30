package com.NebulaDemo.tests;

import com.NebulaDemo.drivers.GuiDrivers;
import com.NebulaDemo.pages.HomePage;
import com.NebulaDemo.pages.LoginPage;
import com.NebulaDemo.utils.dataReader.JsonReader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {
    //Test
    @Test
    public void testLogin() {
        new LoginPage(driver).enterCredentials(testData.getJsonData("userName"),
                        testData.getJsonData("password"));
        new HomePage(driver).getSiteTitleText();

    }

    //Configurations

    @BeforeClass
    public void beforeClass() {
        testData = new JsonReader("LoginData");
    }

    @BeforeMethod
    public void setUp() {
        driver  = new GuiDrivers();
        new LoginPage(driver).navigate();
        driver.browser().closeExtensionTab();

    }

    @AfterMethod
    public void tearDown() {
        driver.quitDriver();
    }
}
