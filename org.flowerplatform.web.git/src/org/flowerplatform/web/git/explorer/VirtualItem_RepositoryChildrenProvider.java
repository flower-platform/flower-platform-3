package org.flowerplatform.web.git.explorer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.explorer.AbstractVirtualItemChildrenProvider;
import org.flowerplatform.web.git.GitNodeType;

/**
 * Parent node = Repository (i.e. Pair<Repository, nodeType>).<br/>
 * Child node = virtual items, i.e. Pair<Repository, nodeType>.
 * 
 * @author Cristina Constantinescu
 */
public class VirtualItem_RepositoryChildrenProvider extends AbstractVirtualItemChildrenProvider {
	
	private static final String NODE_TYPE_FILTER_KEY = "nodeTypeFilter";
	
	public VirtualItem_RepositoryChildrenProvider() {
		super();
		childNodeTypes = Arrays.asList(new String[] {
				GitNodeType.NODE_TYPE_LOCAL_BRANCHES,
				GitNodeType.NODE_TYPE_REMOTE_BRANCHES,
				GitNodeType.NODE_TYPE_TAGS,
				GitNodeType.NODE_TYPE_REMOTES,
				GitNodeType.NODE_TYPE_WDIRS});
	}
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		@SuppressWarnings("unchecked")
		List<String> filter = (List<String>) context.get(NODE_TYPE_FILTER_KEY);
		List<String> oldChildNodeTypes = childNodeTypes;
		if (filter != null) {
			childNodeTypes = filter;
		}		
		Collection<Pair<Object, String>> result = super.getChildrenForNode(node, treeNode, context);
		childNodeTypes = oldChildNodeTypes;
		
		return result;
	}
}
