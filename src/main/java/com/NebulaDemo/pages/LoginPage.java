package com.NebulaDemo.pages;

import com.NebulaDemo.drivers.GuiDrivers;
import com.NebulaDemo.utils.dataReader.PropertyReader;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class LoginPage {
    private final GuiDrivers driver;
    public LoginPage(GuiDrivers driver) {
        this.driver = driver;
    }

    //Locators
    private By usernameField=By.id("username-input");
    private By passwordField=By.id("password-input");
    private By loginButton=By.id("btn-login");
    private By checkIya=By.id("btn-toggle-password");



    //Actions
    @Step("Navigate to home page")
    public LoginPage navigate() {
        driver.browser().navigateTo(PropertyReader.getProperty("baseUrlWeb"));
        return this;
    }
@Step("Enter username and password")
    public void enterCredentials(String username, String password){
      driver.element().typing(usernameField,username).typing(passwordField,password).clicing(loginButton);


    }

    //Validations
}
