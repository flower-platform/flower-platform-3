package org.flowerplatform.flexdiagram.samples.mindmap.controller
{
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayList;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.events.PropertyChangeEvent;
	import mx.events.PropertyChangeEventKind;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapConnector;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	import org.flowerplatform.flexdiagram.samples.mindmap.renderer.MindMapModelRenderer;
	import org.flowerplatform.flexdiagram.util.ParentAwareArrayList;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapModelRendererController extends ClassReferenceRendererController {
			
		public function MindMapModelRendererController(diagramShell:DiagramShell, rendererClass:Class) {
			super(diagramShell, MindMapModelRenderer);
		}
		
		override public function associatedModelToRenderer(model:Object, renderer:IVisualElement):void {
			IEventDispatcher(model).addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
			addConnector(model);			
		}
		
		override public function unassociatedModelFromRenderer(model:Object, renderer:IVisualElement, isModelDisposed:Boolean):void {
			removeConnector(model);
			if (isModelDisposed) {
				if (renderer != null) {					
					IVisualElementContainer(renderer.parent).removeElement(renderer);					
				}
				IEventDispatcher(model).removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
			} else {
				// weak referenced. In theory, this is not needed, but to be sure...
				// The only case where it would make sense: if the model children controller fails to inform us of a disposal;
				// but in this case, the stray model may be as well left on the diagram 
				IEventDispatcher(model).addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler, false, 0, true);
			}
		}
		
		private function modelChangedHandler(event:PropertyChangeEvent):void {
			var model:Object = event.target;				
			var shouldRefreshNodePositions:Boolean = event.property == "expanded" || event.property == "height" || event.property == "width";
			var shouldUpdateConnectors:Boolean = event.property == "x" || event.property == "y" || shouldRefreshNodePositions;
			
			if (event.property == "parent") { // parent changed (after a drag & drop)
				// remove old model from display
				removeModelFromRootChildren(model);		
				// add new model to display
				var parent:Object = event.newValue;
				if (getModelController(parent).getExpanded(parent)) {
					addModelToRootChildren(model);
				}
				// refresh structure
				MindMapDiagramShell(diagramShell).refreshNodePositions(ParentAwareArrayList(diagramShell.rootModel).getItemAt(0));
			}
			
			if (event.property == "expanded") {				
				if (Boolean(event.oldValue) == true) { // was expanded
					removeModelFromRootChildren(model, true);
				}
				addModelToRootChildren(model, true);			
			}
			
			if (shouldRefreshNodePositions) {					
				MindMapDiagramShell(diagramShell).refreshNodePositions(model);				
			}
			
			if (shouldUpdateConnectors) {
				if (event.oldValue != event.newValue) {				
					updateConnectors(model);
				}
			}
		}
		
		private function removeModelFromRootChildren(model:Object, removeOnlyChildren:Boolean = false):void {
			var children:ArrayList = new ArrayList();
			children.addAll(ParentAwareArrayList(diagramShell.rootModel));
			for (var i:int = 0; i < children.length; i++) {
				var child:Object = children.getItemAt(i);
				if (model == getModelController(child).getParent(child)) {
					removeModelFromRootChildren(child);			
				}
			}
			if (!removeOnlyChildren) {
				ParentAwareArrayList(diagramShell.rootModel).removeItem(model);				
			}
		}
		
		private function addModelToRootChildren(model:Object, addOnlyChildren:Boolean = false):void {
			if (!addOnlyChildren) {
				ParentAwareArrayList(diagramShell.rootModel).addItem(model);
			}		
			if (getModelController(model).getExpanded(model)) {
				var children:ArrayList = ArrayList(diagramShell.getControllerProvider(model).getModelChildrenController(model).getChildren(model));
				for (var i:int = 0; i < children.length; i++) {
					addModelToRootChildren(children.getItemAt(i));
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
		
		private function updateConnectors(model:Object):void {
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