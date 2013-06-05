package org.flowerplatform.flexdiagram.renderer {
	import flash.geom.Rectangle;

	/**
	 * @author Cristian Spiescu
	 */
	public interface IAbsoluteLayoutRenderer {
		function getViewportRect():Rectangle;		
		function setContentRect(rect:Rectangle):void;
		
		function set noNeedToRefreshRect(value:Rectangle):void;
		function get noNeedToRefreshRect():Rectangle; 
	}
}