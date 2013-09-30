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
	CreateRemoteFolderTest.class,
	RenameMoveTest.class,
	CleanupTest.class,
	BranchTagTest.class,
	BranchTagProjectsTest.class,
	SwitchToBranchTest.class,
	ShareProjectTest.class
})
public class SVNTests  extends EclipseDependentTestSuiteBase {

}
