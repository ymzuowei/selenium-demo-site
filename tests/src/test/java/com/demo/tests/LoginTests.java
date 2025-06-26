package com.demo.tests;
import com.demo.base.BaseWebTest;
import com.demo.config.Config;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class LoginTests extends BaseWebTest {
    @BeforeEach
    public void loadFormPage() {
        driver.get(Config.getBaseUrl() + "login.html");
    }

    @Test
    public void testSuccessfulLogin() throws InterruptedException {
        driver.findElement(By.id("email")).sendKeys("eve.holt@reqres.in");
        driver.findElement(By.id("password")).sendKeys("cityslicka");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String msg = wait.until(d -> {
            String text = d.findElement(By.id("msg")).getText();
            return (text != null && !text.trim().isEmpty()) ? text : null;
        });

        assertEquals("Login successful!", msg);

        // Check if cookie "auth_token" is set
        Cookie tokenCookie = driver.manage().getCookieNamed("auth_token");
        assertNotNull(tokenCookie);
        assertFalse(tokenCookie.getValue().isEmpty());
    }

    @Test
    public void testFailedLogin() throws InterruptedException {
        driver.findElement(By.id("email")).sendKeys("wrong@example.com");
        driver.findElement(By.id("password")).sendKeys("invalidpass");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String error = wait.until(d -> {
            String text = d.findElement(By.id("error")).getText();
            return (text != null && !text.trim().isEmpty()) ? text : null;
        });

        assertTrue(error.contains("user not found") || error.contains("Login failed"));

        Cookie tokenCookie = driver.manage().getCookieNamed("auth_token");
        assertNull(tokenCookie);
    }
}
