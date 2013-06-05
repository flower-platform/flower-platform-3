package org.flowerplatform.flexdiagram.controller.renderer {
	import mx.core.IVisualElement;
	
	/**
	 * @author Cristian Spiescu
	 */
	public interface IRendererController {
		/**
		 * This usually returns the Class of the renderer. For Sequential Layout mechanism the method
		 * MUST return the Class of the renderer as key.
		 */
		function geUniqueKeyForRendererToRecycle(model:Object):Object;
		function createRenderer(model:Object):IVisualElement;
		function associatedModelToRenderer(model:Object, renderer:IVisualElement):void;
		function unassociatedModelFromRenderer(model:Object, renderer:IVisualElement, modelIsDisposed:Boolean):void;
	}
}