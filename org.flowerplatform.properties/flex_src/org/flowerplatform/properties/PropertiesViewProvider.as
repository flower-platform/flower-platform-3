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
package org.flowerplatform.properties {
	import mx.containers.HBox;
	import mx.containers.VBox;
	import mx.core.UIComponent;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.tree.GenericTreeList;
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.properties.PropertiesPlugin;
	
	import spark.components.Button;
	import spark.components.Group;
	import spark.components.VGroup;
	
	/**
	 * @author Razvan Tache
	 */
	public class PropertiesViewProvider implements IViewProvider {
		
		public static const ID:String = "properties";
		
		public function getId():String {
			return ID;
		}
		
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			PropertiesPlugin.getInstance().propertyList = new PropertiesList();
			
			return PropertiesPlugin.getInstance().propertyList;
		}
		
		public function getTitle(viewLayoutData:ViewLayoutData=null):String	{
			return "Properties";
		}
		
		public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			if (viewLayoutData == null) {
				return null;
			}
			return PropertiesPlugin.getInstance().getResourceUrl("images/properties_view.png");
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {
			return null;
		}
		
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}
	}
}