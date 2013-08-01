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
package com.crispico.flower.mp.codesync.wiki.adapter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.flowerplatform.model.astcache.wiki.AstCacheWikiFactory;
import org.flowerplatform.model.astcache.wiki.AstCacheWikiPackage;
import org.flowerplatform.model.astcache.wiki.Page;

import com.crispico.flower.mp.codesync.base.action.ActionResult;
import com.crispico.flower.mp.codesync.base.FilteredIterable;
import com.crispico.flower.mp.codesync.merge.SyncElementModelAdapter;
import com.crispico.flower.mp.codesync.wiki.WikiDiff;
import com.crispico.flower.mp.codesync.wiki.WikiPlugin;
import com.crispico.flower.mp.codesync.wiki.WikiTreeBuilder;
import com.crispico.flower.mp.model.codesync.AstCacheElement;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * Mapped to {@link CodeSyncElement}.
 * 
 * @author Mariana
 */
public class WikiNodeModelAdapter extends SyncElementModelAdapter {
	
	protected String technology;
	
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	
	@Override
	public boolean hasChildren(Object modelElement) {
		return getChildren(modelElement).size() > 0;
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		return getWikiNode(modelElement).getChildren();
	}

	@Override
	public String getLabel(Object modelElement) {
		return getWikiNode(modelElement).getName();
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(feature)) {
			return new FilteredIterable(getWikiNode(element).getChildren().iterator()) {

				@Override
				protected boolean isAccepted(Object candidate) {
					String candidateType = getWikiNode(candidate).getType();
					return WikiPlugin.FOLDER_CATEGORY.equals(candidateType) 
							|| WikiPlugin.PAGE_CATEGORY.equals(candidateType);
				}
			};
		}
		return Collections.emptyList();
	}

	@Override
	public Object getMatchKey(Object element) {
		return getWikiNode(element).getName();
	}

	@Override
	public void addToMap(Object element, Map<Object, Object> map) {
		map.put(getMatchKey(element), element);
	}

	@Override
	public Object removeFromMap(Object element, Map<Object, Object> leftOrRightMap, boolean isRight) {
		return leftOrRightMap.remove(getMatchKey(element));
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (AstCacheWikiPackage.eINSTANCE.getPage_InitialContent().equals(feature)) {
			Page page = getWikiPage(element);
			if (page != null) {
				WikiDiff diff = (WikiDiff) page.getDiff();
				if (diff != null) {
					diff.applyAll(getWikiNode(element), diff.isLeftModified());
				}
			}
		} else {
			super.setValueFeatureValue(element, feature, value);
		}
	}

	@Override
	public Object createCorrespondingModelElement(Object element) {
		CodeSyncElement cse = getWikiNode(element);
		if (cse != null) {
			AstCacheElement ace = cse.getAstCacheElement();
			if (ace != null) {
				if (ace instanceof Page) {
					Page page = AstCacheWikiFactory.eINSTANCE.createPage();
					page.setDiff(((Page) ace).getDiff());
					page.setLineDelimiter(((Page) ace).getLineDelimiter());
					return page;
				}
				return super.createCorrespondingModelElement(ace);
			}
		}
		return null;
	}

	@Override
	public boolean save(Object element) {
		Page page = getWikiPage(element);
		if (page != null) {
			WikiDiff diff = (WikiDiff) page.getDiff();
			if (diff != null) {
//				diff.save();
				doSave(page.getCodeSyncElement(), diff);
//				page.setDiff(null);
			}
		}
		return true;
	}

	@Override
	public boolean discard(Object element) {
		Page page = getWikiPage(element);
		if (page != null) {
			page.setDiff(null);
		}
		return true;
	}

	@Override
	public void beforeFeaturesProcessed(Object element, Object correspondingElement) {
		// nothing to do
	}

	@Override
	public void featuresProcessed(Object element) {
		// nothing to do
	}

	@Override
	public void actionPerformed(Object element, Object feature, ActionResult result) {
		// nothing to do
	}

	@Override
	public void allActionsPerformedForFeature(Object element, Object correspondingElement, Object feature) {
		// nothing to do
	}

	@Override
	public void allActionsPerformed(Object element, Object correspondingElement) {
		// nothing to do
	}
	
	protected CodeSyncElement getWikiNode(Object element) {
		return (CodeSyncElement) element;
	}
	
	protected Page getWikiPage(Object element) {
		return (Page) getWikiNode(element).getAstCacheElement();
	}
	
	protected void doSave(CodeSyncElement cse, WikiDiff diff) {
		String wikiText = diff.getLeft();
		Page page = (Page) cse.getAstCacheElement();
		if (!page.getInitialContent().equals(wikiText)) {
			page.setInitialContent(wikiText);
			WikiPlugin.getInstance().savePage(page, technology);
		}
	}
}