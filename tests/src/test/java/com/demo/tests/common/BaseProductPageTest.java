package com.demo.tests.common;

import com.demo.pages.ProductPage;
import com.demo.util.FileAssertions;
import com.demo.util.ImageAssertions;

import io.qameta.allure.*;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class BaseProductPageTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected ProductPage productPage;

    public BaseProductPageTest(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.productPage = new ProductPage(driver);
    }

    @Test
    @Epic("Product Page")
    @Feature("Media Validation")
    @Story("Image Load Check")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify all product images are loaded correctly without errors")
    public void testAllProductImagesLoaded() {
        List<WebElement> images = productPage.getImages();
        ImageAssertions.assertImagesLoaded(driver, images);
    }

    @Test
    @Epic("Product Page")
    @Feature("Media Validation")
    @Story("Video Playback and Mute Check")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify all product videos are playable and muted by default")
    public void testAllProductVideosArePlayableAndMuted() {
        List<WebElement> videos = productPage.getVideos();
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
    @Epic("Product Page")
    @Feature("Cart Interaction")
    @Story("Add to Cart Functionality")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the Add to Cart button adds the product and updates local storage")
    public void testAddToCartButtonWorks() {
        WebElement firstButton = wait.until(ExpectedConditions.elementToBeClickable(
                productPage.getAddToCartButtons().get(0)));
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
    @Epic("Product Page")
    @Feature("Manual Downloads")
    @Story("Manual Link Validation")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify all product manual download links are present and point to downloadable PDFs")
    public void testAllProductManualLinksAreDownloadable() {
        List<WebElement> downloadLinks = productPage.getManualLinks();
        assertFalse(downloadLinks.isEmpty(), "No download links found");

        for (WebElement link : downloadLinks) {
            String fileUrl = link.getAttribute("href");
            assertNotNull(fileUrl, "Link href is null");
            assertTrue(fileUrl.endsWith(".pdf"), "Expected a PDF link: " + fileUrl);

            FileAssertions.assertDownloadable(fileUrl);
        }
    }
}
