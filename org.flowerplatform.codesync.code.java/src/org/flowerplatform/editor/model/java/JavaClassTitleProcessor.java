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

import org.eclipse.emf.ecore.EObject;
import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Mariana Gheorghe
 */
public class JavaClassTitleProcessor extends JavaClassChildProcessor {

	@Override
	protected String getLabel(EObject object) {
		return (String) CodeSyncPlugin.getInstance().getFeatureValue((CodeSyncElement) object, 
				CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name());
	}

	@Override
	protected String getImageForVisibility(int type) {
		return "images/obj16/SyncClass.gif";
	}

}