package com.demo.util;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ImageAssertions {

    /**
     * Scrolls to image, waits if needed, and asserts it is fully loaded.
     */
    public static void assertImagesLoaded(WebDriver driver, List<WebElement> images) {
        assertFalse(images.isEmpty(), "No images found to assert.");

        JavascriptExecutor js = (JavascriptExecutor) driver;

        for (WebElement img : images) {
            // Scroll into view to trigger lazy loading
            js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", img);

            // Optional: wait briefly to allow loading
            try {
                new WebDriverWait(driver, Duration.ofSeconds(2)).until(d -> 
                    (Boolean) js.executeScript("return arguments[0].complete && arguments[0].naturalWidth > 0", img)
                );
            } catch (TimeoutException e) {
                // fall back to checking manually
            }

            boolean isLoaded = (Boolean) js.executeScript(
                "return arguments[0].complete && arguments[0].naturalWidth > 0", img);

            assertTrue(isLoaded, "Broken image: " + img.getAttribute("src"));
        }
    }
}
