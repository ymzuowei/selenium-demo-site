package com.demo.tests;

import com.demo.base.BaseWebTest;
import com.demo.config.Config;
import com.demo.tests.common.BaseProductPageTest;

import io.qameta.allure.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("Product Page")
@Feature("Product Media & Interactions")
@DisplayName("Product Page UI and Functional Tests")
public class ProductPageTests extends BaseWebTest {

    private BaseProductPageTest sharedTests;

    @BeforeEach
    @Step("Navigate to product.html and initialize shared test logic")
    public void setupSharedTests() {
        sharedTests = new BaseProductPageTest(driver, wait) {};
        driver.get(Config.getBaseUrl() + "product.html");
    }

    @Test
    @Story("Media Validation")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("All product images should load correctly")
    public void testAllProductImagesLoaded() {
        sharedTests.testAllProductImagesLoaded();
    }

    @Test
    @Story("Media Validation")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Product videos should be playable and muted by default")
    public void testAllProductVideosArePlayableAndMuted() {
        sharedTests.testAllProductVideosArePlayableAndMuted();
    }

    @Test
    @Story("Cart Interaction")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Add to cart button should add items properly")
    public void testAddToCartButtonWorks() {
        sharedTests.testAddToCartButtonWorks();
    }

    @Test
    @Story("Manual Download")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("All product manual links should be downloadable")
    public void testAllProductManualLinksAreDownloadable() {
        sharedTests.testAllProductManualLinksAreDownloadable();
    }
}
