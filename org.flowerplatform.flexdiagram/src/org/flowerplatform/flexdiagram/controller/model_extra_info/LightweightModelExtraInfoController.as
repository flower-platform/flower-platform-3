package org.flowerplatform.flexdiagram.controller.model_extra_info {
	import mx.core.FlexGlobals;
	import mx.core.IVisualElement;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.DiagramShell;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class LightweightModelExtraInfoController extends ControllerBase implements IModelExtraInfoController {
		
		protected static const NO_RENDERER_ASSOCIATED_MARKER:Object = new Object();
		
		public function LightweightModelExtraInfoController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function getRenderer(extraInfo:Object):IVisualElement {
			if (extraInfo == NO_RENDERER_ASSOCIATED_MARKER) {
				return null;
			} else {
				return IVisualElement(extraInfo);
			}
		}
		
		public function setRenderer(model:Object, extraInfo:Object, renderer:IVisualElement):void	{
			if (renderer == null) {
				diagramShell.modelToExtraInfoMap[model] = NO_RENDERER_ASSOCIATED_MARKER;	
			} else {
				diagramShell.modelToExtraInfoMap[model] = renderer;
			}
		}
		
		public function createExtraInfo(model:Object):Object {
			return NO_RENDERER_ASSOCIATED_MARKER;
		}
	}
}