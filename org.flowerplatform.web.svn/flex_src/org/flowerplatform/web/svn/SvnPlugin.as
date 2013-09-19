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

	import flash.geom.Utils3D;

	
	import flash.net.registerClassAlias;
	
	import mx.collections.IList;

	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.web.WebPlugin;
	import org.flowerplatform.web.common.WebCommonPlugin;

	import org.flowerplatform.web.svn.common.SvnCommonPlugin;

	//import org.flowerplatform.web.svn.actions.ShowHistoryAction;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	//import org.flowerplatform.web.svn.history.HistoryEntry;
	//import org.flowerplatform.web.svn.history.SvnHistoryViewProvider;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	

	/**
	 * @author Gabriela Murgoci
	 */

	public class SvnPlugin extends AbstractFlowerFlexPlugin {
	
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
			//FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new SvnHistoryViewProvider());
			//WebPlugin.getInstance().perspectives.push(new GitPerspective());	
			//WebCommonPlugin.getInstance().explorerTreeClassFactoryActionProvider.actionClasses.push(ShowHistoryAction);
		}
		
		/**
		 * @flowerModelElementId _DxQ3YAM1EeOrJqcAep-lCg
		 */
		protected override function registerClassAliases():void {				
			super.registerClassAliases();
			//registerClassAlias("org.flowerplatform.web.svn.remote.history.HistoryEntry", HistoryEntry);
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
		
		public function showHistoryIsPossible(selection:IList):Boolean {
			if (selection.length != 1)
				return false;			
			if (!(selection.getItemAt(0) is TreeNode)) {
				return false;
			}
			var isSvnProjectFile:Boolean = true;
			var isSvnRepositoryFile:Boolean = true;			
			var element:TreeNode = TreeNode(selection.getItemAt(0));			
			if (element.customData == null ||
				element.customData.svnFileType == null ||
				element.customData.svnFileType == false) {
				isSvnProjectFile = false;
			}						
			var node_type:String = element.pathFragment.type;			
			if(node_type != SvnCommonPlugin.NODE_TYPE_REPOSITORY && 
				node_type != SvnCommonPlugin.NODE_TYPE_FILE) {
				isSvnRepositoryFile = false;
			}			
			return isSvnProjectFile || isSvnRepositoryFile;
			
		} 
	}
	
}
