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
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
	import org.flowerplatform.communication.transferable_object.ReferenceHolder;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ExpandCompartmentActionProvider implements IActionProvider {
		
		public function getActions(selection:IList):Vector.<IAction> {
			var result:Vector.<IAction> = new Vector.<IAction>();
			if (selection.length != 1) {
				return result;
			}
			
			if (selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				var codeSyncType:String = node.viewType.substr(node.viewType.lastIndexOf(".") + 1);
				var availableChildren:ArrayCollection = CodeSyncPlugin.getInstance().availableChildrenForCodeSyncType[codeSyncType];
				var categories:ArrayCollection = new ArrayCollection();
				for each (var descriptor:CodeSyncElementDescriptor in availableChildren) {
					var category:String = descriptor.category;
					if (!categories.contains(category)) {
						var alreadyExpanded:Boolean = false;
						for each (var ref:ReferenceHolder in node.persistentChildren_RH) {
							// this action provider is called also when new info arrives from server
							// because changing the list of children_RH triggers a selection change
							// and at that time the RHs for the children have not been processed yet
							if (ref.registry != null) {
								var child:Node = Node(ref.referencedObject);
								if (child.viewDetails != null && category == child.viewDetails["title"]) {
									alreadyExpanded = true;
									break;
								}
							}
						}
						if (!alreadyExpanded) {
							categories.addItem(category);
						}
					}
				}
				
				for each (var category:String in categories) {
					var action:ExpandCompartmentAction = new ExpandCompartmentAction(category);
					result.push(action);
				}
			}
			
			return result;
		}
	}
}