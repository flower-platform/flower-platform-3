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
	import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexutil.action.ComposedAction;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class AddNewCodeSyncElementActionProvider implements IActionProvider {
		
		public static var ADD_ELEMENT_PARENT_ID:String = "addElement";
		
		public function getActions(selection:IList):Vector.<IAction> {
			if (selection.length > 1) {
				return null;
			}
			
			var result:Vector.<IAction> = new Vector.<IAction>();
			// by default topLevel
			var selectedCodeSyncElementType:String = "";
			if (selection.length == 1) {
				var view:View = View(selection.getItemAt(0));
				if (view is Node) {					
					selectedCodeSyncElementType = view.viewType.substr(view.viewType.lastIndexOf(".") + 1);
				} else if (!(view is Diagram)) { // don't show this actions for edges
					return null;
				}
			}
			
			for each (var availableCodeSyncElement:CodeSyncElementDescriptor in CodeSyncPlugin.getInstance().availableChildrenForCodeSyncType[selectedCodeSyncElementType]) {
				result.push(new AddNewCodeSyncElementAction(availableCodeSyncElement, null));
				if (availableCodeSyncElement.initializationTypes != null) {
					for each (var initializationType:String in availableCodeSyncElement.initializationTypes) {
						result.push(new AddNewCodeSyncElementAction(availableCodeSyncElement, initializationType));
					}
				}
			}		
			
			return result;
		}
	}
}
