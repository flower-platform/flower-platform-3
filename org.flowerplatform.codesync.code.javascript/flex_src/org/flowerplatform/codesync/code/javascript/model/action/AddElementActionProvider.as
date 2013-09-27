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
package org.flowerplatform.codesync.code.javascript.model.action {
	
	import mx.collections.IList;
	
	import org.flowerplatform.codesync.code.javascript.CodeSyncCodeJavascriptPlugin;
	import org.flowerplatform.emf_model.notation.ExpandableNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.flexutil.popup.ComposedAction;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IActionProvider;
	
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
			
			var addElementAction:ComposedAction = new ComposedAction();
			addElementAction.label = CodeSyncCodeJavascriptPlugin.getInstance().getMessage("diagram.addElement");
			addElementAction.id = ADD_ELEMENT_PARENT_ID;
			result.push(addElementAction);
			
			var selectedTemplate:String = "";
			if (selection.length == 1) {
				if (selection.getItemAt(0) is ExpandableNode) {
					selectedTemplate = ExpandableNode(selection.getItemAt(0)).template;
				} else {
					return null;
				}
			}
			
			for each (var availableTemplate:String in CodeSyncCodeJavascriptPlugin.getInstance().availableTemplates[selectedTemplate]) {
				result.push(new AddElementAction(availableTemplate));
			}
			
			if (selection.length == 1) {
				result.push(new DeleteElementAction());
			}
			
			return result;
		}
	}
}