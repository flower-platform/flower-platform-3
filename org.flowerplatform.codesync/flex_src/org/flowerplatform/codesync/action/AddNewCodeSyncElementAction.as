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
	
	import com.crispico.flower.mp.codesync.base.editor.CodeSyncEditorDescriptor;
	
	import flash.geom.Rectangle;
	
	import mx.collections.IList;
	import mx.core.FlexGlobals;
	import mx.core.UIComponent;
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
	import org.flowerplatform.editor.model.NotationDiagramShell;
	import org.flowerplatform.editor.model.action.AddNewElementAction;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.emf_model.notation.View;
	
	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 * @author Cristian Spiescu
	 */
	public class AddNewCodeSyncElementAction extends AddNewElementAction {
		
		protected var descriptor:CodeSyncElementDescriptor
		
		protected var initializationType:String;
		
		public function AddNewCodeSyncElementAction(descriptor:CodeSyncElementDescriptor, initializationType:String) {
			super();
			this.descriptor = descriptor;
			this.initializationType = initializationType;
			this.icon = CodeSyncPlugin.getInstance().getResourceUrl(descriptor.iconUrl);
			if (initializationType == null) {
				// either composed (if has subtypes), or executable (if no subtypes possible)
				parentId = "new";
				label = descriptor.label;
				id = descriptor.codeSyncType; // so that the subactions recognize it
				actAsNormalAction = descriptor.initializationTypes == null;
			} else {
				// executable for sure; set the right parentId; don't set id
				parentId = descriptor.codeSyncType;
				label = descriptor.initializationTypesLabels[descriptor.initializationTypes.getItemIndex(initializationType)];
				actAsNormalAction = true;
			}
		}
		
		override public function get visible():Boolean {
			return true;
		}
		
		override protected function showLocationForNewElementsDialog():Boolean {
			// show dialog only if diagram selected (actions for top level elements)
			// and if the codeSyncElement can be created on server side
			return selection.length == 1 && selection.getItemAt(0) is Diagram && 
				descriptor.codeSyncTypeCategories.getItemIndex(CodeSyncPlugin.CODE_SYNC_TYPE_CATEGORY_DONT_NEED_LOCATION) < 0;
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
			
			parameters[CodeSyncPlugin.CONTEXT_INITIALIZATION_TYPE] = initializationType;
			
			NotationDiagramEditorStatefulClient(NotationDiagramShell(diagramShell).editorStatefulClient)
				.service_addNew(parentViewId, descriptor.codeSyncType, parameters);			
		}			
	}
	
}