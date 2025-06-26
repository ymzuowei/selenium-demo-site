package com.demo.drivers;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URI; // Added import
import java.net.URISyntaxException; // Added import
import java.util.Map;

public class DriverFactory {


    public static WebDriver getDriver(BrowserType type) {
        switch (type) {
            case CHROME:
                ChromeOptions chromeOpts = new ChromeOptions();
                if (isHeadless()) {
                    chromeOpts.addArguments("--headless=new");
                }
                return new ChromeDriver(chromeOpts);
            case CHROME_360:
                System.setProperty("webdriver.chrome.driver", "D:/chromedrivers/132.0.6834.83/chromedriver-win64/chromedriver.exe");
                ChromeOptions opt360 = new ChromeOptions();
                opt360.setBinary("C:/Users/ymzuo/AppData/Roaming/360se6/Application/360se.exe");
                if (isHeadless()) {
                    // opt360.addArguments("--headless=new");
                    // opt360.addArguments("--remote-debugging-port=9222");
                    // The above 2 options not work at all, show warning message
                    System.out.println("[WARN] 360 Browser does not support headless mode. Running in headed mode.");
                }
                // opt360.addArguments("--no-sandbox", "--disable-dev-shm-usage", "window-size=1920,1080");
                return new ChromeDriver(opt360);
            case FIREFOX:
                FirefoxOptions ffOpts = new FirefoxOptions();
                if (isHeadless()) {
                    ffOpts.addArguments("--headless");
                }
                return new FirefoxDriver(ffOpts);
            case EDGE:
                EdgeOptions edgeOptions = new EdgeOptions();
                if (isHeadless()) {
                    edgeOptions.addArguments("--headless=new");
                }
                return new EdgeDriver(edgeOptions);
            case SAFARI:
                return new SafariDriver();
            case CHROME_RESPONSIVE:
            String isCI = System.getenv("CI");
                if (isCI == null || !isCI.equals("true")) {
                    // Local Windows development
                    System.setProperty("webdriver.chrome.driver", "D:/chromedrivers/138.0.7204.50/chromedriver-win64/chromedriver.exe");
                }

                ChromeOptions mobileOpts = new ChromeOptions();
                mobileOpts.setExperimentalOption("mobileEmulation", Map.of("deviceName", "Pixel 2"));
                mobileOpts.addArguments("--window-size=375,667"); // iPhone X resolution
                if (isHeadless()) {
                    mobileOpts.addArguments("--headless=new");
                }
                return new ChromeDriver(mobileOpts);
            case ANDROID_REAL:
                return createAndroidDriver();
            case IOS_REAL:
                return createIOSDriver();
            default:
                throw new UnsupportedOperationException("Unsupported browser: " + type);
        }
    }

    private static WebDriver createAndroidDriver() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Android Device");
        caps.setCapability("browserName", "Chrome");
        caps.setCapability("automationName", "UiAutomator2");
        // D:\chromedrivers\124.0.6367.207
        caps.setCapability("chromedriverExecutable", "D:\\chromedrivers\\124.0.6367.207\\chromedriver.exe");

        caps.setCapability("systemPort", 8300);

        try {
            return new AndroidDriver(new URI("http://localhost:4723/").toURL(), caps);
        } catch (MalformedURLException | URISyntaxException e) { // Added URISyntaxException
            throw new RuntimeException("Invalid Appium server URL", e);
        }
    }

    private static WebDriver createIOSDriver() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("deviceName", "iPhone");
        caps.setCapability("platformVersion", "16.0");
        caps.setCapability("browserName", "Safari");
        caps.setCapability("automationName", "XCUITest");

        try {
            return new IOSDriver(new URI("http://localhost:4723/").toURL(), caps);
        } catch (MalformedURLException | URISyntaxException e) { // Added URISyntaxException
            throw new RuntimeException("Invalid Appium server URL", e);
        }
    }

    private static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless", "true"));
    }
}
