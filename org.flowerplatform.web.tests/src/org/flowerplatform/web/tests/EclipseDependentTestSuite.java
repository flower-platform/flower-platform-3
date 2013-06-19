package org.flowerplatform.web.tests;

import org.flowerplatform.web.tests.security.sandbox.SecurityPermissionsTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	SecurityPermissionsTests.class
})
public class EclipseDependentTestSuite extends EclipseDependentTestSuiteBase {
}
