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
package org.flowerplatform.codesync.views.loaded_descriptors {
	import mx.core.UIComponent;
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;

	/**
	 * @author Mircea Negreanu
	 */
	public class LoadedDescriptorsViewProvider implements IViewProvider {
		
		public static const ICON_URL:String = EditorPlugin.getInstance().getResourceUrl("images/open_resources_view.png");
		
		public static const ID:String = "loaded_descriptors";
		
		public function getId():String {
			return ID;
		}
		
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			return new LoadedDescriptorsView();
		}
		
		public function getTitle(viewLayoutData:ViewLayoutData=null):String {
			return CodeSyncPlugin.getInstance().getMessage("loadDesc.view.title");
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