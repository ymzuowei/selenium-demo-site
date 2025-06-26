package com.demo.pages;

import org.openqa.selenium.*;

import com.demo.config.Config;

public class LoginPage {
    private WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get(Config.getBaseUrl() + "login.html");
    }

    public void login(String username, String password) {
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("loginBtn")).click();
    }

    public boolean isLoginSuccessful() {
        return driver.getCurrentUrl().contains("dashboard.html");
    }
}
