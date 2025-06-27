package com.demo.tests;

import com.demo.base.BaseWebTest;
import com.demo.pages.CartPage;
import com.demo.pages.ProductPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Epic("E-commerce")
@Feature("Shopping Cart")
@DisplayName("Cart Tests")
public class CartTests extends BaseWebTest {

    private ProductPage productPage;
    private CartPage cartPage;

    @BeforeEach
    @Step("Load product page and initialize page objects")
    public void loadProductPage() {
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        productPage.open();
    }

    @AfterEach
    @Step("Clear cart state after each test")
    public void clearCart() {
        ((JavascriptExecutor) driver).executeScript("localStorage.clear();");
        driver.manage().deleteAllCookies();
    }

    @Step("Handle alert if present")
    public void handleAlertIfPresent() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.alertIsPresent())
                    .accept();
        } catch (TimeoutException ignored) {}
    }

    @Test
    @Story("Cart Calculation")
    @DisplayName("Single item subtotal should be price × quantity")
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
    @Story("Cart Calculation")
    @DisplayName("Multiple product quantities should update subtotal and total correctly")
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
    @Story("Cart Persistence")
    @DisplayName("Quantity dropdown should persist after reload")
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
    @Story("Cart Calculation")
    @DisplayName("Updating quantity updates subtotal and total")
    public void testUpdateQuantityUpdatesSubtotalAndTotal() {
        productPage.addToCart(0);
        handleAlertIfPresent();
        cartPage.open();

        WebElement row = cartPage.getCartRows().get(0);
        cartPage.changeQuantity(row, 3);

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("total"), "$"));

        WebElement refreshedRow = cartPage.getCartRows().get(0);
        double price = cartPage.getPrice(refreshedRow);
        double subtotal = cartPage.getSubtotal(refreshedRow);
        double total = cartPage.getDisplayedTotal();

        assertEquals(price * 3, subtotal, 0.01);
        assertEquals(subtotal, total, 0.01);
    }

    @Test
    @Story("Cart Modification")
    @DisplayName("Removing item should empty the cart")
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
    @Story("Cart UI")
    @DisplayName("Cart shows empty state when there are no items")
    public void testCartEmptyState() {
        cartPage.open();
        assertEquals(0, cartPage.getCartRows().size());
    }

    @Test
    @Story("Cart Navigation")
    @DisplayName("Continue shopping link should go to product page")
    public void testContinueShoppingLink() {
        cartPage.open();
        cartPage.getContinueShoppingLink().click();
        wait.until(ExpectedConditions.urlContains("product.html"));
        assertTrue(driver.getCurrentUrl().endsWith("product.html"));
    }

    @Test
    @Story("Checkout Flow")
    @DisplayName("Checkout redirects to login if not authenticated")
    public void testCheckoutButtonGoesToLoginIfNotLoggedIn() {
        productPage.addToCart(0);
        handleAlertIfPresent();
        cartPage.open();
        cartPage.getCheckoutButton().click();
        wait.until(ExpectedConditions.urlContains("login.html"));
        assertTrue(driver.getCurrentUrl().contains("login.html"));
    }
}
