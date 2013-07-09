package org.flowerplatform.editor.java.propertypage.remote {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	
	public class WorkingDirectoryTreeStatefulClient extends GenericTreeStatefulClient {
		
		public function WorkingDirectoryTreeStatefulClient() {
			super();
			
			clientIdPrefix = "Explorer";
			requestDataOnSubscribe = false;
			requestDataOnServer = false;
			statefulServiceId = "explorerTreeStatefulService";
			
			context[GenericTreeStatefulClient.DONT_UPDATE_MAP_KEY] = true;
			context[GenericTreeStatefulClient.WHOLE_TREE_KEY] = true;
		}		
		
		[RemoteInvocation]
		override public function updateNode(path:ArrayCollection, newNode:TreeNode, expandNode:Boolean = false, collapseNode:Boolean = false, selectNode:Boolean = false):void {
			treeList.rootNode = newNode;
		}
		
	}
}