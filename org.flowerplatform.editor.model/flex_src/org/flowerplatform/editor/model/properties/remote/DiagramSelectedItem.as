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
package org.flowerplatform.editor.model.properties.remote {
	import org.flowerplatform.properties.remote.SelectedItem;

	[Bindable]
	[RemoteClass]
	[SecureSWF(rename="off")]
	/**
	 * @author Razvan Tache
	 */
	public class DiagramSelectedItem extends SelectedItem {
		/**
		 * 
		 */
		[SecureSWF(rename="off")]
		public var xmiID:String;
		
		/** 
		 * 
		 */
		[SecureSWF(rename="off")]
		public var diagramEditableResourcePath:String;
		
		/** 
		 * 
		 */
		[SecureSWF(rename="off")]
		public var editorStatefulServiceId:String;
		
		public function DiagramSelectedItem(xmiID:String, diagramEditableResourcePath:String, editorStatefulServiceId:String, itemType:String) {
			this.diagramEditableResourcePath = diagramEditableResourcePath;
			this.xmiID = xmiID;
			this.editorStatefulServiceId = editorStatefulServiceId;
			this.itemType = itemType;
		}
	}
}