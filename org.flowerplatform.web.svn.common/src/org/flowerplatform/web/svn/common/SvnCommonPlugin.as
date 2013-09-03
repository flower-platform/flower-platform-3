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
package org.flowerplatform.web.svn.common {
	import flash.net.registerClassAlias;
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.svn.common.action.BranchTagAction;
	import org.flowerplatform.web.svn.common.action.CreateRemoteFolderAction;
	import org.flowerplatform.web.svn.common.action.CreateSvnRepositoryAction;
	import org.flowerplatform.web.svn.common.action.RenameMoveAction;
	import org.flowerplatform.web.svn.common.remote.BranchResource;
	
	/**
	 * @author Gabriela Murgoci
	 * 	 
	 * @flowerModelElementId _5Z5NMAMvEeOrJqcAep-lCg
	 */
	
	public class SvnCommonPlugin extends AbstractFlowerFlexPlugin  {
		
		/**
		 * @flowerModelElementId _fUOOYAM7EeOrJqcAep-lCg
		 */
		protected static var INSTANCE:SvnCommonPlugin;
		
		public static const NODE_TYPE_ORGANIZATION:String = "organization";
		
		public static const NODE_TYPE_SVN_REPOSITORIES:String  = "svnRepositories";
		
		public static const  NODE_TYPE_REPOSITORY:String = "svnRepository";
		
		public static const  NODE_TYPE_FILE:String = "svnFile";
		
		/**
		 * @flowerModelElementId _RqGPcAM1EeOrJqcAep-lCg
		 */
		public override function preStart():void {	
			super.preStart();			
			WebCommonPlugin.getInstance().explorerTreeClassFactoryActionProvider.actionClasses.push(CreateRemoteFolderAction, CreateSvnRepositoryAction, RenameMoveAction, BranchTagAction);
		}
		
		/**
		 * @flowerModelElementId _RqKg4AM1EeOrJqcAep-lCg
		 */
		override protected function registerClassAliases():void {				
		
			registerClassAlias("org.flowerplatform.web.svn.remote.BranchResource", BranchResource);			
			
		}
		
		/**
		 * @flowerModelElementId _RqOLQAM1EeOrJqcAep-lCg
		 */
		public override function start():void {				
			super.start();
			if (INSTANCE != null) {
				throw new Error("Plugin " + Utils.getClassNameForObject(this, true) + " has already been started");
			}
			INSTANCE = this;			
		}
		
		/**
		 * @flowerModelElementId _v7-q0AM7EeOrJqcAep-lCg
		 */
		public static function getInstance():SvnCommonPlugin {
			return INSTANCE;
		}
	}
	
}
