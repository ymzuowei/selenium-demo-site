package com.demo.base;

import com.demo.util.ScreenshotUtil;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "window-size=1280,960");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        System.out.println("[BaseTest] Starting test: " + testInfo.getDisplayName());
    }

    @AfterEach
    public void tearDown(TestInfo testInfo) {
        if (driver != null) {
            // Capture screenshot BEFORE quitting the driver
            ScreenshotUtil.capture(driver, testInfo.getTestMethod().map(m -> m.getName()).orElse("unknownTest"));

            driver.quit();
            System.out.println("[BaseTest] Driver quit.");
        }
    }
}
