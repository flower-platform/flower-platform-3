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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.entity.WorkingDirectory;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parent node = {@link WorkingDirectory}.<br/>
 * Child node = a project, i.e. Pair<project File, nodeType>.
 * 
 * @author Cristian Spiescu
 */
public class Project_WorkingDirectoryChildrenProvider implements IChildrenProvider {

	private static final Logger logger = LoggerFactory.getLogger(Project_WorkingDirectoryChildrenProvider.class);
	
	public static final String NODE_TYPE_PROJECT = "project";
	
	protected File getFile(Object node) {
		WorkingDirectory wd = (WorkingDirectory) node;
		File organizationDir = ProjectsService.getInstance().getOrganizationDir(wd.getOrganization().getName());
		File wdFile = new File(organizationDir, wd.getPathFromOrganization());
		return wdFile;
	}

	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		File file = getFile(node);
		List<File> projects = ProjectsService.getInstance().getWorkingDirectoryToProjectsMap().get(file);
		if (projects == null) {
			logger.error("Could not find in map the workingDir = {}", file);
			return Collections.emptyList();
		}
		
		List<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>();
		for (File project : projects) {
			result.add(new Pair<Object, String>(new Pair<Object, String>(project, NODE_TYPE_PROJECT), NODE_TYPE_PROJECT));
		}
		return result;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		File file = getFile(node);
		List<File> projects = ProjectsService.getInstance().getWorkingDirectoryToProjectsMap().get(file);
		if (projects == null) {
			logger.error("Could not find in map the workingDir = {}", file);
			return false;
		}
		return projects.size() > 0;
	}

}