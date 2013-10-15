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
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.flexutil.action.ActionBase;
//	import org.flowerplatform.web.common.WebCommonPlugin;
	
	[RemoteClass(alias="org.flowerplatform.codesync.remote.CodeSyncAction")]
	public class CodeSyncAction extends ActionBase {
		
		public var path:String;
		
		public var technology:String;
		
		public function CodeSyncAction(label:String, technology:String) {
			super();
			
			// TODO CS/FP2 msg
			this.label = label;
			this.technology = technology;
//			this.icon = WebCommonPlugin.getInstance().getResourceUrl("images/common/refresh.png");
		}
		
		override public function get visible():Boolean {
			if (selection == null || selection.length != 1) {
				return false;
			}
			return true;
		}
		
		
		override public function run():void {
			path = EditorPlugin.getInstance().getEditableResourcePathFromTreeNode(TreeNode(selection.getItemAt(0)));
			CommunicationPlugin.getInstance().bridge.sendObject(this);
		}
		
	}
}