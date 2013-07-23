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
package org.flowerplatform.web.common.security.dto
{
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.web.common.entity.dto.NamedDto;
	
	
	/**
	 * @see Corresponding Java class.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * @flowerModelElementId _Q9EsgF34EeGwLIVyv_iqEg
	 */ 
	[RemoteClass(alias="org.flowerplatform.web.security.dto.OrganizationAdminUIDto")]
	public class OrganizationAdminUIDto	extends NamedDto {
		
		public var label:String;
		
		public var URL:String;
		
		public var logoURL:String;
		
		public var iconURL:String;
		
		public var activated:Boolean;
		
		public var projectsCount:int;
		
		public var filesCount:int;

		public var modelsCount:int;
		
		public var diagramsCount:int;
		
		public var pinned:Array;
		
		public var status:Object;
		
		/**
		 * @flowerModelElementId _Q9FTkl34EeGwLIVyv_iqEg
		 */
		public var groups:ArrayCollection;		
		
		public var SVNRepositoryURLs:ArrayCollection;
		
		override public function toString():String {
			return label;
		}
	}
	
}