package com.crispico.flower.mp.codesync.wiki.dokuwiki;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.web.security.sandbox.FlowerWebPrincipal;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.wiki.WikiPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.Page;
import dw.xmlrpc.PageDW;
import dw.xmlrpc.exception.DokuException;

public class DokuWikiPlugin extends AbstractFlowerJavaPlugin {

	public static final String TECHNOLOGY = "Doku";
	
	protected static DokuWikiPlugin INSTANCE = new DokuWikiPlugin();
	
	public static DokuWikiPlugin getInstance() {
		return INSTANCE;
	}
	
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		WikiPlugin.getInstance().getConfigurationProviders().put(TECHNOLOGY, new DokuWikiConfigurationProvider());
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
	
	public List<DokuWikiPage> getWikiPages(String namespace) {
		try {
			DokuJClient client = getClient();
			List<DokuWikiPage> pages = new ArrayList<DokuWikiPage>();
			if (namespace == null) {
				for (Page page : client.getAllPages()) {
					String pageId = page.id();
					pages.add(new DokuWikiPage(pageId, client.getPage(pageId)));
				}
			} else {
				String content = client.getPage(namespace);
				if (content.length() > 0) {
					pages.add(new DokuWikiPage(namespace, content));
				}
				for (PageDW page : client.getPagelist(namespace)) {
					String pageId = page.id();
					pages.add(new DokuWikiPage(pageId, client.getPage(pageId)));
				}
			}
			return pages;
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}
	
	public void savePage(astcache.wiki.Page page) {
		try {
			DokuJClient client = getClient();
			client.putPage(WikiPlugin.getInstance().getPagePath(page, ":", false), page.getInitialContent());
		} catch (Exception e) {
		}
	}
	
	private DokuJClient getClient() {
		try {
			DokuWikiClientConfiguration clientConfig = getClientConfiguration();
			return new DokuJClient(clientConfig.getUrl(), clientConfig.getUser(), clientConfig.getPassword());
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid client configuration for DokuWiki", e);
		}
	}
	
	protected DokuWikiClientConfiguration getClientConfiguration() {
		FlowerWebPrincipal principal = (FlowerWebPrincipal) CommunicationPlugin.tlCurrentPrincipal.get();
		DokuWikiClientConfiguration clientConfig = (DokuWikiClientConfiguration) principal.getWikiClientConfigurations().get(TECHNOLOGY);
		return clientConfig;
	}
	
	public String getWikiName() {
		try {
			return getClient().getTitle();
		} catch (Exception e) {
			return null;
		}
	}
	
}