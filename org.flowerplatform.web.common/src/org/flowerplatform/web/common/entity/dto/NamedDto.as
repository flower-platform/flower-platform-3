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
package org.flowerplatform.web.common.entity.dto {
	
	/**
	 * @see Corresponding Java class.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * 	 
	 * @flowerModelElementId _rf68wFfcEeG3xJSMfQ3HWg
	 */
	[RemoteClass(alias="org.flowerplatform.web.entity.dto.Dto")]
	[Bindable]	
	public class NamedDto extends Dto {
		
		/**
		 * @flowerModelElementId _wjOekFfcEeG3xJSMfQ3HWg
		 */
		public var name:String;
		
		public function toString():String {
			return name;
		}
		
		
	}
	
}