package com.demo.base;

import com.demo.drivers.DriverFactory;
import com.demo.drivers.BrowserType;
import com.demo.util.ScreenshotUtil;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Method;
import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseWebTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeAll
    public void globalSetUp() {
        BrowserType browser = getBrowserType();
        driver = DriverFactory.getDriver(browser);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        System.out.println("[BaseWebTest] Driver initialized for browser: " + browser);
    }

    @AfterAll
    public void globalTearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("[BaseWebTest] Driver quit.");
        }
    }

    @AfterEach
    public void captureAfterEach(TestInfo testInfo) {
        ScreenshotUtil.capture(driver, testInfo.getTestMethod().map(Method::getName).orElse("unknownTest"));
    }

    /**
     * Subclasses can override this to customize browser type.
     */
    protected BrowserType getBrowserType() {
        return BrowserType.fromEnvOrDefault(); // Default behavior if not overridden
    }
}
