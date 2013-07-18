package org.flowerplatform.emf_model.notation {
		
	[RemoteClass]
	[Bindable]
	public class MindMapNode extends Node {
		
		public var expanded:Boolean;
		
		public var side:int;
		
		[Transient]
		public var x:Number = 0;
		[Transient]
		public var y:Number = 0;
		[Transient]
		public var width:Number = 20;
		[Transient]
		public var height:Number = 20;
	}
}