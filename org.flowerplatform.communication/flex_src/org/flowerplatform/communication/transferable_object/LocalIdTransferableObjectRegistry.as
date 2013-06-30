package org.flowerplatform.communication.transferable_object {
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;
	
	public class LocalIdTransferableObjectRegistry extends AbstractTransferableObjectRegistry {
		
		override protected function getIdForObject(object:IEventDispatcher):Object {
			return TransferableObject(object).id;
		}
		
		override protected function copyProperty(source:Object, dest:Object, propertyName:String, postProcessOnly:Boolean):void {
			super.copyProperty(source, dest, propertyName, postProcessOnly);
			var value:Object = dest[propertyName];
			if (value is ReferenceHolder) {
				ReferenceHolder(value).registry = this;
			} else if (value is ArrayCollection) {
				for each (var currentElement:Object in ArrayCollection(value)) {
					if (!(currentElement is ReferenceHolder)) {
						// if the first is not a RefHold => the next elems are not RefHold for sure
						break;
					} else {
						ReferenceHolder(currentElement).registry = this;
					}
				}
			}
		}
		
	}
}