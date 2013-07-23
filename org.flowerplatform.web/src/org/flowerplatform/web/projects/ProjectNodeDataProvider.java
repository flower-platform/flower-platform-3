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
package org.flowerplatform.web.projects;

import java.io.File;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.explorer.AbstractFileWrapperNodeDataProvider;
import org.flowerplatform.web.projects.remote.ProjectsService;

/**
 * Node = a project, i.e. Pair<project File, nodeType>.
 * 
 * @author Cristian Spiescu
 */
public class ProjectNodeDataProvider extends AbstractFileWrapperNodeDataProvider {

	protected String getPathFromWorkingDirectory(Object node) {
		File file = getFile(node);
		File wd = ProjectsService.getInstance().getProjectToWorkingDirectoryAndIProjectMap().get(file).a;
		return CommonPlugin.getInstance().getPathRelativeToFile(file, wd);
	}
	
	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		super.populateTreeNode(source, destination, context);
		destination.setLabel(getPathFromWorkingDirectory(source));
		destination.setIcon(WebPlugin.getInstance().getResourceUrl("images/project.gif"));
		return true;
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		return new PathFragment(getPathFromWorkingDirectory(node), nodeType);
	}

}