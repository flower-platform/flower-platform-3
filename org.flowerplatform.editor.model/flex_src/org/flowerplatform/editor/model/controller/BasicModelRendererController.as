package org.flowerplatform.editor.model.controller {
	import mx.core.IVisualElement;
	
	import org.flowerplatform.editor.model.renderer.BoxRenderer;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class BasicModelRendererController extends ClassReferenceRendererController {
		
		public function BasicModelRendererController(diagramShell:DiagramShell) {
			super(diagramShell, BoxRenderer);
		}
		
		override public function associatedModelToRenderer(model:Object, renderer:IVisualElement):void {
//			IEventDispatcher(model).removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
		}
		
		override public function unassociatedModelFromRenderer(model:Object, renderer:IVisualElement, isModelDisposed:Boolean):void {
//			if (isModelDisposed) {
//				if (renderer != null) {
//					IVisualElementContainer(renderer.parent).removeElement(renderer);
//				}
//				IEventDispatcher(model).removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
//			} else {
//				// weak referenced. In theory, this is not needed, but to be sure...
//				// The only case where it would make sense: if the model children controller fails to inform us of a disposal;
//				// but in this case, the stray model may be as well left on the diagram 
//				IEventDispatcher(model).addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler, false, 0, true);
//			}
		}
		
//		private function modelChangedHandler(event:PropertyChangeEvent):void {
//			if (event.property == "x" || event.property == "y" || event.property == "height" || event.property == "width") {
//				diagramShell.shouldRefreshVisualChildren(diagramShell.rootModel);
//			}
//		}
	}
}
