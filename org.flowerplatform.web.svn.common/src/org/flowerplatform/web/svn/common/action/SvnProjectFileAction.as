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
	
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	
	/**
	 * @author Victor Badila
	 */ 
	public class SvnProjectFileAction extends ActionBase {
		
		public function SvnProjectFileAction() {
			super();
		}
		
		public override function get visible():Boolean {
			for (var i:int=0; i<selection.length; i++) {
				var currentSelection:Object = selection.getItemAt(i);
				if (!(currentSelection is TreeNode)) {
					return false;
				}
				if (currentSelection.customData == null ||
					currentSelection.customData.svnFileType == null ||
					currentSelection.customData.svnFileType == false) {
					return false;
				}
			}			
			return true;
		}
	}
}