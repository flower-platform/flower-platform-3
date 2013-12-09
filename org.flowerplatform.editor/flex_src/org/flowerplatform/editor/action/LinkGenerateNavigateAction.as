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
package org.flowerplatform.editor.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.BasicEditorDescriptor;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.LinkGenerateNavigateView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class LinkGenerateNavigateAction extends ActionBase  {
				
		public function LinkGenerateNavigateAction() {
			label = EditorPlugin.getInstance().getMessage("link.navigate");
			icon = EditorPlugin.getInstance().getResourceUrl("images/external_link.png");
			orderIndex = 350;
		}
		
		override public function get visible():Boolean {
			if (selection.length == 0)  
				return false; // At least a node must be selected
			
			var currentEditorDescriptor:BasicEditorDescriptor;
			for (var i:int = 0; i < selection.length; i++) {
				// top level action: Open => search for the first editorDescriptor
				var treeNode:TreeNode = TreeNode(selection.getItemAt(0));
				var ctIndex:int = treeNode.customData[EditorPlugin.TREE_NODE_KEY_CONTENT_TYPE];				
				var foundEditorDescriptor:BasicEditorDescriptor = EditorPlugin.getInstance().getFirstEditorDescriptorForNode(ctIndex);
				if (foundEditorDescriptor == null || !foundEditorDescriptor.canCalculateFriendlyEditableResourcePath())
					return false; // node is not openable
			}			
			return true;
		}
		
		override public function run():void {
			var generateUrlView:LinkGenerateNavigateView = new LinkGenerateNavigateView();
			generateUrlView.explorerSelection = selection;
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(generateUrlView)
				.setWidth(550)
				.setHeight(400)
				.show();
		}
	}
}
