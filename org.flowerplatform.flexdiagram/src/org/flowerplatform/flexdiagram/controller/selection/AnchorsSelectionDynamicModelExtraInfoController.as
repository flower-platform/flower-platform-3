package org.flowerplatform.flexdiagram.controller.selection {
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.renderer.selection.AnchorsSelectionRenderer;
	
	/**
	 * @author Cristina Constantinescu
	 * @author Cristian Spiescu
	 */ 
	public class AnchorsSelectionDynamicModelExtraInfoController extends DynamicModelExtraInfoController {
		
		public var anchorsSelectionRendererClass:Class;
		
		public function AnchorsSelectionDynamicModelExtraInfoController(diagramShell:DiagramShell, anchorsSelectionRendererClass:Class = null) {
			super(diagramShell);
			this.anchorsSelectionRendererClass = anchorsSelectionRendererClass;
		}
		
		public function createAnchorsSelectionRenderer(model:Object):AnchorsSelectionRenderer {
			return new anchorsSelectionRendererClass();
		}
		
		public function getAnchorsSelectionRenderer(extraInfo:Object):AnchorsSelectionRenderer {
			if (extraInfo == null || extraInfo.anchorsSelectionRenderer == NO_RENDERER_ASSOCIATED_MARKER) {
				return null;
			} else {
				return AnchorsSelectionRenderer(extraInfo.anchorsSelectionRenderer);
			}
		}
		
		public function setAnchorsSelectionRenderer(model:Object, extraInfo:Object, renderer:AnchorsSelectionRenderer):void {	
			if (extraInfo == null) {
				diagramShell.addInModelMapIfNecesssary(model, diagramShell.getControllerProvider(model));
			}
			if (renderer == null) {
				diagramShell.modelToExtraInfoMap[model].anchorsSelectionRenderer = NO_RENDERER_ASSOCIATED_MARKER;	
			} else {
				diagramShell.modelToExtraInfoMap[model].anchorsSelectionRenderer = renderer;
			}
		}	
	}
}