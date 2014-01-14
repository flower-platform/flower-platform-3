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
package org.flowerplatform.codesync.code.javascript {
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class CodeSyncCodeJavascriptPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:CodeSyncCodeJavascriptPlugin;
		
		public static function getInstance():CodeSyncCodeJavascriptPlugin {
			return INSTANCE;
		}
		
		override public function start():void {
			super.start();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
		}
		
		override protected function registerMessageBundle():void {
			// no messages yet
		}
		
	}
}
