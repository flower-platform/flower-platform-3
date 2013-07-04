package org.flowerplatform.web.tests.codesync;

import org.flowerplatform.web.tests.EclipseDependentTestSuiteBase;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Mariana
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	CodeSyncTest.class, 
	CodeSyncWikiTest.class })
public class CodeSyncTestSuite extends EclipseDependentTestSuiteBase {

}
