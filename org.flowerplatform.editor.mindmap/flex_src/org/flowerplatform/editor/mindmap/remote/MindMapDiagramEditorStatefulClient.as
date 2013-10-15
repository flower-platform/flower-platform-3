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
package org.flowerplatform.editor.mindmap.remote {
	
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.stateful_service.ServiceInvocationOptions;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.MindMapNode;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapDiagramEditorStatefulClient extends DiagramEditorStatefulClient	{
		
		private static const SERVICE_ID:String = "mindMapDiagramOperationsService";
		
		public var viewTypeToAcceptedViewTypeChildren:Dictionary = new Dictionary();
		
		public function MindMapDiagramEditorStatefulClient() {
			viewTypeToAcceptedViewTypeChildren["default"] = new ArrayList(["default"]);
//			viewTypeToAcceptedViewTypeChildren["folder"] = new ArrayList(["folder", "page"]);
//			viewTypeToAcceptedViewTypeChildren["page"] = new ArrayList(["heading 1", "heading 2", "heading 3", "paragraph"]);
//			viewTypeToAcceptedViewTypeChildren["heading 1"] = new ArrayList(["heading 2", "heading 3", "paragraph"]);
//			viewTypeToAcceptedViewTypeChildren["heading 2"] = new ArrayList(["heading 3", "paragraph"]);
//			viewTypeToAcceptedViewTypeChildren["heading 3"] = new ArrayList(["paragraph"]);
//			viewTypeToAcceptedViewTypeChildren["headline 4"] = new ArrayList(["headline 5", "headline 6", "paragraph"]);
//			viewTypeToAcceptedViewTypeChildren["headline 5"] = new ArrayList(["headline 6", "paragraph"]);
//			viewTypeToAcceptedViewTypeChildren["headline 6"] = new ArrayList(["paragraph"]);
//			viewTypeToAcceptedViewTypeChildren["paragraph"] = new ArrayList([]);
		}
		
		public override function getStatefulServiceId():String {	
			return "mindmapEditorStatefulService";
		}
				
		private function convertSelection(selection:IList):ArrayCollection {
			var array:ArrayCollection = new ArrayCollection();
			for (var i:int=0; i <selection.length; i++) {
				array.addItem(MindMapNode(selection.getItemAt(i)).id);
			}
			return array;
		}
		
		public function service_setSide(viewId:Object, side:int):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(SERVICE_ID, "setSide", [viewId, side]));
		}
		
		public function service_setExpanded(viewId:Object, expanded:Boolean):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(SERVICE_ID, "setExpanded", [viewId, expanded]));
		}
		
		public function service_addView(viewId:Object):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(SERVICE_ID, "addView", [viewId]));
		}
		
		public function service_setText(viewId:Object, text:String):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(SERVICE_ID, "setText", [viewId, text]));
		}
		
		public function service_changeParent(viewId:Object, parentViewId:Object, index:Number, side:int, callbackObject:Object, callbackFunction:Function):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(SERVICE_ID, "changeParent", [viewId, parentViewId, index, side], 
				callbackObject, callbackFunction));
		}
		
		public function service_createNode(viewId:Object, viewType:String, callbackObject:Object = null, callbackFunction:Function = null):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(SERVICE_ID, "createNode", [viewId, viewType], callbackObject, callbackFunction));
		}
		
		public function service_moveUp(viewId:Object, callbackObject:Object, callbackFunction:Function):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(SERVICE_ID, "moveUp", [viewId], callbackObject, callbackFunction));
		}
		
		public function service_moveDown(viewId:Object, callbackObject:Object, callbackFunction:Function):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(SERVICE_ID, "moveDown", [viewId], callbackObject, callbackFunction));
		}
		
		public function service_delete(viewId:Object):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(SERVICE_ID, "delete", [viewId]));
		}
		
		public function service_removeAllIcons(selection:IList, callbackObject:Object = null, callbackFunction:Function = null):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(SERVICE_ID, "removeAllIcons", [convertSelection(selection)], callbackObject, callbackFunction));
		}
		
		public function service_removeFirstIcon(selection:IList, callbackObject:Object = null, callbackFunction:Function = null):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(SERVICE_ID, "removeFirstIcon", [convertSelection(selection)], callbackObject, callbackFunction));
		}
		
		public function service_removeLastIcon(selection:IList, callbackObject:Object = null, callbackFunction:Function = null):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(SERVICE_ID, "removeLastIcon", [convertSelection(selection)], callbackObject, callbackFunction));
		}
		
		public function service_addIcon(selection:IList, icon:String, callbackObject:Object = null, callbackFunction:Function = null):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(SERVICE_ID, "addIcon", [convertSelection(selection), icon], callbackObject, callbackFunction));
		}
		
		public function service_setMinMaxWidth(selection:IList, minWidth:Number, minHeight:Number, callbackObject:Object = null, callbackFunction:Function = null):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(SERVICE_ID, "setMinMaxWidth", [convertSelection(selection), minWidth, minHeight], callbackObject, callbackFunction));
		}
	}
}