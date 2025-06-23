package com.demo.suites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.demo.tests.CheckoutTests;
import com.demo.tests.CheckoutMobileTests;

@Suite
@SelectClasses({
    CheckoutTests.class,
    CheckoutMobileTests.class
})
public class CheckoutTestSuite {
    // Test suite grouping
}