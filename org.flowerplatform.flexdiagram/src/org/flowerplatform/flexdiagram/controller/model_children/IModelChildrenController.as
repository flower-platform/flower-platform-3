package org.flowerplatform.flexdiagram.controller.model_children {
	import mx.collections.IList;

	/**
	 * @author Cristian Spiescu
	 */
	public interface IModelChildrenController {
		function getChildren(model:Object):IList;
		function beginListeningForChanges(model:Object):void;
		function endListeningForChanges(model:Object):void;
	}
}