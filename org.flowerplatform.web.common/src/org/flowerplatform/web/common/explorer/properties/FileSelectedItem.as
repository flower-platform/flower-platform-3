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
package org.flowerplatform.web.common.explorer.properties {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.properties.remote.SelectedItem;

	[Bindable]
	[RemoteClass(alias="org.flowerplatform.web.properties.remote.FileSelectedItem")]
	[SecureSWF(rename="off")]
	/**
	 * @author Razvan Tache
	 */
	public class FileSelectedItem extends SelectedItem {
		/** 
		 * 
		 */
		[SecureSWF(rename="off")]
		public var pathWithRoot:ArrayCollection;
		
		public function FileSelectedItem(pathWithRoot:ArrayCollection) {
			this.pathWithRoot = pathWithRoot;	
			this.itemType = "file";
		}
		
	}
}