package org.flowerplatform.editor.model.remote {
	
	import mx.collections.IList;
	
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.stateful_service.ServiceInvocationOptions;

	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class NotationDiagramEditorStatefulClient extends DiagramEditorStatefulClient {
		
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
		
		public function service_addNew(viewId:Object, label:String):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand("classDiagramOperationsDispatcher", "addNew_attribute", [viewId, label]));
		}
	}
}