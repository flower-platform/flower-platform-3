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
			if (extraInfo == null) {
				diagramShell.addInModelMapIfNecesssary(model, diagramShell.getControllerProvider(model));
			}
			if (renderer == null) {
				diagramShell.modelToExtraInfoMap[model].renderer = NO_RENDERER_ASSOCIATED_MARKER;	
			} else {
				diagramShell.modelToExtraInfoMap[model].renderer = renderer;
			}
		}
		
		public function createExtraInfo(model:Object):Object {
			return new Object();
		}	
		
		public function isSelected(model:Object):Boolean {
			return diagramShell.modelToExtraInfoMap[model].selected;
		}
		
		public function setSelected(model:Object, value:Boolean):void {		
			diagramShell.modelToExtraInfoMap[model].selected = value;
		}
		
	}	
}