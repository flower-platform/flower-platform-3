package org.flowerplatform.web.tests.codesync;

import astcache.wiki.Page;

import com.crispico.flower.mp.codesync.wiki.dokuwiki.DokuWikiConfigurationProvider;

/**
 * @author Mariana
 */
public class DummyDokuWikiConfigurationProvider extends DokuWikiConfigurationProvider {

	public Page page;
	
	@Override
	public void savePage(Page page) {
		this.page = page;
	}
	
}
