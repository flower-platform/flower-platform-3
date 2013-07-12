package org.flowerplatform.flexdiagram.samples.mindmap.controller
{
	import flash.events.IEventDispatcher;
	import flash.sampler.getInvocationCount;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayList;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.samples.mindmap.MindMapConnector;
	import org.flowerplatform.flexdiagram.samples.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.samples.mindmap.MindMapModelController;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.MindMapModel;
	import org.flowerplatform.flexdiagram.samples.mindmap.renderer.MindMapModelRenderer;
	import org.flowerplatform.flexdiagram.util.ParentAwareArrayList;
	
	public class MindMapModelRendererController extends ClassReferenceRendererController {
			
		public function MindMapModelRendererController(diagramShell:DiagramShell, rendererClass:Class) {
			super(diagramShell, MindMapModelRenderer);
		}
		
		override public function associatedModelToRenderer(model:Object, renderer:IVisualElement):void {
			IEventDispatcher(model).addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
			addConnector(model);
		}
		
		override public function unassociatedModelFromRenderer(model:Object, renderer:IVisualElement, isModelDisposed:Boolean):void {
			if (isModelDisposed) {
				if (renderer != null) {
					removeConnector(model);
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
			if (event.property == "x" || event.property == "y" || event.property == "height" || event.property == "width" || event.property == "expanded") {
				if (event.property == "expanded") {
					refreshModelChildren(event.target);
					MindMapDiagramShell(diagramShell).refreshNodePositions(event.target);
				}
				diagramShell.shouldRefreshVisualChildren(diagramShell.rootModel);
				if (event.target.parent != null) {
					var connector:MindMapConnector = DynamicModelExtraInfoController(diagramShell.getControllerProvider(event.target).getModelExtraInfoController(event.target)).getDynamicObject(event.target).connector;
					connector.invalidateDisplayList();
				}
				
			}
		}
		
		private function refreshModelChildren(model:Object):void {			
			var list:ArrayList = new ArrayList();
			
			if (!model.expanded) {
				removeModel(model, true);
			}
			addModel(model, true);
		}
		
		private function removeModel(model:Object, removeOnlyChildren:Boolean = false):void {
			var children:ArrayList = MindMapDiagramShell(diagramShell).getMindMapController(model).getChildren(model);
			for (var i:int = 0; i < children.length; i++) {
				removeModel(children.getItemAt(i));				
			}
			if (!removeOnlyChildren) {
				ParentAwareArrayList(diagramShell.rootModel).removeItem(model);				
			}
		}
		
		private function addModel(model:Object, addOnlyChildren:Boolean = false):void {
			if (!addOnlyChildren) {
				ParentAwareArrayList(diagramShell.rootModel).addItem(model);
			}
		
			if (model.expanded) {
				var children:ArrayList = MindMapDiagramShell(diagramShell).getMindMapController(model).getChildren(model);
				for (var i:int = 0; i < children.length; i++) {
					addModel(children.getItemAt(i));
				}
				
			}
		}
		
		private function removeConnector(model:Object):void {
			if (model.parent == null) {
				return;
			}
			var connector:MindMapConnector = DynamicModelExtraInfoController(diagramShell.getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).connector;
			diagramShell.diagramRenderer.removeElement(connector);
			delete DynamicModelExtraInfoController(diagramShell.getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).connector;
		}
		
		private function addConnector(model:Object):void {
			if (model.parent == null) {
				return;
			}
			var connector:MindMapConnector = new MindMapConnector().setSource(model).setTarget(model.parent);
			DynamicModelExtraInfoController(diagramShell.getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model).connector = connector;
			diagramShell.diagramRenderer.addElement(connector);
		}
		
	}
}