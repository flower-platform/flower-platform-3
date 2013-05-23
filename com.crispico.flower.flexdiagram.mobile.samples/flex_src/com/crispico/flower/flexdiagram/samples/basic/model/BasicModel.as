package com.crispico.flower.flexdiagram.samples.basic.model {
	
	[Bindable]
	public class BasicModel {

		public var name:String = "Default Text";
		public var x:int;
		public var y:int;
		public var width:int;
		public var height:int;
		
		public function toString():String {
			return "BasicModel[" + x + ", " + y + "]";
		}
		
	}
}