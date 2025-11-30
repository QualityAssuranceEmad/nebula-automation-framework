package com.NebulaDemo.pages;

import com.NebulaDemo.drivers.GuiDrivers;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class HomePage {
    private final GuiDrivers driver;

    public HomePage(GuiDrivers driver) {
        this.driver = driver;
    }
    //Locators
    private By siteTitle= By.id("site-title");
    private By homeButton=By.id("nav-home");
    //Actions

    //Validations
@Step("Get site title text")
    public HomePage getSiteTitleText(){
    String expectedTitle= "Nebula TestLab";
    String actualTitle= driver.element().getText(siteTitle);
    driver.validation().Equals(expectedTitle,actualTitle,"Validating site title text");
    System.out.println("Site title text is: "+actualTitle);
    return this;
    }

}

