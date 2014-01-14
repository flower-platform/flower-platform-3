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
package org.flowerplatform.codesync.regex {
	
	import mx.core.UIComponent;
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.codesync.regex.ui.RegexConfigsView;
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;

	/**
	 * @author Cristina Constantinescu
	 */ 
	public class RegexConfigsViewProvider implements IViewProvider {
		
		public static const ID:String = "regex_configs";
		
		public function getId():String {
			return ID;
		}
		
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			return new RegexConfigsView();
		}
		
		public function getTitle(viewLayoutData:ViewLayoutData=null):String {
			return CodeSyncPlugin.getInstance().getMessage("regex.configs");
		}
		
		public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return CodeSyncPlugin.getInstance().getResourceUrl("images/regex/wrench.png");
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {
			return null;
		}
		
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}
		
	}
}