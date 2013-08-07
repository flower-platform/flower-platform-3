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
package org.flowerplatform.editor.mindmap.controller {
	
	import flashx.textLayout.events.UpdateCompleteEvent;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolderList;
	import org.flowerplatform.communication.transferable_object.TransferableObjectDisposedEvent;
	import org.flowerplatform.communication.transferable_object.TransferableObjectUpdatedEvent;
	import org.flowerplatform.emf_model.notation.MindMapNode;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.UpdateConnectionEndsEvent;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapNodeChildrenController extends ControllerBase implements IModelChildrenController {
		
		private static const EMPTY_LIST:ArrayList = new ArrayList();
		
		public function MindMapNodeChildrenController(diagramShell:DiagramShell) {
			super(diagramShell);
		}		
		
		public function getParent(model:Object):Object {
			return View(model).parentView_RH.referencedObject;
		}
		
		public function getChildren(model:Object):IList	{
			// no children; this controller is used only to dispatch events
			return EMPTY_LIST;
		}
		
		public function beginListeningForChanges(model:Object):void {
			View(model).addEventListener(TransferableObjectUpdatedEvent.OBJECT_UPDATED, objectUpdatedHandler);
			View(model).addEventListener(TransferableObjectDisposedEvent.OBJECT_DISPOSED, objectDisposedHandler);
			View(model).addEventListener(UpdateConnectionEndsEvent.UPDATE_CONNECTION_ENDS, updateConnectionEndsHandler);
		}
		
		public function endListeningForChanges(model:Object):void {
			View(model).removeEventListener(TransferableObjectUpdatedEvent.OBJECT_UPDATED, objectUpdatedHandler);
			View(model).removeEventListener(TransferableObjectDisposedEvent.OBJECT_DISPOSED, objectDisposedHandler);
			View(model).removeEventListener(UpdateConnectionEndsEvent.UPDATE_CONNECTION_ENDS, updateConnectionEndsHandler);
		}
		
		protected function objectUpdatedHandler(event:TransferableObjectUpdatedEvent):void {		
			var model:MindMapNode = MindMapNode(event.object);	

			MindMapDiagramShell(diagramShell).refreshDiagramChildren();			
			MindMapDiagramShell(diagramShell).refreshNodePositions(model);	
			MindMapDiagramShell(diagramShell).shouldRefreshVisualChildren(diagramShell.rootModel);
		}
		
		protected function objectDisposedHandler(event:TransferableObjectDisposedEvent):void {
			var model:MindMapNode = MindMapNode(event.object);
						
			diagramShell.unassociateModelFromRenderer(model, diagramShell.getRendererForModel(model), true);

			MindMapDiagramShell(diagramShell).refreshDiagramChildren();
			
			if (diagramShell.getControllerProvider(model).getModelChildrenController(model).getParent(model) is MindMapNode) {
				MindMapDiagramShell(diagramShell).refreshNodePositions(diagramShell.getControllerProvider(model).getModelChildrenController(model).getParent(model));
			} else {
				var rootModel:Object = getChildren(diagramShell.rootModel).getItemAt(0);
				MindMapDiagramShell(diagramShell).refreshNodePositions(rootModel);
			}
			
			MindMapDiagramShell(diagramShell).shouldRefreshVisualChildren(diagramShell.rootModel);
		}
		
		protected function updateConnectionEndsHandler(event:UpdateConnectionEndsEvent):void {
			MindMapNodeRendererController(MindMapDiagramShell(diagramShell).getControllerProvider(event.target).getRendererController(event.target)).updateConnectors(event.target);
		}
		
		private function getDynamicObject(model:Object):Object {
			return DynamicModelExtraInfoController(diagramShell.getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model);
		}
	}
}