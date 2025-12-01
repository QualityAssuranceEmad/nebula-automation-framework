package com.NebulaDemo.drivers;

import com.NebulaDemo.utils.dataReader.PropertyReader;
import com.NebulaDemo.utils.logs.LogsManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ChromeFactory extends AbstractDriver {
    private final String remoteHost = PropertyReader.getProperty("remoteHost");
    private final String remotePort = PropertyReader.getProperty("remotePort");

    private ChromeOptions getOptions() {
        ChromeOptions options = new ChromeOptions();

        // Basic arguments
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--start-maximized");

        // Download preferences
        Map<String, Object> prefs = new HashMap<>();
        String userDir = System.getProperty("user.dir");
        String downloadPath = userDir + "\\src\\test\\resources\\downloads";
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.default_directory", downloadPath);
        options.setExperimentalOption("prefs", prefs);

        // Capabilities
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        options.setCapability(CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
        options.setCapability(CapabilityType.ENABLE_DOWNLOADS, true);
        options.setAcceptInsecureCerts(true);

        // REMOVED THE PROBLEMATIC EXTENSIONS:
        if (PropertyReader.getProperty("execution").equalsIgnoreCase("enabled"))
            options.addExtensions(haramBlurExtension); // COMMENTED OUT

        switch (PropertyReader.getProperty("executionType")) {
            case "LocalHeadless" -> options.addArguments("--headless=new");
            case "Remote" -> {
                options.addArguments("--disable-gpu");
                options.addArguments("--disable-extensions"); // Disable extensions for remote
                options.addArguments("--headless=new");
            }
        }

        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        return options;
    }

    @Override
    public WebDriver createDriver() {
/*
        System.setProperty("webdriver.edge.driver",
                "D:\\10-7-2025-Automation\\NebulaTeatAutomation\\drivers\\chromedriver.exe");
*/

        if (PropertyReader.getProperty("executionType").equalsIgnoreCase("Local") ||
                PropertyReader.getProperty("executionType").equalsIgnoreCase("LocalHeadless")) {
            return new ChromeDriver(getOptions());
        } else if (PropertyReader.getProperty("executionType").equalsIgnoreCase("Remote")) {
            try {
                return new RemoteWebDriver(
                        new URL("http://" + remoteHost + ":" + remotePort + "/wd/hub").toURI().toURL(),
                        getOptions()
                );
            } catch (Exception e) {
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