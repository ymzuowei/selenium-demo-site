package com.demo.tests;

import com.demo.base.BaseWebTest;
import com.demo.config.Config;
import com.demo.drivers.BrowserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("responsive")
public class DashboardResponsiveTests extends BaseWebTest {

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
    @DisplayName("1. Sidebar should toggle and support responsive behavior")
    public void testSidebarToggleAndResponsive() {
        // Login
        driver.get(Config.getBaseUrl() + "login.html");
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
        driver.findElement(By.id("email")).sendKeys("eve.holt@reqres.in");
        driver.findElement(By.id("password")).sendKeys("cityslicka");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(d -> d.findElement(By.id("msg")).getText().equals("Login successful!"));

        // Navigate to dashboard
        driver.get(Config.getBaseUrl() + "dashboard.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sidebar")));

        WebElement sidebar = driver.findElement(By.id("sidebar"));
        WebElement toggleBtn = driver.findElement(By.id("sidebarToggle"));

        String classAttr = sidebar.getAttribute("class");
        assertTrue(classAttr != null && !classAttr.contains("collapsed"), 
            "Sidebar should be expanded initially, but class was: " + classAttr);

        toggleBtn.click();
        classAttr = sidebar.getAttribute("class");
        assertTrue(classAttr != null && classAttr.contains("collapsed"),
            "Sidebar should be collapsed after toggle, but class was: " + classAttr);

        String sidebarWidth = sidebar.getCssValue("width");
        System.out.println("Sidebar width: " + sidebarWidth);
        assertTrue(sidebarWidth.equals("100%") || sidebarWidth.endsWith("px"));
    }
}
