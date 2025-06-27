package com.demo.tests;

import com.demo.base.BaseWebTest;
import com.demo.config.Config;
import io.qameta.allure.*;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Form Validation")
@Feature("Submission Validations")
@DisplayName("Form Input Validation Tests")
public class FormValidationTests extends BaseWebTest {

    @BeforeEach
    @Step("Load the form.html page before each test")
    public void loadFormPage() {
        driver.get(Config.getBaseUrl() + "forms/form.html");
    }

    @Test
    @Story("Valid Submission")
    @DisplayName("Submit form with valid data")
    @Severity(SeverityLevel.CRITICAL)
    public void testValidSubmission() {
        driver.findElement(By.id("email")).sendKeys("test@example.com");
        driver.findElement(By.id("phone")).sendKeys("1234567890");

        Select country = new Select(driver.findElement(By.id("country")));
        country.selectByValue("us");

        driver.findElement(By.id("agree")).click();
        driver.findElement(By.cssSelector("input[name='gender'][value='male']")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String message = wait.until(d -> d.findElement(By.id("msg"))).getText();
        assertEquals("Submitted!", message);
    }

    @Test
    @Story("Invalid Email & Phone")
    @DisplayName("Email and phone number should be validated")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidEmailAndPhone() {
        driver.findElement(By.id("email")).sendKeys("invalid-email");
        driver.findElement(By.id("phone")).sendKeys("123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String emailError = driver.findElement(By.id("emailErr")).getText();
        String phoneError = driver.findElement(By.id("phoneErr")).getText();

        assertEquals("Invalid email", emailError);
        assertEquals("Phone too short", phoneError);
    }

    @Test
    @Story("Required Country Field")
    @DisplayName("Form should reject missing country selection")
    @Severity(SeverityLevel.NORMAL)
    public void testMissingCountry() {
        driver.findElement(By.id("email")).sendKeys("test@example.com");
        driver.findElement(By.id("phone")).sendKeys("1234567890");

        driver.findElement(By.id("agree")).click();
        driver.findElement(By.cssSelector("input[name='gender'][value='female']")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String countryError = driver.findElement(By.id("countryErr")).getText();
        assertEquals("Please select a country", countryError);
    }

    @Test
    @Story("Terms Agreement Required")
    @DisplayName("User must agree to terms before submitting")
    @Severity(SeverityLevel.NORMAL)
    public void testUncheckedAgreement() {
        driver.findElement(By.id("email")).sendKeys("test@example.com");
        driver.findElement(By.id("phone")).sendKeys("1234567890");

        Select country = new Select(driver.findElement(By.id("country")));
        country.selectByValue("cn");

        driver.findElement(By.cssSelector("input[name='gender'][value='female']")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String agreeError = driver.findElement(By.id("agreeErr")).getText();
        assertEquals("You must agree to terms", agreeError);
    }

    @Test
    @Story("Gender Selection Required")
    @DisplayName("User must select gender before submission")
    @Severity(SeverityLevel.NORMAL)
    public void testMissingGender() {
        driver.findElement(By.id("email")).sendKeys("test@example.com");
        driver.findElement(By.id("phone")).sendKeys("1234567890");

        Select country = new Select(driver.findElement(By.id("country")));
        country.selectByValue("cn");

        driver.findElement(By.id("agree")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String genderError = driver.findElement(By.id("genderErr")).getText();
        assertEquals("Select a gender", genderError);
    }

    @Test
    @Story("Email Uniqueness Check")
    @DisplayName("Form should detect duplicate email via async check")
    @Severity(SeverityLevel.MINOR)
    public void testEmailUniquenessCheck() {
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("taken@example.com");

        driver.findElement(By.id("phone")).click(); // trigger onblur

        String emailStatus = wait.until(d -> {
            String text = d.findElement(By.id("emailStatus")).getText();
            return "Email already registered".equals(text) ? text : null;
        });

        assertEquals("Email already registered", emailStatus);
    }

    @Test
    @Story("Email Availability Check")
    @DisplayName("Form should confirm email availability via async check")
    @Severity(SeverityLevel.MINOR)
    public void testEmailAvailableCheck() {
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("newuser@example.com");

        driver.findElement(By.id("phone")).click(); // trigger onblur

        String emailStatus = wait.until(d -> {
            String text = d.findElement(By.id("emailStatus")).getText();
            return "Email available".equals(text) ? text : null;
        });

        assertEquals("Email available", emailStatus);
    }
}
