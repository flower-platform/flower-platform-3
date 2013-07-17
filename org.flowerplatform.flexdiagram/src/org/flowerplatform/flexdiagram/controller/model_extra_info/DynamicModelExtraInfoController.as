package org.flowerplatform.flexdiagram.controller.model_extra_info {
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.IModelExtraInfoController;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class DynamicModelExtraInfoController extends ControllerBase implements IModelExtraInfoController {
	
		protected static const NO_RENDERER_ASSOCIATED_MARKER:Object = new Object();
		
		public function DynamicModelExtraInfoController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function getRenderer(extraInfo:Object):IVisualElement {
			if (extraInfo == null || extraInfo.renderer == NO_RENDERER_ASSOCIATED_MARKER) {
				return null;
			} else {
				return IVisualElement(extraInfo.renderer);
			}
		}
		
		public function setRenderer(model:Object, extraInfo:Object, renderer:IVisualElement):void {
			if (renderer == null) {
				getDynamicObject(model).renderer = NO_RENDERER_ASSOCIATED_MARKER;	
			} else {
				getDynamicObject(model).renderer = renderer;
			}
		}
		
		public function createExtraInfo(model:Object):Object {
			return new Object();
		}	
				
		public function getDynamicObject(model:Object):Object {
			if (diagramShell.modelToExtraInfoMap[model] == null) {
				diagramShell.addInModelMapIfNecesssary(model, diagramShell.getControllerProvider(model));
			}
			return diagramShell.modelToExtraInfoMap[model];
		}
		
	}	
}