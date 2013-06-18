package org.flowerplatform.web.git.explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.explorer.RootChildrenProvider;
import org.flowerplatform.web.git.entity.GitNodeType;
import org.flowerplatform.web.git.entity.RemoteNode;
import org.flowerplatform.web.git.entity.SimpleNode;

/**
 * @author Cristina Constantienscu
 */
public class RemotesChildrenProvider implements IChildrenProvider {

	public static final String GIT_REPOSITORIES_NAME = ".git-repositories";
	
	public static final String MAIN_WORKING_DIRECTORY_NAME = "main-working-directory";
	public static final String VIRTUAL_WORKING_DIRECTORIES_NAME = "virtual-working-directories";
		
	public File getOrganizationFile() {
		return new File(RootChildrenProvider.getWorkspaceRoot(), "org1/.gitRepositories/");
	}
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>();
		
		SimpleNode simpleNode = (SimpleNode) node;		
		Repository repo = simpleNode.getRepository();
		
		Set<String> configNames = repo.getConfig().getSubsections(ConfigConstants.CONFIG_KEY_REMOTE);

		Pair<Object, String> child;		
		RemoteNode childNode;	
		for (String configName : configNames) {
			childNode = new RemoteNode(simpleNode, repo, configName);
			child = new Pair<Object, String>(childNode, GitNodeType.NODE_TYPE_REMOTE);
			result.add(child);
		}
		return result;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		SimpleNode simpleNode = (SimpleNode) node;		
		Repository repo = simpleNode.getRepository();
		
		return repo.getConfig().getSubsections(ConfigConstants.CONFIG_KEY_REMOTE).size() > 0;
	}

}
