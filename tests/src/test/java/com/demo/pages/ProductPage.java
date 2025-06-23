package com.demo.pages;

import org.openqa.selenium.*;
import java.util.List;

public class ProductPage {
    private WebDriver driver;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("http://localhost:8080/product.html");
    }

    public List<WebElement> getAddToCartButtons() {
        return driver.findElements(By.cssSelector(".add-to-cart"));
    }

    public void addToCart(int index) {
        getAddToCartButtons().get(index).click();
    }

    public List<WebElement> getProductCards() {
        return driver.findElements(By.cssSelector(".card"));
    }

    public List<WebElement> getImages() {
        return driver.findElements(By.cssSelector(".card img"));
    }

    public List<WebElement> getVideos() {
        return driver.findElements(By.cssSelector(".card video"));
    }

    public List<WebElement> getManualLinks() {
        return driver.findElements(By.cssSelector(".download-manual"));
    }
}