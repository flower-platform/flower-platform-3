package org.flowerplatform.web.git.explorer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.git.GitNodeType;

/**
 * Parent node = Virtual node (Remotes) (i.e. Pair<Repository, nodeType>).<br/>
 * Child node = remote, i.e. Pair<remoteName, nodeType>.
 * 
 * @author Cristina Constantinescu
 */
public class Remote_VirtualItemChildrenProvider implements IChildrenProvider {
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		@SuppressWarnings("unchecked")
		Repository repository = ((Pair<Repository, String>) node).a;		
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>();
		
		Set<String> configNames = repository.getConfig().getSubsections(ConfigConstants.CONFIG_KEY_REMOTE);

		Pair<Object, String> child;	
		Pair<String, String> realChild;
		for (String configName : configNames) {
			realChild = new Pair<String, String>(configName, GitNodeType.NODE_TYPE_REMOTE);
			child = new Pair<Object, String>(realChild, GitNodeType.NODE_TYPE_REMOTE);
			result.add(child);
		}
		return result;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		@SuppressWarnings("unchecked")
		Repository repository = ((Pair<Repository, String>) node).a;
		return repository.getConfig().getSubsections(ConfigConstants.CONFIG_KEY_REMOTE).size() > 0;
	}

}
