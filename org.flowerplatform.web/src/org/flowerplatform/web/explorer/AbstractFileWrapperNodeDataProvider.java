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
import java.io.IOException;
import java.util.List;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IGenericTreeStatefulServiceAware;
import org.flowerplatform.communication.tree.INodeByPathRetriever;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.explorer.remote.ExplorerTreeStatefulService;

/**
 * This class can be used as is, without overriding anything. This class doesn't need
 * an explicit node type, because the node type is passed from the children provider.
 * 
 * <p>
 * I kept the name prefixed with Abstract, to have it together with the other Abstract*.
 * We may say that it's "abstract" from the real point of view (= general), not java point of
 * view (i.e. cannot be instantiated...).
 * 
 * @author Cristian Spiescu
 */
public class AbstractFileWrapperNodeDataProvider implements INodeDataProvider, INodeByPathRetriever {

	public static final String NODE_TYPE_CATEGORY_PATH_FRAGMENT_NAME_POINTS_TO_FILE = "pathFragmentNamePointsToFile";
	
	protected boolean shouldIncludePathFragmentInFilePath(PathFragment pathFragment) {
		return WebPlugin.getInstance().getNodeTypeCategoryToNodeTypesMap().get(NODE_TYPE_CATEGORY_PATH_FRAGMENT_NAME_POINTS_TO_FILE).contains(pathFragment.getType());
	}
	
	protected Object getChildToProvide(File realChild, String nodeType) {
		return new Pair<File, String>(realChild, nodeType);
	}
	
	protected File getFile(Object node) {
		if (node instanceof Pair) {
			@SuppressWarnings("unchecked")
			Pair<File, String> realNode = (Pair<File, String>) node;
			return realNode.a;
		} else {
			throw new IllegalArgumentException(String.format("Don't know how to get the File from class = %s, instance = %s. SUPER should have overridden and handled this type", node.getClass(), node));
		}
	}
	
	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		File file = getFile(source);
		destination.setLabel(file.getName());
		if (file.isDirectory()) {
			destination.setIcon(WebPlugin.getInstance().getResourceUrl("images/folder.gif"));
		} else {
			destination.setIcon(WebPlugin.getInstance().getResourceUrl("images/file.gif"));
		}
		return true;
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		File file = getFile(node);
		return new PathFragment(file.getName(), nodeType);
	}

	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {
		StringBuffer sb;
		try {
			sb = new StringBuffer(CommonPlugin.getInstance().getWorkspaceRoot().getCanonicalPath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		for (PathFragment pf : fullPath) {
			if (shouldIncludePathFragmentInFilePath(pf)) {
				sb.append(File.separatorChar);
				sb.append(pf.getName());
			} 
		}
		return getChildToProvide(new File(sb.toString()), fullPath.get(fullPath.size() - 1).getType());
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {
		return getFile(node).getName();
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