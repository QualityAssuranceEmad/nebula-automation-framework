package com.NebulaDemo.utils.report;

import com.NebulaDemo.utils.dataReader.PropertyReader;
import com.NebulaDemo.utils.logs.LogsManager;
import com.NebulaDemo.utils.reports.AllureBinaryManager;
import com.NebulaDemo.utils.reports.AllureConstants;
import com.google.common.collect.ImmutableMap;

import java.io.File;

import static com.NebulaDemo.utils.dataReader.PropertyReader.getProperty;
import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

public class AllureEnvironmentManager {
        public static void setEnvironmentVariables () {
            allureEnvironmentWriter(
                    ImmutableMap.<String, String>builder()
                            .put("OS", getProperty("os.name"))
                            .put("Java version:", getProperty("java.runtime.version"))
                            .put("Browser", getProperty("browserType"))
                            .put("Execution Type", getProperty("executionType"))
                            .put("URL", getProperty("baseUrlWeb"))
                            .build(), String.valueOf(AllureConstants.RESULTS_FOLDER) + File.separator
            );
            LogsManager.info("Allure environment variables set.");
            AllureBinaryManager.downloadAndExtract();
        }
    }
