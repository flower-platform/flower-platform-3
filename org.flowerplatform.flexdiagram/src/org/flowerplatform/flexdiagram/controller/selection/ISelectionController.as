package org.flowerplatform.flexdiagram.controller.selection {
	import mx.core.IVisualElement;

	/**
	 * @author Cristina Constantinescu
	 */ 
	public interface ISelectionController {
		function setSelectedState(model:Object, renderer:IVisualElement, isSelected:Boolean, isMainSelection:Boolean):void;
		function associatedModelToSelectionRenderer(model:Object, renderer:IVisualElement):void;
		function unassociatedModelFromSelectionRenderer(model:Object, renderer:IVisualElement):void;
	}
	
}