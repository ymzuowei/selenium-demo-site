package com.demo.tests;

import com.demo.base.BaseWebTest;
import com.demo.config.Config;
import com.demo.drivers.BrowserType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("responsive")
public class CheckoutResponsiveTests extends BaseWebTest {

    @Override
    protected BrowserType getBrowserType() {
        String browser = System.getProperty("browser", "CHROME").toUpperCase();

        if (BrowserType.isRealDevice(browser)) {
            return BrowserType.valueOf(browser);
        } else {
            return BrowserType.CHROME_RESPONSIVE;
        }
    }

    @Test
    public void testCheckoutResponsiveLayout() {
        try {
            driver.get(Config.getBaseUrl() + "product.html");
            wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
            WebElement card = driver.findElement(By.className("card"));
            assertTrue(card.isDisplayed());
        } finally {
            driver.quit();
        }
    }
}
