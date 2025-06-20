package com.demo.tests;

import com.demo.base.BaseMobileTest;
import com.demo.tests.common.BaseProductPageTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

public class ProductPageMobileTests extends BaseMobileTest {

    private BaseProductPageTest sharedTests;

    @BeforeEach
    public void setupSharedTests() {
        sharedTests = new BaseProductPageTest(driver, wait) {};
        driver.get("http://localhost:8080/product.html");
    }

    @Test
    public void testAllProductImagesLoadedOnMobile() {
        sharedTests.testAllProductImagesLoaded();
    }

    @Test
    public void testAllProductVideosArePlayableAndMutedOnMobile() {
        sharedTests.testAllProductVideosArePlayableAndMuted();
    }

    @Test
    public void testAddToCartWorksOnMobile() {
        sharedTests.testAddToCartButtonWorks();
    }

    @Test
    public void testAllProductCardsVisibleOnMobile() {
        driver.findElements(By.cssSelector(".card")).forEach(
            card -> assertTrue(card.isDisplayed(), "Card not visible on mobile")
        );
    }

    @Test
    public void testAllProductManualLinksAreDownloadableOnMobile() {
        sharedTests.testAllProductManualLinksAreDownloadable();
    }
}
