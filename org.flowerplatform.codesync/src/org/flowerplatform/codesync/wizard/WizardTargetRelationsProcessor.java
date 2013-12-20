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
package org.flowerplatform.codesync.wizard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.flowerplatform.codesync.remote.CodeSyncDiagramOperationsService;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.codesync.remote.RelationDescriptor;
import org.flowerplatform.editor.model.changes_processor.Changes;
import org.flowerplatform.editor.model.changes_processor.IChangesProcessor;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.Relation;

/**
 * @author Cristina Constantinescu
 */
public class WizardTargetRelationsProcessor implements IChangesProcessor {

	private String wizardDependencyTypeForSource;
	
	private String wizardDependencyTypeForTarget;
	
	private String sourceCodeSyncName;
		
	public WizardTargetRelationsProcessor(String wizardDependencyTypeForSource, String wizardDependencyTypeForTarget, String sourceCodeSyncName) {		
		this.wizardDependencyTypeForSource = wizardDependencyTypeForSource;
		this.wizardDependencyTypeForTarget = wizardDependencyTypeForTarget;
		this.sourceCodeSyncName = sourceCodeSyncName;
	}

	@Override
	public void processChanges(Map<String, Object> context, EObject object, Changes changes) {
		ECrossReferenceAdapter adapter = ECrossReferenceAdapter.getCrossReferenceAdapter(object);
		for (Setting setting : new CopyOnWriteArrayList<Setting>(adapter.getNonNavigableInverseReferences(object))) {
			if (setting.getEObject() instanceof Relation) {
				Relation relation = (Relation) setting.getEObject();
				RelationDescriptor descriptor = CodeSyncPlugin.getInstance().getRelationDescriptor(relation.getType());
				if (descriptor instanceof WizardDependencyDescriptor) {
					CodeSyncElement source = null;
					CodeSyncElement target = null;
					if (((WizardDependencyDescriptor) descriptor).getType().equals(wizardDependencyTypeForSource)) {				
						source = relation.getTarget();
					} else if (((WizardDependencyDescriptor) descriptor).getType().equals(wizardDependencyTypeForTarget)) {
						target = relation.getTarget();
					}
									
					if (source == null) {
						Relation wizardDependency = CodeSyncOperationsService.getInstance().getRelation(relation.getSource(), wizardDependencyTypeForSource);	
						if (wizardDependency != null) {
							source = wizardDependency.getTarget();
						}
					}
								
					if (source != null) {
						if (sourceCodeSyncName != null) {
							for (CodeSyncElement child : source.getChildren()) {
								if (CodeSyncOperationsService.getInstance().getKeyFeatureValue(child).equals(sourceCodeSyncName)) {
									source = child;
									break;
								}
							}							
						}
					}
					
					if (target == null) {
						Relation wizardDependency = CodeSyncOperationsService.getInstance().getRelation(relation.getSource(), wizardDependencyTypeForTarget);	
						if (wizardDependency != null) {
							target = wizardDependency.getTarget();
						}
					}
					
					if (source != null && target != null) {
						List<RelationDescriptor> descriptors = CodeSyncPlugin.getInstance().getRelationDescriptorsHavingThisEndTypes(source.getType(), target.getType());
						for (RelationDescriptor relationDescriptor : descriptors) {
							Map<String, Object> parameters = new HashMap<String, Object>();		
							parameters.put(CodeSyncPlugin.SOURCE, source);
							parameters.put(CodeSyncPlugin.TARGET, target);
														
							
							if (CodeSyncOperationsService.getInstance().getRelation(source, relationDescriptor.getType()) != null) {
								return;
							}
							try {
								CodeSyncDiagramOperationsService.getInstance().addNewRelationElement(null, relationDescriptor.getType(), parameters);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}					
					}					
					
				}
			}
			
		}
	}
	
}
