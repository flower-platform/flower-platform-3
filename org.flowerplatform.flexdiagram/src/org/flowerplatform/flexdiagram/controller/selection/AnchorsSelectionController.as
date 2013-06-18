package org.flowerplatform.flexdiagram.controller.selection {
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.renderer.selection.AnchorsSelectionRenderer;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class AnchorsSelectionController extends ControllerBase implements ISelectionController {
		
		public function AnchorsSelectionController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function setSelectedState(model:Object, renderer:IVisualElement, isSelected:Boolean, isMainSelection:Boolean):void {
			var modelExtraInfoController:AnchorsSelectionDynamicModelExtraInfoController = 
				AnchorsSelectionDynamicModelExtraInfoController(diagramShell.getControllerProvider(model).getModelExtraInfoController(model));			
			var extraInfo:Object = diagramShell.modelToExtraInfoMap[model];
			
			if (extraInfo.selected != isSelected) {
				modelExtraInfoController.setSelected(model, isSelected);
			}
					
			if (renderer == null) {
				return;
			}
			
			// add/delete anchorsSelectionRenderer
			
			if (isSelected) {		
				var anchorsSelectionRenderer:AnchorsSelectionRenderer = modelExtraInfoController.getAnchorsSelectionRenderer(extraInfo);				
				if (anchorsSelectionRenderer == null) {
					anchorsSelectionRenderer = modelExtraInfoController.createAnchorsSelectionRenderer(model);	
					
					anchorsSelectionRenderer.setMainSelection(isMainSelection);
					anchorsSelectionRenderer.activate(diagramShell, renderer);
					
					modelExtraInfoController.setAnchorsSelectionRenderer(model, extraInfo, anchorsSelectionRenderer);
				} else {
					if (anchorsSelectionRenderer.getMainSelection() != isMainSelection) {
						anchorsSelectionRenderer.setMainSelection(isMainSelection);
					}
				}					
			} else {
				unassociatedModelFromSelectionRenderer(model, renderer);
			}
		}
		
		public function associatedModelToSelectionRenderer(model:Object, renderer:IVisualElement):void {	
			setSelectedState(model, renderer, diagramShell.modelToExtraInfoMap[model].selected, diagramShell.mainSelectedItem == model);			
		}
		
		public function unassociatedModelFromSelectionRenderer(model:Object, renderer:IVisualElement):void {
			var modelExtraInfoController:AnchorsSelectionDynamicModelExtraInfoController = 
				AnchorsSelectionDynamicModelExtraInfoController(diagramShell.getControllerProvider(model).getModelExtraInfoController(model));
			var extraInfo:Object = diagramShell.modelToExtraInfoMap[model];
			var anchorsSelectionRenderer:AnchorsSelectionRenderer = modelExtraInfoController.getAnchorsSelectionRenderer(extraInfo);
			
			if (anchorsSelectionRenderer != null) {
				anchorsSelectionRenderer.deactivate();				
				modelExtraInfoController.setAnchorsSelectionRenderer(model, extraInfo, null);
			}
		}
	}
}