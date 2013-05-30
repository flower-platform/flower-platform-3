package org.flowerplatform.flexdiagram.controller.renderer {
	import flash.events.IEventDispatcher;
	
	import mx.core.IInvalidating;
	import mx.core.IVisualElement;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ClassReferenceRendererController extends ControllerBase implements IRendererController {
		
		protected var rendererClass:Class;
		
		public function ClassReferenceRendererController(diagramShell:DiagramShell, rendererClass:Class) {
			super(diagramShell);
			this.rendererClass = rendererClass;
		}
		
		public function geUniqueKeyForRendererToRecycle(model:Object):Object {
			return rendererClass;
		}
		
		public function createRenderer(model:Object):IVisualElement {
			return new rendererClass();
		}
		
		public function associatedModelToRenderer(model:Object, renderer:IVisualElement):void {
		}
		
		public function unassociatedModelFromRenderer(model:Object, renderer:IVisualElement, modelIsDisposed:Boolean):void {
		}
		
	}
}