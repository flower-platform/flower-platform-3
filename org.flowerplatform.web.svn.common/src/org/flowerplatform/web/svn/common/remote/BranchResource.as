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

package org.flowerplatform.web.svn.common.remote {
	import mx.collections.ArrayCollection;
	import mx.controls.List;
	
	import org.flowerplatform.communication.tree.remote.PathFragment;
	
	/**
	 * @see Corresponding Java class.
	 * 
	 * @author Gabriela Murgoci
	 * 
	 * @flowerModelElementId _n4QD4GxTEeGmX-bWmnlzew
	 */
	[Bindable]
	[SecureSWF(rename="off")]
	public class BranchResource {
		
		/**
		 * @flowerModelElementId _pYRsMGxTEeGmX-bWmnlzew
		 */
		[SecureSWF(rename="off")]
		public var path:Object;
		
		/**
		 * @flowerModelElementId _p-R8QGxTEeGmX-bWmnlzew
		 */
		[SecureSWF(rename="off")]
		public var name:String;
		
		/**
		 * @flowerModelElementId _mYLi0GxUEeGmX-bWmnlzew
		 */
		[SecureSWF(rename="off")]
		public var image:String;
		
		/**
		 * @flowerModelElementId _-oXZoHJ9EeGZAqbcNpPqCg
		 */
		[SecureSWF(rename="off")]
		public var partialPath:String;
		
		/**
		 * @flowerModelElementId _-oYnwHJ9EeGZAqbcNpPqCg
		 */
		[SecureSWF(rename="off")]
		public var customizedPath:String;
		
	}
}