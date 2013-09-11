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
	
	import flash.events.MouseEvent;
	
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.editor.model.content_assist.NotationDiagramContentAssistProvider;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.flexutil.content_assist.ContentAssistList;
	import org.flowerplatform.flexutil.popup.IMessageBox;
	
	import spark.components.Button;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class RenameAction extends TextInputAction {
		public function RenameAction() {
			super();
			
			label = "Rename";
			icon = EditorModelPlugin.getInstance().getResourceUrl("images/rename.png");
			preferShowOnActionBar = true;
		}
		
		override public function get visible():Boolean {
			if (selection != null && selection.length == 1) {
				if (selection.getItemAt(0) is Node) {
					return true;
				}
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			
			NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_getInplaceEditorText(node.id,
				function(labelForEditing:String):void {
					
					var messageBox:IMessageBox = askForTextInput(labelForEditing, "Rename", "Rename",
						function(name:String):void {
							NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_setInplaceEditorText(node.id, name);
						});
				});
		}
	}
}