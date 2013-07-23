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

import astcache.wiki.Page;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

/**
 * @author Mariana
 */
public interface IConfigurationProvider {

	/**
	 * Add expressions and set parameters for <code>config</code>.
	 */
	void buildConfiguration(WikiRegexConfiguration config, CodeSyncElement cse);
	
	Class<? extends WikiTreeBuilder> getWikiTreeBuilderClass();
	
	WikiTextBuilder getWikiTextBuilder(CodeSyncElement cse);
	
	/**
	 * Generates a wiki tree from specific wiki information.
	 */
	CodeSyncRoot getWikiTree(Object wiki);
	
	/**
	 * Saves the page.
	 */
	void savePage(Page page);
}