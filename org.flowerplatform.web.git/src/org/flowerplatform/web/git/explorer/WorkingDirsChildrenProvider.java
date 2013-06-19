package org.flowerplatform.web.git.explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.entity.GitNodeType;
import org.flowerplatform.web.git.entity.RepositoryNode;
import org.flowerplatform.web.git.entity.SimpleNode;
import org.flowerplatform.web.git.entity.WorkingDirNode;

/**
 * @author Cristina Constantienscu
 */
public class WorkingDirsChildrenProvider implements IChildrenProvider {
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {	
		SimpleNode simpleNode = (SimpleNode) node;
		RepositoryNode repoNode = (RepositoryNode) simpleNode.getParent();
		File repoFile = repoNode.getObject();
		
		File[] children = repoFile.listFiles();
		
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>(children.length);
		for (File child : children) {		
			Repository repo = GitPlugin.getInstance().getUtils().getRepository(child);
			
			if (repo != null) {
				WorkingDirNode childNode = new WorkingDirNode(node, repo, child);
				
				Pair<Object, String> pair = new Pair<Object, String>(childNode, GitNodeType.NODE_TYPE_WDIR);
				result.add(pair);
			}
		}		
		return result;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		return true;
	}

}
