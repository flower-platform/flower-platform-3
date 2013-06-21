package org.flowerplatform.editor.model.remote {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.transferable_object.LocalIdTransferableObjectRegistry;
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.editor.model.DiagramEditorFrontend;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	
	public class DiagramEditorStatefulClient extends EditorStatefulClient {
		
		protected var transferableObjectRegistry:LocalIdTransferableObjectRegistry = new LocalIdTransferableObjectRegistry();
		
		public override function getStatefulServiceId():String {	
			return "diagramEditorStatefulService";
		}
	
		override protected function areLocalUpdatesAppliedImmediately():Boolean	{
			return false;
		}
		
		///////////////////////////////////////////////////////////////
		// @RemoteInvocation methods
		///////////////////////////////////////////////////////////////
		[RemoteInvocation]
		public function updateTransferableObjects(objectsToUpdate:ArrayCollection, objectsIdsToDispose:ArrayCollection):void {
			if (objectsToUpdate != null) {
				transferableObjectRegistry.updateObjects(objectsToUpdate);
			}
			if (objectsIdsToDispose != null) {
				transferableObjectRegistry.removeAndDisposeObjects(objectsIdsToDispose);
			}
		}
		
		///////////////////////////////////////////////////////////////
		// @RemoteInvocation methods
		///////////////////////////////////////////////////////////////
		[RemoteInvocation]
		public function openDiagram(diagramId:Object):void {
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