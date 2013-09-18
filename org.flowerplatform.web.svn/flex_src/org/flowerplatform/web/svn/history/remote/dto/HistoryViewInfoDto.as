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
package org.flowerplatform.web.svn.history.remote.dto {
	
	
			
	/**
	 *	@author Victor Badila
	 */ 
	[RemoteClass]
	public class HistoryViewInfoDto {
		
		public var repositoryLocation:String;	
		
		public var repositoryName:String;
		
		public var selectedObject:Object;
		
		public var isResource:Boolean;
		
		public var statefulClientId:String;
		
		public static const SHOWALLRESOURCE:int = 0;
		
		public static const SHOWALLFOLDER:int = 1;
		
		public static const SHOWALLPROJECT:int = 2;
		
		public static const SHOWALLREPO:int = 3;
				
		public var path:String;
		
		public var info:String;		
							
		public var filter:int;
	}
	
}