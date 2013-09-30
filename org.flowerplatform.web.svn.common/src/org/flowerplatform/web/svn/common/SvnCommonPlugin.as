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
	
	import mx.collections.IList;
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.svn.common.action.AddToSvnIgnoreAction;
	import org.flowerplatform.web.svn.common.action.AddToVersionControlAction;
	import org.flowerplatform.web.svn.common.action.BranchTagAction;
	import org.flowerplatform.web.svn.common.action.BranchTagProjectAction;
	import org.flowerplatform.web.svn.common.action.CheckoutAction;
	import org.flowerplatform.web.svn.common.action.CleanupAction;
	import org.flowerplatform.web.svn.common.action.CommitAction;
	import org.flowerplatform.web.svn.common.action.CopyToAction;
	import org.flowerplatform.web.svn.common.action.CopyUrlToClipboardAction;
	import org.flowerplatform.web.svn.common.action.CreateRemoteFolderAction;
	import org.flowerplatform.web.svn.common.action.CreateSvnRepositoryAction;
	import org.flowerplatform.web.svn.common.action.DeleteAction;
	import org.flowerplatform.web.svn.common.action.MarkResolvedAction;
	import org.flowerplatform.web.svn.common.action.MergeAction;
	import org.flowerplatform.web.svn.common.action.RefreshRemoteResourceAction;
	import org.flowerplatform.web.svn.common.action.RenameMoveAction;
	import org.flowerplatform.web.svn.common.action.RevertAction;
	import org.flowerplatform.web.svn.common.action.ShareProjectAction;
	import org.flowerplatform.web.svn.common.action.SvnChangeCredentialsAction;
	import org.flowerplatform.web.svn.common.action.SwitchAction;
	import org.flowerplatform.web.svn.common.action.UpdateToHeadAction;
	import org.flowerplatform.web.svn.common.action.UpdateToVersionAction;
	import org.flowerplatform.web.svn.common.action.remote.OpenSvnCredentialsWindowClientCommand;
	import org.flowerplatform.web.svn.common.history.HistoryEntry;
	import org.flowerplatform.web.svn.common.remote.BranchResource;
	import org.flowerplatform.web.svn.common.remote.dto.FileDto;
	import org.flowerplatform.web.svn.common.remote.dto.GetModifiedFilesDto;

	/**
	 * @author Gabriela Murgoci
	 * 	 
	 */
	public class SvnCommonPlugin extends AbstractFlowerFlexPlugin {
		/**
		 * @flowerModelElementId _fUOOYAM7EeOrJqcAep-lCg
		 */
		protected static var INSTANCE:SvnCommonPlugin;
		
		public static const NODE_TYPE_ORGANIZATION:String = "organization";
		
		public static const NODE_TYPE_SVN_REPOSITORIES:String  = "svnRepositories";
		
		public static const  NODE_TYPE_REPOSITORY:String = "svnRepository";
		
		public static const  NODE_TYPE_FILE:String = "svnFile";
		
		public static const NODE_TYPE_FOLDER:String = "svnFolder";
		
		public static const NUMBER_LOG_ENTRIES:Number = 25;
		
		/**
		 * @flowerModelElementId _RqGPcAM1EeOrJqcAep-lCg
		 */
		public override function preStart():void {	
			super.preStart();			
			WebCommonPlugin.getInstance().explorerTreeClassFactoryActionProvider.
				actionClasses.push(CreateRemoteFolderAction,
					RenameMoveAction,
					BranchTagAction,
					BranchTagProjectAction,
					CopyToAction,
					CopyUrlToClipboardAction,
					SwitchAction,
					CleanupAction,
					ShareProjectAction,
					CreateSvnRepositoryAction,
					DeleteAction,
					SvnChangeCredentialsAction,
					RefreshRemoteResourceAction,
					CheckoutAction,
					UpdateToHeadAction,
					UpdateToVersionAction,
					MergeAction,
					CommitAction,
					RevertAction,
					MarkResolvedAction,
					AddToVersionControlAction,
					AddToSvnIgnoreAction);
		}

		protected override function registerClassAliases():void {	
			super.registerClassAliases();
			registerClassAlias("org.flowerplatform.web.svn.remote.BranchResource", BranchResource);		
			registerClassAlias("org.flowerplatform.web.svn.remote.OpenSvnCredentialsWindowClientCommand", OpenSvnCredentialsWindowClientCommand);
			registerClassAlias("org.flowerplatform.web.svn.remote.dto.FileDto", FileDto);
			registerClassAlias("org.flowerplatform.web.svn.remote.dto.GetModifiedFilesDto", GetModifiedFilesDto);	
			registerClassAlias("org.flowerplatform.web.svn.history.HistoryEntry", HistoryEntry);			
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
		
		public static function getWorkingDirectoryTreeNode(treeNode:TreeNode):TreeNode {
			var treeNode2:TreeNode = treeNode;
			while (treeNode2.pathFragment.type != "workingDirectory")
				treeNode2 = treeNode2.parent;
			return treeNode2;
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
				node_type != SvnCommonPlugin.NODE_TYPE_FILE && node_type != SvnCommonPlugin.NODE_TYPE_FOLDER) {
				isSvnRepositoryFile = false;
			}			
			return isSvnProjectFile || isSvnRepositoryFile;
		} 
	}
}
