package org.flowerplatform.flexdiagram.samples.mindmap.model {
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayList;
	import mx.core.INavigatorContent;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.util.ParentAwareArrayList;
	
	/**
	 * @author Cristina Constantinescu
	 */
	[Bindable]
	public class MindMapModel {
				
		public var parent:MindMapModel;
		public var children:ArrayList = new ArrayList();
		
		public var x:Number = 0;
		public var y:Number = 0;
		public var width:Number = 0;
		public var height:Number = 0;
		
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