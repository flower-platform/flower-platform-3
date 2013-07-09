package org.flowerplatform.common.regex;

import static org.flowerplatform.common.regex.RegexUtil.CLOSE_BRACKET;
import static org.flowerplatform.common.regex.RegexUtil.MULTI_LINE_COMMENT;
import static org.flowerplatform.common.regex.RegexUtil.OPEN_BRACKET;
import static org.flowerplatform.common.regex.RegexUtil.SINGLE_LINE_COMMENT;
import static org.flowerplatform.common.regex.RegexUtil.XML_CDATA_END;
import static org.flowerplatform.common.regex.RegexUtil.XML_CDATA_START;
import static org.flowerplatform.common.regex.RegexUtil.XML_MULTI_LINE_COMMENT;
import static org.flowerplatform.common.regex.JavaRegexConfigurationProvider.ATTRIBUTE_CATEGORY;
import static org.flowerplatform.common.regex.JavaRegexConfigurationProvider.METHOD_CATEGORY;

import org.flowerplatform.common.regex.RegexConfiguration;
import org.flowerplatform.common.regex.RegexWithAction;
import org.flowerplatform.common.regex.ActionscriptRegexConfigurationProvider;

/**
 * @author Sorin
 */
public class MxmlRegexConfigurationProvider extends	ActionscriptRegexConfigurationProvider {
	
	private static int MXML_NESTING_LEVEL_FOR_DECLARATIONS = 1; // must pass <![CDATA[ which is considered as increasing the nesting
	
	public static void buildMxmlConfiguration(RegexConfiguration config) {
		config
			.setTargetNestingForMatches(MXML_NESTING_LEVEL_FOR_DECLARATIONS) 
			.setUseUntilFoundThisIgnoreAll(false)
			.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(ATTRIBUTE_CATEGORY, ACTIONSCRIPT_ATTRIBUTE, ATTRIBUTE_CATEGORY))
			.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(METHOD_CATEGORY, ACTIONSCRIPT_METHOD, METHOD_CATEGORY))
			
			.add(new RegexWithAction.IfFindThisModifyNesting("Opening CDATA", XML_CDATA_START, 1))
			.add(new RegexWithAction.IfFindThisModifyNesting("Closing CDATA", XML_CDATA_END, -1))
			.add(new RegexWithAction.IfFindThisModifyNesting("Opening curly bracket", OPEN_BRACKET, 1))
			.add(new RegexWithAction.IfFindThisModifyNesting("Closing curly bracket", CLOSE_BRACKET, -1))
			

			.add(new RegexWithAction.IfFindThisSkip("XML comment", XML_MULTI_LINE_COMMENT))
			.add(new RegexWithAction.IfFindThisSkip("Multi-line comment", MULTI_LINE_COMMENT))
			.add(new RegexWithAction.IfFindThisSkip("Single-line comment", SINGLE_LINE_COMMENT))
			.compile();
	}
}
