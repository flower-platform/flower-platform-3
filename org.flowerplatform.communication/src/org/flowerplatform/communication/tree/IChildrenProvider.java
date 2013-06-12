package org.flowerplatform.communication.tree;

import java.util.Collection;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.remote.TreeNode;

public interface IChildrenProvider {
	Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context);
	
	Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context);
}
