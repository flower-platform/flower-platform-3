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
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.editor.model.NotationDiagramShell;
	import org.flowerplatform.editor.model.action.DiagramShellAwareActionBase;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.emf_model.notation.View;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ExpandCompartmentAction extends DiagramShellAwareActionBase {
		
		private var category:String;
		
		private function get diagram():Diagram {
			return Diagram(NotationDiagramShell(diagramShell).rootModel);
		}
		
		public function ExpandCompartmentAction(category:String) {
			super();
			
			this.label = CodeSyncPlugin.getInstance().getMessage("action.expandCompartment", [category]);
			this.category = category;			
			icon = EditorModelPlugin.getInstance().getResourceUrl("images/obj16/expandall.gif");
			orderIndex = 700;
		}
		
		override public function get visible():Boolean {
			if (selection != null && selection.length == 1) {
				if (selection.getItemAt(0) is Node) {
					var node:Node = Node(selection.getItemAt(0));
//					if (node.viewType == "class") {
						return true;
//					}
				}
			}
			return false;
		}
		
		override public function run():void {
			var view:View = View(selection.getItemAt(0));
			notationDiagramEditorStatefulClient.service_expandCompartment(view.id, category);
		}
		
	}
}