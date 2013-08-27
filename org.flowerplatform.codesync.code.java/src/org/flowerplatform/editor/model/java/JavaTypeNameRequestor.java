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
package org.flowerplatform.editor.model.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.search.TypeNameRequestor;
import org.flowerplatform.editor.model.ContentAssistItem;

/**
 * Collects the fully qualified names of the matched types into the {@link #matches} list.
 * 
 * @author Mariana Gheorghe
 */
public class JavaTypeNameRequestor extends TypeNameRequestor {

	private List<ContentAssistItem> matches = null;

	public List<ContentAssistItem> getMatches() {
		return matches;
	}

	@Override
	public void acceptType(int modifiers, char[] packageName,
			char[] simpleTypeName, char[][] enclosingTypeNames, String path) {
		String pck = String.copyValueOf(packageName);
		String type = String.copyValueOf(simpleTypeName);
		String parentTypes = new String(); // TODO support nested types
		String fullyQualifiedName = 
				pck 		+ (pck.length() == 0 			? "" : ".") + 
				parentTypes + (parentTypes.length() == 0 	? "" : ".") +
				type;
		if (matches == null) {
			matches = new ArrayList<ContentAssistItem>();
		}
		ContentAssistItem item = new ContentAssistItem(fullyQualifiedName, type, pck, null);
		matches.add(item);
	}

}
