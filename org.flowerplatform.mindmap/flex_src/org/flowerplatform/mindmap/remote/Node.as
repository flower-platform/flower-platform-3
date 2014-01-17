package org.flowerplatform.mindmap.remote {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.transferable_object.TransferableObject;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.mindmap.MindMapPlugin;
	
	[Bindable]
	[RemoteClass]
	public class Node {
		
		public var id:String;
		
		public var body:String;
		
		public var type:String;
		
		public var hasChildren:Boolean;
		
		public var properties:Object;
		
		[Transient]
		public var parent:Node;
		
		[Transient]
		public var children:ArrayCollection;
		
		[Transient]
		public var side:int = MindMapDiagramShell.RIGHT;
			
	}
}