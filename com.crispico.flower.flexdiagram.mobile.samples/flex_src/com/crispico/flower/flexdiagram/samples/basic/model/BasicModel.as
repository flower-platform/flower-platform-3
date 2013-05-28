package com.crispico.flower.flexdiagram.samples.basic.model {
	
	[Bindable]
	public class BasicModel	{

		public static const NODE:int = 0;
		
		public static const CONNECTION_LABEL:int = 1;
		
		public var type:int = NODE;
		
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