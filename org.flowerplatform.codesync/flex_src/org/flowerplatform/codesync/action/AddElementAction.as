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
	
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class AddElementAction extends ActionBase {
		
		protected var codeSyncType:String;
		
		public function AddElementAction(codeSyncType:String, iconUrl:String) {
			super();
			parentId = "new";
			this.codeSyncType = codeSyncType;
			label = codeSyncType;
			if (iconUrl != null) {
				// can't use getComposedImageUrl() because it adds the current plugin
				icon = "servlet/image-composer/" + iconUrl;
			}
		}
		
		override public function run():void {
			var parentViewId:Object = selection.length == 0 ? null : Node(selection.getItemAt(0)).id;
			
			var parameters:Object = {
				x : 200,
				y : 200,
				width : 200,
				height : 200,
				location : "src/a/b/"
			};
			
			NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE)
					.service_addNew(parentViewId, codeSyncType, parameters);
			
//			var type:String, keyParameter:String, isCategory:Boolean, parameters:Object, childType:String = template, nextSiblingSeparator:String, parentCategory:String;
//			
//			// test
//			if (template == "Table") {
//				type = "htmlFile";
//				keyParameter = "name";
//				isCategory = false;
//				parameters = {
//					"name" : "Companies.html",
//					"tableId" : "companies-list",
//					"headerRowId" : "companies-list-header"
//				};
//			} else if (template == "TableHeaderEntry") {
//				type = "htmlTableHeaderEntry";
//				keyParameter = "title";
//				isCategory = false;
//				parameters = { "title" : "Logo" };
//			} else if (template == "TableItem") {
//				type = "htmlFile";
//				keyParameter = "name";
//				isCategory = false;
//				parameters = {
//					"name" : "CompaniesTableItem.html",
//					"itemUrl" : "#company/<%= id %>"
//				};
//			} else if (template == "TableItemEntry") {
//				type = "htmlTableItemEntry";
//				keyParameter = "valueExpression";
//				isCategory = false;
//				parameters = {
//					"valueExpression" : "name"
//				};
//			} else if (template == "Form") {
//				type = "htmlFile";
//				keyParameter = "name";
//				isCategory = false;
//				parameters = {
//					"name" : "Company.html"
//				};
//			} else if (template == "FormItem") {
//				type = "htmlFormItem";
//				keyParameter = "title";
//				isCategory = false;
//				parameters = {
//					"title" : "Revenue",
//					"valueExpression" : "revenue",
//					"editId" : "revenue"
//				};
//			} else if (template == "BackboneClass") {
//				type = "jsFile";
//				keyParameter = "name";
//				isCategory = false;
//				parameters = {
//					"name" : "Company.js",
//					"superClass" : "Backbone.View"
//				};
//			} else if (template == "RequireEntry") {
//				type = "jsRequireEntry";
//				keyParameter = "varName";
//				isCategory = false;
//				parameters = {
//					"varName" : "Backbone",
//					"dependencyPath" : "backbone"
//				};
//				parentCategory = "Require";
//			} else if (template == "Attribute") {
//				type = "jsAttribute";
//				keyParameter = "name";
//				isCategory = false;
//				parameters = {
//					"name" : "name",
//					"defaultValue" : "John"
//				};
//				childType = "ClassMember";
//				nextSiblingSeparator = ",\r\n";
//				parentCategory = "Attribute";
//			} else if (template == "Operation") {
//				type = "jsOperation";
//				keyParameter = "name";
//				isCategory = false;
//				parameters = {
//					"name" : "render",
//					"parameters" : "param"
//				};
//				childType = "ClassMember";
//				nextSiblingSeparator = ",\r\n";
//				parentCategory = "Operation";
//			} else if (template == "EventsAttribute") {
//				// add a category too
//				NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE)
//						.service_addElement("jsEventsAttribute", "name", true, { "name" : "EventsAttribute" }, "EventsAttribute", null, null, parentViewId, null);
//				
//				type = "jsEventsAttribute";
//				keyParameter = "name";
//				isCategory = false;
//				parameters = {
//					"name" : "events"
//				};
//				childType = "ClassMember";
//				nextSiblingSeparator = ",\r\n";
//				parentCategory = "Attribute";
//			} else if (template == "EventsAttributeEntry") {
//				type = "jsEventsAttributeEntry";
//				keyParameter = "event";
//				isCategory = false;
//				parameters = {
//					"event" : "click",
//					"selector" : "#edit",
//					"function" : "editView"
//				};
//				nextSiblingSeparator = ",\r\n";
//			} else if (template == "RoutesAttribute") {
//				// add a category too
//				NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE)
//						.service_addElement("jsRoutesAttribute", "name", true, { "name" : "RoutesAttribute" }, "RoutesAttribute", null, null, parentViewId, null);
//				
//				type = "jsRoutesAttribute";
//				keyParameter = "name";
//				isCategory = false;
//				parameters = {
//					"name" : "routes"
//				};
//				childType = "ClassMember";
//				nextSiblingSeparator = ",\r\n";
//				parentCategory = "Attribute";
//			} else if (template == "RoutesAttributeEntry") {
//				type = "jsRoutesAttributeEntry";
//				keyParameter = "path";
//				isCategory = false;
//				parameters = {
//					"path" : "companies",
//					"function" : "companiesDetails"
//				};
//				nextSiblingSeparator = ",\r\n";
//			}
//			
//			NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE)
//					.service_addElement(type, keyParameter, isCategory, parameters, template, childType, nextSiblingSeparator, parentViewId, parentCategory);
		}
		
	}
}