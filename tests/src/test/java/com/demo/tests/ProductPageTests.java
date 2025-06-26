package com.demo.tests;

import com.demo.base.BaseWebTest;
import com.demo.config.Config;
import com.demo.tests.common.BaseProductPageTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductPageTests extends BaseWebTest {

    private BaseProductPageTest sharedTests;

    @BeforeEach
    public void setupSharedTests() {
        sharedTests = new BaseProductPageTest(driver, wait) {};
        driver.get(Config.getBaseUrl() + "product.html");
    }

    @Test
    public void testAllProductImagesLoaded() {
        sharedTests.testAllProductImagesLoaded();
    }

    @Test
    public void testAllProductVideosArePlayableAndMuted() {
        sharedTests.testAllProductVideosArePlayableAndMuted();
    }

    @Test
    public void testAddToCartButtonWorks() {
        sharedTests.testAddToCartButtonWorks();
    }

    @Test
    public void testAllProductManualLinksAreDownloadable() {
        sharedTests.testAllProductManualLinksAreDownloadable();
    }
}
