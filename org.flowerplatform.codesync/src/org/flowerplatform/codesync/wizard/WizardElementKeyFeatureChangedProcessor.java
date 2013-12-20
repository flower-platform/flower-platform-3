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

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.codesync.remote.RelationDescriptor;
import org.flowerplatform.editor.model.changes_processor.Changes;
import org.flowerplatform.editor.model.changes_processor.IChangesProcessor;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.Relation;

/**
 * @author Cristina Constantinescu
 */
public class WizardElementKeyFeatureChangedProcessor implements IChangesProcessor {
	
	@Override
	public void processChanges(Map<String, Object> context, EObject object,	Changes changes) {
		if (changes.getRemovedFromContainer() != null) {
			// wizard attribute deleted; don't do anything
			return;
		}
		
		if (changes.getFeatureChanges() != null) {
			for (FeatureChange featureChange : changes.getFeatureChanges()) {
				if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_FeatureChanges().equals(featureChange.getFeature())) {
					CodeSyncElement wizardElement = (CodeSyncElement) object;
					for (Relation relation : wizardElement.getRelations()) {
						RelationDescriptor descriptor = CodeSyncPlugin.getInstance().getRelationDescriptor(relation.getType());
						if (descriptor instanceof WizardDependencyDescriptor) {
							getService().setKeyFeatureValue(
									relation.getTarget(),
									String.format(((WizardDependencyDescriptor) descriptor).getNewCodeSyncElementKeyFeatureFormat(), getService().getKeyFeatureValue(wizardElement)));
						}
					}
				}
			}
		}
	}

	private CodeSyncOperationsService getService() {
		return CodeSyncOperationsService.getInstance();
	}
	
}
