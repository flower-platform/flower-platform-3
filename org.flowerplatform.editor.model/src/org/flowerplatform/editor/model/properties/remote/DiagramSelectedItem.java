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
package org.flowerplatform.editor.model.properties.remote;

import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.properties.remote.SelectedItem;
/**
 * @author Tache Razvan Mihai
 * @return
 */
public class DiagramSelectedItem extends SelectedItem {
	
	private String xmiID;
		
	private String editorStatefulServiceId;

	private String diagramEditableResourcePath ;

	public DiagramSelectedItem(String xmiID, String editorStatefulServiceId, String diagramEditableResourcePath, String diagramType) {
		super();
		setItemType(diagramType);
		this.xmiID = xmiID;
		this.editorStatefulServiceId = editorStatefulServiceId;
		this.diagramEditableResourcePath = diagramEditableResourcePath;
	}
	
	public DiagramSelectedItem() {
		
	}
	
	public String getXmiID() {
		return xmiID;
	}

	public void setXmiID(String xmiID) {
		this.xmiID = xmiID;
	}

	public String getDiagramEditableResourcePath() {
		return diagramEditableResourcePath;
	}

	public void setDiagramEditableResourcePath(String diagramEditableResourcePath) {
		this.diagramEditableResourcePath = diagramEditableResourcePath;
	}

	public String getEditorStatefulServiceId() {
		return editorStatefulServiceId;
	}

	public void setEditorStatefulServiceId(String editorStatefulServiceId) {
		this.editorStatefulServiceId = editorStatefulServiceId;
	}
	/**
	 * @deprecated will be removed after the implementation of CodeSyncPropertiesProvider and the getFeatures
	 * is finished
	 * @return
	 */
	public DiagramEditorStatefulService getEditorStatefulService() {
		return  (DiagramEditorStatefulService)CommunicationPlugin.getInstance().getServiceRegistry().getService(editorStatefulServiceId);
	}
}
