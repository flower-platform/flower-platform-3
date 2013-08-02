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
	
	import mx.utils.DescribeTypeCache;

	/**
	 * A very important class/foundation of the TransferableObjects mechanism. A
	 * Transferable object is an object that exist both on the Java backend and on the
	 * Flex client(s). They have the following characteristics:
	 * <ul>
	 * 	<li>Are uniquely identified by an ID. The Flex client has a global registry
	 * 		(<code>TransferableObjectRegistry</code>) that provides a mapping between
	 * 		an ID and an object instance.
	 * 	<li>They are sent ONLY from Java to Flex using <code>TransferObjectsCommand</code>.
	 * 		Java is responsible for listening to the Java version of the objects; when they are 
	 * 		updated/disposed (or when new instances are created) Java should send the command.
	 * 		The Flex implementation would look in the registry for objects by id. If not 
	 * 		found they are added to the registry; otherwise they are retrieved and ALL the 
	 * 		properties of the deserialized instances are copied to the existing ones. The 
	 * 		deserialized instance is "thrown away" after this operation. In the case of a 
	 * 		"dispose" they are unregistered and the <code>dispose()</code> method is called.
	 * 		This method should set the object fields to null to easen up the garbage
	 * 		collection.
	 * 	<li>References between TransferableObjects ("one" or "many") use 
	 * 		<code>ReferenceHolder</code> class (or a list of them) as wrapper. We have this
	 * 		way a weak linking between objects, and when changes occur in Java only modified
	 * 		objects are sent (instead of a whole tree). These fields have the "_RH" suffix.
	 * 	<li>AS version of the objects can have (usually have) less fields than Java version.
	 * 		<code>TransferableObjectDescriptor</code> should be sent to Java after the app
	 * 		has been initialized (within a subclass of a <code>FlexAppInitializedCommand</code>).
	 * </ul>
	 * 
	 * The above items should be treated as responsabilities when implementing/working with
	 * TransferableObjects.
	 * 
	 * @author Cristi
	 * 
	 */ 
	[Bindable]
	public class TransferableObject implements IDisposable {
		
		protected var _id:Object;
		
		public function get id():Object {
			return _id;
		}

		public function set id(id:Object):void {
			_id = id;
		}

		
		/**
		 * Called by the registry when the object is disposed (as effect of 
		 * an update command from Java).
		 */
		public function dispose():void {
			_id = 0;
		}
		
		public function get idAsString():String {
			return String(id);
		}
		
	}
}