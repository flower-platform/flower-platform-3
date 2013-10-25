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
package org.flowerplatform.codesync.remote {
	import mx.collections.IList;
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.model.NotationDiagramShell;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	[RemoteClass(alias="org.flowerplatform.codesync.remote.CodeSyncAction")]
	public class CodeSyncAction extends ActionBase implements IDiagramShellAware {
		
		public var path:String;
		
		public var technology:String;
		
		private var _diagramShell:DiagramShell;
		
		public function CodeSyncAction(label:String, technology:String) {
			super();
			
			this.label = label;
			this.technology = technology;
			this.icon = CodeSyncPlugin.getInstance().getResourceUrl("images/Synchronize.gif");
			
			preferShowOnActionBar = true;
		}
		
		public function get diagramShell():DiagramShell {		
			return _diagramShell;
		}
		
		public function set diagramShell(value:DiagramShell):void {
			_diagramShell = value;
		}
		
		private function get diagram():Diagram {
			return Diagram(NotationDiagramShell(diagramShell).rootModel);
		}
		
		override public function get visible():Boolean {
//			if (selection == null || selection.length != 1) {
//				return false;
//			}
			return true;
		}
		
		override public function run():void {
			if (selection.getItemAt(0) is TreeNode) {
				path = EditorPlugin.getInstance().getEditableResourcePathFromTreeNode(TreeNode(selection.getItemAt(0)));
//				CommunicationPlugin.getInstance().bridge.sendObject(this);
			} else if (selection.getItemAt(0) is View) {
				var editorStatefulClient:NotationDiagramEditorStatefulClient = 
					NotationDiagramEditorStatefulClient(NotationDiagramShell(diagramShell).editorStatefulClient);
				path = editorStatefulClient.editableResourcePath;
				editorStatefulClient.service_synchronize(path, technology);
			}
		}
		
	}
}