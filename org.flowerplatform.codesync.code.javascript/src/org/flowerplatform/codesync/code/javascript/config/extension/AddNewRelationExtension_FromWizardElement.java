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
import org.flowerplatform.codesync.config.extension.AddNewExtension_TopLevelElement;
import org.flowerplatform.codesync.config.extension.AddNewRelationExtension;
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.codesync.wizard.WizardDependencyDescriptor;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.Relation;

/**
 * @author Cristina Constantinescu
 */
public abstract class AddNewRelationExtension_FromWizardElement implements AddNewRelationExtension {

	protected String dependencyType;
	
	protected String newCodeSyncElementType;
		
	protected String newCodeSyncElementKeyFeatureFormat = "%s";
	
	public AddNewRelationExtension_FromWizardElement(String dependencyType, String newCodeSyncElementType) {
		super();
		this.dependencyType = dependencyType;
		this.newCodeSyncElementType = newCodeSyncElementType;
	}

	@Override
	public boolean addNew(Relation relation, Resource codeSyncMappingResource, Map<String, Object> parameters) throws Exception {		
		if (preperareAdd(relation, codeSyncMappingResource, parameters)) {
			return true;
		}			
		WizardDependencyDescriptor descriptor = (WizardDependencyDescriptor) CodeSyncPlugin.getInstance().getRelationDescriptor(relation.getType());
		
		CodeSyncElement parent = AddNewExtension_TopLevelElement.getOrCreateCodeSyncElementForLocation(codeSyncMappingResource, descriptor.getTargetCodeSyncElementLocation().split("/"));
		
		CodeSyncElement codeSyncElement = createNewCodeSyncElement(parameters);
		CodeSyncOperationsService.getInstance().add(parent, codeSyncElement);
		
		parameters.put(CodeSyncPlugin.TARGET, codeSyncElement);
		
		return false;
	}
	
	protected boolean preperareAdd(Relation relation, Resource codeSyncMappingResource, Map<String, Object> parameters) throws Exception {
		if (!relation.getType().equals(dependencyType)) {
			return true;
		}			
		WizardDependencyDescriptor descriptor = (WizardDependencyDescriptor) CodeSyncPlugin.getInstance().getRelationDescriptor(relation.getType());
		CodeSyncElement source = (CodeSyncElement) parameters.get(CodeSyncPlugin.SOURCE);
				
		if (descriptor.getRequiredWizardDependencyTypes() != null) {
			for (String requiredType : descriptor.getRequiredWizardDependencyTypes()) {
				boolean found = false;
				for (Relation sourceRelation : ((CodeSyncElement) source.eContainer()).getRelations()) {
					if (requiredType.equals(sourceRelation.getType())) {
						found = true;
						break;
					}
				}
				if (!found) {
					throw new Exception(
							CodeSyncPlugin.getInstance().getMessage("relation.error", 
							CodeSyncPlugin.getInstance().getMessage("wizard.relation.error", descriptor.getLabel()),
							CodeSyncPlugin.getInstance().getMessage("wizard.relationRequired", CodeSyncPlugin.getInstance().getRelationDescriptor(requiredType).getLabel()))
					);
				}
			}			
		}
		
		for (Relation sourceRelation : source.getRelations()) {
			if (sourceRelation.getType().equals(relation.getType())) {
				throw new Exception(
						CodeSyncPlugin.getInstance().getMessage("relation.error", 
						CodeSyncPlugin.getInstance().getMessage("wizard.relation.error", descriptor.getLabel()),
						CodeSyncPlugin.getInstance().getMessage("wizard.relationExists", CodeSyncOperationsService.getInstance().getCodeSyncElementPath(sourceRelation.getTarget())))
				);
			}
		}
		return false;				
	}
	
	protected CodeSyncElement createNewCodeSyncElement(Map<String, Object> parameters) {
		CodeSyncElement codeSyncElement = CodeSyncOperationsService.getInstance().create(newCodeSyncElementType);

		populateNewCodeSyncElement(codeSyncElement, parameters);
		
		return codeSyncElement;
	}
	
	protected void populateNewCodeSyncElement(CodeSyncElement codeSyncElement, Map<String, Object> parameters) {
		CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(codeSyncElement.getType());
		CodeSyncOperationsService.getInstance().setKeyFeatureValue(codeSyncElement, getKeyFeatureValue(CodeSyncOperationsService.getInstance().getKeyFeatureValue(((CodeSyncElement) parameters.get(CodeSyncPlugin.SOURCE)))) + "." + descriptor.getExtension());	
	}
	
	protected String getKeyFeatureValue(Object... keyFeatureFormatArgs) {
		 return String.format(newCodeSyncElementKeyFeatureFormat, keyFeatureFormatArgs);	
	}

}
