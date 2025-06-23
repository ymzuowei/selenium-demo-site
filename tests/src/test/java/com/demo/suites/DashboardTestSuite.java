package com.demo.suites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.demo.tests.DashboardTests;
import com.demo.tests.DashboardMobileTests;

@Suite
@SelectClasses({
    DashboardTests.class,
    DashboardMobileTests.class
})
public class DashboardTestSuite {
    // Test suite grouping
}