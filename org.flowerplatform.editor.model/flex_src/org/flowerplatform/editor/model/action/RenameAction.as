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
			var text:String = node.viewDetails.label;
			var messageBox:IMessageBox = askForTextInput(text, "Rename", "Rename",
				function(name:String):void {
					NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_setInplaceEditorText(node.id, name);
				});
			
			// TODO this will be moved when we have an ied skin that includes the content assist list
			var parent:IVisualElementContainer = IVisualElementContainer(IVisualElementContainer(messageBox).getElementAt(1));
			var textInput:IVisualElement = parent.getElementAt(0);
			var contentAssist:ContentAssistList = new ContentAssistList();
			contentAssist.setDispatcher(textInput);
			contentAssist.setContentAssistProvider(new NotationDiagramContentAssistProvider(node.id));
			var index:int = parent.getElementIndex(textInput);
			parent.addElementAt(contentAssist, ++index);
			contentAssist.percentWidth = 100;
			contentAssist.height = 200;
			contentAssist.y = textInput.height;
//			contentAssist.includeInLayout = false;
			
			var action:ContentAssistAction = new ContentAssistAction();
			action.contentAssist = contentAssist;
			var button:Button = new Button();
			button.label = "Content Assist";
			button.addEventListener(MouseEvent.CLICK, function(evt:MouseEvent):void {
				action.run();
			});
			parent.addElement(button);
		}
	}
}