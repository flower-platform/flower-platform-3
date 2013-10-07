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
package org.flowerplatform.web.common.properties.ui {
	import mx.core.UIComponent;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.tree.GenericTreeList;
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.web.common.explorer.ExplorerTreeList;

	public class PropertiesViewProvider implements IViewProvider {
		
		public static const ID:String = "properties";
		
		public function getId():String {
			return ID;
		}
		
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			var propertiesList:PropertiesList = new PropertiesList();
			var statefulClient:GenericTreeStatefulClient = new GenericTreeStatefulClient();
			
//			PropertiesList.dispatchEnabled = true;
//			PropertiesList.statefulClient = statefulClient;
			
//			statefulClient.statefulServiceId = "explorerTreeStatefulService";
//			statefulClient.clientIdPrefix = "Explorer2";
//			statefulClient.treeList = treeList;
			
//			CommunicationPlugin.getInstance().statefulClientRegistry.register(statefulClient, null);
			return propertiesList;
		}
		
		public function getTitle(viewLayoutData:ViewLayoutData=null):String	{
			return "Properties";
		}
		
		public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return null;
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {
			return null;
		}
		
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}
	}
}