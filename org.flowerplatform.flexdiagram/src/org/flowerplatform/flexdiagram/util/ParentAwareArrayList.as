package org.flowerplatform.flexdiagram.util {
	import flash.utils.ByteArray;
	
	import mx.collections.ArrayList;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.events.PropertyChangeEvent;
	import mx.utils.ArrayUtil;
	
	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class ParentAwareArrayList extends ArrayList {
		
		public var parent:Object;
		
		public function ParentAwareArrayList(parent:Object, source:Array=null) {
			super(source);
			this.parent = parent;
		}
		
		/**
		 * If the source wasn't empty, 
		 * dispatches REMOVE event for each old item.
		 */
		override public function set source(s:Array):void {
			var oldSource:Array = source != null ? source.concat() : null;
			
			super.source = s;
			
			if (oldSource != null)	{
				var len:int = length;
				for (var i:int = 0; i < oldSource.length; i++) {
					dispatchRemoveEvent(oldSource[i], i);
				}
			}    
		}
		
		/**
		 * Dispatches REMOVE event for each removed item.
		 */ 
		override public function removeAll():void {
			var oldSource:Array = source != null ? source.concat() : null;
			
			super.removeAll();
				
			if (oldSource != null)	{
				var len:int = length;
				for (var i:int = 0; i < oldSource.length; i++) {
					dispatchRemoveEvent(oldSource[i], i);					
				}
			}    
		}
		
		private function dispatchRemoveEvent(item:Object, location:int):void {
			var event:CollectionEvent = new CollectionEvent(CollectionEvent.COLLECTION_CHANGE);
			event.kind = CollectionEventKind.REMOVE;
			event.items.push(item);
			event.location = location;
			dispatchEvent(event);
		}
		
	}
}