package org.flowerplatform.web.git.explorer;

import java.util.Arrays;

import org.flowerplatform.web.explorer.AbstractVirtualItemChildrenProvider;
import org.flowerplatform.web.git.GitNodeType;

/**
 * Parent node = Repository (i.e. Pair<Repository, nodeType>).<br/>
 * Child node = virtual items, i.e. Pair<Repository, nodeType>.
 * 
 * @author Cristina Constantinescu
 */
public class VirtualItem_RepositoryChildrenProvider extends AbstractVirtualItemChildrenProvider {
	
	public VirtualItem_RepositoryChildrenProvider() {
		super();
		childNodeTypes = Arrays.asList(new String[] {
				GitNodeType.NODE_TYPE_LOCAL_BRANCHES,
				GitNodeType.NODE_TYPE_REMOTE_BRANCHES,
				GitNodeType.NODE_TYPE_TAGS,
				GitNodeType.NODE_TYPE_REMOTES,
				GitNodeType.NODE_TYPE_WDIRS});
	}
	
}
