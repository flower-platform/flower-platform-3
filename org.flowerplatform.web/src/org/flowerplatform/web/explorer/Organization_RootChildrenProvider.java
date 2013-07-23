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
import java.util.Collection;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.TreeNode;

public class Organization_RootChildrenProvider extends AbstractFileWrapperChildrenProvider implements IChildrenProvider {

	public static final String NODE_TYPE_ORGANIZATION = "organization";

	@Override
	protected File getFile(Object node) {
		return CommonPlugin.getInstance().getWorkspaceRoot();
	}

	@Override
	protected String getNodeType() {
		return NODE_TYPE_ORGANIZATION;
	}

	@Override
	protected Object getChildToProvide(File realChild) {
		return realChild;
	}

	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		if (node != GenericTreeStatefulService.ROOT_NODE_MARKER) {
			throw new IllegalArgumentException("Trying to get children of root, but the argument is not root: " + node);
		}

		return super.getChildrenForNode(node, treeNode, context);
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		return true;
	}

}