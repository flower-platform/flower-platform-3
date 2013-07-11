package org.flowerplatform.flexdiagram.samples.mindmap.model {
	import mx.collections.ArrayList;
	
	import org.flowerplatform.flexdiagram.util.ParentAwareArrayList;
	
	[Bindable]
	public class MindMapModel {
		
		public var parent:MindMapModel;
		public var children:ArrayList = new ArrayList();
		
		public var x:int;
		public var y:int;
		public var width:int;
		public var height:int;
		
		public var text:String;
		private var _expanded:Boolean;
		
		public function MindMapModel() {

		}
		
		public function get expanded():Boolean {
			return _expanded;
		}
		
		public function set expanded(value:Boolean) {
			_expanded = value;
		}
		
	}
	
}