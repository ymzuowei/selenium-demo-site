package com.demo.tests;
import com.demo.base.BaseTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class LoginTests extends BaseTest {
    @BeforeEach
    public void loadFormPage() {
        driver.get("http://localhost:8080/forms/form.html");
    }

    @Test
    public void testSuccessfulLogin() throws InterruptedException {
        driver.findElement(By.id("email")).sendKeys("eve.holt@reqres.in");
        driver.findElement(By.id("password")).sendKeys("cityslicka");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait for login success message - simple wait loop, or better with WebDriverWait
        Thread.sleep(3000);  // replace with WebDriverWait if preferred

        String msg = wait.until(d -> {
            String text = d.findElement(By.id("msg")).getText();
            return text.equals("Login successful!") ? text : null;
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
            return text.equals("Login successful!") ? text : null;
        });

        assertTrue(error.contains("user not found") || error.contains("Login failed"));

        Cookie tokenCookie = driver.manage().getCookieNamed("auth_token");
        assertNull(tokenCookie);
    }
}
