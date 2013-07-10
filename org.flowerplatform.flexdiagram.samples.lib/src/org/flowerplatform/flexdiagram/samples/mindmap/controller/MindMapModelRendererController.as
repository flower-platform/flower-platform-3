package org.flowerplatform.flexdiagram.samples.mindmap.controller
{
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayList;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
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
		}
		
		override public function unassociatedModelFromRenderer(model:Object, renderer:IVisualElement, isModelDisposed:Boolean):void {
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
			if (event.property == "x" || event.property == "y" || event.property == "height" || event.property == "width" || event.property == "expanded") {
				if (event.property == "expanded") {
					refreshRootModelChildren();
					MindMapDiagramShell(diagramShell).refreshNodePositions(event.target);
				}
				diagramShell.shouldRefreshVisualChildren(diagramShell.rootModel);
			}
		}
		
		private function refreshRootModelChildren():void {
			var firstModel:MindMapModel = MindMapModel(ParentAwareArrayList(diagramShell.rootModel).getItemAt(0));
			var list:ArrayList = new ArrayList();
			list.addItem(firstModel);
			addChildrenInRoot(firstModel, list);
			ParentAwareArrayList(diagramShell.rootModel).removeAll();
			ParentAwareArrayList(diagramShell.rootModel).addAll(list);
		}
		
		private function addChildrenInRoot(model:Object, list:ArrayList):void {
			if (model.expanded) {
				var children:ArrayList = MindMapDiagramShell(diagramShell).getMindMapController(model).getChildren(model);
				list.addAll(children);
				for (var i:int = 0; i < children.length; i++) {
					addChildrenInRoot(children.getItemAt(i), list);
				}		
			}
		}
		
	}
}