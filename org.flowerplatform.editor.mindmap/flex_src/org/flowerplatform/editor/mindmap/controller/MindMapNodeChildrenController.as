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
package org.flowerplatform.editor.mindmap.controller
{
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.UIComponent;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolderList;
	import org.flowerplatform.communication.transferable_object.TransferableObjectDisposedEvent;
	import org.flowerplatform.communication.transferable_object.TransferableObjectUpdatedEvent;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.MindMapNode;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelChildrenController;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	
	public class MindMapNodeChildrenController extends ControllerBase implements IMindMapModelChildrenController {
		
		public function MindMapNodeChildrenController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function getChildrenBasedOnSide(model:Object, side:int = 0):IList /* of MindMapNode */ {
			if (side == 0) {
				side = model.side;
			}
			var list:ArrayList = new ArrayList();			
			for (var i:int = 0; i < getChildren(model).length; i++) {
				var child:MindMapNode = MindMapNode(getChildren(model).getItemAt(i));
				if (side == 0 || side == child.side) {
					list.addItem(child);
				}
			}
			return list;
		}
		
		public function getParent(model:Object):Object {
			return View(model).parentView_RH.referencedObject;
		}
		
		public function getChildren(model:Object):IList	{
			return new ReferenceHolderList(View(model).persistentChildren_RH);
		}
		
		public function beginListeningForChanges(model:Object):void {
			View(model).addEventListener(TransferableObjectUpdatedEvent.OBJECT_UPDATED, objectUpdatedHandler);
			View(model).addEventListener(TransferableObjectDisposedEvent.OBJECT_DISPOSED, objectDisposedHandler);
		}
		
		public function endListeningForChanges(model:Object):void {
			View(model).removeEventListener(TransferableObjectUpdatedEvent.OBJECT_UPDATED, objectUpdatedHandler);
			View(model).removeEventListener(TransferableObjectDisposedEvent.OBJECT_DISPOSED, objectDisposedHandler);
		}
		
		protected function objectUpdatedHandler(event:TransferableObjectUpdatedEvent):void {		
			var model:MindMapNode = MindMapNode(event.object);	

			var root:Object = diagramShell.getControllerProvider(diagramShell.rootModel).getModelChildrenController(diagramShell.rootModel).getChildren(diagramShell.rootModel).getItemAt(0);
		
			MindMapDiagramShell(diagramShell).removeModelFromRootChildren(model, true);		
			MindMapDiagramShell(diagramShell).addModelToRootChildren(model, true);	
			diagramShell.shouldRefreshVisualChildren(diagramShell.rootModel);
			
			MindMapDiagramShell(diagramShell).refreshNodePositions(root);
		}
		
		protected function objectDisposedHandler(event:TransferableObjectDisposedEvent):void {
			var model:MindMapNode = MindMapNode(event.object);
						
			diagramShell.unassociateModelFromRenderer(model, diagramShell.getRendererForModel(model), true);
			diagramShell.shouldRefreshVisualChildren(diagramShell.rootModel);
	
			MindMapDiagramShell(diagramShell).removeModelFromRootChildren(model, true);
			
			if (MindMapDiagramShell(diagramShell).getModelController(model).getParent(model) != null) {
				MindMapDiagramShell(diagramShell).refreshNodePositions(MindMapDiagramShell(diagramShell).getModelController(model).getParent(model));
			} else {
				var rootModel:Object = diagramShell.getControllerProvider(diagramShell.rootModel).getModelChildrenController(diagramShell.rootModel).getChildren(diagramShell.rootModel).getItemAt(0);
				MindMapDiagramShell(diagramShell).refreshNodePositions(rootModel);
			}
		}
		
		private function getDynamicObject(model:Object):Object {
			return DynamicModelExtraInfoController(diagramShell.getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model);
		}
	}
}