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
package org.flowerplatform.codesync;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.editor.model.properties.remote.DiagramSelectedItem;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.View;
import org.flowerplatform.properties.providers.IPropertiesProvider;
import org.flowerplatform.properties.remote.Property;
import org.flowerplatform.properties.remote.SelectedItem;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Tache Razvan Mihai
 * @author Mariana Gheorghe
 */
public class CodeSyncPropertiesProvider implements IPropertiesProvider<DiagramSelectedItem, CodeSyncElement> {

	@Override
	public void setProperty(DiagramSelectedItem selectedItem, CodeSyncElement codeSyncElement,
			String propertyName, Object propertyValue) {
		if (codeSyncElement == null) {
			return;
		}
		CodeSyncOperationsService.getInstance().setFeatureValue(codeSyncElement, propertyName, propertyValue);
	}

	@Override
	public List<String> getPropertyNames(DiagramSelectedItem selectedItem, CodeSyncElement codeSyncElement) {
		
		List<String> features = CodeSyncOperationsService.getInstance().getFeatures(codeSyncElement.getType());
		CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(codeSyncElement.getType());
		if (descriptor.getKeyFeature() != null) {
			features.remove(descriptor.getKeyFeature());
		}
		
		return features;
	}

	@Override
	public Property getProperty(DiagramSelectedItem selectedItem, CodeSyncElement codeSyncElement,
			String propertyName) {
		return new Property()
				.setName(propertyName)
				.setValue(CodeSyncOperationsService.getInstance().getFeatureValue(codeSyncElement, propertyName))
				.setReadOnly(false);
	}


	@Override
	public CodeSyncElement resolveSelectedItem(DiagramSelectedItem selectedItem) {
		DiagramEditorStatefulService diagramEditorStatefulService = ((DiagramSelectedItem) selectedItem).getEditorStatefulService();		
		String diagramEditableResourcePath = ((DiagramSelectedItem) selectedItem).getDiagramEditableResourcePath();
		String id = ((DiagramSelectedItem) selectedItem).getXmiID();
		
		DiagramEditableResource diagramEditableResource = (DiagramEditableResource) diagramEditorStatefulService.getEditableResource(diagramEditableResourcePath);
		View view = (View) diagramEditableResource.getEObjectById(id);
		return (CodeSyncElement) view.getDiagrammableElement();
	}

	@Override
	public Pair<String, String> getIconAndLabel(DiagramSelectedItem selectedItem, CodeSyncElement codeSyncElement) {
		
		String icon = CodeSyncPlugin.getInstance().getResourceUrl(CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(codeSyncElement.getType()).getIconUrl());
		String label = codeSyncElement.getName();
		
		return new Pair<String, String>(icon, label);
	}

}
