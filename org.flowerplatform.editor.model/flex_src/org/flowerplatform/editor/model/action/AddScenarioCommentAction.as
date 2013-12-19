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
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class AddScenarioCommentAction extends TextInputAction {
		
		public function AddScenarioCommentAction() {
			super();
			
			label = "Add Comment";
			preferShowOnActionBar = true;
		}
		
		override public function get visible():Boolean {
			if (selection == null || selection.length == 1) {
				if (selection.getItemAt(0) is TreeNode) {
					return true;
				} 
			}
			return false;
		}
		
		override public function run():void {
			var node:TreeNode = TreeNode(selection.getItemAt(0));
			askForTextInput("Comment", "Add Comment", "Add",
				function(name:String):void {
					notationDiagramEditorStatefulClient.service_addNewComment(node.getPathForNode(), name);
				});
		}
	}
}