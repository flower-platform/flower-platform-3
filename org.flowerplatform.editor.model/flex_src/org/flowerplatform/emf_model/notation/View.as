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
package org.flowerplatform.emf_model.notation {
	import mx.collections.IList;
	
	import org.flowerplatform.communication.transferable_object.AbstractReferenceHolder;
	import org.flowerplatform.communication.transferable_object.ReferenceHolder;
	import org.flowerplatform.communication.transferable_object.TransferableObject;

	[Bindable]
	[RemoteClass]
	public class View extends TransferableObject {
		
		public var parentView_RH:ReferenceHolder;
		
		public var viewType:String;
		public var persistentChildren_RH:IList;
		
		[Transient]
		public var viewDetails:Object;
	}
}