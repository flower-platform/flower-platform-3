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