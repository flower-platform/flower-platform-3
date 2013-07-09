package org.flowerplatform.editor.java.propertypage.remote {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	
	public class WorkingDirectoryTreeStatefulClient extends GenericTreeStatefulClient {
		
		public function WorkingDirectoryTreeStatefulClient() {
			super();
			
			clientIdPrefix = "Working Directory";
			requestDataOnSubscribe = false;
			requestDataOnServer = false;
			statefulServiceId = "workingDirectoryTreeStatefulService";
		}		
		
		
//		[RemoteInvocation]
//		override public function updateNode(path:ArrayCollection, newNode:TreeNode, expandNode:Boolean = false, collapseNode:Boolean = false, selectNode:Boolean = false):void {
//			treeList.rootNode = newNode;
//		}
		
	}
}