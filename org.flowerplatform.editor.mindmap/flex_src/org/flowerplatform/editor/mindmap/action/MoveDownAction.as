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
	import flash.profiler.showRedrawRegions;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.flowerplatform.editor.mindmap.MindMapModelPlugin;
	import org.flowerplatform.editor.mindmap.NotationMindMapDiagramShell;
	import org.flowerplatform.editor.mindmap.remote.MindMapDiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.MindMapNode;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexutil.action.ActionBase;

	/**
	 * @author Cristina Constantinescu
	 */ 
	public class MoveDownAction extends ActionBase {
		
		public function MoveDownAction() {			
			label = MindMapModelPlugin.getInstance().getMessage("movedown.action.label");
			icon = MindMapModelPlugin.getInstance().getResourceUrl("images/down.png");		
			preferShowOnActionBar = true;
		}
			
		override public function get visible():Boolean {			
			if (selection != null && selection.length == 1) {
				return true;	
			}			
			return false;
		}
		
		override public function run():void {			
			var selectedNode:MindMapNode = MindMapNode(selection.getItemAt(0));
			MindMapDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_moveDown(
				selectedNode.id, 
				this, selectModelAfterMovingHandler);			
		}
		
		private function selectModelAfterMovingHandler(value:Object):void {
//			diagramShell.selectedItems.removeAll();
//			diagramShell.selectedItems.addItem(value);
		}
	}
}