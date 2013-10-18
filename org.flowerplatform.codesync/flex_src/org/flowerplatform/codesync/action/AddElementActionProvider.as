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
package org.flowerplatform.codesync.action {
	
	import mx.collections.IList;
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.flexutil.action.ComposedAction;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class AddElementActionProvider implements IActionProvider {
		
		public static var ADD_ELEMENT_PARENT_ID:String = "addElement";
		
		public function AddElementActionProvider() {
			
		}
		
		public function getActions(selection:IList):Vector.<IAction> {
			if (selection.length > 1) {
				return null;
			}
			
			var result:Vector.<IAction> = new Vector.<IAction>();
			
			var selectedCodeSyncElementType:String = "";
			if (selection.length == 1) {
				if (selection.getItemAt(0) is Node) {
					var node:Node = Node(selection.getItemAt(0));
					selectedCodeSyncElementType = node.viewType.substr(node.viewType.lastIndexOf(".") + 1);
				} else {
					return null;
				}
			}
			
			for each (var availableCodeSyncElementType:String in CodeSyncPlugin.getInstance().availableChildrenForCodeSyncType[selectedCodeSyncElementType]) {
				result.push(new AddElementAction(availableCodeSyncElementType));
			}
			
			return result;
		}
	}
}