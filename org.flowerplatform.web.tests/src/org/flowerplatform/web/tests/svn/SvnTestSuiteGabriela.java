package org.flowerplatform.web.tests.svn;

/**
 * 
 * @author Gabriela Murgoci
 */
import org.flowerplatform.web.tests.EclipseDependentTestSuiteBase;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	SvnTestsRepositoryActionsGabriela.class,
	SvnTestsProjectActionsGabriela.class
})
	public class SvnTestSuiteGabriela extends EclipseDependentTestSuiteBase {
}
