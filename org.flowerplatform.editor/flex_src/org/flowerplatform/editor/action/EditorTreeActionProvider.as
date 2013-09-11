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
	import mx.collections.IList;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.BasicEditorDescriptor;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.remote.ContentTypeDescriptor;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.flexutil.popup.ComposedAction;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IActionProvider;

	public class EditorTreeActionProvider implements IActionProvider {
		
		public static const OPEN_WITH_ACTION_ID:String = "openWith";
		
		public function EditorTreeActionProvider() {
		}
		
		/**
		 * @author ?? (original author)
		 * @author Victor Badila
		 */
		public function getActions(selection:IList):Vector.<IAction> {
			var result:Vector.<IAction> = new Vector.<IAction>();
			
			if (selection.length == 1) {
				// add Open and Open with
				var treeNode:TreeNode = TreeNode(selection.getItemAt(0));
				if (treeNode.customData == null || treeNode.customData[EditorPlugin.TREE_NODE_KEY_CONTENT_TYPE] == null) {
					// node not openable
					return null;
				}
				result.push(new OpenAction(null, false));
				var openWithAction:ComposedAction = new ComposedAction();
				openWithAction.label = EditorPlugin.getInstance().getMessage("editor.openWith");
				openWithAction.icon = OpenAction.ICON_URL;
				openWithAction.id = OPEN_WITH_ACTION_ID;
				result.push(openWithAction);
				
				var ctIndex:int = treeNode.customData[EditorPlugin.TREE_NODE_KEY_CONTENT_TYPE];
				var ctDescriptor:ContentTypeDescriptor = EditorPlugin.getInstance().contentTypeDescriptors[ctIndex];
				var defaultEditorDescriptorProcessed:Boolean = false;
				for each (var editorName:String in ctDescriptor.compatibleEditors) {
					var descriptor:BasicEditorDescriptor = EditorPlugin.getInstance().getEditorDescriptorByName(editorName);
					if (descriptor == null) {
						throw new Error("Cannot find editor descriptor for editor name = " + editorName);
					}
					result.push(new OpenAction(descriptor, !defaultEditorDescriptorProcessed));
					if (!defaultEditorDescriptorProcessed) {
						// i.e. for index == 0; this ensures that the first item will have an action with "force..."
						defaultEditorDescriptorProcessed = true;
					}
				}
<<<<<<< HEAD
			} else {
=======
			} else {
>>>>>>> origin/GH93-Revert
				for (var i:int = 0; i < selection.length; i++) {
					if (TreeNode(selection.getItemAt(i)).customData == null || TreeNode(selection.getItemAt(i)).customData[EditorPlugin.TREE_NODE_KEY_CONTENT_TYPE] == null) {
						// found at least one node not openable
						return null;
					}
				}
				result.push(new OpenAction(null, false));
			}
			
			return result;
		}
		
	}
}
