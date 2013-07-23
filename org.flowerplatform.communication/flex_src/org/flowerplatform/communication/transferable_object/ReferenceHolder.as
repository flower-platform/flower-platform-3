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
	
	
	[RemoteClass(alias="org.flowerplatform.blazeds.custom_serialization.ReferenceHolder")]
	public class ReferenceHolder extends AbstractReferenceHolder {
	
		public var registry:LocalIdTransferableObjectRegistry;
		
		override protected function getReferencedObjectFromRegistry(id:Object):Object {
			if (registry == null) {
				throw new Error("Trying to access LocalIdReferenceHolder.referencedObject, but the registry is null");
			}
			return registry.getObjectById(id);	
		}
		
		public function get referenceIdAsString():String {
			return String(referenceId);
		}
	}
}