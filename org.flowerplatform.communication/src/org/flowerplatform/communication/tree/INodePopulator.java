package org.flowerplatform.communication.tree;

import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService2;
import org.flowerplatform.communication.tree.remote.TreeNode;

public interface INodePopulator {
	
	/**
	 * This method can safely use the {@link GenericTreeStatefulService2#getVisibleNodes()}. 
	 */
	boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context);
}
