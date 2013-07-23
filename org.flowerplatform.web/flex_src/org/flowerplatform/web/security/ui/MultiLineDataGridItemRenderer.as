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
package org.flowerplatform.web.security.ui {
	
	import mx.collections.ArrayCollection;
	import mx.controls.dataGridClasses.DataGridItemRenderer;
	
	import org.flowerplatform.web.common.entity.dto.NamedDto;
	import org.flowerplatform.web.common.security.dto.OrganizationUserAdminUIDto;
	
	
	/**
	 * Custom data grid item renderer that displays the information on multiple
	 * lines.
	 * 
	 * @author Mariana
	 */ 
	public class MultiLineDataGridItemRenderer extends DataGridItemRenderer {
		
		public var listName:String;
		
		override public function set data(value:Object):void {
			var str:String = "";			
			var list:ArrayCollection = ArrayCollection(value[listName]);
			for each (var obj:Object in list) {
				if (listName == "groups") {
					var group:NamedDto = NamedDto(obj);
					str += "@" + group.name + "\n";
				}
				if (listName == "organizationUsers") {
					var ou:OrganizationUserAdminUIDto = OrganizationUserAdminUIDto(obj);
					str += ou.organization.label + " [" + ou.status + "]\n";
				}
			}
			listData.label = str.substring(0, str.length - 1);
			super.data = value;
		}
		
	}
}