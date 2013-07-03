package org.flowerplatform.web.git.common.remote {
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.web.git.common.remote.dto.GitRef;
	import org.flowerplatform.web.git.common.remote.dto.RemoteConfig;

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
		
		public function fetch(selectedNode:TreeNode, remoteConfig:RemoteConfig,
							  resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod(
				"fetch",
				[selectedNode.getPathForNode(true), remoteConfig],
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function push(selectedNode:TreeNode, remoteConfig:RemoteConfig,
							  resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod(
				"push",
				[selectedNode.getPathForNode(true), remoteConfig],
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function checkout(selectedNode:TreeNode, name:String, upstreamBranch:GitRef, remote:RemoteConfig, rebase:Boolean,
								 resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod(
				"checkout", 
				[selectedNode.getPathForNode(true), name, upstreamBranch, remote, rebase],
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function getProjects(selectedNode:TreeNode,
								 resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod(
				"getProjects", 
				[selectedNode.getPathForNode(true)],
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function importProjects(selection:ArrayCollection,
									resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod(
				"importProjects", 
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
		
		public function pushToUpstream(repositoryLocation:String):void {
			invokeServiceMethod(
				"pushToUpstream", 
				[repositoryLocation]);
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
		
		public function deleteBranch(selectedNode:TreeNode,
									resultCallbackObject:Object, resultCallbackFunction:Function):void {
			invokeServiceMethod(
				"deleteBranch", 
				[selectedNode.getPathForNode(true)],
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function configBranch(selectedNode:TreeNode, upstreamBranch:GitRef, remote:RemoteConfig, rebase:Boolean, 
									 resultCallbackObject:Object = null, resultCallbackFunction:Function = null):void {
			invokeServiceMethod(
				"configBranch", 
				[selectedNode.getPathForNode(true), upstreamBranch, remote, rebase],
				resultCallbackObject, resultCallbackFunction);
		}
		
		public function getConfigBranchData(selectedNode:TreeNode,
									  resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {			
			invokeServiceMethod(
				"getConfigBranchData", 
				[selectedNode.getPathForNode(true)], 
				resultCallbackObject, resultCallbackFunction);	
		}
	}
}