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

import static org.flowerplatform.editor.model.java.JavaTypeIconsConstants.ANNOTATION_ICON;
import static org.flowerplatform.editor.model.java.JavaTypeIconsConstants.CLASS_ICON;
import static org.flowerplatform.editor.model.java.JavaTypeIconsConstants.ENUM_ICON;
import static org.flowerplatform.editor.model.java.JavaTypeIconsConstants.INTERFACE_ICON;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.TypeNameMatch;
import org.eclipse.jdt.core.search.TypeNameMatchRequestor;
import org.flowerplatform.editor.model.ContentAssistItem;
import org.flowerplatform.editor.model.IContentAssist;

/**
 * Collects the fully qualified names of the matched types into the {@link #matches} list.
 * 
 * @author Mariana Gheorghe
 */
public class JavaTypeNameRequestor extends TypeNameMatchRequestor {

	private IProgressMonitor progressMonitor = new NullProgressMonitor();
	
	private List<ContentAssistItem> matches = new ArrayList<ContentAssistItem>();

	public List<ContentAssistItem> getMatches() {
		return matches;
	}
	
	public IProgressMonitor getProgressMonitor() {
		return progressMonitor;
	}
	
	@Override
	public void acceptTypeNameMatch(TypeNameMatch match) {
		String fullyQualifiedName = match.getFullyQualifiedName();
		String simpleName = match.getSimpleTypeName();
		String pck = match.getPackageName();
		String containerType = match.getTypeContainerName();
		if (containerType.length() > 0) {
			pck = containerType;
		}
		String iconUrl = getIconUrl(match.getType());
		ContentAssistItem item = new ContentAssistItem(fullyQualifiedName, simpleName, pck, iconUrl);
		matches.add(item);
		
		if (matches.size() == IContentAssist.MAX_TYPES_COUNT) {
			progressMonitor.setCanceled(true);
		}
	}
	
	protected String getIconUrl(IType type) {
		try {
			if (type.isEnum()) {
				return ENUM_ICON;
			}
			if (type.isAnnotation()) {
				return ANNOTATION_ICON;
			}
			if (type.isClass()) {
				return CLASS_ICON;
			}
			if (type.isInterface()) {
				return INTERFACE_ICON;
			}
		} catch (JavaModelException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

}
