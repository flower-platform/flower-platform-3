package org.flowerplatform.flexdiagram.samples {
	import org.flowerplatform.flexdiagram.util.ParentAwareArrayList;

	[Bindable]
	/**
	 * @author Cristian Spiescu
	 */
	public interface IModelHolder {
		function get rootModel():ParentAwareArrayList;
		function set rootModel(value:ParentAwareArrayList):void;
	}
}