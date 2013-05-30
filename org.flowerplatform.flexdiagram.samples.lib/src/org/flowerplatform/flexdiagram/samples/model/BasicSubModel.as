package org.flowerplatform.flexdiagram.samples.model {
	
	[Bindable]
	/**
	 * @author Cristian Spiescu
	 */
	public class BasicSubModel {

		public var name:String = "Default Text";
		
		public function BasicSubModel(name:String = null) {
			this.name = name;
		}
		
		public function toString():String {
			return "BasicSubModel[" + name + "]";
		}
		
	}
}