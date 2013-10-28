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
package org.flowerplatform.codesync.processor;

import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.editor.model.change_processor.IconDiagrammableElementFeatureChangesProcessor;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.impl.CodeSyncElementImpl;

/**
 * @author Sebastian Solomon
 * @author Mariana Gheorghe
 * @author Mircea Negreanu
 */
public abstract class CodeSyncDecoratorsProcessor extends
		IconDiagrammableElementFeatureChangesProcessor {

	@Override
	protected String getIconUrls(EObject object) {
		String url = prefixWithCodeSyncBundleName(getIconBeforeCodeSyncDecoration(object));

		if (object instanceof CodeSyncElementImpl) {

			CodeSyncElementImpl codeSyncElement = (CodeSyncElementImpl) object;
			if (codeSyncElement.isDeleted()) {
				url += "|"
						+ prefixWithCodeSyncBundleName("/images/full/ovr16/syncMarker_deleted.gif");
				return url;
			}

			if (codeSyncElement.isAdded()) {
				url += "|" 
						+ prefixWithCodeSyncBundleName("/images/full/ovr16/syncMarker_added.gif");
				return url;
			}

			if (!codeSyncElement.isSynchronized()) {
				url += "|" 
						+ prefixWithCodeSyncBundleName("/images/full/ovr16/syncMarker_red.gif");
			} else if (codeSyncElement.isChildrenSynchronized()) {
				url += "|"
						+ prefixWithCodeSyncBundleName("/images/full/ovr16/syncMarker_green.gif");
			} else
				url += "|"
						+ prefixWithCodeSyncBundleName("/images/full/ovr16/syncMarker_orange.gif");
		}

		return url;
	}

	private String prefixWithCodeSyncBundleName(String image) {
		String codeSyncPackage = CodeSyncPlugin.getInstance()
				.getBundleContext().getBundle().getSymbolicName();
		if (!image.startsWith("/")) {
			image = "/" + image;
		}
		return codeSyncPackage + image;
	}

	abstract public String getIconBeforeCodeSyncDecoration(EObject object);
	
}
