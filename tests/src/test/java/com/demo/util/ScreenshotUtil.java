package com.demo.util;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {

    public static void capture(WebDriver driver, String testMethodName) {
        if (driver == null) {
            System.out.println("[ScreenshotUtil] Driver is null, skipping screenshot.");
            return;
        }

        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.createDirectories(Paths.get("screenshots"));

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String method = testMethodName.replaceAll("[^a-zA-Z0-9_]", "");
            String fileName = String.format("%s_%s.png", method, timestamp);
            Path destination = Paths.get("screenshots", fileName);

            Files.copy(screenshot.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("📸 Screenshot saved: " + destination);
        } catch (Exception e) {
            System.err.println("❌ Error saving screenshot: " + e.getMessage());
        }
    }
}
