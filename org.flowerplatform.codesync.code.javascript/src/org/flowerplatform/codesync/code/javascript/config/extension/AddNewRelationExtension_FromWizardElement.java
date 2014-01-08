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
import org.flowerplatform.codesync.config.extension.AddNewExtension;
import org.flowerplatform.codesync.config.extension.AddNewExtension_TopLevelElement;
import org.flowerplatform.codesync.config.extension.AddNewRelationExtension;
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.codesync.wizard.WizardChildrenPropagatorProcessor;
import org.flowerplatform.codesync.wizard.WizardDependencyDescriptor;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.Relation;

/**
 * @author Cristina Constantinescu
 */
public class AddNewRelationExtension_FromWizardElement implements AddNewRelationExtension {

	protected String dependencyType;

	private String targetCodeSyncElementInitializationType;
	
	public AddNewRelationExtension_FromWizardElement(String dependencyType) {
		super();
		this.dependencyType = dependencyType;		
	}

	public AddNewRelationExtension_FromWizardElement(String dependencyType, String targetCodeSyncElementInitializationType) {
		this(dependencyType);			
		this.targetCodeSyncElementInitializationType = targetCodeSyncElementInitializationType;
	}
	
	@Override
	public boolean addNew(Relation relation, Resource codeSyncMappingResource, Map<String, Object> parameters) throws Exception {		
		if (preperareAdd(relation, parameters)) {
			return true;
		}			
		WizardDependencyDescriptor descriptor = getRelationDescriptor(relation.getType());
		
		parameters.put(AddNewExtension_TopLevelElement.LOCATION, descriptor.getTargetCodeSyncElementLocation());
		if (targetCodeSyncElementInitializationType != null) {
			parameters.put(CodeSyncPlugin.CONTEXT_INITIALIZATION_TYPE, targetCodeSyncElementInitializationType);
		}
		
		CodeSyncElement parent = AddNewExtension_TopLevelElement.getOrCreateCodeSyncElementForLocation(codeSyncMappingResource, ((String) parameters.get(AddNewExtension_TopLevelElement.LOCATION)).split("/"));
				
		CodeSyncElement codeSyncElement = createNewCodeSyncElement(codeSyncMappingResource, descriptor, parameters);
		CodeSyncOperationsService.getInstance().add(parent, codeSyncElement);
		
		parameters.put(CodeSyncPlugin.TARGET, codeSyncElement);
				
		return false;
	}
	
	protected boolean preperareAdd(Relation relation, Map<String, Object> parameters) throws Exception {
		if (parameters.containsKey(CodeSyncPlugin.TARGET)) {
			return true;
		}
		if (!relation.getType().equals(dependencyType)) {
			return true;
		}			
		WizardDependencyDescriptor descriptor = getRelationDescriptor(relation.getType());
		CodeSyncElement source = getSource(parameters);
				
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
							CodeSyncPlugin.getInstance().getMessage("wizard.relationRequired", getRelationDescriptor(requiredType).getLabel()))
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
	
	protected CodeSyncElement createNewCodeSyncElement(Resource codeSyncMappingResource, WizardDependencyDescriptor descriptor, Map<String, Object> parameters) {
		CodeSyncElement codeSyncElement = CodeSyncOperationsService.getInstance().create(descriptor.getTargetCodeSyncTypes().get(0));

		for (AddNewExtension addNewExtension : CodeSyncPlugin.getInstance().getAddNewExtensions()) {
			if (!addNewExtension.configNew(codeSyncElement, codeSyncMappingResource, parameters)) {
				// element created, don't allow other extensions to perform add logic
				break;
			}
		}
		
		populateNewCodeSyncElement(codeSyncElement, descriptor, parameters);
				
		return codeSyncElement;
	}
	
	protected void populateNewCodeSyncElement(CodeSyncElement codeSyncElement, WizardDependencyDescriptor relationDescriptor, Map<String, Object> parameters) {
		CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(codeSyncElement.getType());
		CodeSyncOperationsService.getInstance().setKeyFeatureValue(
				codeSyncElement, 
				getKeyFeatureValue(relationDescriptor, getKeyFeatureValue(getSource(parameters))) + "." + descriptor.getExtension());
	}
	
	protected String getKeyFeatureValue(WizardDependencyDescriptor relationDescriptor, Object... keyFeatureFormatArgs) {
		 return String.format(relationDescriptor.getNewCodeSyncElementKeyFeatureFormat(), keyFeatureFormatArgs);	
	}
	
	protected WizardDependencyDescriptor getRelationDescriptor(String type) {
		return (WizardDependencyDescriptor) CodeSyncPlugin.getInstance().getRelationDescriptor(type);
	}
	
	protected Object getKeyFeatureValue(CodeSyncElement codeSyncElement) {
		return CodeSyncOperationsService.getInstance().getKeyFeatureValue(codeSyncElement);
	}

	protected CodeSyncElement getSource(Map<String, Object> parameters) {
		return (CodeSyncElement) parameters.get(CodeSyncPlugin.SOURCE);
	}

	/**
	 * If relation's source is {@link CodeSyncPlugin#WIZARD_ELEMENT},
	 * propagate adding relations to its children.
	 */
	@Override
	public boolean doAfterAddingRelationInModel(Relation relation, Map<String, Object> parameters) {		
		if (CodeSyncPlugin.WIZARD_ELEMENT.equals(relation.getSource().getType())) {
			for (CodeSyncElement wizardAttribute : relation.getSource().getChildren()) {
				WizardChildrenPropagatorProcessor.propagate(relation.getSource(), wizardAttribute);						
			}
		}
		
		return true;
	}
	
}
