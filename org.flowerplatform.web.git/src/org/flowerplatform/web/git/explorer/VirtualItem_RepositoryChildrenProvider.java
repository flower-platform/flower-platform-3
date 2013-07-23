/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
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