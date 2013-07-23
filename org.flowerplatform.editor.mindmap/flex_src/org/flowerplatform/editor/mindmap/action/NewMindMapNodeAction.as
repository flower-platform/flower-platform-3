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
package org.flowerplatform.editor.mindmap.action {
	import mx.collections.ArrayList;
	
	import org.flowerplatform.editor.mindmap.remote.MindMapDiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.MindMapNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class NewMindMapNodeAction extends ActionBase {
		
		protected var viewType:String;
		
		public function NewMindMapNodeAction() {
			super();
		}
		
		protected function getAcceptedViewTypes(model:MindMapNode):ArrayList {
			return ArrayList(MindMapDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).viewTypeToAcceptedViewTypeChildren[model.viewType]);
		}
		
		override public function get visible():Boolean {			
			if (selection != null && selection.length == 1) {
				var selectedNode:MindMapNode = MindMapNode(selection.getItemAt(0));
				return getAcceptedViewTypes(selectedNode) != null && getAcceptedViewTypes(selectedNode).getItemIndex(viewType) != -1;
			}			
			return false;
		}
		
		override public function run():void {			
			var selectedNode:MindMapNode = MindMapNode(selection.getItemAt(0));			
			MindMapDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_createNew(selectedNode.id, viewType);
		}
			
	}
}