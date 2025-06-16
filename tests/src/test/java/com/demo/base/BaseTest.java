package com.demo.base;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;
public class BaseTest {
  protected WebDriver driver;
  @BeforeEach public void setUp(){
    driver = new ChromeDriver();
  }
  @AfterEach public void tearDown(){
    if(driver!=null) driver.quit();
  }
}
