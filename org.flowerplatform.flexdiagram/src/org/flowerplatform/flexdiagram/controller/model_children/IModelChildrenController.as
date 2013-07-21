package org.flowerplatform.flexdiagram.controller.model_children {
	import mx.collections.IList;

	/**
	 * Should be provided even by elements that have no children, if they are connectable
	 * with connectors!
	 * 
	 * @author Cristian Spiescu
	 */
	public interface IModelChildrenController {
		function getParent(model:Object):Object;
		function getChildren(model:Object):IList;
		function beginListeningForChanges(model:Object):void;
		function endListeningForChanges(model:Object):void;
	}
}