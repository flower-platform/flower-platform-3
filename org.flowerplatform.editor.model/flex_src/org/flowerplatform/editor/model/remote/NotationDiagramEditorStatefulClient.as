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
package org.flowerplatform.editor.model.remote {
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.stateful_service.ServiceInvocationOptions;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.model.remote.scenario.ScenarioTreeStatefulClient;

	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 * @author Mariana Gheorghe
	 */
	public class NotationDiagramEditorStatefulClient extends DiagramEditorStatefulClient {
		
		public var scenarioTreeStatefulClient:ScenarioTreeStatefulClient;
		
		[RemoteInvocation]
		public function updateNode(path:ArrayCollection, newNode:TreeNode, expandNode:Boolean = false, collapseNode:Boolean = false, selectNode:Boolean = false):void {			
			scenarioTreeStatefulClient.updateNode(path, newNode, expandNode, collapseNode, selectNode);
		}
		
		///////////////////////////////////////////////////////////////
		// Proxies to service methods
		///////////////////////////////////////////////////////////////
		
		public function service_handleDragOnDiagram(pathsWithRoot:IList):void {
			attemptUpdateContent(null, invokeServiceMethod("handleDragOnDiagram", [pathsWithRoot, diagramId], new ServiceInvocationOptions().setReturnCommandWithoutSending(true)));
		}
		
		public function service_setInplaceEditorText(viewId:Object, text:String):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand("classDiagramOperationsDispatcher", "setInplaceEditorText", [viewId, text]));
			//			attemptUpdateContent(null, invokeServiceMethod("setInplaceEditorText", [viewId, text], new ServiceInvocationOptions().setReturnCommandWithoutSending(true)));
		}
		
		public function service_getInplaceEditorText(viewId:Object, callbackFunction:Function):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand("classDiagramOperationsDispatcher", "getInplaceEditorText", [viewId], null, callbackFunction));
		}
		
		public function service_collapseCompartment(viewId:Object):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand("classDiagramOperationsDispatcher", "collapseCompartment", [viewId]));
		}
		
		public function service_expandCompartment_attributes(viewId:Object):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand("classDiagramOperationsDispatcher", "expandCompartment_attributes", [viewId]));
		}
		
		public function service_expandCompartment_operations(viewId:Object):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand("classDiagramOperationsDispatcher", "expandCompartment_operations", [viewId]));
		}
		
		public function service_addNew(method:String, viewId:Object, label:String):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand("classDiagramOperationsDispatcher", method, [viewId, label]));
		}
		
		public function service_deleteView(viewId:Object):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand("classDiagramOperationsDispatcher", "deleteView", [viewId]));
		}
		
		public function service_contentAssist(viewId:Object, pattern:String, callbackFunction:Function):void {
			var options:ServiceInvocationOptions = new ServiceInvocationOptions().setReturnCommandWithoutSending(true).setResultCallbackFunction(callbackFunction);
			attemptUpdateContent(null, invokeServiceMethod("contentAssist", [viewId, pattern], options));
		}
		
		public function service_addNewConnection(sourceViewId:Object, targetViewId:Object):void {
			attemptUpdateContent(null, invokeServiceMethod("addNewConnection", [editableResourcePath, diagramId, sourceViewId, targetViewId], new ServiceInvocationOptions().setReturnCommandWithoutSending(true)));
		}

		public function service_addNewScenario(name:String):void {
			attemptUpdateContent(null, invokeServiceMethod("addNewScenario", [editableResourcePath, name], new ServiceInvocationOptions().setReturnCommandWithoutSending(true)));
		}
		
		public function service_openScenarioNode(path:ArrayCollection, context:Object):void {
			attemptUpdateContent(null, invokeServiceMethod("openNode", [path, context], new ServiceInvocationOptions().setReturnCommandWithoutSending(true)));
		}
		
		public function service_addNewComment(path:ArrayCollection, comment:String):void {
			attemptUpdateContent(null, invokeServiceMethod("addNewComment", [path, editableResourcePath, comment, scenarioTreeStatefulClient.context], new ServiceInvocationOptions().setReturnCommandWithoutSending(true)));
		}
		
		public function service_deleteScenarioElement(path:ArrayCollection):void {
			attemptUpdateContent(null, invokeServiceMethod("deleteScenarioElement", [path, editableResourcePath, scenarioTreeStatefulClient.context], new ServiceInvocationOptions().setReturnCommandWithoutSending(true)));
		}
		
		public function service_expandCollapseCompartment(viewId:Object, expand:Boolean):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand("jsClassDiagramOperationsDispatcher", "expandCollapseCompartment", [viewId, expand]));
		}
		
		public function service_addElement(type:String, keyParameter:String, isCategory:Boolean, parameters:Object, template:String, childType:String, nextSiblingSeparator:String, parentViewId:Object, parentCategory:String):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand("jsClassDiagramOperationsDispatcher", "addElement", [type, keyParameter, isCategory, parameters, template, childType, nextSiblingSeparator, parentViewId, parentCategory], new ServiceInvocationOptions().setReturnCommandWithoutSending(true)));
		}
		
		public function service_deleteElement(viewId:Object):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand("jsClassDiagramOperationsDispatcher", "deleteElement", [viewId]));
		}
	}
}
