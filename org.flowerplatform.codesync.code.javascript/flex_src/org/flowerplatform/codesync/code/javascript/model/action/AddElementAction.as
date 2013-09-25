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
	
	import org.flowerplatform.codesync.code.javascript.CodeSyncCodeJavascriptPlugin;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.ExpandableNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class AddElementAction extends ActionBase {
		
		protected var template:String;
		
		public function AddElementAction(template:String) {
			super();
			this.template = template;
			label = template;
			parentId = AddElementActionProvider.ADD_ELEMENT_PARENT_ID;
		}
		
		override public function run():void {
			var parentViewId:Object = selection.length == 0 ? null : ExpandableNode(selection.getItemAt(0)).id;
			
			var type:String, keyParameter:String, isCategory:Boolean, parameters:Object;
			
			NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE)
					.service_addElement(type, keyParameter, isCategory, parameters, template, parentViewId);
			
//			if (selection.length == 0) {
//				var parameters:Object = {
//					"name" : "Companies.html",
//					"tableId" : "companies-list",
//					"headerRowId" : "companies-list-header"
//				};
//				NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE)
//					.service_addElement("htmlFile", "name", false, parameters, "Table", null);
//			} else {
//				var id:Object = ExpandableNode(selection.getItemAt(0)).id;
//				NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE)
//					.service_addElement("tableHeaderEntry", "title", false, { "title" : "#" }, "TableHeaderEntry", id); 
//				NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE)
//					.service_addElement("tableHeaderEntry", "title", false, { "title" : "Logo" }, "TableHeaderEntry", id); 
//				NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE)
//					.service_addElement("tableHeaderEntry", "title", false, { "title" : "Name" }, "TableHeaderEntry", id); 
//				NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE)
//					.service_addElement("tableHeaderEntry", "title", false, { "title" : "Industry" }, "TableHeaderEntry", id); 
//				NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE)
//					.service_addElement("tableHeaderEntry", "title", false, { "title" : "Revenue" }, "TableHeaderEntry", id); 
//				NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE)
//					.service_addElement("tableHeaderEntry", "title", false, { "title" : "Employees" }, "TableHeaderEntry", id); 
//				NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE)
//					.service_addElement("tableHeaderEntry", "title", false, { "title" : "Action" }, "TableHeaderEntry", id); 
//			}
		}
		
	}
}