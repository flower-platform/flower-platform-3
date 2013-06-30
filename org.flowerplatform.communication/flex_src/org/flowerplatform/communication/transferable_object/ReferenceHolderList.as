package org.flowerplatform.communication.transferable_object {
	import flash.events.Event;
	
	import mx.collections.IList;
	
	public class ReferenceHolderList implements IList {
		
		protected var referenceHolders:IList;
		
		public function ReferenceHolderList(referenceHolders:IList) {
			this.referenceHolders = referenceHolders;
		}
		
		public function get length():int {
			return referenceHolders.length;
		}
		
		public function addItem(item:Object):void {
			throw new Error("Unsupported operation");
		}
		
		public function addItemAt(item:Object, index:int):void {
			throw new Error("Unsupported operation");
		}
		
		public function getItemAt(index:int, prefetch:int=0):Object	{
			return ReferenceHolder(referenceHolders.getItemAt(index, prefetch)).referencedObject;
		}
		
		public function getItemIndex(item:Object):int {
			throw new Error("Unsupported operation");
		}
		
		public function itemUpdated(item:Object, property:Object=null, oldValue:Object=null, newValue:Object=null):void {
			throw new Error("Unsupported operation");
		}
		
		public function removeAll():void {
			throw new Error("Unsupported operation");
		}
		
		public function removeItemAt(index:int):Object {
			throw new Error("Unsupported operation");
		}
		
		public function setItemAt(item:Object, index:int):Object {
			throw new Error("Unsupported operation");
		}
		
		public function toArray():Array {
			throw new Error("Unsupported operation");
		}
		
		public function addEventListener(type:String, listener:Function, useCapture:Boolean=false, priority:int=0, useWeakReference:Boolean=false):void {
			throw new Error("Unsupported operation");
		}
		
		public function removeEventListener(type:String, listener:Function, useCapture:Boolean=false):void {
			throw new Error("Unsupported operation");
		}
		
		public function dispatchEvent(event:Event):Boolean {
			throw new Error("Unsupported operation");
		}
		
		public function hasEventListener(type:String):Boolean {
			throw new Error("Unsupported operation");
		}
		
		public function willTrigger(type:String):Boolean {
			throw new Error("Unsupported operation");
		}
	}
}