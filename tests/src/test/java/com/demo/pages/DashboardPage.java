package com.demo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DashboardPage {
    private final WebDriver driver;

    // Locators
    private final By sidebar = By.id("sidebar");
    private final By sidebarToggleButton = By.id("sidebarToggle");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    // Navigate to dashboard URL
    public void open(String baseUrl) {
        driver.get(baseUrl + "dashboard.html");
    }

    // Get sidebar element
    public WebElement getSidebar() {
        return driver.findElement(sidebar);
    }

    // Get sidebar toggle button
    public WebElement getSidebarToggleButton() {
        return driver.findElement(sidebarToggleButton);
    }

    // Check if sidebar is collapsed
    public boolean isSidebarCollapsed() {
        String classAttr = getSidebar().getAttribute("class");
        return classAttr != null && classAttr.contains("collapsed");
    }

    // Toggle sidebar
    public void toggleSidebar() {
        getSidebarToggleButton().click();
    }

    // Get sidebar width (for responsive checks)
    public String getSidebarWidth() {
        return getSidebar().getCssValue("width");
    }
}
