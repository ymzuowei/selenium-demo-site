package com.demo.suites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.demo.tests.ProductPageTests;
import com.demo.tests.ProductPageMobileTests;

@Suite
@SelectClasses({
    ProductPageTests.class,
    ProductPageMobileTests.class
})
public class ProductTestSuite {
    // Test suite grouping
}
