package com.demo.pages;

import org.openqa.selenium.*;

public class CheckoutPage {
    private WebDriver driver;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("http://localhost:8080/checkout.html");
    }

    public WebElement getOrderSummary() {
        return driver.findElement(By.id("order-summary"));
    }

    public void placeOrder() {
        driver.findElement(By.id("place-order-btn")).click();
    }

    public String getSuccessMessage() {
        return driver.findElement(By.id("order-success-msg")).getText();
    }
}
