package com.crispico.flower.flexdiagram.samples.basic.model {
	import mx.collections.ArrayCollection;
	
	[Bindable]
	public class BasicConnection {

		public var source:BasicModel;
		public var target:BasicModel;
			
		public var children:ArrayCollection = new ArrayCollection();
	}
}