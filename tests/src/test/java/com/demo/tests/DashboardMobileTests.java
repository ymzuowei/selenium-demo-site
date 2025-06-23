package com.demo.tests;

import com.demo.base.BaseMobileTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;

public class DashboardMobileTests extends BaseMobileTest {

    @Test
    @DisplayName("1. Sidebar should toggle and support responsive behavior")
    public void testSidebarToggleAndResponsive() {
        // Login
        driver.get("http://localhost:8080/login.html");
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
        driver.findElement(By.id("email")).sendKeys("eve.holt@reqres.in");
        driver.findElement(By.id("password")).sendKeys("cityslicka");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(d -> d.findElement(By.id("msg")).getText().equals("Login successful!"));

        // Navigate to dashboard
        driver.get("http://localhost:8080/dashboard.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sidebar")));

        WebElement sidebar = driver.findElement(By.id("sidebar"));
        WebElement toggleBtn = driver.findElement(By.id("sidebarToggle"));

        assertFalse(sidebar.getAttribute("class").contains("collapsed"));

        toggleBtn.click();
        assertTrue(sidebar.getAttribute("class").contains("collapsed"));

        String sidebarWidth = sidebar.getCssValue("width");
        System.out.println("Sidebar width: " + sidebarWidth);
        assertTrue(sidebarWidth.equals("100%") || sidebarWidth.endsWith("px"));
    }
}
