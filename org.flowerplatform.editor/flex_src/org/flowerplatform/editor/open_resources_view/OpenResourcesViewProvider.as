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
package org.flowerplatform.editor.open_resources_view {
	
	import com.crispico.flower.util.layout.view.ITabCustomizer;
	import com.crispico.flower.util.layout.view.ViewPopupWindow;
	
	import mx.core.UIComponent;
	
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;

	/**
	 * View provider for the open resources view.
	 * 
	 * @author Cristi
	 */ 
	public class OpenResourcesViewProvider implements IViewProvider {

		public static const ICON_URL:String = EditorPlugin.getInstance().getResourceUrl("images/open_resources_view.png");
		
		public static const ID:String = "open_resources";
		
		
		public function getId():String {
			return ID;
		}
		
		
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			return new OpenResourcesView();
		}
		
		
		public function getTitle(viewLayoutData:ViewLayoutData=null):String	{
			return "Open Resources";
		}
		
		
		public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return ICON_URL;
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {
			return null;
		}
	
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}
	}
}
