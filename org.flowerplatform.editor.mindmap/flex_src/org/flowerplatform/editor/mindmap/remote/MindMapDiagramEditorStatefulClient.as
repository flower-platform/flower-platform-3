package org.flowerplatform.editor.mindmap.remote {
	
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.stateful_service.ServiceInvocationOptions;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapDiagramEditorStatefulClient extends DiagramEditorStatefulClient	{
		
		private static const SERVICE_ID:String = "mindMapDiagramOperationsService";
		
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
				new ServiceInvocationOptions().setResultCallbackObject(callbackObject).setResultCallbackFunction(callbackFunction)));
		}
	}
}