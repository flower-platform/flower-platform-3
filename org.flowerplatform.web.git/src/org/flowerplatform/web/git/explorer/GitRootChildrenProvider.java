package org.flowerplatform.web.git.explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.explorer.RootChildrenProvider;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.entity.GitNodeType;
import org.flowerplatform.web.git.entity.RepositoryNode;

/**
 * @author Cristina Constantienscu
 */
public class GitRootChildrenProvider implements IChildrenProvider {
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {		
		@SuppressWarnings("unchecked")
		File parentFile = (File) ((Pair<Object, String>) node).a;
		File[] children = GitPlugin.getInstance().getUtils().getGitRepositoriesFile(parentFile).listFiles();
		
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>(children.length);
		for (File child : children) {		
			Repository repo = GitPlugin.getInstance().getUtils().getMainRepository(child);
			if (repo != null) {
				RepositoryNode childNode = new RepositoryNode(node, repo, child);
				
				Pair<Object, String> pair = new Pair<Object, String>(childNode, GitNodeType.NODE_TYPE_REPOSITORY);
				result.add(pair);
			}
		}		
		return result;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode,  GenericTreeContext context) {
		@SuppressWarnings("unchecked")
		File file = (File) ((Pair<Object, String>) node).a;	
		return file.isDirectory() && file.list().length > 0;
	}

}
