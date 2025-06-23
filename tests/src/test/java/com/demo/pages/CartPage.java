package com.demo.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class CartPage {
    private WebDriver driver;

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("http://localhost:8080/cart.html");
    }

    public List<WebElement> getCartRows() {
        return driver.findElements(By.cssSelector("#cart-table tbody tr"));
    }

    public double getDisplayedTotal() {
        String total = driver.findElement(By.id("total")).getText().replace("$", "").trim();
        return total.isEmpty() ? 0.0 : Double.parseDouble(total);
    }

    public void changeQuantity(WebElement row, int value) {
        Select select = new Select(row.findElement(By.cssSelector(".quantity select")));
        select.selectByValue(String.valueOf(value));
    }

    public int getSelectedQuantity(WebElement row) {
        Select select = new Select(row.findElement(By.cssSelector(".quantity select")));
        return Integer.parseInt(select.getFirstSelectedOption().getText());
    }

    public void removeItem(WebElement row) {
        row.findElement(By.tagName("button")).click();
    }

    public double getPrice(WebElement row) {
        return Double.parseDouble(row.findElement(By.className("price")).getText().replace("$", ""));
    }

    public double getSubtotal(WebElement row) {
        return Double.parseDouble(row.findElement(By.className("subtotal")).getText().replace("$", ""));
    }

    public String getItemName(WebElement row) {
        return row.findElement(By.cssSelector("td:first-child")).getText();
    }

    public WebElement getContinueShoppingLink() {
        return driver.findElement(By.linkText("Continue Shopping"));
    }

    public WebElement getCheckoutButton() {
        return driver.findElement(By.id("checkoutBtn"));
    }
}
