package com.demo.util;

import com.demo.drivers.BrowserType;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {

    public static void capture(WebDriver driver, String testMethodName) {
        capture(driver, testMethodName, BrowserType.fromEnvOrDefault());
    }

    public static void capture(WebDriver driver, String testMethodName, BrowserType browserType) {
        if (driver == null) {
            System.out.println("[ScreenshotUtil] Driver is null, skipping screenshot.");
            return;
        }

        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            String browserFolder = browserType.name().toLowerCase(); // e.g., "android_real"
            String dateFolder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));

            String method = testMethodName.replaceAll("[^a-zA-Z0-9_]", "");
            String fileName = String.format("%s_%s.png", method, timestamp);

            Path destinationDir = Paths.get("screenshots", browserFolder, dateFolder);
            Files.createDirectories(destinationDir);

            Path destination = destinationDir.resolve(fileName);
            Files.copy(screenshot.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("📸 Screenshot saved: " + destination);
        } catch (Exception e) {
            System.err.println("❌ Error saving screenshot: " + e.getMessage());
        }
    }
}
