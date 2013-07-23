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