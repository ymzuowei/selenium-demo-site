package com.demo.tests;
import com.demo.base.BaseTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import static org.junit.jupiter.api.Assertions.*;
public class FormValidationTests extends BaseTest {
  @Test public void testFormValidationErrors(){
    driver.get("http://localhost:8080/forms/form.html");
    driver.findElement(By.id("email")).sendKeys("invalid");
    driver.findElement(By.id("phone")).sendKeys("123");
    driver.findElement(By.tagName("button")).click();
    assertEquals("Invalid email", driver.findElement(By.id("emailErr")).getText());
    assertEquals("Phone too short", driver.findElement(By.id("phoneErr")).getText());
  }
  @Test public void testFormSuccess(){
    driver.get("http://localhost:8080/forms/form.html");
    driver.findElement(By.id("email")).sendKeys("u@e.com");
    driver.findElement(By.id("phone")).sendKeys("1234567890");
    driver.findElement(By.tagName("button")).click();
    assertEquals("Submitted!", driver.findElement(By.id("msg")).getText());
  }
}
