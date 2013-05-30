package org.flowerplatform.flexdiagram.samples {
	import mx.collections.ArrayList;
	
	import org.flowerplatform.flexdiagram.controller.visual_children.AbsoluteLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.IModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	import org.flowerplatform.flexdiagram.controller.visual_children.IVisualChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.LightweightModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.model_children.ParentAwareArrayListModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.visual_children.SequentialLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.samples.controller.BasicModelAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.samples.controller.BasicModelModelChildrenController;
	import org.flowerplatform.flexdiagram.samples.controller.BasicModelRendererController;
	import org.flowerplatform.flexdiagram.samples.model.BasicModel;
	import org.flowerplatform.flexdiagram.samples.model.BasicSubModel;
	
	import spark.components.supportClasses.ItemRenderer;
	import org.flowerplatform.flexdiagram.samples.renderer.SubModelIconItemRenderer;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SamplesDiagramShell extends DiagramShell implements IControllerProvider {
		
		private var lightweightModelExtraInfoController:LightweightModelExtraInfoController;
		private var absoluteLayoutVisualChildrenController:AbsoluteLayoutVisualChildrenController;
		private var arrayListModelChildrenController:ParentAwareArrayListModelChildrenController;
		
		private var basicModelAbsoluteLayoutRectangleController:BasicModelAbsoluteLayoutRectangleController;
		private var basicModelRendererController:BasicModelRendererController;
		private var basicModelModelChildrenController:IModelChildrenController;
		private var sequentialLayoutVisualChildrenController:IVisualChildrenController;
		
		private var basicSubModelRendererController:ClassReferenceRendererController;
		
		public function SamplesDiagramShell() {
			lightweightModelExtraInfoController = new LightweightModelExtraInfoController(this);
			absoluteLayoutVisualChildrenController = new AbsoluteLayoutVisualChildrenController(this);
			arrayListModelChildrenController = new ParentAwareArrayListModelChildrenController(this, true);
			
			basicModelAbsoluteLayoutRectangleController = new BasicModelAbsoluteLayoutRectangleController(this);
			basicModelRendererController = new BasicModelRendererController(this);
			basicModelModelChildrenController = new BasicModelModelChildrenController(this);
			sequentialLayoutVisualChildrenController = new SequentialLayoutVisualChildrenController(this);
			
			basicSubModelRendererController = new ClassReferenceRendererController(this, SubModelIconItemRenderer);
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
		
		override public function getControllerProvider(model:Object):IControllerProvider {
			return this;
		}
		
	}
}