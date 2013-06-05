package org.flowerplatform.flexdiagram.samples.model {
	import mx.collections.ArrayList;
	
	import org.flowerplatform.flexdiagram.util.ParentAwareArrayList;
	
	[Bindable]
	/**
	 * @author Cristian Spiescu
	 */
	public class BasicModel {

		public var name:String = "Default Text";
		public var x:int;
		public var y:int;
		public var width:int;
		public var height:int;
		public var subModels:ParentAwareArrayList;
		
		public function BasicModel() {
			subModels = new ParentAwareArrayList(this);
		}
		
		public function toString():String {
			return "BasicModel[" + x + ", " + y + "]";
		}
		
	}
}