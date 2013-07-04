package org.flowerplatform.web.tests.regex;

import static org.flowerplatform.common.regex.JavaRegexConfigurationProvider.ATTRIBUTE_CATEGORY;
import static org.flowerplatform.common.regex.JavaRegexConfigurationProvider.METHOD_CATEGORY;
import static org.flowerplatform.common.regex.JavaRegexConfigurationProvider.buildJavaConfiguration;

import org.flowerplatform.common.regex.JavaRegexConfigurationProvider;
import org.flowerplatform.common.regex.RegexConfiguration;
import org.flowerplatform.common.regex.RegexProcessingSession;
import org.flowerplatform.web.tests.TestUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Cristi
 * @author Sorin
 */
public class RegexJavaTest extends RegexTestBase {
	
	private static final String testFile1Path = FILES_ROOT_DIR + "TestFile1.java";
	
	private static final int numberOfFieldsInFile1 = 28;
	
	private static final String fieldPreffixInFile1 = "attr";
	
	private static final int numberOfMethodsInFile1 = 3;
	
	private static final String methodsPreffixInFile1 = "meth";

	private CharSequence testFile1;

	@Before
	public void setUp() {
		testFile1 = TestUtil.readFile(testFile1Path);
	}

	@Test
	public void testJavaDeclarationsExist() {
		RegexConfiguration config = new RegexTestBase.CategoryRecorderRegexConfiguration();
		buildJavaConfiguration(config);
		RegexTestBase.CategoryRecorderRegexProcessingSession session = (RegexTestBase.CategoryRecorderRegexProcessingSession) config.startSession(testFile1);
		
		while (session.find()) {
		}
		
		assertAllExpectedElementsFound("attributes", session.getRecorderCategory(ATTRIBUTE_CATEGORY), fieldPreffixInFile1, 1, numberOfFieldsInFile1);
		assertAllExpectedElementsFound("methods", session.getRecorderCategory(METHOD_CATEGORY), methodsPreffixInFile1, 1, numberOfMethodsInFile1);
	}
	
	@Test
	public void testJavaFindRange() {
		RegexConfiguration config = new RegexConfiguration();
		JavaRegexConfigurationProvider.buildJavaConfiguration(config);
		RegexProcessingSession session = config.startSession(testFile1);
		String testInputFileAsString = testFile1.toString();
		
		for (int i = 1; i <= numberOfFieldsInFile1; i++) {
			session.reset(true);
			
			String elementName = fieldPreffixInFile1 + i;
			int[] range = session.findRangeFor(JavaRegexConfigurationProvider.ATTRIBUTE_CATEGORY, elementName);
			assertIdentifierInRange(elementName, range, testInputFileAsString);
		}
	}
}
