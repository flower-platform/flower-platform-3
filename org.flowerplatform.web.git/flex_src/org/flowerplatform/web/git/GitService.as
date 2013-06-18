package org.flowerplatform.web.git {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.TreeNode;

	/**
	 * @author Cristina Constantinescu
	 */
	public class GitService {
		
		public static const SERVICE_ID:String = "gitService";
		
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
	}
}