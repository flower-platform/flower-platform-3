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
package org.flowerplatform.editor.model.action {
	
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.flexutil.popup.ActionBase;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ExpandCompartmentAction extends ActionBase {
		
		public function ExpandCompartmentAction() {
			super();
			
			icon = EditorModelPlugin.getInstance().getResourceUrl("images/obj16/expandall.gif");
		}
		
		override public function get visible():Boolean {
			if (selection != null && selection.length == 1) {
				if (selection.getItemAt(0) is Node) {
					var node:Node = Node(selection.getItemAt(0));
					if (node.viewType == "class") {
						return true;
					}
				}
			}
			return false;
		}
	}
}