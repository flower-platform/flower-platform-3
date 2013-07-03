package org.flowerplatform.web.git.explorer;

import java.io.File;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.explorer.AbstractVirtualItemInOrganizationNodeDataProvider;
import org.flowerplatform.web.git.GitNodeType;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.GitUtils;

/**
 * @author Cristina Constantienscu
 */
public class GitRepositoriesNodeDataProvider extends AbstractVirtualItemInOrganizationNodeDataProvider {
	
	public GitRepositoriesNodeDataProvider() {
		super();
		nodeInfo.put(GitNodeType.NODE_TYPE_GIT_REPOSITORIES, 
				new String[] {
				GitPlugin.getInstance().getMessage("git.repositories"), 
				GitPlugin.getInstance().getResourceUrl("images/eview16/repo_rep.gif")});	
	}
	
	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		@SuppressWarnings("unchecked")
		String nodeType1 = ((Pair<File, String>) node).b;
		return new PathFragment(GitUtils.GIT_REPOSITORIES_NAME, nodeType1);
	}
}
