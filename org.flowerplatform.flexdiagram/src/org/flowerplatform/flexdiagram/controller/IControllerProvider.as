package org.flowerplatform.flexdiagram.controller {
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.IModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.ISelectionController;
	import org.flowerplatform.flexdiagram.controller.visual_children.IVisualChildrenController;

	/**
	 * @author Cristian Spiescu
	 */
	public interface IControllerProvider {
		
		function getVisualChildrenController(model:Object):IVisualChildrenController;
		function getModelExtraInfoController(model:Object):IModelExtraInfoController;
		function getModelChildrenController(model:Object):IModelChildrenController;
		function getAbsoluteLayoutRectangleController(model:Object):IAbsoluteLayoutRectangleController;
		function getRendererController(model:Object):IRendererController;
		function getSelectionController(model:Object):ISelectionController;
	}
}