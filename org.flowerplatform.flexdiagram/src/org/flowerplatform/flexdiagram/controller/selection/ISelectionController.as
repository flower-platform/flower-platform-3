package org.flowerplatform.flexdiagram.controller.selection {
	import mx.core.IVisualElement;

	public interface ISelectionController {
		function setSelectiedState(model:Object, renderer:IVisualElement, isSelected:Boolean, isMainSelection:Boolean):void;
	}
}