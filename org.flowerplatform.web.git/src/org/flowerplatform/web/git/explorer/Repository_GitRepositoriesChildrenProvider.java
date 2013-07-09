package org.flowerplatform.web.git.explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.git.GitNodeType;
import org.flowerplatform.web.git.GitPlugin;

/**
 * Parent node = Git Repositories virtual node (i.e. Pair<File, String>).<br/>
 * Child node = repository, i.e. Repository.
 * 
 * @author Cristina Constantinescu
 */
public class Repository_GitRepositoriesChildrenProvider implements IChildrenProvider {
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {		
		@SuppressWarnings("unchecked")
		File parentFile = (File) ((Pair<Object, String>) node).a;
		File[] children = GitPlugin.getInstance().getUtils().getGitRepositoriesFile(parentFile).listFiles();
		
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>(children.length);
		for (File child : children) {		
			Repository repo = GitPlugin.getInstance().getUtils().getMainRepository(child);
			if (repo != null) {		
				Pair<Object, String> pair = new Pair<Object, String>(repo, GitNodeType.NODE_TYPE_REPOSITORY);
				result.add(pair);
			}
		}		
		return result;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode,  GenericTreeContext context) {
		@SuppressWarnings("unchecked")
		File file = (File) ((Pair<Object, String>) node).a;	
		File gitRepos = GitPlugin.getInstance().getUtils().getGitRepositoriesFile(file);
		return gitRepos.exists() && gitRepos.isDirectory() && gitRepos.list().length > 0;
	}

}
