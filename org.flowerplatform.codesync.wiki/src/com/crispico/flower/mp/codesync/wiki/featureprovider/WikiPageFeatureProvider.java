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
package com.crispico.flower.mp.codesync.wiki.featureprovider;

import java.util.List;

import org.flowerplatform.model.astcache.wiki.AstCacheWikiPackage;

import com.crispico.flower.mp.codesync.base.CodeSyncElementFeatureProvider;

/**
 * @author Mariana
 */
public class WikiPageFeatureProvider extends CodeSyncElementFeatureProvider {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<?> getFeatures(Object element) {
		List features = super.getFeatures(element);
		features.add(AstCacheWikiPackage.eINSTANCE.getPage_InitialContent());
		return features;
	}
	
}