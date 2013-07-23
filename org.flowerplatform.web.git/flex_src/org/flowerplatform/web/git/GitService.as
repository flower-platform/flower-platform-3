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
package org.flowerplatform.web.git {
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.web.git.dto.GitRef;
	import org.flowerplatform.web.git.dto.RemoteConfig;

	/**
	 * @author Cristina Constantinescu
	 */
	public class GitService {
		
		public static const SERVICE_ID:String = "gitService";
		
		private function convertSelection(selection:ArrayList):ArrayCollection {
			var array:ArrayCollection = new ArrayCollection();
			for (var i:int=0; i <selection.length; i++) {
				array.addItem(TreeNode(selection.getItemAt(i)).getPathForNode(true));
			}
			return array;
		}
		
		private function invokeServiceMethod(methodName:String, parameters:Array, 
											 resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand(	SERVICE_ID, methodName, parameters, resultCallbackObject, resultCallbackFunction));
		}
		
		public function validateRepositoryURL(selectedNode:TreeNode, repositoryURL:String, 
											  resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {			
			invokeServiceMethod(
				"validateRepositoryURL", 
				[selectedNode.getPathForNode(true), repositoryURL], 
				resultCallbackObject, resultCallbackFunction);	
		}
		
		public function getBranches(repositoryURL:String, 
									resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			invokeServiceMethod(
				"getBranches", 
				[repositoryURL], 
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function cloneRepository(selectedNode:TreeNode, repositoryURL:String, branches:ArrayCollection,
										remoteName:String, cloneAllBranches:Boolean, 
										resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			invokeServiceMethod(
				"cloneRepository", 
				[selectedNode.getPathForNode(true), repositoryURL, branches, remoteName, cloneAllBranches], 
				resultCallbackObject, resultCallbackFunction);	
		}
	
		public function deleteRepository(selectedNode:TreeNode,
										   resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {			
			invokeServiceMethod(
				"deleteRepository", 
				[selectedNode.getPathForNode(true)], 
				resultCallbackObject, resultCallbackFunction);	
		}
		
		public function login(repositoryURI:String, user:String, password:String, command:InvokeServiceMethodServerCommand):void {
			invokeServiceMethod(
				"login", 
				[repositoryURI, user, password, command]);
		}
		
		public function changeCredentials(repositoryURI:String, user:String, password:String):void {
			invokeServiceMethod(
				"changeCredentials", 
				[repositoryURI, user, password]);
		}
		
		public function merge(repository:String, branch:String, squash:Boolean,
							  resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			invokeServiceMethod(
					"merge", 
					[repository, branch, squash], 
					resultCallbackObject, resultCallbackFunction);	
		}
		
		public function rebase(repository:String, branch:String,
							   resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			invokeServiceMethod(
					"rebase", 
					[repository, branch], 
					resultCallbackObject, resultCallbackFunction);	
		}
		
		public function reset(repository:String, targetRef:String, resetType:int,
							  resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			invokeServiceMethod( 
					"reset", 
					[repository, targetRef, resetType], 
					resultCallbackObject, resultCallbackFunction);	
		}
		
		public function getNodeAdditionalData(selectedNode:TreeNode,
							  resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			invokeServiceMethod( 
				"getNodeAdditionalData", 
				[selectedNode.getPathForNode(true)], 
				resultCallbackObject, resultCallbackFunction);	
		}
		
		public function configRemote(selectedNode:TreeNode,remoteConfig:RemoteConfig,
									 resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod(
				"configRemote",
				[selectedNode.getPathForNode(true),remoteConfig],
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function getRemoteConfigData(selectedNode:TreeNode,
											resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod(
				"getRemoteConfigData",
				[selectedNode.getPathForNode(true)],
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function deleteRemote(selectedNode:TreeNode):void {
			invokeServiceMethod(
				"deleteRemote",
				[selectedNode.getPathForNode(true)]);
		}
		
		public function getFetchPushConfigData(selectedNode:TreeNode, fetchData:Boolean,
										   resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod(
				"getFetchPushConfigData",
				[selectedNode.getPathForNode(true), fetchData],
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function fetch(selectedNode:TreeNode, remoteConfig:RemoteConfig, tagOpt:int, saveConfig:Boolean,
							  resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod(
				"fetch",
				[selectedNode.getPathForNode(true), remoteConfig, tagOpt, saveConfig],
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function push(selectedNode:TreeNode, remoteConfig:RemoteConfig, saveConfig:Boolean,
							  resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod(
				"push",
				[selectedNode.getPathForNode(true), remoteConfig, saveConfig],
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function checkout(selectedNode:TreeNode, name:String, upstreamBranch:GitRef, remote:RemoteConfig, rebase:Boolean,
								 resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod(
				"checkout", 
				[selectedNode.getPathForNode(true), name, upstreamBranch, remote, rebase],
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function getProjects(selection:ArrayList,
								 resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod(
				"getProjects", 
				[convertSelection(selection)],
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function importExistingProjects(selection:ArrayCollection,
									resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod(
				"importExistingProjects", 
				[selection],
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function importAsProjects(selection:ArrayCollection,
											   resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod(
				"importAsProjects", 
				[selection],
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function getCommitData(selection:ArrayList,
							   resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod(
				"getCommitData", 
				[convertSelection(selection)],
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function commit(repositoryLocation:String, files:ArrayCollection, author:String, committer:String, message:String, amending:Boolean,
										 resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod(
				"commit", 
				[repositoryLocation, files, author, committer, message, amending],
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function getAllRemotes(selectedNode:TreeNode,
										 resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {			
			invokeServiceMethod(
				"getAllRemotes", 
				[selectedNode.getPathForNode(true)], 
				resultCallbackObject, resultCallbackFunction);	
		}
		
		public function getRemoteBranches(selectedNode:TreeNode, remote:RemoteConfig,
									  resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {			
			invokeServiceMethod(
				"getRemoteBranches", 
				[selectedNode.getPathForNode(true), remote], 
				resultCallbackObject, resultCallbackFunction);	
		}
		
		public function pull(selectedNode:TreeNode,
								 resultCallbackObject:Object = null, resultCallbackFunction:Function = null):void {
			invokeServiceMethod(
				"pull", 
				[selectedNode.getPathForNode(true)],
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function openCredentials(selectedNode:TreeNode):void {
			invokeServiceMethod("openCredentials", [selectedNode.getPathForNode(true)]);
		}
		
		public function clearCredentials(selectedNode:TreeNode):void {
			invokeServiceMethod("clearCredentials", [selectedNode.getPathForNode(true)]);
		}
	}
}