package org.flowerplatform.flexdiagram.samples.controller {
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.renderer.ConnectionRendererController;
	import org.flowerplatform.flexdiagram.samples.model.BasicConnection;
	
	public class BasicConnectionRendererController extends ConnectionRendererController {
		public function BasicConnectionRendererController(diagramShell:DiagramShell, rendererClass:Class=null) {
			super(diagramShell, rendererClass);
		}
	
		override public function unassociatedModelFromRenderer(model:Object, renderer:IVisualElement, isModelDisposed:Boolean):void {
			super.unassociatedModelFromRenderer(model, renderer, isModelDisposed);
			if (isModelDisposed && renderer != null) {
					IVisualElementContainer(renderer.parent).removeElement(renderer);
			}
		}
		
		override public function getSourceModel(connectionModel:Object):Object {
			return BasicConnection(connectionModel).source;
		}
		
		override public function getTargetModel(connectionModel:Object):Object {
			return BasicConnection(connectionModel).target;
		}
		
		
	}
}