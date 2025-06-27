package com.demo.tests;

import com.demo.base.BaseWebTest;
import com.demo.config.Config;
import io.qameta.allure.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Authentication")
@Feature("Login Functionality")
@DisplayName("Login Tests")
public class LoginTests extends BaseWebTest {

    @BeforeEach
    @Step("Load login page before each test")
    public void loadFormPage() {
        driver.get(Config.getBaseUrl() + "login.html");
    }

    @Test
    @Story("Valid Login")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Successful login with valid credentials")
    @Description("Checks that a valid user can log in and receive an auth_token cookie.")
    public void testSuccessfulLogin() throws InterruptedException {
        step("Enter valid credentials and submit");
        driver.findElement(By.id("email")).sendKeys("eve.holt@reqres.in");
        driver.findElement(By.id("password")).sendKeys("cityslicka");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        step("Wait for success message");
        String msg = wait.until(d -> {
            String text = d.findElement(By.id("msg")).getText();
            return (text != null && !text.trim().isEmpty()) ? text : null;
        });
        assertEquals("Login successful!", msg);

        step("Check auth_token cookie is set");
        Cookie tokenCookie = driver.manage().getCookieNamed("auth_token");
        assertNotNull(tokenCookie);
        assertFalse(tokenCookie.getValue().isEmpty());
    }

    @Test
    @Story("Invalid Login")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Failed login with wrong credentials")
    @Description("Verifies that login fails and no auth_token is set for invalid credentials.")
    public void testFailedLogin() throws InterruptedException {
        step("Enter invalid credentials and submit");
        driver.findElement(By.id("email")).sendKeys("wrong@example.com");
        driver.findElement(By.id("password")).sendKeys("invalidpass");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        step("Wait for error message");
        String error = wait.until(d -> {
            String text = d.findElement(By.id("error")).getText();
            return (text != null && !text.trim().isEmpty()) ? text : null;
        });
        assertTrue(error.contains("user not found") || error.contains("Login failed"));

        step("Check auth_token cookie is not set");
        Cookie tokenCookie = driver.manage().getCookieNamed("auth_token");
        assertNull(tokenCookie);
    }

    @Step("{0}")
    private void step(String stepDescription) {
        // used for logging Allure steps
    }
}
