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

package org.flowerplatform.web.svn.common.action {
	
	import flash.desktop.Clipboard;
	import flash.desktop.ClipboardFormats;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.PathFragment;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	import org.flowerplatform.web.common.WebCommonPlugin;
	
	/**
	 * @author Cristina Necula
	 */
	
	public class CopyUrlToClipboardAction extends ActionBase {
		
		public function CopyUrlToClipboardAction() {
			
			label = "Copy URL to Clipboard";
			icon = WebCommonPlugin.getInstance().getResourceUrl("images/common/edit.png");
			
		}
		
		public override function get visible():Boolean {	
			
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				return (TreeNode(selection.getItemAt(0)).pathFragment.type == SvnCommonPlugin.NODE_TYPE_FILE) ||
					(TreeNode(selection.getItemAt(0)).pathFragment.type == SvnCommonPlugin.NODE_TYPE_REPOSITORY)
			}
			return false;
			
		}
		
		public override function run():void {
			
			// selected node
			var treeNode:TreeNode = TreeNode(selection.getItemAt(0));			
			// saves the url to clipboard
			Clipboard.generalClipboard.setData(ClipboardFormats.TEXT_FORMAT, newGetPath());
			
		}
		
		public function newGetPath(delimiter:String="/"):String {
			
			var treeNode:TreeNode = TreeNode(selection.getItemAt(0));
			var paths:ArrayCollection = treeNode.getPathForNode();
			var path:String = "";
			
			for each (var pathFragment:PathFragment in paths) {
				if (path != "") {
					path += delimiter;
				}
				if (pathFragment.type != SvnCommonPlugin.NODE_TYPE_ORGANIZATION && 
					pathFragment.type != SvnCommonPlugin.NODE_TYPE_SVN_REPOSITORIES) {
					path += pathFragment.name;
				}
			}
			return path;
			
		}
	
	}
}