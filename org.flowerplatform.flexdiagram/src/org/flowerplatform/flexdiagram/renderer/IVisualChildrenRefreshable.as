package org.flowerplatform.flexdiagram.renderer {

	/**
	 * @author Cristian Spiescu
	 */
	public interface IVisualChildrenRefreshable {
		function get shouldRefreshVisualChildren():Boolean;
		function set shouldRefreshVisualChildren(value:Boolean):void;
	}
}