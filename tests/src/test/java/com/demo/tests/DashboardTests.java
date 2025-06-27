package com.demo.tests;

import com.demo.base.BaseWebTest;
import com.demo.config.Config;
import io.qameta.allure.*;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Dashboard")
@Feature("UI Behavior and Auth Redirect")
@DisplayName("Dashboard Functional Tests")
public class DashboardTests extends BaseWebTest {

    @Step("Perform login with email: {0}")
    public void doLogin(String email, String password) {
        driver.get(Config.getBaseUrl() + "login.html");
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(d -> d.findElement(By.id("msg")).getText().equals("Login successful!"));
    }

    @BeforeEach
    @Step("Before each test: login and open dashboard unless testing unauthenticated flow")
    public void startTests(TestInfo testInfo) {
        if (!testInfo.getDisplayName().equals("testDashboardRedirectsToLoginIfNotLoggedIn()")) {
            doLogin("eve.holt@reqres.in", "cityslicka");
        }
        driver.get(Config.getBaseUrl() + "dashboard.html");
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
    }

    @AfterEach
    @Step("Clear session cookies")
    public void endTests() {
        driver.manage().deleteAllCookies();
    }

    @Test
    @Story("Collapsible Cards")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Expand and collapse a card section on dashboard")
    @Description("Ensures the UI card toggle behavior expands and collapses content as expected.")
    public void testCardExpandCollapse() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("card")));

        dummyStep();

        WebElement card1Header = driver.findElement(By.cssSelector("#card1 .card-header"));
        WebElement card1Content = driver.findElement(By.cssSelector("#card1 .card-content"));
        WebElement arrow = card1Header.findElement(By.className("arrow"));

        assertFalse(card1Content.isDisplayed(), "Card content should be initially hidden");

        card1Header.click();
        assertTrue(card1Content.isDisplayed(), "Card content should be visible after click");
        assertTrue(arrow.getAttribute("class").contains("down"), "Arrow should indicate expanded state");

        card1Header.click();
        assertFalse(card1Content.isDisplayed(), "Card content should be hidden after second click");
        assertFalse(arrow.getAttribute("class").contains("down"), "Arrow should indicate collapsed state");
    }

    @Test
    @Story("User Menu")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("User menu should show dropdown and support logout")
    @Description("Validates the user dropdown visibility and proper logout redirect behavior.")
    public void testUserMenuAndLogout() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userBtn")));

        WebElement userBtn = driver.findElement(By.id("userBtn"));
        WebElement userDropdown = driver.findElement(By.id("userDropdown"));

        assertFalse(userDropdown.isDisplayed(), "User dropdown should be hidden initially");

        userBtn.click();
        assertTrue(userDropdown.isDisplayed(), "User dropdown should be visible after click");

        WebElement logoutBtn = driver.findElement(By.id("logoutBtn"));
        logoutBtn.click();

        wait.until(ExpectedConditions.urlContains("login.html"));
        assertTrue(driver.getCurrentUrl().contains("login.html"), "After logout, URL should contain login.html");

        assertNull(driver.manage().getCookieNamed("auth_token"), "Auth token cookie should be cleared after logout");
    }

    @Test
    @Story("Settings Interaction")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Settings button should trigger alert in expanded card")
    @Description("Expands the card containing settings button and verifies alert behavior.")
    public void testSettingsButtonAlert() {
        WebElement cardHeader = driver.findElement(By.cssSelector("#card3 .card-header"));
        WebElement cardContent = driver.findElement(By.cssSelector("#card3 .card-content"));

        if (!cardContent.isDisplayed()) {
            cardHeader.click();
            wait.until(ExpectedConditions.visibilityOf(cardContent));
        }

        WebElement settingsBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("settingsBtn")));
        settingsBtn.click();

        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Settings clicked!", alert.getText());
        alert.accept();
    }

    @Test
    @Story("Authentication Redirect")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Redirect to login if accessing dashboard unauthenticated")
    @Description("Checks that accessing the dashboard without being logged in redirects to the login page.")
    public void testDashboardRedirectsToLoginIfNotLoggedIn() {
        // Clear cookies and storage before test
        driver.manage().deleteAllCookies();
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
        ((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");

        // Visit dashboard.html
        driver.get(Config.getBaseUrl() + "dashboard.html");

        // Redirect to login.html
        wait.until(ExpectedConditions.urlContains("login.html"));

        assertTrue(driver.getCurrentUrl().contains("login.html"),
                "Accessing dashboard without login should redirect to login page");
    }

    @Test
    @Story("Smoke")
    @Severity(SeverityLevel.NORMAL)
    @Description("Dummy check")
    public void dummyTest() {
        assertTrue(true);
    }

    @Step("Manually invoked Allure step for verification")
    private void dummyStep() {
        System.out.println("Inside dummy step");
    }
}
