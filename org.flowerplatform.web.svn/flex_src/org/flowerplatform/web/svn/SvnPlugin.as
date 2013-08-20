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
package  org.flowerplatform.web.svn {
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	import org.flowerplatform.web.WebPlugin;
	import org.flowerplatform.web.common.WebCommonPlugin;
	
	/**
	 * @author Gabriela Murgoci
	 */

	/**
	 * @flowerModelElementId _FijBEAMwEeOrJqcAep-lCg
	 */
	public class SvnPlugin extends AbstractFlowerFlexPlugin  {
		/**
		 * @flowerModelElementId _syXvMAMzEeOrJqcAep-lCg
		 */
		
	
		/**
		 * @flowerModelElementId _YDrY8AM7EeOrJqcAep-lCg
		 */
		protected static var INSTANCE:SvnPlugin;
		
		protected var svnCommonPlugin:SvnCommonPlugin = new SvnCommonPlugin();
		
		public static const TREE_NODE_KEY_IS_FOLDER:String = "isFolder";
				
		/**
		 * @flowerModelElementId _DxJioAM1EeOrJqcAep-lCg
		 */
		public override function preStart():void {	
		
			svnCommonPlugin.preStart();
			if (INSTANCE != null) {
				throw new Error("Plugin " + Utils.getClassNameForObject(this, true) + " has already been started");
			}
			INSTANCE = this;	
		
		}
		/**
		 * @flowerModelElementId _DxQ3YAM1EeOrJqcAep-lCg
		 */
		protected override function registerClassAliases():void {	
			
			super.registerClassAliases();
		}
		/**
		 * @flowerModelElementId _DxUhxAM1EeOrJqcAep-lCg
		 */
		public override function start():void { 	
		
			super.start();
			svnCommonPlugin.flexPluginDescriptor = flexPluginDescriptor;	
			svnCommonPlugin.start();
		}
		/**
		 * @flowerModelElementId _tv-tUAM7EeOrJqcAep-lCg
		 */
		public static function getInstance():SvnPlugin {
			
			return INSTANCE;
		}
	}
}