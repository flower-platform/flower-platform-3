package org.flowerplatform.flexdiagram.samples {
	import flash.display.IDrawCommand;
	
	import mx.collections.ArrayList;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	import org.flowerplatform.flexdiagram.controller.IDragToCreateRelationController;
	import org.flowerplatform.flexdiagram.controller.IInplaceEditorController;
	import org.flowerplatform.flexdiagram.controller.IResizeController;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_children.ParentAwareArrayListModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.IModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.LightweightModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.AnchorsSelectionController;
	import org.flowerplatform.flexdiagram.controller.selection.AnchorsSelectionDynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.selection.ISelectionController;
	import org.flowerplatform.flexdiagram.controller.visual_children.AbsoluteLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.controller.visual_children.IVisualChildrenController;
	import org.flowerplatform.flexdiagram.controller.visual_children.SequentialLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.samples.controller.BasicModelAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.samples.controller.BasicModelDragToCreateRelationController;
	import org.flowerplatform.flexdiagram.samples.controller.BasicModelModelChildrenController;
	import org.flowerplatform.flexdiagram.samples.controller.BasicModelRendererController;
	import org.flowerplatform.flexdiagram.samples.controller.BasicModelResizeController;
	import org.flowerplatform.flexdiagram.samples.controller.BasicSubModelInplaceEditorController;
	import org.flowerplatform.flexdiagram.samples.controller.BasicSubModelSelectionController;
	import org.flowerplatform.flexdiagram.samples.model.BasicModel;
	import org.flowerplatform.flexdiagram.samples.model.BasicSubModel;
	import org.flowerplatform.flexdiagram.samples.renderer.BasicModelAnchorsSelectionRenderer;
	import org.flowerplatform.flexdiagram.samples.renderer.SubModelIconItemRenderer;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SamplesDiagramShell extends DiagramShell implements IControllerProvider {
		
		private var lightweightModelExtraInfoController:LightweightModelExtraInfoController;
		private var dynamicModelExtraInfoController:DynamicModelExtraInfoController;
		
		private var absoluteLayoutVisualChildrenController:AbsoluteLayoutVisualChildrenController;
		private var arrayListModelChildrenController:ParentAwareArrayListModelChildrenController;
		
		private var basicModelAbsoluteLayoutRectangleController:BasicModelAbsoluteLayoutRectangleController;
		private var basicModelRendererController:BasicModelRendererController;
		private var basicModelModelChildrenController:IModelChildrenController;
		private var sequentialLayoutVisualChildrenController:IVisualChildrenController;
		
		private var basicSubModelRendererController:ClassReferenceRendererController;
		
		private var basicModelSelectionController:AnchorsSelectionController;
		private var basicModelExtraInfoController:AnchorsSelectionDynamicModelExtraInfoController;
			
		private var basicSubModelSelectionController:BasicSubModelSelectionController;
		
		private var basicSubModelInplaceEditorController:BasicSubModelInplaceEditorController;
		private var basicModelResizeController:BasicModelResizeController;
		private var basicModelDragToCreateRelationController:BasicModelDragToCreateRelationController;
		
		public function SamplesDiagramShell() {
			lightweightModelExtraInfoController = new LightweightModelExtraInfoController(this);
			dynamicModelExtraInfoController = new DynamicModelExtraInfoController(this);
			
			absoluteLayoutVisualChildrenController = new AbsoluteLayoutVisualChildrenController(this);
			arrayListModelChildrenController = new ParentAwareArrayListModelChildrenController(this, true);
			
			basicModelAbsoluteLayoutRectangleController = new BasicModelAbsoluteLayoutRectangleController(this);
			basicModelRendererController = new BasicModelRendererController(this);
			basicModelModelChildrenController = new BasicModelModelChildrenController(this);
			sequentialLayoutVisualChildrenController = new SequentialLayoutVisualChildrenController(this);
			
			basicSubModelRendererController = new ClassReferenceRendererController(this, SubModelIconItemRenderer);
			
			basicModelSelectionController = new AnchorsSelectionController(this);
			basicModelExtraInfoController = new AnchorsSelectionDynamicModelExtraInfoController(this, BasicModelAnchorsSelectionRenderer);
			
			basicSubModelSelectionController = new BasicSubModelSelectionController(this);
			basicSubModelInplaceEditorController = new BasicSubModelInplaceEditorController(this);
			basicModelResizeController = new BasicModelResizeController(this);
			basicModelDragToCreateRelationController = new BasicModelDragToCreateRelationController(this);
		}
		
		public function getAbsoluteLayoutRectangleController(model:Object):IAbsoluteLayoutRectangleController {
			if (model is BasicModel) {
				return basicModelAbsoluteLayoutRectangleController;
			}
			return null;
		}
		
		public function getModelChildrenController(model:Object):IModelChildrenController {
			if (model is ArrayList) {
				return arrayListModelChildrenController;
			} else if (model is BasicModel) {
				return basicModelModelChildrenController;
			}
			return null;
		}
		
		public function getModelExtraInfoController(model:Object):IModelExtraInfoController {
			if (model is BasicModel) {
				return basicModelExtraInfoController;
			} else if (model is BasicSubModel) {
				return dynamicModelExtraInfoController;
			}
			return lightweightModelExtraInfoController;
		}
		
		public function getRendererController(model:Object):IRendererController {
			if (model is BasicModel) {
				return basicModelRendererController;
			} else if (model is BasicSubModel) {
				return basicSubModelRendererController;
			}
			return null;
		}
		
		public function getVisualChildrenController(model:Object):IVisualChildrenController {
			if (model is ArrayList) {
				return absoluteLayoutVisualChildrenController;
			} else if (model is BasicModel) {
				return sequentialLayoutVisualChildrenController;
			}
			return null;
		}
		
		public function getSelectionController(model:Object):ISelectionController {
			if (model is BasicModel) {
				return basicModelSelectionController;
			} else if (model is BasicSubModel) {
				return basicSubModelSelectionController;
			}
			return null;
		}
		
		public function getInplaceEditorController(model:Object):IInplaceEditorController {
			if (model is BasicSubModel) {
				return basicSubModelInplaceEditorController;
			}
			return null;
		}
		
		public function getResizeController(model:Object):IResizeController {
			if (model is BasicModel) {
				return basicModelResizeController;
			}
			return null;
		}
		
		public function getDragToCreateRelationController(model:Object):IDragToCreateRelationController{
			if (model is BasicModel) {
				return basicModelDragToCreateRelationController;
			}
			return null;
		}
		
		override public function getControllerProvider(model:Object):IControllerProvider {
			return this;
		}
		
	}
}