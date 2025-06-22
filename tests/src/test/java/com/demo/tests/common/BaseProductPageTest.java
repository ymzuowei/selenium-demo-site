package com.demo.tests.common;

import com.demo.util.FileAssertions;
import com.demo.util.ImageAssertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class BaseProductPageTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BaseProductPageTest(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    @Test
    public void testAllProductImagesLoaded() {
        List<WebElement> images = driver.findElements(By.cssSelector(".card img"));
        ImageAssertions.assertImagesLoaded(driver, images);
    }

    @Test
    public void testAllProductVideosArePlayableAndMuted() {
        List<WebElement> videos = driver.findElements(By.cssSelector(".card video"));
        assertFalse(videos.isEmpty(), "No product videos found");

        for (WebElement video : videos) {
            boolean isPlayable = (Boolean) ((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].readyState >= 2", video);
            assertTrue(isPlayable, "Video not ready: " + video.getAttribute("src"));

            boolean isMuted = (Boolean) ((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].muted", video);
            assertTrue(isMuted, "Video not muted: " + video.getAttribute("src"));
        }
    }

    @Test
    public void testAddToCartButtonWorks() {
        WebElement firstButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".card button.add-to-cart")));
        firstButton.click();

        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            assertTrue(alert.getText().contains("added to cart"));
            alert.accept();
        } catch (TimeoutException ignored) {}

        String cart = (String) ((JavascriptExecutor) driver)
                .executeScript("return localStorage.getItem('cart');");
        assertNotNull(cart);
        assertTrue(cart.contains("Product"), "Cart should contain product data");
    }

    @Test
    public void testAllProductManualLinksAreDownloadable() {
        List<WebElement> downloadLinks = driver.findElements(By.cssSelector(".download-manual"));
        assertFalse(downloadLinks.isEmpty(), "No download links found");

        for (WebElement link : downloadLinks) {
            String fileUrl = link.getAttribute("href");
            assertNotNull(fileUrl, "Link href is null");
            assertTrue(fileUrl.endsWith(".pdf"), "Expected a PDF link: " + fileUrl);

            FileAssertions.assertDownloadable(fileUrl);
        }
    }
}
