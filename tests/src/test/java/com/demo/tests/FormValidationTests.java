package com.demo.tests;

import com.demo.base.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.*;

public class FormValidationTests extends BaseTest {
    @BeforeEach
    public void loadFormPage() {
        driver.get("http://localhost:8080/forms/form.html");
    }

    @Test
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
    public void testMissingCountry() {
        driver.findElement(By.id("email")).sendKeys("test@example.com");
        driver.findElement(By.id("phone")).sendKeys("1234567890");

        // Don't select a country
        driver.findElement(By.id("agree")).click();
        driver.findElement(By.cssSelector("input[name='gender'][value='female']")).click();

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String countryError = driver.findElement(By.id("countryErr")).getText();
        assertEquals("Please select a country", countryError);
    }

    @Test
    public void testUncheckedAgreement() {
        driver.findElement(By.id("email")).sendKeys("test@example.com");
        driver.findElement(By.id("phone")).sendKeys("1234567890");

        Select country = new Select(driver.findElement(By.id("country")));
        country.selectByValue("cn");

        // Don't check agreement
        driver.findElement(By.cssSelector("input[name='gender'][value='female']")).click();

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String agreeError = driver.findElement(By.id("agreeErr")).getText();
        assertEquals("You must agree to terms", agreeError);
    }

    @Test
    public void testMissingGender() {
        driver.findElement(By.id("email")).sendKeys("test@example.com");
        driver.findElement(By.id("phone")).sendKeys("1234567890");

        Select country = new Select(driver.findElement(By.id("country")));
        country.selectByValue("cn");

        driver.findElement(By.id("agree")).click();

        // Don't select gender
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String genderError = driver.findElement(By.id("genderErr")).getText();
        assertEquals("Select a gender", genderError);
    }
}
