package org.flowerplatform.editor.mindmap.remote {
	
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayList;
	
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.stateful_service.ServiceInvocationOptions;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapDiagramEditorStatefulClient extends DiagramEditorStatefulClient	{
		
		private static const SERVICE_ID:String = "mindMapDiagramOperationsService";
		
		public var viewTypeToAcceptedViewTypeChildren:Dictionary = new Dictionary();
		
		public function MindMapDiagramEditorStatefulClient() {
			viewTypeToAcceptedViewTypeChildren["folder"] = new ArrayList(["folder", "page"]);
			viewTypeToAcceptedViewTypeChildren["page"] = new ArrayList(["heading 1", "heading 2", "heading 3", "paragraph"]);
			viewTypeToAcceptedViewTypeChildren["heading 1"] = new ArrayList(["heading 2", "heading 3", "paragraph"]);
			viewTypeToAcceptedViewTypeChildren["heading 2"] = new ArrayList(["heading 3", "paragraph"]);
			viewTypeToAcceptedViewTypeChildren["heading 3"] = new ArrayList(["paragraph"]);
//			viewTypeToAcceptedViewTypeChildren["headline 4"] = new ArrayList(["headline 5", "headline 6", "paragraph"]);
//			viewTypeToAcceptedViewTypeChildren["headline 5"] = new ArrayList(["headline 6", "paragraph"]);
//			viewTypeToAcceptedViewTypeChildren["headline 6"] = new ArrayList(["paragraph"]);
			viewTypeToAcceptedViewTypeChildren["paragraph"] = new ArrayList([]);
		}
		public override function getStatefulServiceId():String {	
			return "mindmapEditorStatefulService";
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
		
		public function service_createNew(viewId:Object, viewType:String):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand(SERVICE_ID, "createNew", [viewId, viewType]));
		}
	}
}