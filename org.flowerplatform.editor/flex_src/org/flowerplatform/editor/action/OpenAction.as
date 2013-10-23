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
package  org.flowerplatform.editor.action {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.BasicEditorDescriptor;
	import org.flowerplatform.editor.EditorDescriptor;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.remote.ContentTypeDescriptor;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * Opens an editor. Exists directly in the context menu or
	 * as a child of "Open with" menu.
	 * 
	 * @author Cristi
	 * @author Mariana
     * @author Sorin
	 * 
	 */
	public class OpenAction extends ActionBase {
		
		public static const ICON_URL:String = EditorPlugin.getInstance().getResourceUrl("images/open_resource.png");
		
		/**
		 * This id set to the default open action (the top level action).
		 * Used to execute action at ENTER or double click event.
		 * 
		 * @author Cristina Constantinescu
		 */ 
		public static const DEFAULT_OPEN_ACTION_ID:String = "defaultOpenActionId";
		
		private var editorDescriptor:BasicEditorDescriptor;
		
		public var forceNewEditor:Boolean;
		
		/**
		 * There will be an editorEntry only when this action is added in the Open With submenu
		 * 
		 */ 
		public function OpenAction(editorDescriptor:BasicEditorDescriptor, forceNewEditor:Boolean):void {
			if (editorDescriptor == null) {
				// top level action: Open
				id = DEFAULT_OPEN_ACTION_ID;
				label = EditorPlugin.getInstance().getMessage("editor.open");
				icon = ICON_URL;
				preferShowOnActionBar = true;
				orderIndex = 150;
			} else {
				// child action of Open With
				this.editorDescriptor = editorDescriptor;
				this.forceNewEditor = forceNewEditor;
				label = editorDescriptor.getTitle();
				icon = editorDescriptor.getIcon();
				parentId = EditorTreeActionProvider.OPEN_WITH_ACTION_ID;
			}
		}
		
		/**
		 * @author Cristina Constatinescu
		 */
		override public function run():void {
			var currentEditorDescriptor:BasicEditorDescriptor = editorDescriptor;
			for (var i:int = 0; i < selection.length; i++) {
				if (currentEditorDescriptor == null) {
					// top level action: Open => search for the first editorDescriptor
					var treeNode:TreeNode = TreeNode(selection.getItemAt(0));
					var indexes:ArrayCollection = treeNode.customData[EditorPlugin.TREE_NODE_KEY_CONTENT_TYPE];
					for (var j:int = 0; j < indexes.length; j++) {
						var ctDescriptor:ContentTypeDescriptor = EditorPlugin.getInstance().contentTypeDescriptors[indexes.getItemAt(j)];
						if (ctDescriptor.defaultEditor != null) {
							currentEditorDescriptor = EditorPlugin.getInstance().getEditorDescriptorByName(ctDescriptor.defaultEditor);
							break;
						}
					}
					if (currentEditorDescriptor == null) {
						currentEditorDescriptor = EditorPlugin.getInstance().getEditorDescriptorByName(String(ctDescriptor.compatibleEditors.getItemAt(0)));
					}
				}
				var editableResourcePath:String = EditorPlugin.getInstance().getEditableResourcePathFromTreeNode(TreeNode(selection.getItemAt(i)));
				currentEditorDescriptor.openEditor(editableResourcePath, forceNewEditor);
			}
		}
	}
}
