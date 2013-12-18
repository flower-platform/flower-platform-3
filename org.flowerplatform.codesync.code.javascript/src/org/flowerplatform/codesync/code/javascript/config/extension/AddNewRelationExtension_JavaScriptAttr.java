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

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Cristina Constantinescu
 */
public class AddNewRelationExtension_JavaScriptAttr extends AddNewRelationExtension_FromWizardAttribute {

	public AddNewRelationExtension_JavaScriptAttr(String dependencyType, String parentDependencyType) {
		super(dependencyType, JavaScriptDescriptors.TYPE_JAVASCRIPT_ATTRIBUTE, parentDependencyType);
	}

	protected void populateNewCodeSyncElement(CodeSyncElement codeSyncElement, Map<String, Object> parameters) {
		super.populateNewCodeSyncElement(codeSyncElement, parameters);
		CodeSyncOperationsService.getInstance().setFeatureValue(
				codeSyncElement, 
				JavaScriptDescriptors.FEATURE_DEFAULT_VALUE, 
				String.format("'%s'", CodeSyncOperationsService.getInstance().getKeyFeatureValue(((CodeSyncElement) parameters.get(CodeSyncPlugin.SOURCE)))));
	}
	
}
