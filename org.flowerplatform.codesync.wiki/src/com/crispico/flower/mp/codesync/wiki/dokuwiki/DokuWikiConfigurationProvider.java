package com.crispico.flower.mp.codesync.wiki.dokuwiki;

import java.util.List;

import astcache.wiki.Page;
import astcache.wiki.WikiPackage;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;
import com.crispico.flower.mp.codesync.wiki.IConfigurationProvider;
import com.crispico.flower.mp.codesync.wiki.WikiRegexConfiguration;
import com.crispico.flower.mp.codesync.wiki.WikiTextBuilder;
import com.crispico.flower.mp.codesync.wiki.WikiTreeBuilder;
import com.crispico.flower.mp.common.regex.RegexWithAction;

/**
 * @author Mariana
 */
public class DokuWikiConfigurationProvider implements IConfigurationProvider {

	public static final String HEADLINE_LEVEL_1_CATEGORY = "Headline 1";
	public static final String HEADLINE_LEVEL_2_CATEGORY = "Headline 2";
	public static final String HEADLINE_LEVEL_3_CATEGORY = "Headline 3";
	public static final String HEADLINE_LEVEL_4_CATEGORY = "Headline 4";
	public static final String HEADLINE_LEVEL_5_CATEGORY = "Headline 5";
	
	public static final String PARAGRAPH_CATEGORY = "Paragraph";
	
	private final String CAPTURE_PARAGRAPH = "(\\S.*?)";	// must start with a non-whitespace char => skip empty paragraphs
	
	private final String PARAGRAPH = CAPTURE_PARAGRAPH + "[\r\n]";
	private final String FINAL_PARAGRAPH = CAPTURE_PARAGRAPH + "\\z";
	
	@Override
	public void buildConfiguration(WikiRegexConfiguration config) {
		config
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADLINE_LEVEL_1_CATEGORY, getHeadline(1), HEADLINE_LEVEL_1_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADLINE_LEVEL_2_CATEGORY, getHeadline(2), HEADLINE_LEVEL_2_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADLINE_LEVEL_3_CATEGORY, getHeadline(3), HEADLINE_LEVEL_3_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADLINE_LEVEL_4_CATEGORY, getHeadline(4), HEADLINE_LEVEL_4_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADLINE_LEVEL_5_CATEGORY, getHeadline(5), HEADLINE_LEVEL_5_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(PARAGRAPH_CATEGORY, PARAGRAPH, PARAGRAPH_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(PARAGRAPH_CATEGORY, FINAL_PARAGRAPH, PARAGRAPH_CATEGORY))
		.setUseUntilFoundThisIgnoreAll(false);
	}
	
	@Override
	public Class<? extends WikiTreeBuilder> getWikiTreeBuilderClass() {
		return DokuWikiTreeBuilder.class;
	}
	
	public WikiTextBuilder getWikiTextBuilder() {
		return new DokuWikiTextBuilder();
	}
	
	private String getHeadline(int level) {
		String delim = String.format("={%s}", 7 - level);
		return delim + "(.*?)" + delim;
	}

	public static int getHeadlineLevel(String category) {
		if (category == null) {
			return -1;
		}
		if (category.startsWith("Headline")) {
			return Integer.parseInt(category.substring(category.length() - 1)); 
		}
		return -1;
	}

	/**
	 * @param wiki list of {@link DokuWikiPage}s
	 */
	@Override
	public CodeSyncRoot getWikiTree(Object wiki) {
		List<DokuWikiPage> pages = (List<DokuWikiPage>) wiki;
		CodeSyncRoot root = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncRoot();
		root.setName(DokuWikiPlugin.getInstance().getWikiName());
		root.setType(WikiTreeBuilder.FOLDER_CATEGORY);
		if (pages != null) {
			for (DokuWikiPage page : pages) {
				CodeSyncElement crtNode = root;
				String[] fragments = page.getId().split(":");
				for (int i = 0; i < fragments.length; i++) {
					String fragment = fragments[i];
					CodeSyncElement child = getChild(crtNode, fragment);
					if (child == null) {
						child = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncElement();
						child.setName(fragment);
						child.setType(WikiTreeBuilder.FOLDER_CATEGORY);
						if (i == fragments.length - 1) {
							child.setType(WikiTreeBuilder.PAGE_CATEGORY);
							Page wikiPage = WikiPackage.eINSTANCE.getWikiFactory().createPage();
							wikiPage.setInitialContent(page.getContent());
							wikiPage.setLineDelimiter(getLineDelimiter(page.getContent()));
							child.setAstCacheElement(wikiPage);
						}
						crtNode.getChildren().add(child);
					}
					crtNode = child;
				}
			}
		}
		return root;
	}
	
	private CodeSyncElement getChild(CodeSyncElement parent, String name) {
		for (CodeSyncElement child : parent.getChildren()) {
			if (child.getName().equals(name)) {
				return child;
			}
		}
		return null;
	}

	private String getLineDelimiter(String content) {
		if (content.contains("\r\n")) {
			return "\r\n";
		}
		if (content.contains("\r")) {
			return "\r";
		}
		return "\n";
	}
	
	@Override
	public void savePage(Page page) {
		DokuWikiPlugin.getInstance().savePage(page);
	}
	
}
