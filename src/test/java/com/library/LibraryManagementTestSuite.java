package com.library;

import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Library Management System Test Suite")
@SelectPackages({
    "com.library.service",
    "com.library.controller",
    "com.library.repository"
})
@IncludeClassNamePatterns(".*Test")
public class LibraryManagementTestSuite {
    // Test suite configuration
}
