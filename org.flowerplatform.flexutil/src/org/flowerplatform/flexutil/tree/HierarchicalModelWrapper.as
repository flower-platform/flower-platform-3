package org.flowerplatform.flexutil.tree {
	import flash.events.IEventDispatcher;
	
	[Bindable]
	public class HierarchicalModelWrapper {
		public var nestingLevel:int;
		public var expanded:Boolean;
		public var treeNode:IEventDispatcher;
	}
}