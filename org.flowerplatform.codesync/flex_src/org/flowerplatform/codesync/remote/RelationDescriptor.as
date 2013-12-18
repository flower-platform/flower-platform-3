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
package org.flowerplatform.codesync.remote {
	
	import mx.collections.ArrayCollection;
	
	/**
	 * @author Cristian Spiescu
	 */
	[RemoteClass]
	[Bindable]
	public class RelationDescriptor {
	
		public var type:String;
		public var label:String;
		public var iconUrl:String;
		public var sourceCodeSyncTypes:ArrayCollection;
		public var targetCodeSyncTypes:ArrayCollection;
		public var sourceCodeSyncTypeCategories:ArrayCollection;
		public var targetCodeSyncTypeCategories:ArrayCollection;
		public var acceptTargetNullIfNoCodeSyncTypeDetected:Boolean;
	}
}