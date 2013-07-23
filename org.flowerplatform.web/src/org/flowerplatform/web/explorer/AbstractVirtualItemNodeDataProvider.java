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
package org.flowerplatform.web.explorer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;

/**
 * The subclasses should populate {@link #nodeInfo} in constructor.
 * 
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 */
public class AbstractVirtualItemNodeDataProvider implements INodeDataProvider {
	
	/**
	 * Key = node type <br>
	 * Value = [node label, node icon]
	 */
	protected Map<String, String[]> nodeInfo = new HashMap<String, String[]>();
	
	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		destination.setLabel(nodeInfo.get(destination.getPathFragment().getType())[0]);
		destination.setIcon(nodeInfo.get(destination.getPathFragment().getType())[1]);

		return true;
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		@SuppressWarnings("unchecked")
		String nodeType1 = ((Pair<File, String>) node).b;
		return new PathFragment(nodeType1, nodeType1);
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {
		return nodeInfo.get(nodeType)[0];
	}

	@Override
	public String getInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> fullPath) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> path, String text) {
		throw new UnsupportedOperationException();
	}


}