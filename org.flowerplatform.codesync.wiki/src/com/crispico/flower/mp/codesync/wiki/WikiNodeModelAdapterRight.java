package com.crispico.flower.mp.codesync.wiki;

import com.crispico.flower.mp.codesync.merge.CodeSyncMergePlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

import astcache.wiki.Page;
import astcache.wiki.WikiPackage;

/**
 * @author Mariana
 */
public class WikiNodeModelAdapterRight extends WikiNodeModelAdapter {

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (WikiPackage.eINSTANCE.getPage_InitialContent().equals(feature)) {
			return WikiPlugin.getInstance().getWikiText(getWikiNode(element), technology);
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	/**
	 * Creates the page tree from the {@link WikiDiff} and resets
	 * the initial content of the page.
	 */
	@Override
	protected void doSave(CodeSyncElement cse, WikiDiff diff) {
		diff.getRight(cse);
		Page page = (Page) cse.getAstCacheElement();
		page.setInitialContent(diff.getLeft());
		CodeSyncMergePlugin.getInstance().saveResource(cse.eResource());
	}
	
}
