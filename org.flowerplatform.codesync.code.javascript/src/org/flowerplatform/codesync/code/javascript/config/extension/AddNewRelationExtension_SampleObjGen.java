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
import org.flowerplatform.codesync.code.javascript.config.JavaScriptDescriptors;
import org.flowerplatform.codesync.config.extension.AddNewExtension_TopLevelElement;
import org.flowerplatform.codesync.config.extension.AddNewRelationExtension;
import org.flowerplatform.codesync.remote.CodeSyncDiagramOperationsService1;
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.Relation;

/**
 * @author Cristina Constantinescu
 */
public class AddNewRelationExtension_SampleObjGen implements AddNewRelationExtension {

	@Override
	public boolean addNew(Relation relation, Resource codeSyncMappingResource, Map<String, Object> parameters) {	
		if (!relation.getType().equals(JavaScriptDescriptors.DEPENDENCY_SAMPLE_OBJ_GEN)) {
			return true;
		}
		
		CodeSyncElement parent = AddNewExtension_TopLevelElement.getOrCreateCodeSyncElementForLocation(codeSyncMappingResource, ((String) parameters.get("Location")).split("/"));
		CodeSyncElement child = CodeSyncOperationsService.getInstance().create(JavaScriptDescriptors.TYPE_BACKBONE_CLASS);
		CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(child.getType());
		CodeSyncOperationsService.getInstance().setKeyFeatureValue(child, ((String) parameters.get("Name")) + "." + descriptor.getExtension());
		CodeSyncOperationsService.getInstance().add(parent, child);
		
		parameters.put(CodeSyncDiagramOperationsService1.TARGET, child);
		
		return true;
	}

}
