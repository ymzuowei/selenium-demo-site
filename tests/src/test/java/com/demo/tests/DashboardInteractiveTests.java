package com.demo.tests;

import com.demo.base.BaseTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class DashboardInteractiveTests extends BaseTest {

    public void doLogin(String email, String password) {
        driver.get("http://localhost:8080/login.html");
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(d -> d.findElement(By.id("msg")).getText().equals("Login successful!"));
    }

    @BeforeEach
    public void startTests(TestInfo testInfo) {
        if (!testInfo.getDisplayName().equals("testDashboardRedirectsToLoginIfNotLoggedIn()")) {
            doLogin("eve.holt@reqres.in", "cityslicka");
        }
        driver.get("http://localhost:8080/dashboard.html");
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
    }

    @AfterEach
    public void endTests() {
        driver.manage().deleteAllCookies();
    }

    @Test
    @DisplayName("1. Sidebar should toggle and support responsive behavior")
    public void testSidebarToggleAndResponsive() throws Exception {
        WebDriver mobileDriver = null;
        Path tempMobileUserDataDir = null;
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("window-size=375,667");
            options.addArguments("--remote-debugging-port=0");

            // Create unique temp profile for mobile driver
            tempMobileUserDataDir = Files.createTempDirectory("chrome-mobile-user-data");
            options.addArguments("--user-data-dir=" + tempMobileUserDataDir.toAbsolutePath());

            mobileDriver = new ChromeDriver(options);
            WebDriverWait mobileWait = new WebDriverWait(mobileDriver, Duration.ofSeconds(10));

            // Login
            mobileDriver.get("http://localhost:8080/login.html");
            mobileWait.until(d -> ((JavascriptExecutor) d)
                    .executeScript("return document.readyState").equals("complete"));
            mobileDriver.findElement(By.id("email")).sendKeys("eve.holt@reqres.in");
            mobileDriver.findElement(By.id("password")).sendKeys("cityslicka");
            mobileDriver.findElement(By.cssSelector("button[type='submit']")).click();
            mobileWait.until(d -> d.findElement(By.id("msg")).getText().equals("Login successful!"));

            // Navigate to dashboard
            mobileDriver.get("http://localhost:8080/dashboard.html");
            mobileWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sidebar")));

            WebElement sidebar = mobileDriver.findElement(By.id("sidebar"));
            WebElement toggleBtn = mobileDriver.findElement(By.id("sidebarToggle"));

            assertFalse(sidebar.getAttribute("class").contains("collapsed"));

            toggleBtn.click();
            assertTrue(sidebar.getAttribute("class").contains("collapsed"));

            String sidebarWidth = sidebar.getCssValue("width");
            System.out.println("Sidebar width: " + sidebarWidth);
            assertTrue(sidebarWidth.equals("100%") || sidebarWidth.endsWith("px"));

        } finally {
            if (mobileDriver != null) {
                mobileDriver.quit();
            }
            if (tempMobileUserDataDir != null) {
                try {
                    Files.walk(tempMobileUserDataDir)
                            .sorted((a, b) -> b.compareTo(a)) // delete children before parents
                            .forEach(p -> {
                                try {
                                    Files.delete(p);
                                } catch (Exception ignored) {}
                            });
                } catch (Exception ignored) {}
            }
        }
    }

    @Test
    public void testCardExpandCollapse() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("card")));

        WebElement card1Header = driver.findElement(By.cssSelector("#card1 .card-header"));
        WebElement card1Content = driver.findElement(By.cssSelector("#card1 .card-content"));
        WebElement arrow = card1Header.findElement(By.className("arrow"));

        assertFalse(card1Content.isDisplayed());

        card1Header.click();
        assertTrue(card1Content.isDisplayed());
        assertTrue(arrow.getAttribute("class").contains("down"));

        card1Header.click();
        assertFalse(card1Content.isDisplayed());
        assertFalse(arrow.getAttribute("class").contains("down"));
    }

    @Test
    public void testUserMenuAndLogout() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userBtn")));

        WebElement userBtn = driver.findElement(By.id("userBtn"));
        WebElement userDropdown = driver.findElement(By.id("userDropdown"));

        assertFalse(userDropdown.isDisplayed());

        userBtn.click();
        assertTrue(userDropdown.isDisplayed());

        WebElement logoutBtn = driver.findElement(By.id("logoutBtn"));
        logoutBtn.click();

        wait.until(ExpectedConditions.urlContains("login.html"));
        assertTrue(driver.getCurrentUrl().contains("login.html"));

        assertNull(driver.manage().getCookieNamed("auth_token"));
    }

    @Test
    public void testSettingsButtonAlert() {
        WebElement cardContent = driver.findElement(By.cssSelector("#card3 .card-content"));
        WebElement cardHeader = driver.findElement(By.cssSelector("#card3"));
        if (!cardContent.isDisplayed()) {
            cardHeader.click();
            wait.until(ExpectedConditions.visibilityOf(cardContent));
        }

        // List<WebElement> elements = driver.findElements(By.id("settingsBtn"));
        // System.out.println("settingsBtn count: " + elements.size());
        // if (!elements.isEmpty()) {
        //     WebElement btn = elements.get(0);
        //     System.out.println("Displayed? " + btn.isDisplayed());
        //     System.out.println("CSS display: " + btn.getCssValue("display"));
        //     System.out.println("CSS visibility: " + btn.getCssValue("visibility"));
        // }

        try {
            WebElement settingsBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("settingsBtn")));
            settingsBtn.click();
        } catch (Exception e) {
            throw e;
        }

        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Settings clicked!", alert.getText());
        alert.accept();
    }

    @Test
    public void testDashboardRedirectsToLoginIfNotLoggedIn() {
        // Wait for redirect to login.html
        wait.until(ExpectedConditions.urlContains("login.html"));

        // Assert current URL contains "login.html"
        assertTrue(driver.getCurrentUrl().contains("login.html"),
                "Accessing dashboard without login should redirect to login page");
    }
}
