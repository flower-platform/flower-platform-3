package org.flowerplatform.flexdiagram.samples.mindmap.model {
	import mx.collections.ArrayList;
	import mx.core.INavigatorContent;
	
	import org.flowerplatform.flexdiagram.util.ParentAwareArrayList;
	
	[Bindable]
	public class MindMapModel {
		
		public static const LEFT:int = -1;
		public static const RIGHT:int = 1;
		
		public var parent:MindMapModel;
		public var children:ArrayList = new ArrayList();
		
		public var x:int;
		public var y:int;
		public var width:int;
		public var height:int;
		
		public var text:String;
		public var side:int;
		
		private var _expanded:Boolean;
		
		public function get expanded():Boolean {
			return _expanded;
		}
		
		public function set expanded(value:Boolean):void {
			_expanded = value;
		}
		
	}
	
}