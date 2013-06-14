package org.flowerplatform.web.git.explorer;

import java.util.Collection;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;

public class GitRepositoryChildrenProvider implements IChildrenProvider {
	
	public static final String NODE_TYPE_LOCAL_BRANCHES = "lbr";
	public static final String NODE_TYPE_REMOTE_BRANCHES = "rbr";
	public static final String NODE_TYPE_TAGS = "tgs";
	public static final String NODE_TYPE_REMOTES = "rmts";
	public static final String NODE_TYPE_WDIRS = "wdirs";
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {	
		return null;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		return false;
	}

}
