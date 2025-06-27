package com.demo.tests;

import com.demo.base.BaseWebTest;
import com.demo.config.Config;
import com.demo.drivers.BrowserType;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Responsive Design")
@Feature("Checkout Page Layout")
@Tag("responsive")
@DisplayName("Checkout Responsive Tests")
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
    @Story("Layout Adaptation")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Ensure checkout layout renders correctly on responsive devices")
    @Description("Verifies that the product page layout renders properly in responsive or mobile-simulated browsers.")
    public void testCheckoutResponsiveLayout() {
        step("Navigate to product page");
        driver.get(Config.getBaseUrl() + "product.html");

        step("Wait for document readiness");
        wait.until(d -> ((JavascriptExecutor) d)
            .executeScript("return document.readyState").equals("complete"));

        step("Verify card layout is displayed");
        WebElement card = driver.findElement(By.className("card"));
        assertTrue(card.isDisplayed());
    }

    @Step("{0}")
    private void step(String message) {
        // No-op to record custom Allure steps
    }
}
