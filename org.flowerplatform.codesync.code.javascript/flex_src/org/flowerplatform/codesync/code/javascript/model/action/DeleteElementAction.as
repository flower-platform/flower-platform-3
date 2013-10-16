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
	
	import org.flowerplatform.editor.model.action.DeleteAction;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Node;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class DeleteElementAction extends DeleteAction {
		
		public function DeleteElementAction() {
			super();
			
			label = "Delete Element";
		}
		
		override public function get visible():Boolean {
			return selection.length == 1;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_deleteElement(node.id);
		}
	}
}