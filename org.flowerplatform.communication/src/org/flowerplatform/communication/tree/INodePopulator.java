package org.flowerplatform.communication.tree;

import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.TreeNode;

public interface INodePopulator {
	
	/**
	 * This method can safely use the {@link GenericTreeStatefulService#getVisibleNodes()}. 
	 */
	boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context);
}
