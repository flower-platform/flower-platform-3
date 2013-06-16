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

	public static final String GIT_REPOSITORIES_NAME = ".git-repositories";
	
	public static File getGitRepositoriesFile() {
		return new File(RootChildrenProvider.getWorkspaceRoot(), "org1/" + GIT_REPOSITORIES_NAME + "/");
	}
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		File[] children = getGitRepositoriesFile().listFiles();
		
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
		File file = GitRootChildrenProvider.getGitRepositoriesFile();
		return file.isDirectory() && file.list().length > 0;
	}

}
