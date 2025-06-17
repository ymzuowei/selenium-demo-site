package com.demo.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.demo.base.BaseTest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotOnFailureWatcher implements TestWatcher {

    public ScreenshotOnFailureWatcher() {}

    private WebDriver getDriver() {
        // Access WebDriver instance from BaseTest static getter
        return BaseTest.getDriver();
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        WebDriver driver = getDriver();
        if (driver == null) {
            System.out.println("WebDriver is null, cannot take screenshot.");
            return;
        }
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.createDirectories(Paths.get("screenshots"));

            String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String testMethodName = context.getTestMethod()
                                           .map(method -> method.getName())
                                           .orElse("unknown_method");

            String fileName = testMethodName + "_" + timestamp + ".png";
            Path destination = Paths.get("screenshots", fileName);

            Files.copy(screenshot.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Saved screenshot on failure: " + destination);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        // no action needed on success
    }

    // Implement other methods if needed (e.g., testAborted, testDisabled)
}
