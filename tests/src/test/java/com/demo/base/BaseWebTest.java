package com.demo.base;

import com.demo.drivers.BrowserType;
import com.demo.drivers.DriverFactory;
import com.demo.util.ScreenshotUtil;
import io.qameta.allure.Step;
import io.qameta.allure.junit5.AllureJunit5;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Method;
import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(AllureJunit5.class)
public abstract class BaseWebTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeAll
    @Step("Global setup: Initialize WebDriver and WebDriverWait")
    public void globalSetUp() {
        BrowserType browser = getBrowserType();
        driver = DriverFactory.getDriver(browser);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        System.out.println("[BaseWebTest] Driver initialized for browser: " + browser);
    }

    @AfterAll
    @Step("Global teardown: Quit WebDriver")
    public void globalTearDown() {
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("[BaseWebTest] Driver quit.");
            } catch (Exception e) {
                System.err.println("[BaseWebTest] Error quitting driver: " + e.getMessage());
            }
        }
    }

    @BeforeEach
    @Step("Starting test: {testInfo.testMethod.get().name}")
    public void beforeEach(TestInfo testInfo) {
        System.out.println("[BaseWebTest] Starting test " + testInfo.getTestMethod().map(Method::getName).orElse("unknownTest"));
    }

    @AfterEach
    @Step("Capture screenshot after each test")
    public void captureAfterEach(TestInfo testInfo) {
        String testName = testInfo.getTestMethod().map(Method::getName).orElse("unknownTest");
        ScreenshotUtil.capture(driver, testName);
    }

    /**
     * Subclasses can override this to customize browser type.
     */
    protected BrowserType getBrowserType() {
        return BrowserType.fromEnvOrDefault(); // Default behavior if not overridden
    }
}
