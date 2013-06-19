package org.flowerplatform.web.git.explorer;

import java.util.ArrayList;
import java.util.Collection;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.git.entity.GitNodeType;
import org.flowerplatform.web.git.entity.RepositoryNode;
import org.flowerplatform.web.git.entity.SimpleNode;

/**
 * @author Cristina Constantienscu
 */
public class RepositoryChildrenProvider implements IChildrenProvider {
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {	
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>(5);

		RepositoryNode repoNode = (RepositoryNode) node;
		Pair<Object, String> child;		
		SimpleNode childNode;
		
		childNode = new SimpleNode(repoNode, repoNode.getRepository(), GitNodeType.NODE_TYPE_LOCAL_BRANCHES);		
		child = new Pair<Object, String>(childNode, GitNodeType.NODE_TYPE_LOCAL_BRANCHES);
		result.add(child);
		
		childNode = new SimpleNode(repoNode, repoNode.getRepository(), GitNodeType.NODE_TYPE_REMOTE_BRANCHES);
		child = new Pair<Object, String>(childNode, GitNodeType.NODE_TYPE_REMOTE_BRANCHES);
		result.add(child);
		
		childNode = new SimpleNode(repoNode, repoNode.getRepository(), GitNodeType.NODE_TYPE_TAGS);
		child = new Pair<Object, String>(childNode, GitNodeType.NODE_TYPE_TAGS);
		result.add(child);
		
		childNode = new SimpleNode(repoNode, repoNode.getRepository(), GitNodeType.NODE_TYPE_REMOTES);
		child = new Pair<Object, String>(childNode, GitNodeType.NODE_TYPE_REMOTES);
		result.add(child);
	
		childNode = new SimpleNode(repoNode, repoNode.getRepository(), GitNodeType.NODE_TYPE_WDIRS);
		child = new Pair<Object, String>(childNode, GitNodeType.NODE_TYPE_WDIRS);
		result.add(child);
		
		return result;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		return true;
	}

}
