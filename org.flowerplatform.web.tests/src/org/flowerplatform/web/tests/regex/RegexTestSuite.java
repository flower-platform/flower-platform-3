package org.flowerplatform.web.tests.regex;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ 
	RegexEngineTest.class,
	RegexJavaTest.class,
	RegexActionscriptTest.class, 
	RegexMxmlTest.class
})
public class RegexTestSuite {

}
