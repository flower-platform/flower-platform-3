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
	 * @author Mariana Gheorghe
	 */
	[RemoteClass(alias="org.flowerplatform.codesync.remote.CodeSyncElementDescriptor")]
	public class CodeSyncElementDescriptor {
	
		public var codeSyncType:String;
		public var label:String;
		public var iconUrl:String;
		public var defaultName:String;
		public var extension:String;
		public var codeSyncTypeCategories:ArrayCollection;
		public var childrenCodeSyncTypeCategories:ArrayCollection;
		public var features:ArrayCollection;
		public var keyFeature:String;		
		public var standardDiagramControllerProviderFactory:String;
		
		/**
		 * @author Cristina Constantinescu
		 */
		public var createCodeSyncElement:Boolean;
	}
}