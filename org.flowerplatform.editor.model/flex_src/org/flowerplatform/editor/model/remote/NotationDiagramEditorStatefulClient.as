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
		
		public function service_addNewConnection(sourceViewId:Object, targetViewId:Object):void {
			attemptUpdateContent(null, invokeServiceMethod("addNewConnection", [editableResourcePath, diagramId, sourceViewId, targetViewId], new ServiceInvocationOptions().setReturnCommandWithoutSending(true)));
		}

		public function service_addNewScenario(name:String):void {
			attemptUpdateContent(null, invokeServiceMethod("addNewScenario", [editableResourcePath, name], new ServiceInvocationOptions().setReturnCommandWithoutSending(true)));
		}
		
		public function service_openScenarioNode(path:ArrayCollection, context:Object):void {
			attemptUpdateContent(null, invokeServiceMethod("openNode", [path, context], new ServiceInvocationOptions().setReturnCommandWithoutSending(true)));
		}
	}
}