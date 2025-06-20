package com.demo.tests;

import com.demo.base.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CartTests extends BaseTest {

    @BeforeEach
    public void loadProductPage() {
        driver.get("http://localhost:8080/product.html");
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
    }

    @AfterEach
    public void clearCart() {
        ((JavascriptExecutor) driver).executeScript("localStorage.clear();");
        driver.manage().deleteAllCookies();
    }

    public void handleAlertIfPresent() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.alertIsPresent())
                .accept();
        } catch (TimeoutException ignored) {}
    }

    @Test
    public void testSingleItemSubtotalCalculation() {
        driver.findElement(By.cssSelector(".add-to-cart")).click();
        handleAlertIfPresent();
        driver.get("http://localhost:8080/cart.html");

        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#cart-table tbody tr")));
        Select quantity = new Select(row.findElement(By.cssSelector(".quantity select")));

        String priceText = row.findElement(By.className("price")).getText().replace("$", "");
        String subtotalText = row.findElement(By.className("subtotal")).getText().replace("$", "");

        double price = Double.parseDouble(priceText);
        double subtotal = Double.parseDouble(subtotalText);
        int qty = Integer.parseInt(quantity.getFirstSelectedOption().getText());

        assertEquals(qty * price, subtotal, 0.01);
    }

    @Test
    public void testMultipleProductsInCart() {
        driver.get("http://localhost:8080/product.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("product-grid")));

        List<WebElement> addToCartButtons = driver.findElements(By.cssSelector(".add-to-cart"));
        assertTrue(addToCartButtons.size() >= 2, "Need at least two products");

        // Add first product (e.g., Product A) 3 times
        for (int i = 0; i < 3; i++) {
            addToCartButtons.get(0).click();
            handleAlertIfPresent();
        }

        // Add second product (e.g., Product B) 2 times
        for (int i = 0; i < 2; i++) {
            addToCartButtons.get(1).click();
            handleAlertIfPresent();
        }

        driver.get("http://localhost:8080/cart.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cart-table")));

        List<WebElement> rows = driver.findElements(By.cssSelector("#cart-table tbody tr"));
        assertEquals(2, rows.size(), "There should be 2 unique products in cart");

        double calculatedTotal = 0.0;

        for (WebElement row : rows) {
            String name = row.findElement(By.cssSelector("td:first-child")).getText();
            double price = Double.parseDouble(row.findElement(By.className("price")).getText().replace("$", "").trim());
            double subtotal = Double.parseDouble(row.findElement(By.className("subtotal")).getText().replace("$", "").trim());
            int quantity = Integer.parseInt(new Select(row.findElement(By.cssSelector(".quantity select")))
                    .getFirstSelectedOption().getText());

            // Assert quantity matches what we added
            if (name.equals("Product A")) {
                assertEquals(3, quantity, "Product A should have quantity 3");
            } else if (name.equals("Product B")) {
                assertEquals(2, quantity, "Product B should have quantity 2");
            } else {
                fail("Unexpected product in cart: " + name);
            }

            assertEquals(price * quantity, subtotal, 0.01, "Subtotal should match quantity × price");
            calculatedTotal += subtotal;
        }

        String totalText = driver.findElement(By.id("total")).getText().replace("$", "").trim();
        double displayedTotal = Double.parseDouble(totalText);
        assertEquals(calculatedTotal, displayedTotal, 0.01, "Total should match sum of subtotals");
    }

    @Test
    public void testQuantityDropdownPersistenceAfterReload() {
        driver.findElement(By.cssSelector(".add-to-cart")).click();
        handleAlertIfPresent();

        driver.get("http://localhost:8080/cart.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cart-table")));

        WebElement select = driver.findElement(By.cssSelector(".quantity select"));
        new Select(select).selectByValue("3");

        driver.navigate().refresh();
        WebElement refreshedSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".quantity select")));
        String selected = new Select(refreshedSelect).getFirstSelectedOption().getText();

        assertEquals("3", selected);
    }

    @Test
    public void testUpdateQuantityUpdatesSubtotalAndTotal() {
        driver.findElement(By.cssSelector(".add-to-cart")).click();
        handleAlertIfPresent();
        driver.get("http://localhost:8080/cart.html");

        WebElement quantitySelect = wait.until(ExpectedConditions
            .visibilityOfElementLocated(By.cssSelector(".quantity select")));
        new Select(quantitySelect).selectByValue("3");

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".subtotal"), "$"));

        String priceStr = driver.findElement(By.className("price")).getText().replace("$", "");
        String subtotalStr = driver.findElement(By.className("subtotal")).getText().replace("$", "");
        String totalStr = driver.findElement(By.id("total")).getText().replace("$", "");

        double price = Double.parseDouble(priceStr);
        double subtotal = Double.parseDouble(subtotalStr);
        double total = Double.parseDouble(totalStr);

        assertEquals(price * 3, subtotal, 0.01);
        assertEquals(subtotal, total, 0.01);
    }

    @Test
    public void testRemoveItemEmptiesCart() {
        driver.findElement(By.cssSelector(".add-to-cart")).click();
        handleAlertIfPresent();
        driver.get("http://localhost:8080/cart.html");

        WebElement deleteBtn = wait.until(ExpectedConditions
            .elementToBeClickable(By.cssSelector("button[onclick^='removeItem']")));
        deleteBtn.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#cart-table tbody tr")));

        String totalText = driver.findElement(By.id("total")).getText();
        assertTrue(totalText.equals("$0") || totalText.equals(""));
    }

    @Test
    public void testCartEmptyState() {
        driver.get("http://localhost:8080/cart.html");
        WebElement tableBody = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#cart-table tbody")));
        List<WebElement> rows = tableBody.findElements(By.tagName("tr"));
        assertEquals(0, rows.size(), "Cart should be empty");
    }

    @Test
    public void testContinueShoppingLink() {
        driver.get("http://localhost:8080/cart.html");
        WebElement continueLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Continue Shopping")));
        continueLink.click();

        wait.until(ExpectedConditions.urlContains("product.html"));
        assertTrue(driver.getCurrentUrl().endsWith("product.html"));
    }

    @Test
    public void testCheckoutButtonGoesToLoginIfNotLoggedIn() {
        driver.findElement(By.cssSelector(".add-to-cart")).click();
        handleAlertIfPresent();
        driver.get("http://localhost:8080/cart.html");

        driver.findElement(By.id("checkoutBtn")).click();
        wait.until(d -> d.getCurrentUrl().contains("login.html"));

        assertTrue(driver.getCurrentUrl().contains("login.html"), "Should redirect to login page");
    }

}
