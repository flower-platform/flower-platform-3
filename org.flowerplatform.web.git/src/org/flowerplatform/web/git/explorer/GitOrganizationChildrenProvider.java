package org.flowerplatform.web.git.explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.git.entity.GitNodeType;

/**
 * @author Cristina Constantienscu
 */
public class GitOrganizationChildrenProvider implements IChildrenProvider {

	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {	
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>(4);
		
		Pair<File, String> realChild;
		Pair<Object, String> child;
		
		realChild = new Pair<File, String>((File) node, GitNodeType.NODE_TYPE_GIT_REPOSITORIES);
		child = new Pair<Object, String>(realChild, GitNodeType.NODE_TYPE_GIT_REPOSITORIES);
		result.add(child);
		
		return result;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		return true;
	}

}
