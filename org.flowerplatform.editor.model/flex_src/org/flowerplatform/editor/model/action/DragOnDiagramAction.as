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
package org.flowerplatform.editor.model.action {
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	public class DragOnDiagramAction extends ActionBase {
		public function DragOnDiagramAction() {
			// TODO CS/FP2 msg
			label = "Add to Diagram";
			preferShowOnActionBar = true;
			super();
		}
		
		override public function get visible():Boolean {
			return DiagramEditorStatefulClient.TEMP_INSTANCE != null;
		}
		
		override public function run():void {
			var pathsWithRoot:ArrayList = new ArrayList();
			for (var i:int = 0; i < selection.length; i++) {
				var treeNode:TreeNode = TreeNode(selection.getItemAt(i));
				var path:ArrayCollection = treeNode.getPathForNode(true);
				pathsWithRoot.addItem(path);
			}
			NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_handleDragOnDiagram(pathsWithRoot);
		}
		
	}
}