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

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.Relation;

/**
 * @author Cristina Constantinescu
 */
public class AddNewRelationExtension_FromWizardAttribute extends AddNewRelationExtension_FromWizardElement {
	
	protected String parentDependencyType;
	
	public AddNewRelationExtension_FromWizardAttribute(
			String dependencyType, String newCodeSyncElementType, String parentDependencyType) {		
		super(dependencyType, newCodeSyncElementType);		
		this.parentDependencyType = parentDependencyType;
	}

	@Override
	public boolean addNew(Relation relation, Resource codeSyncMappingResource, Map<String, Object> parameters) throws Exception {	
		if (preperareAdd(relation, codeSyncMappingResource, parameters)) {
			return true;
		}	
		
		CodeSyncElement sourceParentWizardElement = (CodeSyncElement) ((CodeSyncElement) parameters.get(CodeSyncPlugin.SOURCE)).eContainer();
		Relation wizardDependency = CodeSyncOperationsService.getInstance().getRelation(sourceParentWizardElement, parentDependencyType);
		
		CodeSyncElement codeSyncElement = createNewCodeSyncElement(parameters);		
		CodeSyncOperationsService.getInstance().add(wizardDependency.getTarget(), codeSyncElement);	
		
		parameters.put(CodeSyncPlugin.TARGET, codeSyncElement);
		
		return false;
	}
	
	protected void populateNewCodeSyncElement(CodeSyncElement codeSyncElement, Map<String, Object> parameters) {		
		CodeSyncOperationsService.getInstance().setKeyFeatureValue(codeSyncElement, getKeyFeatureValue(CodeSyncOperationsService.getInstance().getKeyFeatureValue(((CodeSyncElement) parameters.get(CodeSyncPlugin.SOURCE)))));	
	}

}
