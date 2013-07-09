package org.flowerplatform.web.tests.regex;

import static org.flowerplatform.common.regex.JavaRegexConfigurationProvider.ATTRIBUTE_CATEGORY;
import static org.flowerplatform.common.regex.JavaRegexConfigurationProvider.METHOD_CATEGORY;
import static org.flowerplatform.common.regex.MxmlRegexConfigurationProvider.buildMxmlConfiguration;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.common.regex.MxmlRegexConfigurationProvider;
import org.flowerplatform.common.regex.RegexConfiguration;
import org.flowerplatform.common.regex.RegexProcessingSession;
import org.flowerplatform.web.tests.TestUtil;
import org.junit.BeforeClass;
import org.junit.Test;

public class RegexMxmlTest extends RegexTestBase {
	
	private static final String testFile3Path = FILES_ROOT_DIR + "TestFile3.mxml";

	private static CharSequence testFile3;

	private List<String> testFile3_attributeNames = Arrays.asList(
								"keyBindings", 
								"MODULES_LOCATION", 
								"showDebugMenu", 
								"shouldUpdatePerspectiveUserEntryOnLayoutChange");
	
	private List<String> testFile3_methodNames = Arrays.asList(
								"viewerClass",
								"getModulesToLoad",
								"sendObject",
								"applicationCompleteHandler");
	
	@BeforeClass
	public static void loadTestFile() {
		testFile3 = TestUtil.readFile(testFile3Path);
	}
	
	@Test
	public void testMxmlDeclarationsExist() {
		RegexConfiguration config = new RegexTestBase.CategoryRecorderRegexConfiguration();
		MxmlRegexConfigurationProvider.buildMxmlConfiguration(config);
		RegexTestBase.CategoryRecorderRegexProcessingSession session = (RegexTestBase.CategoryRecorderRegexProcessingSession) config.startSession(testFile3);
		
		while (session.find()) {
		}
		
		assertAllExist(session.getRecorderCategory(ATTRIBUTE_CATEGORY), testFile3_attributeNames);
		assertAllExist(session.getRecorderCategory(METHOD_CATEGORY), testFile3_methodNames);
	}
	
	@Test
	public void testMxmlFindRange() {
		RegexConfiguration config = new RegexConfiguration();
		buildMxmlConfiguration(config);
		RegexProcessingSession session = config.startSession(testFile3);
		
		String testFileContent = testFile3.toString();
		
		/////////////////////////////
		// Do action/Check result: range for a given attribute is correct
		/////////////////////////////
	
		for (String attributeName : testFile3_attributeNames) {
			session.reset(true);
			int[] range = session.findRangeFor(ATTRIBUTE_CATEGORY, attributeName);
			assertIdentifierInRange(attributeName, range, testFileContent);
		}
		
		/////////////////////////////
		// Do action/Check result: range for a given method is correct
		/////////////////////////////

		for (String methodName : testFile3_methodNames) {
			session.reset(true);
			int[] range = session.findRangeFor(METHOD_CATEGORY, methodName);
			assertIdentifierInRange(methodName, range, testFileContent);
		}
	}
	
}
