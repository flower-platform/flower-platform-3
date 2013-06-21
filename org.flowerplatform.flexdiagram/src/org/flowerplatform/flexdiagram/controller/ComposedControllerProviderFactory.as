package org.flowerplatform.flexdiagram.controller {
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.IModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.ISelectionController;
	import org.flowerplatform.flexdiagram.controller.visual_children.IVisualChildrenController;
	import org.flowerplatform.flexdiagram.tool.controller.IDragController;
	import org.flowerplatform.flexdiagram.tool.controller.IDragToCreateRelationController;
	import org.flowerplatform.flexdiagram.tool.controller.IInplaceEditorController;
	import org.flowerplatform.flexdiagram.tool.controller.IResizeController;
	import org.flowerplatform.flexdiagram.tool.controller.ISelectOrDragToCreateElementController;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ComposedControllerProviderFactory {
		
//		public var visualChildrenControllerClass:Class;
//		public var modelExtraInfoControllerClass:Class;
//		public var modelChildrenControllerClass:Class;
//		public var absoluteLayoutRectangleControllerClass:Class;
//		public var rendererControllerClass:Class;
//		public var selectionControllerClass:Class;
//		
//		public var inplaceEditorControllerClass:Class;
//		public var resizeControllerClass:Class;
//		public var dragToCreateRelationControllerClass:Class;
//		public var dragControllerClass:Class;
//		public var selectOrDragToCreateElementControllerClass:Class;
//		
//		public function createComposedControllerProvider(shell:DiagramShell):ComposedControllerProvider {
//			var result:ComposedControllerProvider = new ComposedControllerProvider();
//			result.visualChildrenController = new visualChildrenControllerClass(shell);
//			result.modelExtraInfoController = new modelExtraInfoControllerClass(shell);
//			result.modelChildrenController = new modelChildrenControllerClass(shell);
//			result.absoluteLayoutRectangleController = new absoluteLayoutRectangleControllerClass(shell);
//			result.rendererController = new rendererControllerClass(shell);
//			result.selectionController = new selectionControllerClass(shell);
//			
//			result.inplaceEditorController = new inplaceEditorControllerClass(shell);
//			result.resizeController = new resizeControllerClass(shell);
//			result.dragToCreateRelationController = new dragToCreateRelationControllerClass(shell);
//			result.dragController = new dragControllerClass(shell);
//			result.selectOrDragToCreateElementController = new selectOrDragToCreateElementControllerClass(shell);
//			return result;
//		}
		
		public var visualChildrenControllerClass:ControllerFactory;
		public var modelExtraInfoControllerClass:ControllerFactory;
		public var modelChildrenControllerClass:ControllerFactory;
		public var absoluteLayoutRectangleControllerClass:ControllerFactory;
		public var rendererControllerClass:ControllerFactory;
		public var selectionControllerClass:ControllerFactory;
		
		public var inplaceEditorControllerClass:ControllerFactory;
		public var resizeControllerClass:ControllerFactory;
		public var dragToCreateRelationControllerClass:ControllerFactory;
		public var dragControllerClass:ControllerFactory;
		public var selectOrDragToCreateElementControllerClass:ControllerFactory;
		
		public function createComposedControllerProvider(shell:DiagramShell):ComposedControllerProvider {
			var result:ComposedControllerProvider = new ComposedControllerProvider();
			if (visualChildrenControllerClass != null) {
				result.visualChildrenController = visualChildrenControllerClass.newInstance(shell);
			}
			if (modelExtraInfoControllerClass != null) {
				result.modelExtraInfoController = modelExtraInfoControllerClass.newInstance(shell);
			}
			if (modelChildrenControllerClass != null) {
				result.modelChildrenController = modelChildrenControllerClass.newInstance(shell);
			}
			if (absoluteLayoutRectangleControllerClass != null) {
				result.absoluteLayoutRectangleController = absoluteLayoutRectangleControllerClass.newInstance(shell);
			}
			if (rendererControllerClass != null) {
				result.rendererController = rendererControllerClass.newInstance(shell);
			}
			if (selectionControllerClass != null) {
				result.selectionController = selectionControllerClass.newInstance(shell);
			}
			
			if (inplaceEditorControllerClass != null) {
				result.inplaceEditorController = inplaceEditorControllerClass.newInstance(shell);
			}
			if (resizeControllerClass != null) {
				result.resizeController = resizeControllerClass.newInstance(shell);
			}
			if (dragToCreateRelationControllerClass != null) {
				result.dragToCreateRelationController = dragToCreateRelationControllerClass.newInstance(shell);
			}
			if (dragControllerClass != null) {
				result.dragController = dragControllerClass.newInstance(shell);
			}
			if (selectOrDragToCreateElementControllerClass != null) {
				result.selectOrDragToCreateElementController = selectOrDragToCreateElementControllerClass.newInstance(shell);
			}
			return result;
		}

	}
}