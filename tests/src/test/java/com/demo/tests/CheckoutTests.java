package com.demo.tests;

import com.demo.base.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutTests extends BaseTest {
    @BeforeEach
    public void loadProductPage() {
        driver.get("http://localhost:8080/product.html");
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
    }

    @AfterEach
    public void clearStorage() {
        ((JavascriptExecutor) driver).executeScript("localStorage.clear();");
        driver.manage().deleteAllCookies();
    }

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

    public void doLogin(String email, String password) {
        driver.get("http://localhost:8080/login.html");
        wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(d -> d.findElement(By.id("msg")).getText().equals("Login successful!"));
    }

    @Test
    public void testProceedToCheckout() {
        driver.findElement(By.cssSelector(".add-to-cart")).click();
        handleAlertIfPresent(driver);
        
        doLogin("eve.holt@reqres.in", "cityslicka");

        driver.get("http://localhost:8080/cart.html");
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
        driver.get("http://localhost:8080/confirmation.html");

        WebElement addr = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("address")));
        WebElement pay = driver.findElement(By.id("payment"));

        assertTrue(addr.getText().contains("123 Demo St"));
        assertTrue(pay.getText().toLowerCase().contains("wechat"));
    }

    @Test
    public void testProductResponsiveOnMobile() throws Exception {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "window-size=375,667");
        WebDriver mobileDriver = new ChromeDriver(options);
        WebDriverWait mobileWait = new WebDriverWait(mobileDriver, Duration.ofSeconds(10));

        try {
            mobileDriver.get("http://localhost:8080/product.html");
            mobileWait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
            WebElement card = mobileDriver.findElement(By.className("card"));
            assertTrue(card.isDisplayed());
        } finally {
            mobileDriver.quit();
        }
    }

    @Test
    public void testCheckoutFailsWithEmptyFields() {
        doLogin("eve.holt@reqres.in", "cityslicka");
        driver.get("http://localhost:8080/checkout.html");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Should not redirect to confirmation.html
        assertFalse(driver.getCurrentUrl().contains("confirmation.html"));
    }

    @Test
    public void testConfirmationShowsNoOrderIfEmpty() {
        ((JavascriptExecutor) driver).executeScript("localStorage.removeItem('lastOrder');");
        driver.get("http://localhost:8080/confirmation.html");

        WebElement noOrder = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noOrderMsg")));
        assertTrue(noOrder.isDisplayed());
    }

}
