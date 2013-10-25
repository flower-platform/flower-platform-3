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
package org.flowerplatform.codesync.action {
	
	import flash.sampler.stopSampling;
	
	import org.flowerplatform.editor.model.NotationDiagramShell;
	import org.flowerplatform.editor.model.action.NewModelAction;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	public class AddElementAction extends NewModelAction {
		
		protected var codeSyncType:String;
		
		public function AddElementAction(codeSyncType:String, label:String, iconUrl:String) {
			super();
			parentId = "new";
			this.codeSyncType = codeSyncType;
			this.label = label;
			if (iconUrl != null) {
				// can't use getComposedImageUrl() because it adds the current plugin
				icon = "servlet/image-composer/" + iconUrl;
			}
		}
		
		override protected function createNewModelElement(location:String):void {
			var selectedParentView:View = View(storedSelection.getItemAt(0));
			var parentViewId:Object = null;
			if (selectedParentView is Node) {
				parentViewId = selectedParentView.id;
			}
			
			var parameters:Object = new Object();
			parameters.location = location;
			if (storedContext.rectangle) { // from drag to create tool
				parameters.x = storedContext.rectangle.x;
				parameters.y = storedContext.rectangle.y;
				parameters.width = storedContext.rectangle.width;
				parameters.height = storedContext.rectangle.height;
			} else {
				parameters.x = storedContext.x;
				parameters.y = storedContext.y;				
			}
			
			NotationDiagramEditorStatefulClient(NotationDiagramShell(diagramShell).editorStatefulClient)
			.service_addNew(parentViewId, codeSyncType, parameters);			
		}			
	}
	
}