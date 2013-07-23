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
package org.flowerplatform.web.git.history.remote.dto {
	import org.flowerplatform.web.git.remote.dto.ViewInfoDto;
	
			
	/**
	 *	@author Cristina Constantinescu
	 */ 
	[RemoteClass]
	public class HistoryViewInfoDto extends ViewInfoDto {
		
		public static const SHOWALLRESOURCE:int = 0;
		
		public static const SHOWALLFOLDER:int = 1;
		
		public static const SHOWALLPROJECT:int = 2;
		
		public static const SHOWALLREPO:int = 3;
				
		public var path:String;
		
		public var info:String;		
							
		public var filter:int;
	}
	
}