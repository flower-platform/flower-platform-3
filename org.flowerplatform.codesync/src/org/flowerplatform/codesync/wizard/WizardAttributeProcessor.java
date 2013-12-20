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

import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.codesync.remote.CodeSyncDiagramOperationsService;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.codesync.remote.RelationDescriptor;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.editor.model.changes_processor.Changes;
import org.flowerplatform.editor.model.changes_processor.IChangesProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.Relation;

/**
 * @author Cristina Constantinescu
 */
public class WizardAttributeProcessor implements IChangesProcessor {
	
	private final static Logger logger = LoggerFactory.getLogger(WizardAttributeProcessor.class);
	
	@Override
	public void processChanges(Map<String, Object> context, EObject object, Changes changes) {
		if (changes.getRemovedFromContainer() != null) {
			// wizard attribute deleted; don't do anything
			return;
		}
		
		EObject parent = changes.getAddedToContainer();
		if (parent != null) {
			CodeSyncElement wizardElement = (CodeSyncElement) parent;
			CodeSyncElement wizardAttribute = (CodeSyncElement) object;
			
			List<RelationDescriptor> descriptors = CodeSyncPlugin.getInstance().getRelationDescriptorsHavingThisTypeAsSourceCodeSyncType(wizardAttribute.getType());
			for (RelationDescriptor descriptor : descriptors) {
				for (String requiredDependencyType : ((WizardDependencyDescriptor) descriptor).getRequiredWizardDependencyTypes()) {
					Relation relation = CodeSyncOperationsService.getInstance().getRelation(wizardElement, requiredDependencyType);
					if (relation != null) {
						Map<String, Object> parameters = new HashMap<String, Object>();		
						parameters.put(CodeSyncPlugin.SOURCE, object);
							
						CodeSyncDiagramOperationsService service = (CodeSyncDiagramOperationsService) 
								CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncDiagramOperationsService.ID);
						
						try {
							service.addNewRelationElement(null, descriptor.getType(), parameters);
						} catch (Exception e) {			
							logger.error("Exception thrown while adding wizard dependency!", e.getMessage());
						}		
					}
				}
			}			
		}		
	}

}
