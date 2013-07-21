package org.flowerplatform.editor.mindmap.controller {
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayList;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolderList;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
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
		}
		
		override public function associatedModelToRenderer(model:Object, renderer:IVisualElement):void {			
			addConnector(model);			
		}
		
		override public function unassociatedModelFromRenderer(model:Object, renderer:IVisualElement, isModelDisposed:Boolean):void {		
			removeConnector(model);
			
			if (isModelDisposed) {				
				if (renderer != null) {					
					IVisualElementContainer(renderer.parent).removeElement(renderer);					
				}			
			}
		}
				
		private function removeConnector(model:Object):void {		
			var connector:MindMapConnector = getDynamicObject(model).connector;
			if (connector != null) {
				diagramShell.diagramRenderer.removeElement(connector);
				delete getDynamicObject(model).connector;
			}
		}
		
		private function addConnector(model:Object):void {
			if (getModelController(model).getParent(model) == null) { // root node, no connectors
				return;
			}
			var connector:MindMapConnector = new MindMapConnector().setSource(model).setTarget(getModelController(model).getParent(model));
			getDynamicObject(model).connector = connector;
			diagramShell.diagramRenderer.addElementAt(connector, 0);
			
			// update other connectors (especially the ones where model is the target)
			updateConnectors(model);
		}
		
		public function updateConnectors(model:Object):void {
			// refresh connector to parent
			if (getModelController(model).getParent(model) != null) {	
				if (getDynamicObject(model).connector != null) {
					getDynamicObject(model).connector.invalidateDisplayList();
				}
			}
			// refresh connectors to children
			for (var i:int = 0; i < diagramShell.getControllerProvider(model).getModelChildrenController(model).getChildren(model).length; i++) {
				var child:Object = diagramShell.getControllerProvider(model).getModelChildrenController(model).getChildren(model).getItemAt(i);						
				if (getDynamicObject(child).connector != null) {
					getDynamicObject(child).connector.invalidateDisplayList();
				}
			}
		}
		
		private function getDynamicObject(model:Object):Object {
			return DynamicModelExtraInfoController(diagramShell.getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model);
		}
		
		private function getModelController(model:Object):IMindMapModelController {
			return MindMapDiagramShell(diagramShell).getModelController(model);
		}
	}
}