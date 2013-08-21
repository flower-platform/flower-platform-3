/*
license-start

Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation version 3.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details, at <http://www.gnu.org/licenses/>.

Contributors:
Crispico - Initial API and implementation  The product is licensed under GPL. This is the new version of the license.

license-end
*/
package org.flowerplatform.editor.javascript
{
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.editor.javascript.propertypage.JavaScriptPropertyPageProvider;
	import org.flowerplatform.editor.javascript.propertypage.remote.JavaScriptProjectPropertyPageService;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.web.common.WebCommonPlugin;
	/**
	 * @author Razvan Tache
	 * @see JavaPropertyPageProvider
	 */
	public class JavaScriptEditorPlugin extends AbstractFlowerFlexPlugin {

		protected static var INSTANCE:JavaScriptEditorPlugin;
		
		public var javaScriptProjectPropertyPageService:JavaScriptProjectPropertyPageService = new JavaScriptProjectPropertyPageService();
		
		public static function getInstance():JavaScriptEditorPlugin {
			return INSTANCE;
		}
		 
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this; 			
		}
		
		override public function start():void {
			super.start();
			WebCommonPlugin.getInstance().projectPropertyProviders.addItem(new JavaScriptPropertyPageProvider());
		}
		
	}
	
}