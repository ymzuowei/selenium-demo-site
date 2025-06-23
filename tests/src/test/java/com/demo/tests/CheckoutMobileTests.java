package com.demo.tests;

import com.demo.base.BaseMobileTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckoutMobileTests extends BaseMobileTest {

    @Test
    public void testCheckoutResponsiveLayout() {
        try {
            driver.get("http://localhost:8080/product.html");
            wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
            WebElement card = driver.findElement(By.className("card"));
            assertTrue(card.isDisplayed());
        } finally {
            driver.quit();
        }
    }
}
