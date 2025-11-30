package com.NebulaDemo.drivers;

import com.NebulaDemo.utils.dataReader.PropertyReader;
import com.NebulaDemo.utils.logs.LogsManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FirefoxFactory extends AbstractDriver {
    private  FirefoxOptions getOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments("--start-maximized");
        Map<String, Object> prefs = new HashMap<>();
        String userDir = System.getProperty("user.dir");
        String downloadPath = userDir + "\\src\\test\\resources\\downloads";
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.default_directory",downloadPath);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        options.setCapability(CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);

        FirefoxProfile profile = new FirefoxProfile();
        //profile.addExtension(haramBlurExtension);
        options.setProfile(profile);
        switch (PropertyReader.getProperty("executionType")) {
            case "LocalHeadless" -> options.addArguments("--headless=new");
            case "Remote" -> {
                options.addArguments("--disable-gpu");
                options.addArguments("--disable-extensions");
                options.addArguments("--headless=new");
            }
        }return options;
    }

    @Override
    public WebDriver createDriver() {
  /*      System.setProperty("webdriver.edge.driver",
                "D:\\10-7-2025-Automation\\NebulaTeatAutomation\\drivers\\geckodriver.exe");
*/
        if (PropertyReader.getProperty("executionType").equalsIgnoreCase("Local") ||
                PropertyReader.getProperty("executionType").equalsIgnoreCase("LocalHeadless"))
        {
            return new FirefoxDriver(getOptions());
        } else if (PropertyReader.getProperty("executionType").equalsIgnoreCase("Remote")) {
            try {
                return new RemoteWebDriver(
                        new URL("http://"+ remoteHost + ":" + remotePort+ "/wd/hub").toURI().toURL(),
                        getOptions()
                );
            }catch (Exception e) {
                LogsManager.error("Error while creating RemoteWebDriver: " + e.getMessage());
                throw new RuntimeException("Error while creating RemoteWebDriver", e);
            }

        } else {
            LogsManager.error("Invalid execution type: " +
                    PropertyReader.getProperty("executionType"));
            throw new IllegalArgumentException("Invalid execution type: " +
                    PropertyReader.getProperty("executionType"));

        }
    }
}
