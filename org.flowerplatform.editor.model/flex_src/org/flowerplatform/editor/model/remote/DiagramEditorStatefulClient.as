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
		
		override protected function copyLocalDataFromExistingEditorToNewEditor(existingEditor:EditorFrontend, newEditor:EditorFrontend):void {
			super.copyLocalDataFromExistingEditorToNewEditor(existingEditor, newEditor);
			DiagramEditorFrontend(newEditor).diagramShell.rootModel = DiagramEditorFrontend(existingEditor).diagramShell.rootModel;
			
		}		
	
	}
}
