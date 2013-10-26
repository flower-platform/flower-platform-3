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
	
	import flash.geom.Rectangle;
	
	import mx.collections.IList;
	import mx.core.FlexGlobals;
	import mx.core.UIComponent;
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.editor.model.NotationDiagramShell;
	import org.flowerplatform.editor.model.action.AddNewElementAction;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.emf_model.notation.View;
	
	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	public class AddNewCodeSyncElementAction extends AddNewElementAction {
		
		protected var codeSyncType:String;
		
		protected var createCodeSyncElement:Boolean;
		
		public function AddNewCodeSyncElementAction(codeSyncType:String, label:String, iconUrl:String, createCodeSyncElement:Boolean) {
			super();
			parentId = "new";
			this.codeSyncType = codeSyncType;
			this.label = label;
			this.icon = CodeSyncPlugin.getInstance().getResourceUrl(iconUrl);
			this.createCodeSyncElement = createCodeSyncElement;
		}
		
		override protected function showLocationForNewElementsDialog():Boolean {
			// show dialog only if diagram selected (actions for top level elements)
			// and if the codeSyncElement can be created on server side
			return createCodeSyncElement && selection.getItemAt(0) is Diagram;
		}
		
		override protected function createNewModelElement(location:String, selection:IList, context:Object):void {
			var selectedParentView:View = View(selection.getItemAt(0));
			var parentViewId:Object = null;
			if (selectedParentView is Node) {
				parentViewId = selectedParentView.id;
			}
			
			var parameters:Object = new Object();
			parameters.location = location;
			if (context.rectangle) {
				var rectangle:Rectangle = diagramShell.convertCoordinates(context.rectangle, UIComponent(FlexGlobals.topLevelApplication), UIComponent(diagramShell.diagramRenderer));
				parameters.x = rectangle.x;
				parameters.y = rectangle.y;
				if (!isNaN(rectangle.width)) {
					parameters.width = context.rectangle.width;
				}
				if (!isNaN(rectangle.height)) {
					parameters.height = context.rectangle.height;
				}				
			}
			
			NotationDiagramEditorStatefulClient(NotationDiagramShell(diagramShell).editorStatefulClient)
			.service_addNew(parentViewId, codeSyncType, parameters);			
		}			
	}
	
}