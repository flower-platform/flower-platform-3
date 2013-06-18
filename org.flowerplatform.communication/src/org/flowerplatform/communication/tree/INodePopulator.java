package org.flowerplatform.communication.tree;

import org.flowerplatform.communication.tree.remote.TreeNode;

public interface INodePopulator {
	boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context);
}
