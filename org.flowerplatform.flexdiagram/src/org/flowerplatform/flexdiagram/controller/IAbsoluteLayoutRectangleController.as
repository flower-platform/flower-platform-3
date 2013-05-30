package org.flowerplatform.flexdiagram.controller {
	import flash.geom.Rectangle;
	
	import mx.core.IVisualElement;

	/**
	 * @author Cristian Spiescu
	 */
	public interface IAbsoluteLayoutRectangleController {
		function getBounds(model:Object):Rectangle;		
	}
}