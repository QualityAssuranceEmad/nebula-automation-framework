package com.NebulaDemo.utils.actions;

import com.NebulaDemo.utils.WaitManger;
import com.NebulaDemo.utils.logs.LogsManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FrameActions {
    private final WebDriver driver;
    private final WaitManger waitManger;

    public FrameActions(WebDriver driver) {
       this.driver = driver;
        this.waitManger = new WaitManger(driver);

    }
    public void switchToFrameByIndex(String index) {
        waitManger.fluentWait().until(d -> {
            try {
                d.switchTo().frame(index);
                LogsManager.info("Switched to frame by index: " + index);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }
public void switchToFrameByNameOrId(String nameOrId) {
        waitManger.fluentWait().until(d -> {
            try {
                d.switchTo().frame(nameOrId);
                LogsManager.info("Switched to frame by name or ID: " + nameOrId);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }
public void switchToFrameByWebElement(By frameLocator) {
        waitManger.fluentWait().until(d -> {
            try {
                d.switchTo().frame(d.findElement(frameLocator));
                LogsManager.info("Switched to frame by WebElement: " + frameLocator);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }
public void switchToDefaultContent() {
        waitManger.fluentWait().until(d -> {
            try {
                d.switchTo().defaultContent();
                LogsManager.info("Switched to default content");
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }
}
