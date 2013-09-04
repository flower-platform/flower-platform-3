/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package com.crispico.flower.mp.codesync.wiki.dokuwiki;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.web.security.sandbox.FlowerWebPrincipal;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.wiki.WikiPlugin;
import dw.xmlrpc.DokuJClient;
import dw.xmlrpc.Page;
import dw.xmlrpc.PageDW;

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
		FlowerWebPrincipal principal = (FlowerWebPrincipal) CommunicationPlugin.tlCurrentChannel.get().getPrincipal();
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