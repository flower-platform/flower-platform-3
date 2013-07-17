package org.flowerplatform.flexdiagram.controller.model_extra_info {
	import mx.core.IVisualElement;

	/**
	 * @author Cristian Spiescu
	 */
	public interface IModelExtraInfoController {
		function getRenderer(extraInfo:Object):IVisualElement;
		function setRenderer(model:Object, extraInfo:Object, renderer:IVisualElement):void;
		function createExtraInfo(model:Object):Object;		
	}
}