package com.demo.base;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;
import java.time.Duration;

public class BaseTest {
  protected static WebDriver driver;
  protected static WebDriverWait wait;
  @BeforeEach public void setUp(){
    ChromeOptions options = new ChromeOptions();
    // Run Chrome headless in CI
    options.addArguments("--headless=new");  // or just "--headless" if your Chrome version doesn't support new headless mode yet
    options.addArguments("--no-sandbox"); // Required in many CI environments
    options.addArguments("--disable-dev-shm-usage"); // Overcome limited /dev/shm space in containers
    options.addArguments("window-size=1280,960");

    driver = new ChromeDriver(options);
    wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  }
  @AfterEach public void tearDown(){
    if(driver!=null) driver.quit();
  }

  public static WebDriver getDriver() {
      return driver;
  }
}
