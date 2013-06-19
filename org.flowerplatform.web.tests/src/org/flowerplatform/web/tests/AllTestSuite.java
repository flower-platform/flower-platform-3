package org.flowerplatform.web.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	EclipseIndependentTestSuite.class,
	EclipseDependentTestSuite.class
})
public class AllTestSuite {

}
