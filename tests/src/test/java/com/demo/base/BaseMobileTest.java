package com.demo.base;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;

import com.demo.util.ScreenshotUtil;

import java.time.Duration;

public class BaseMobileTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    public void setUpMobile(TestInfo testInfo) {
        System.clearProperty("webdriver.chrome.driver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        options.addArguments("window-size=375,667"); // iPhone 8 portrait for example

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDownMobile(TestInfo testInfo) {
        if (driver != null) {
            // Capture screenshot BEFORE quitting the driver
            ScreenshotUtil.capture(driver, testInfo.getTestMethod().map(m -> m.getName()).orElse("unknownTest"));

            driver.quit();
        }
    }
}
