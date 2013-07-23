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
package org.flowerplatform.editor.java {
	import mx.collections.ArrayList;
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.editor.java.propertypage.JavaPropertyPageProvider;
	import org.flowerplatform.editor.java.propertypage.remote.JavaProjectPropertyPageService;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.web.common.WebCommonPlugin;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class JavaEditorPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:JavaEditorPlugin;
		
		public var javaProjectPropertyPageService:JavaProjectPropertyPageService = new JavaProjectPropertyPageService();
		
		public static function getInstance():JavaEditorPlugin {
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
			WebCommonPlugin.getInstance().projectPropertyProviders.addItem(new JavaPropertyPageProvider());
		}
		
		
	}
}