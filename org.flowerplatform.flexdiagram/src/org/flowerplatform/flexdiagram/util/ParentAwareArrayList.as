/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.flexdiagram.util {
	import flash.events.Event;
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
		
		/**
		 * If <code>true</code>, the next calls to "remove"/"remove all" methods 
		 * will not dispatch remove events.
		 * 
		 * <p>
		 * Use this to dispatch less COLLECTION_CHANGE events.
		 * <p>
		 * Caution: use try/finnaly block when setting this value to <code>true</code>.
		 * Otherwise, a remove event will never be dispatched.
		 */ 
		public var removeEventsCanBeIgnored:Boolean;
		
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
			
			dispatchRemoveEventForEachItem(oldSource); 
		}
		
		/**
		 * Dispatches REMOVE event for each removed item.
		 */ 
		override public function removeAll():void {
			var oldSource:Array = source != null ? source.concat() : null;
						
			super.removeAll();					
				
			dispatchRemoveEventForEachItem(oldSource); 
		}
		
		private function dispatchRemoveEventForEachItem(oldSource:Array):void {
			try {				
				removeEventsCanBeIgnored = true;
				if (oldSource != null)	{
					var len:int = length;
					for (var i:int = 0; i < oldSource.length; i++) {
						dispatchRemoveEvent(oldSource[i], i);					
					}
				}  						
			} finally {
				removeEventsCanBeIgnored = false;
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