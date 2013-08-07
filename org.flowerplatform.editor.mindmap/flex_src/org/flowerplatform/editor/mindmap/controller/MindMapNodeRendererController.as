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
	import flash.events.IEventDispatcher;
	import flash.net.getClassByAlias;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.core.UIComponent;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolderList;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.MindMapNode;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.ISelectionController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapConnector;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapNodeRendererController extends ClassReferenceRendererController {
			
		public function MindMapNodeRendererController(diagramShell:DiagramShell, rendererClass:Class) {
			super(diagramShell, rendererClass);
			removeRendererIfModelIsDisposed = true;
		}
		
		override public function associatedModelToRenderer(model:Object, renderer:IVisualElement):void {			
		}
		
		override public function unassociatedModelFromRenderer(model:Object, renderer:IVisualElement, isModelDisposed:Boolean):void {		
			if (isModelDisposed) {
				removeConnector(model);
			}
			super.unassociatedModelFromRenderer(model, renderer, isModelDisposed);
		}
				
		private function removeConnector(model:Object):void {		
			var connector:MindMapConnector = getDynamicObject(model).connector;
			if (connector != null) {
				diagramShell.diagramRenderer.removeElement(connector);
				delete getDynamicObject(model).connector;
			}
		}
		
		private function addConnector(model:Object):void {
			var modelParent:Object = diagramShell.getControllerProvider(model).getModelChildrenController(model).getParent(model);
			if (modelParent is Diagram) { // root node, no connectors
				return;
			}
			var connector:MindMapConnector = new MindMapConnector().setSource(model).setTarget(modelParent);
			getDynamicObject(model).connector = connector;
			diagramShell.diagramRenderer.addElementAt(connector, 0);			
		}
		
		public function updateConnectors(model:Object):void {			
			if (getDynamicObject(model).connector == null) {
				addConnector(model);				
			}
						
			// refresh connector to parent
			if (diagramShell.getControllerProvider(model).getModelChildrenController(model).getParent(model) is MindMapNode) {	
				if (getDynamicObject(model).connector != null) {
					getDynamicObject(model).connector.invalidateDisplayList();
				}
			}
			// refresh connectors to children
			var children:IList = MindMapDiagramShell(diagramShell).getModelController(model).getChildren(model);
			for (var i:int = 0; i < children.length; i++) {
				var child:Object = children.getItemAt(i);						
				if (getDynamicObject(child).connector != null) {
					getDynamicObject(child).connector.invalidateDisplayList();
				}
			}
		}
		
		private function getDynamicObject(model:Object):Object {
			return DynamicModelExtraInfoController(diagramShell.getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model);
		}
		
	}
}