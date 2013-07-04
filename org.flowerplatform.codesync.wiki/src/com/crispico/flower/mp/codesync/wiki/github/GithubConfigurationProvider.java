package com.crispico.flower.mp.codesync.wiki.github;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import astcache.wiki.Page;
import astcache.wiki.WikiFactory;

import com.crispico.flower.mp.codesync.wiki.IConfigurationProvider;
import com.crispico.flower.mp.codesync.wiki.WikiPlugin;
import com.crispico.flower.mp.codesync.wiki.WikiRegexConfiguration;
import com.crispico.flower.mp.codesync.wiki.WikiTextBuilder;
import com.crispico.flower.mp.codesync.wiki.WikiTreeBuilder;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

/**
 * @author Mariana
 */
public class GithubConfigurationProvider implements IConfigurationProvider {

	public static final String TECHNOLOGY = "Github";
	
	private Map<String, IConfigurationProvider> configurationProviders = new HashMap<String, IConfigurationProvider>();
	
	public GithubConfigurationProvider() {
		configurationProviders.put("md", new MarkdownConfigurationProvider());
	}
	
	@Override
	public void buildConfiguration(WikiRegexConfiguration config, CodeSyncElement cse) {
		getConfigurationProviderForPage(cse).buildConfiguration(config, cse);
	}

	@Override
	public Class<? extends WikiTreeBuilder> getWikiTreeBuilderClass() {
		return WikiTreeBuilder.class;
	}

	@Override
	public WikiTextBuilder getWikiTextBuilder(CodeSyncElement cse) {
		return getConfigurationProviderForPage(cse).getWikiTextBuilder(cse);
	}
	
	/**
	 * @param wiki instance of {@link File}, can be a page or directory
	 */
	@Override
	public CodeSyncRoot getWikiTree(Object wiki) {
		File file = (File) wiki;
		CodeSyncRoot root = CodeSyncFactory.eINSTANCE.createCodeSyncRoot();
		root.setType(WikiTreeBuilder.FOLDER_CATEGORY);
		if (file != null) {
			root.setName(file.isDirectory() ? file.getPath() : file.getParentFile().getPath());
			createWikiPage(file, root);
		}
		return root;
	}

	private void createWikiPage(File file, CodeSyncElement root) {
		CodeSyncElement cse = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
		cse.setName(file.getName());
		if (file.isDirectory()) {
			// create new dir
			cse.setType(WikiTreeBuilder.FOLDER_CATEGORY);
			for (File child : file.listFiles()) {
				// recurse for the files in this dir
				createWikiPage(child, cse);
			}
		} else {
			// create new page
			cse.setType(WikiTreeBuilder.PAGE_CATEGORY);
			Page page = WikiFactory.eINSTANCE.createPage();
			String content;
			try {
				content = FileUtils.readFileToString(file);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			page.setInitialContent(content);
			page.setLineDelimiter(WikiPlugin.getInstance().getLineDelimiter(content));
			cse.setAstCacheElement(page);
		}
		root.getChildren().add(cse);
	}

	@Override
	public void savePage(Page page) {
		String path = WikiPlugin.getInstance().getPagePath(page, "/", true);
		try {
			FileUtils.writeStringToFile(new File(path), page.getInitialContent());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private IConfigurationProvider getConfigurationProviderForPage(CodeSyncElement cse) {
		String extension = cse.getName().substring(cse.getName().lastIndexOf(".") + 1, cse.getName().length());
		return configurationProviders.get(extension);
	}
	
}
