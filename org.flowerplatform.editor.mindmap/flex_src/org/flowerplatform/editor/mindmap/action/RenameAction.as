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
	
	import org.flowerplatform.editor.mindmap.MindMapModelPlugin;
	import org.flowerplatform.editor.mindmap.NotationMindMapDiagramShell;
	import org.flowerplatform.editor.mindmap.remote.MindMapDiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.editor.model.action.TextInputAction;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.MindMapNode;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	
	public class RenameAction extends TextInputAction {
		
		public function RenameAction() {
			super();
			
			label = MindMapModelPlugin.getInstance().getMessage("rename.action.label");
			icon = EditorModelPlugin.getInstance().getResourceUrl("images/rename.png");	
			preferShowOnActionBar = true;
		}
		
		override public function get visible():Boolean {
			if (selection != null && selection.length == 1) {
				var node:MindMapNode = MindMapNode(selection.getItemAt(0));
				return node.viewDetails != null && node.viewDetails.side != MindMapDiagramShell.NONE;
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var text:String = node.viewDetails.text;
			askForTextInput(text, "Rename", "Rename",
				function(name:String):void {
					MindMapDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_setText(node.id, name);
				});
		}
	}
}