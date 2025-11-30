package com.NebulaDemo.utils.actions;

import com.NebulaDemo.utils.WaitManger;
import com.NebulaDemo.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;

public class AllertActions {
    private final WebDriver driver;
    private final WaitManger waitManger;
  public AllertActions(WebDriver driver){
      this.driver = driver;
      this.waitManger = new WaitManger(driver);
  }
  public void acceptAlert() {
      waitManger.fluentWait().until(d -> {
          try {
              d.switchTo().alert().accept();
              return true;
          } catch (Exception e) {
              LogsManager.error("Failed to accept alert:" , e.getMessage());
              return false;
          }
      });
  }
    public void dismissAlert() {
        waitManger.fluentWait().until(d -> {
            try {
                d.switchTo().alert().dismiss();
                return true;
            } catch (Exception e) {
                LogsManager.error("Failed to dismiss alert:" , e.getMessage());
                return false;
            }
        });
    }
    public String getAlertText() {
        return waitManger.fluentWait().until(d -> {
            try {
                String alertText = d.switchTo().alert().getText();
                return !alertText.isEmpty() ? alertText : null;
            } catch (Exception e) {
                LogsManager.error("Failed to get alert text:" , e.getMessage());
                return null;
            }
        });
    }
    public void sendKeysToAlert(String text) {
        waitManger.fluentWait().until(d -> {
            try {
                d.switchTo().alert().sendKeys(text);
                return true;
            } catch (Exception e) {
                LogsManager.error("Failed to send keys to alert:" , e.getMessage());
                return false;
            }
        });
    }



}
