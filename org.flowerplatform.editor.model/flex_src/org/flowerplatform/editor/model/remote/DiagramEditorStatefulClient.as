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