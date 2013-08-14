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
package org.flowerplatform.editor.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mariana Gheorghe
 */
public class ComposedContentAssist implements IContentAssist {

	List<IContentAssist> delegateSearchEngines = new ArrayList<IContentAssist>();
	
	public void addDelegateSearchEngine(IContentAssist delegate) {
		delegateSearchEngines.add(delegate);
	}
	
	@Override
	public List<String> findMatches(Map<String, Object> context, String pattern) {
		List<String> matches = new ArrayList<String>();
		for (IContentAssist delegate : delegateSearchEngines) {
			List<String> result = delegate.findMatches(context, pattern);
			if (result != null) {
				matches.addAll(result);
			}
		}
		return matches;
	}

}
