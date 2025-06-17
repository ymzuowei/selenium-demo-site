package com.demo.base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class BaseTest {
  protected WebDriver driver;
  protected WebDriverWait wait;
  private Path tempUserDataDir; // store temp user data dir for cleanup

  @BeforeEach
  public void setUp() throws Exception {
    ChromeOptions options = new ChromeOptions();

    // Run Chrome headless in CI
    options.addArguments("--headless=new");  // or "--headless"
    options.addArguments("--no-sandbox"); // Required in many CI environments
    options.addArguments("--disable-dev-shm-usage"); // Overcome limited /dev/shm space in containers
    options.addArguments("window-size=1280,960");

    // Create a unique temp directory for chrome user data to avoid session conflicts in parallel CI runs
    tempUserDataDir = Files.createTempDirectory("chrome-user-data-" + UUID.randomUUID());
    options.addArguments("--user-data-dir=" + tempUserDataDir.toAbsolutePath().toString());

    driver = new ChromeDriver(options);
    wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  }

  @AfterEach
  public void tearDown() {
    if (driver != null) {
      driver.quit();
      driver = null;
    }

    // Clean up the temporary user data directory
    if (tempUserDataDir != null) {
      try {
        Files.walk(tempUserDataDir)
          .sorted((a, b) -> b.compareTo(a)) // delete children before parents
          .forEach(path -> {
            try { Files.delete(path); } catch (Exception ignored) {}
          });
      } catch (Exception ignored) {}
      tempUserDataDir = null;
    }
  }
}
