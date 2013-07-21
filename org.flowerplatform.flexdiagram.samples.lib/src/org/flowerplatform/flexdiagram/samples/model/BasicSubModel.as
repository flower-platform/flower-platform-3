package org.flowerplatform.flexdiagram.samples.model {
	
	[Bindable]
	/**
	 * @author Cristian Spiescu
	 */
	public class BasicSubModel {

		public var name:String = "Default Text";
		
		public var parent:BasicModel;
		
		public function BasicSubModel(name:String, parent:BasicModel) {
			this.name = name;
			this.parent = parent;
		}
		
		public function toString():String {
			return "BasicSubModel[" + name + "]";
		}
		
	}
}