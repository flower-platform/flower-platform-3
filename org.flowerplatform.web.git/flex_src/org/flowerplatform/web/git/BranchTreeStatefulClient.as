package org.flowerplatform.web.git {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class BranchTreeStatefulClient extends GenericTreeStatefulClient {
		
		public static const NODE_TYPE_FILTER_KEY:String = "nodeTypeFilter";
		
		public function BranchTreeStatefulClient() {
			super();
			
			clientIdPrefix = "Explorer";
			requestDataOnSubscribe = false;
			requestDataOnServer = false;
			statefulServiceId = "explorerTreeStatefulService";
			
			context[NODE_TYPE_FILTER_KEY] = 
				new ArrayCollection([GitNodeType.NODE_TYPE_LOCAL_BRANCHES, GitNodeType.NODE_TYPE_REMOTE_BRANCHES, GitNodeType.NODE_TYPE_TAGS]);
			context[GenericTreeStatefulClient.DONT_UPDATE_MAP_KEY] = true;
			context[GenericTreeStatefulClient.WHOLE_TREE_KEY] = true;
		}		
		
		[RemoteInvocation]
		override public function updateNode(path:ArrayCollection, newNode:TreeNode, expandNode:Boolean = false, collapseNode:Boolean = false, selectNode:Boolean = false):void {
			treeList.rootNode = newNode;
		}
		
	}	
}