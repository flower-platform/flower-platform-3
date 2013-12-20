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
package org.flowerplatform.codesync.code.javascript.config.extension;

import java.util.Map;

import org.flowerplatform.codesync.code.javascript.config.JavaScriptDescriptors;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.codesync.wizard.WizardDependencyDescriptor;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Cristina Constantinescu
 */
public class AddNewRelationExtension_FormView extends AddNewRelationExtension_FromWizardElement {

	public AddNewRelationExtension_FormView() {
		super(JavaScriptDescriptors.DEPENDENCY_FORM_VIEW);	
	}
	
	@Override
	protected CodeSyncElement createNewCodeSyncElement(WizardDependencyDescriptor descriptor, Map<String, Object> parameters) {
		CodeSyncElement codeSyncElement = super.createNewCodeSyncElement(descriptor, parameters);
				
		CodeSyncElement child = CodeSyncOperationsService.getInstance().create(JavaScriptDescriptors.TYPE_JAVASCRIPT_ATTRIBUTE);
		CodeSyncOperationsService.getInstance().setFeatureValue(child, JavaScriptDescriptors.FEATURE_NAME, "htmlIdSuffix");
		CodeSyncOperationsService.getInstance().add(codeSyncElement, child);		
		
		return codeSyncElement;
	}
	
}
