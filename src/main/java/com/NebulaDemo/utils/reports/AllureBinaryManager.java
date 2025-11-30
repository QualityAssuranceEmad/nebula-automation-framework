package com.NebulaDemo.utils.reports;

import com.NebulaDemo.utils.OSUtils;
import com.NebulaDemo.utils.TerminalUtils;
import com.NebulaDemo.utils.logs.LogsManager;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AllureBinaryManager {

    /**
     * LazyHolder Pattern:
     * -------------------
     * This inner class loads the latest Allure version from GitHub
     * ONLY when it is first accessed.
     *
     * Benefits:
     * ✔ Thread-safe without synchronization
     * ✔ Memory-efficient
     * ✔ No version lookup unless needed
     */
    private static class LazyHolder {
        static final String VERSION = resolveVersion();

        /**
         * Retrieves the latest Allure release version
         * by scraping the GitHub “latest release” redirect.
         *
         * Example:
         *   https://github.com/allure-framework/allure2/releases/tag/2.24.1
         *
         * @return latest version number as a string
         */
        private static String resolveVersion() {
            try {
                String url = Jsoup.connect("https://github.com/allure-framework/allure2/releases/latest")
                        .followRedirects(true)
                        .execute()
                        .url()
                        .toString();

                // Extract version from URL: ".../tag/{version}"
                return url.split("/tag/")[1];
            } catch (Exception e) {
                throw new IllegalStateException("Failed to resolve latest allure version", e);
            }
        }
    }

    /**
     * Downloads Allure CLI binaries (zip file), extracts them,
     * and ensures the allure executable is ready for use.
     *
     * Steps:
     * 1. Get latest version
     * 2. Check if already installed → skip if exists
     * 3. Download ZIP
     * 4. Extract ZIP
     * 5. Grant execute permissions (Mac/Linux)
     * 6. Cleanup zip file
     */
    public static void downloadAndExtract() {
        try {
            String version = LazyHolder.VERSION;

            // Installation directory: e.g. /allure/allure-2.34.1
            Path extractionDir = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), "allure-" + version);

            // Skip if already installed
            if (Files.exists(extractionDir)) {
                LogsManager.info("Allure binaries already exist.");
                return;
            }

            // Ensure execution permissions on Unix systems for the directory
            if (!OSUtils.getCurrentOS().equals(OSUtils.OS.WINDOWS)) {
                TerminalUtils.executeTerminalCommand("chmod", "u+x", AllureConstants.USER_DIR.toString());
            }

            // Step 1: Download zip file
            Path zipPath = downloadZip(version);

            // Step 2: Extract contents
            extractZip(zipPath);

            LogsManager.info("Allure binaries downloaded and extracted.");

            // Step 3: Mark binary as executable (UNIX only)
            if (!OSUtils.getCurrentOS().equals(OSUtils.OS.WINDOWS)) {
                TerminalUtils.executeTerminalCommand("chmod", "u+x", getExecutable().toString());
            }

            // Step 4: Remove the downloaded zip file to save disk space
            Files.deleteIfExists(
                    Files.list(AllureConstants.EXTRACTION_DIR)
                            .filter(p -> p.toString().endsWith(".zip"))
                            .findFirst()
                            .orElseThrow()
            );

        } catch (Exception e) {
            LogsManager.error("Error downloading or extracting binaries: " + e.getMessage());
        }
    }

    /**
     * Returns the path to the Allure executable based on OS.
     *
     * Windows → allure.bat
     * Mac/Linux → allure
     *
     * Example:
     *   C:/.../allure/allure-2.34.1/bin/allure.bat
     */
    public static Path getExecutable() {
        String version = LazyHolder.VERSION;

        Path binaryPath = Paths.get(
                AllureConstants.EXTRACTION_DIR.toString(),
                "allure-" + version,
                "bin",
                "allure"
        );

        // Append .bat for Windows
        return OSUtils.getCurrentOS() == OSUtils.OS.WINDOWS
                ? binaryPath.resolveSibling(binaryPath.getFileName() + ".bat")
                : binaryPath;
    }

    /**
     * Downloads the Allure zip distribution.
     *
     * Example URL:
     *   https://repo.maven.apache.org/.../allure-commandline-2.34.1.zip
     *
     * @param version Allure version to download
     * @return Path to the downloaded ZIP
     */
    private static Path downloadZip(String version) {

        try {
            // Build download URL
            String url = AllureConstants.ALLURE_ZIP_BASE_URL + version +
                    "/allure-commandline-" + version + ".zip";

            // Where to save the file locally
            Path zipFile = Paths.get(
                    AllureConstants.EXTRACTION_DIR.toString(),
                    "allure-" + version + ".zip"
            );

            // Download only if file does not exist
            if (!Files.exists(zipFile)) {
                Files.createDirectories(AllureConstants.EXTRACTION_DIR);

                try (BufferedInputStream in = new BufferedInputStream(new URI(url).toURL().openStream());
                     OutputStream out = Files.newOutputStream(zipFile)) {

                    // Copy bytes from internet stream → file
                    in.transferTo(out);

                } catch (Exception e) {
                    LogsManager.error("Invalid URL for Allure download: " + e.getMessage());
                }
            }

            return zipFile;

        } catch (Exception e) {
            LogsManager.error("Error downloading Allure zip file: " + e.getMessage());
            return Paths.get("");
        }
    }

    /**
     * Extracts the contents of the downloaded ZIP file.
     *
     * Implementation:
     * 1. Loop through each ZIP entry
     * 2. If directory → create directory
     * 3. If file → write file contents to destination
     */
    private static void extractZip(Path zipPath) {
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipPath))) {

            ZipEntry entry;

            while ((entry = zipInputStream.getNextEntry()) != null) {

                // Destination path
                Path filePath = Paths.get(
                        AllureConstants.EXTRACTION_DIR.toString(),
                        File.separator,
                        entry.getName()
                );

                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    Files.createDirectories(filePath.getParent());
                    Files.copy(zipInputStream, filePath);
                }
            }

        } catch (Exception e) {
            LogsManager.error("Error extracting Allure zip file: " + e.getMessage());
        }
    }
}
