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
package org.flowerplatform.web.common.security.dto {
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.web.common.entity.dto.NamedDto;
	
	/**
	 * @see Corresponding Java class.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * 	
	 * @flowerModelElementId _37klIFfcEeG3xJSMfQ3HWg
	 */
	[RemoteClass(alias="org.flowerplatform.web.security.dto.UserAdminUIDto")]
	public class UserAdminUIDto extends NamedDto  {
		
		/**
		 * @flowerModelElementId _5d-MAFfcEeG3xJSMfQ3HWg
		 */
		public var login:String;
		
		/**
		 * @flowerModelElementId _6bAcAFfcEeG3xJSMfQ3HWg
		 */
		public var password:String;
		
		/**
		 * @flowerModelElementId _7rd-UFfcEeG3xJSMfQ3HWg
		 */
		public var email:String;
		
		public var isActivated:Boolean;
		
		/**
		 * @flowerModelElementId _8fQLsFfcEeG3xJSMfQ3HWg
		 */
		public var groups:ArrayCollection;
		
		/**
		 * @flowerModelElementId _-67j8FfcEeG3xJSMfQ3HWg
		 */
		public var organizationUsers:ArrayCollection;
	}
	
}