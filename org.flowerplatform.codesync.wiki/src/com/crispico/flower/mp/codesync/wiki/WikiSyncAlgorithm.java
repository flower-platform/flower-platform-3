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
package com.crispico.flower.mp.codesync.wiki;

import org.flowerplatform.model.astcache.wiki.AstCacheWikiPackage;
import org.flowerplatform.model.astcache.wiki.Page;

import com.crispico.flower.mp.codesync.base.CodeSyncAlgorithm;
import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.Match;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana
 */
public class WikiSyncAlgorithm extends CodeSyncAlgorithm {

	private String technology;
	
	public WikiSyncAlgorithm(ModelAdapterFactorySet modelAdapterFactorySet, String technology) {
		super(modelAdapterFactorySet);
		this.technology = technology;
	}

	@Override
	public void processValueFeature(Object feature, Match match) {
		if (AstCacheWikiPackage.eINSTANCE.getPage_InitialContent().equals(feature)) {
			Object ancestor = match.getAncestor();
			if (!isPage(ancestor)) {
				return;
			}
			Object left = match.getLeft();
			if (!isPage(left)) {
				return;
			}
			Object right = match.getRight();
			if (!isPage(right)) {
				return;
			}
			
			Object ancestorValue = null;
			Object leftValue = null;
			Object rightValue = null;
			
			if (right != null) {
				IModelAdapter modelAdapter = modelAdapterFactorySet.getRightFactory().getModelAdapter(right);
				rightValue = modelAdapter.getValueFeatureValue(right, feature, null);
			}
			
			if (ancestor != null) {
				IModelAdapter modelAdapter = modelAdapterFactorySet.getAncestorFactory().getModelAdapter(ancestor);
				ancestorValue = modelAdapter.getValueFeatureValue(ancestor, feature, rightValue); 
			}
			
			if (left != null) {
				IModelAdapter modelAdapter = modelAdapterFactorySet.getLeftFactory().getModelAdapter(left);
				leftValue = modelAdapter.getValueFeatureValue(left, feature, rightValue);
			}
			
			WikiDiff wikiDiff = new WikiDiff((String) ancestorValue, (String) leftValue, (CodeSyncElement) right, technology);
			WikiDiff leftDiff = null, rightDiff = null;
			
			if (wikiDiff.isLeftModified() && wikiDiff.isRightModified()) {
				leftDiff = new WikiDiff(wikiDiff);
				leftDiff.setRightModified(false);
				rightDiff = wikiDiff;
				rightDiff.setLeftModified(false);
			} else {
				if (wikiDiff.isLeftModified()) {
					leftDiff = wikiDiff;
				}
				if (wikiDiff.isRightModified()) {
					rightDiff = wikiDiff;
				}
			}
			if (leftDiff != null) {
				match.addDiff(leftDiff);
				if (right == null) {
					setDiff(left, leftDiff); // will be copied to right when right is created
				} else {
					setDiff(left, leftDiff);
					setDiff(right, leftDiff);
				}
			}
			if (rightDiff != null) {
				match.addDiff(rightDiff);
				if (left == null) {
					setDiff(right, rightDiff); // will be copied to left when left is created
				} else {
					setDiff(left, rightDiff);
					setDiff(right, rightDiff);
				}
			}
		} else {
			super.processValueFeature(feature, match);
		}
	}

	private void setDiff(Object node, WikiDiff diff) {
		if (node != null && node instanceof CodeSyncElement) {
			CodeSyncElement cse = (CodeSyncElement) node;
			if (cse.getAstCacheElement() != null && cse.getAstCacheElement() instanceof Page) {
				((Page) cse.getAstCacheElement()).setDiff(diff);
			}
		}
	}
	
	private boolean isPage(Object node) {
		CodeSyncElement cse = (CodeSyncElement) node;
		return cse == null || cse.getType().equals(WikiPlugin.PAGE_CATEGORY);
	}
	
}