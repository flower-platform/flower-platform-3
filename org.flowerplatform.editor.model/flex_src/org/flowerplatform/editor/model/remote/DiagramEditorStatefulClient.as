package org.flowerplatform.editor.model.remote {
	import flash.events.Event;
	
	import mx.charts.chartClasses.InstanceCache;
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.stateful_service.ServiceInvocationOptions;
	import org.flowerplatform.communication.transferable_object.LocalIdTransferableObjectRegistry;
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.editor.model.DiagramEditorFrontend;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	import org.flowerplatform.emf_model.notation.View;
	
	public class DiagramEditorStatefulClient extends EditorStatefulClient {
		
		public static const VIEW_DETAILS_UPDATED_EVENT:String = "viewDetailsUpdated";
		
		protected var transferableObjectRegistry:LocalIdTransferableObjectRegistry = new LocalIdTransferableObjectRegistry();
		
		protected var diagramId:Object;
		
		// TODO CS/FP2 get rid of TEMP_INSTANCE
		public static var TEMP_INSTANCE:DiagramEditorStatefulClient;
		
		public override function getStatefulServiceId():String {	
			return "diagramEditorStatefulService";
		}
	
		override protected function areLocalUpdatesAppliedImmediately():Boolean	{
			return false;
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
		
		public function service_expandCompartment(viewId:Object):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand("classDiagramOperationsDispatcher", "expandCompartment_attributes", [viewId]));
		}
		
		public function service_addNew(viewId:Object, label:String):void {
			attemptUpdateContent(null, new InvokeServiceMethodServerCommand("classDiagramOperationsDispatcher", "addNew_attribute", [viewId, label]));
		}
		
		///////////////////////////////////////////////////////////////
		// @RemoteInvocation methods
		///////////////////////////////////////////////////////////////
		[RemoteInvocation]
		public function updateTransferableObjects(objectsToUpdate:ArrayCollection, objectsIdsToDispose:ArrayCollection, viewDetailsUpdates:ArrayCollection):void {
			if (objectsToUpdate != null) {
				transferableObjectRegistry.updateObjects(objectsToUpdate);
			}
			if (objectsIdsToDispose != null) {
				transferableObjectRegistry.removeAndDisposeObjects(objectsIdsToDispose);
			}
			if (viewDetailsUpdates != null) {
				for each (var update:ViewDetailsUpdate in viewDetailsUpdates) {
					var o:Object = transferableObjectRegistry.getObjectById(update.viewId);
					if (o == null || !(o is View)) {
						throw new Error("For id = " + update.viewId + " cannot find view (or is not a view): " + o);
					}
					var view:View = View(o);
					if (view.viewDetails == null) {
						view.viewDetails = update.viewDetails;
					} else {
						for (var key:String in update.viewDetails) {
							view.viewDetails[key] = update.viewDetails[key];
						}
					}
					view.dispatchEvent(new Event(VIEW_DETAILS_UPDATED_EVENT));
				}
			}
		}
		
		[RemoteInvocation]
		public function openDiagram(diagramId:Object):void {
			TEMP_INSTANCE = this;
			this.diagramId = diagramId;
			var diagram:Object = transferableObjectRegistry.getObjectById(diagramId);
			if (diagram == null) {
				throw new Error("Diagram not found in local registry, with id = ", diagramId);
			}
			for each (var ef:DiagramEditorFrontend in editorFrontends) {
				ef.diagramShell.rootModel = diagram;
			}
		}		

	}
}