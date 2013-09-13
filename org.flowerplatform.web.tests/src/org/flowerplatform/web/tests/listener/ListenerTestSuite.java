package org.flowerplatform.web.tests.listener;

import org.flowerplatform.web.tests.EclipseDependentTestSuiteBase;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * @author Tache Razvan Mihai
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	DeleteListenerTest.class,
	RenameListenerTest.class})

public class ListenerTestSuite extends EclipseDependentTestSuiteBase {

}
