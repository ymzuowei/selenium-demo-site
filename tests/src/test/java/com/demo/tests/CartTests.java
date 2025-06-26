package com.demo.tests;

import com.demo.base.BaseWebTest;
import com.demo.pages.CartPage;
import com.demo.pages.ProductPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CartTests extends BaseWebTest {

    private ProductPage productPage;
    private CartPage cartPage;

    @BeforeEach
    public void loadProductPage() {
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        productPage.open();
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
        productPage.addToCart(0);
        handleAlertIfPresent();
        cartPage.open();

        WebElement row = cartPage.getCartRows().get(0);
        double price = cartPage.getPrice(row);
        double subtotal = cartPage.getSubtotal(row);
        int qty = cartPage.getSelectedQuantity(row);

        assertEquals(qty * price, subtotal, 0.01);
    }

    @Test
    public void testMultipleProductsInCart() {
        for (int i = 0; i < 3; i++) {
            productPage.addToCart(0);
            handleAlertIfPresent();
        }
        for (int i = 0; i < 2; i++) {
            productPage.addToCart(1);
            handleAlertIfPresent();
        }

        cartPage.open();
        List<WebElement> rows = cartPage.getCartRows();
        assertEquals(2, rows.size());

        double calculatedTotal = 0.0;

        for (WebElement row : rows) {
            String name = cartPage.getItemName(row);
            double price = cartPage.getPrice(row);
            int quantity = cartPage.getSelectedQuantity(row);
            double subtotal = cartPage.getSubtotal(row);

            if (name.equals("Product A")) {
                assertEquals(3, quantity);
            } else if (name.equals("Product B")) {
                assertEquals(2, quantity);
            } else {
                fail("Unexpected product: " + name);
            }

            assertEquals(price * quantity, subtotal, 0.01);
            calculatedTotal += subtotal;
        }

        assertEquals(calculatedTotal, cartPage.getDisplayedTotal(), 0.01);
    }

    @Test
    public void testQuantityDropdownPersistenceAfterReload() {
        productPage.addToCart(0);
        handleAlertIfPresent();
        cartPage.open();

        WebElement row = cartPage.getCartRows().get(0);
        cartPage.changeQuantity(row, 3);
        driver.navigate().refresh();

        row = cartPage.getCartRows().get(0);
        int selected = cartPage.getSelectedQuantity(row);
        assertEquals(3, selected);
    }

    @Test
    public void testUpdateQuantityUpdatesSubtotalAndTotal() {
        productPage.addToCart(0);
        handleAlertIfPresent();
        cartPage.open();

        WebElement row = cartPage.getCartRows().get(0);
        cartPage.changeQuantity(row, 3);

        // wait subtotal or total refresh and get the element again
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("total"), "$"));

        WebElement refreshedRow = cartPage.getCartRows().get(0);
        double price = cartPage.getPrice(refreshedRow);
        double subtotal = cartPage.getSubtotal(refreshedRow);
        double total = cartPage.getDisplayedTotal();

        assertEquals(price * 3, subtotal, 0.01);
        assertEquals(subtotal, total, 0.01);
    }

    @Test
    public void testRemoveItemEmptiesCart() {
        productPage.addToCart(0);
        handleAlertIfPresent();
        cartPage.open();

        WebElement row = cartPage.getCartRows().get(0);
        cartPage.removeItem(row);

        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.invisibilityOf(row));

        assertEquals(0.0, cartPage.getDisplayedTotal(), 0.01);
    }

    @Test
    public void testCartEmptyState() {
        cartPage.open();
        assertEquals(0, cartPage.getCartRows().size());
    }

    @Test
    public void testContinueShoppingLink() {
        cartPage.open();
        cartPage.getContinueShoppingLink().click();
        wait.until(ExpectedConditions.urlContains("product.html"));
        assertTrue(driver.getCurrentUrl().endsWith("product.html"));
    }

    @Test
    public void testCheckoutButtonGoesToLoginIfNotLoggedIn() {
        productPage.addToCart(0);
        handleAlertIfPresent();
        cartPage.open();
        cartPage.getCheckoutButton().click();
        wait.until(ExpectedConditions.urlContains("login.html"));
        assertTrue(driver.getCurrentUrl().contains("login.html"));
    }
}
