package com.demo.tests;

import com.demo.base.BaseWebTest;
import com.demo.config.Config;
import io.qameta.allure.*;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Checkout")
@Feature("Order Placement")
@DisplayName("Checkout Flow Tests")
public class CheckoutTests extends BaseWebTest {

    @BeforeEach
    @Step("Load product page before each test")
    public void loadProductPage() {
        driver.get(Config.getBaseUrl() + "product.html");
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
    }

    @AfterEach
    @Step("Clear local storage and cookies")
    public void clearStorage() {
        ((JavascriptExecutor) driver).executeScript("localStorage.clear();");
        driver.manage().deleteAllCookies();
    }

    @Step("Handle alert if present")
    public void handleAlertIfPresent(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            System.out.println("Alert text: " + alert.getText());
            alert.accept();
        } catch (TimeoutException e) {
            // No alert
        }
    }

    @Step("Login with email: {0}")
    public void doLogin(String email, String password) {
        driver.get(Config.getBaseUrl() + "login.html");
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(d -> d.findElement(By.id("msg")).getText().equals("Login successful!"));
    }

    @Test
    @Story("Redirect if not logged in")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Redirect to login when accessing checkout without authentication")
    @Description("User should be redirected to login.html when trying to access checkout.html without being logged in.")
    public void testCheckoutRedirectsToLoginIfNotLoggedIn() {
        driver.get(Config.getBaseUrl() + "checkout.html");
        wait.until(ExpectedConditions.urlContains("login.html"));
        assertTrue(driver.getCurrentUrl().contains("login.html"));
    }

    @Test
    @Story("Place order")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Complete checkout with valid data")
    @Description("Logs in the user, adds a product to cart, completes checkout, and verifies confirmation.")
    public void testProceedToCheckout() {
        driver.findElement(By.cssSelector(".add-to-cart")).click();
        handleAlertIfPresent(driver);

        doLogin("eve.holt@reqres.in", "cityslicka");

        driver.get(Config.getBaseUrl() + "cart.html");
        driver.findElement(By.id("checkoutBtn")).click();

        wait.until(ExpectedConditions.urlContains("checkout.html"));
        driver.findElement(By.id("address")).sendKeys("123 Demo St");
        driver.findElement(By.cssSelector("input[value='wechat']")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("confirmation.html"));

        String lastOrder = (String) ((JavascriptExecutor) driver)
                .executeScript("return localStorage.getItem('lastOrder');");
        assertNotNull(lastOrder);
    }

    @Test
    @Story("Confirmation page")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Display submitted order on confirmation page")
    @Description("Verifies that the confirmation page displays correct order data from localStorage.")
    public void testConfirmationDisplaysOrder() {
        String mockOrder = "{" +
                "\"address\":\"123 Demo St\"," +
                "\"payment\":\"wechat\"," +
                "\"items\":[{" +
                "\"name\":\"Test Product\"," +
                "\"price\":10.0," +
                "\"quantity\":2}]" +
                "}";

        ((JavascriptExecutor) driver).executeScript("localStorage.setItem('lastOrder', arguments[0]);", mockOrder);
        driver.get(Config.getBaseUrl() + "confirmation.html");

        WebElement addr = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("address")));
        WebElement pay = driver.findElement(By.id("payment"));

        assertTrue(addr.getText().contains("123 Demo St"));
        assertTrue(pay.getText().toLowerCase().contains("wechat"));
    }

    @Test
    @Story("Form validation")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Checkout form validation should prevent empty submissions")
    @Description("Ensures users cannot proceed without filling out address and payment fields.")
    public void testCheckoutFailsWithEmptyFields() {
        doLogin("eve.holt@reqres.in", "cityslicka");
        driver.get(Config.getBaseUrl() + "checkout.html");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertFalse(driver.getCurrentUrl().contains("confirmation.html"));
    }

    @Test
    @Story("Empty confirmation state")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Show empty message if no order exists")
    @Description("Confirmation page should indicate when there’s no order stored.")
    public void testConfirmationShowsNoOrderIfEmpty() {
        ((JavascriptExecutor) driver).executeScript("localStorage.removeItem('lastOrder');");
        driver.get(Config.getBaseUrl() + "confirmation.html");

        WebElement noOrder = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noOrderMsg")));
        assertTrue(noOrder.isDisplayed());
    }
}
