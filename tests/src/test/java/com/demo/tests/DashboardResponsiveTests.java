package com.demo.tests;

import com.demo.base.BaseWebTest;
import com.demo.config.Config;
import com.demo.drivers.BrowserType;
import com.demo.pages.DashboardPage;

import io.qameta.allure.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Responsive Design")
@Feature("Dashboard Sidebar")
@Tag("responsive")
@DisplayName("Dashboard Responsive Behavior Tests")
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
    @Story("Sidebar Toggle")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("1. Sidebar should toggle and support responsive behavior")
    @Description("Verifies that sidebar in dashboard expands by default, collapses on toggle, and adapts responsively.")
    public void testSidebarToggleAndResponsive() {
        step("Login via login.html");
        driver.get(Config.getBaseUrl() + "login.html");
        waitUntilPageReady();

        driver.findElement(By.id("email")).sendKeys("eve.holt@reqres.in");
        driver.findElement(By.id("password")).sendKeys("cityslicka");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(d -> d.findElement(By.id("msg")).getText().equals("Login successful!"));

        step("Open dashboard and verify sidebar behavior");
        DashboardPage dashboardPage = new DashboardPage(driver);
        dashboardPage.open(Config.getBaseUrl());

        wait.until(ExpectedConditions.visibilityOf(dashboardPage.getSidebar()));

        assertTrue(!dashboardPage.isSidebarCollapsed(), "Sidebar should be expanded initially");

        step("Toggle sidebar");
        dashboardPage.toggleSidebar();

        assertTrue(dashboardPage.isSidebarCollapsed(), "Sidebar should be collapsed after toggle");

        step("Check sidebar width for responsiveness");
        String sidebarWidth = dashboardPage.getSidebarWidth();
        System.out.println("Sidebar width: " + sidebarWidth);
        assertTrue(sidebarWidth.equals("100%") || sidebarWidth.endsWith("px"));
    }

    @Step("Wait for page to be fully loaded")
    private void waitUntilPageReady() {
        wait.until(d -> ((JavascriptExecutor) d)
            .executeScript("return document.readyState").equals("complete"));
    }

    @Step("{0}")
    private void step(String action) {
        // Allure step marker
    }
}
