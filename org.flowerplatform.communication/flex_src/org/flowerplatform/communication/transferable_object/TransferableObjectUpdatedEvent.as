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
	import flash.events.Event;

	/**
	 * Dispatched when a <code>TransferableObject</code> has been
	 * updated (from Java). The difference between this event and PropertyChangeEvent 
	 * is that this event is dispatched only once per object, regardless of
	 * the fact that the copied values are new or not. PropertyChangeEvent
	 * is dispatched once per property (and not per object) only if the new
	 * value is different from the old one.
	 * 
	 * @author Cristi
	 * @flowerModelElementId _62iPAEznEeGsUPSh9UfXpw
	 */
	public class TransferableObjectUpdatedEvent extends Event {
		
		public static const OBJECT_UPDATED:String = "TransferableObjectUpdatedEvent";
		
		public var object:Object;
		
		public function TransferableObjectUpdatedEvent(object:Object) {
			super(OBJECT_UPDATED, false, false);
			this.object = object;
		}
		
	}
}