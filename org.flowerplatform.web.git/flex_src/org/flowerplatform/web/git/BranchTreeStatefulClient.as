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