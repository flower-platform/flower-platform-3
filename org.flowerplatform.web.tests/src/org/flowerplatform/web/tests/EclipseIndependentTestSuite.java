package org.flowerplatform.web.tests;

import org.flowerplatform.web.tests.regex.RegexTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	RegexTestSuite.class
})
public class EclipseIndependentTestSuite {
}
