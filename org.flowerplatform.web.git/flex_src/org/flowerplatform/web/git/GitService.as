package org.flowerplatform.web.git {
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.TreeNode;
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
		
		public function cloneRepository(selectedNode:TreeNode, repositoryURL:String, branches:ArrayCollection, initialBranch:String, 
										remoteName:String, cloneAllBranches:Boolean, 
										resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			invokeServiceMethod(
				"cloneRepository", 
				[selectedNode.getPathForNode(true), repositoryURL, branches, initialBranch, remoteName, cloneAllBranches], 
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
		
		public function openCredentials(selectedNode:TreeNode):void {
			invokeServiceMethod("openCredentials", [selectedNode.getPathForNode(true)]);
		}
		
		public function clearCredentials(selectedNode:TreeNode):void {
			invokeServiceMethod("clearCredentials", [selectedNode.getPathForNode(true)]);
		}
	}
}