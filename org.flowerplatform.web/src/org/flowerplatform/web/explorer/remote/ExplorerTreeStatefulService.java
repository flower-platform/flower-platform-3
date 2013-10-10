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
package org.flowerplatform.web.explorer.remote;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.file_event.FileEvent;
import org.flowerplatform.common.file_event.IFileEventListener;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.IGenericTreeStatefulServiceAware;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.INodePopulator;
import org.flowerplatform.communication.tree.NodeInfo;
import org.flowerplatform.communication.tree.remote.DelegatingGenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.entity.WorkingDirectory;
import org.flowerplatform.web.entity.impl.WorkingDirectoryImpl;
import org.flowerplatform.web.explorer.FsFile_FileSystemChildrenProvider;
import org.flowerplatform.web.projects.ProjFile_ProjectChildrenProvider;
import org.flowerplatform.web.projects.Project_WorkingDirectoryChildrenProvider;
import org.flowerplatform.web.projects.WorkingDirectories_OrganizationChildrenProvider;
import org.flowerplatform.web.projects.WorkingDirectory_WorkingDirectoriesChildrenProvider;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExplorerTreeStatefulService extends DelegatingGenericTreeStatefulService implements IFileEventListener {

	private static Logger logger = LoggerFactory.getLogger(ExplorerTreeStatefulService.class);

	public ExplorerTreeStatefulService() throws CoreException {
		setStatefulClientPrefixId("Explorer");
		CommonPlugin.getInstance().getFileEventDispatcher().addFileEventListener(this);
		{
			// explorerChildrenProvider
			IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.web.explorerChildrenProvider");
			for (IConfigurationElement configurationElement : configurationElements) {
				IChildrenProvider provider = (IChildrenProvider) configurationElement.createExecutableExtension("provider");
				for (IConfigurationElement nodeTypeConfigurationElement : configurationElement.getChildren()) {
					String nodeType = nodeTypeConfigurationElement.getAttribute("nodeType");
					
					List<IChildrenProvider> list = getChildrenProviders().get(nodeType);
					if (list == null) {
						list = new ArrayList<IChildrenProvider>(configurationElement.getChildren().length);
						getChildrenProviders().put(nodeType, list);
					}
					if (provider instanceof IGenericTreeStatefulServiceAware) {
						((IGenericTreeStatefulServiceAware) provider).setGenericTreeStatefulService(this);
					}
					list.add(provider);
				}
			}
			if (logger.isDebugEnabled()) {
				for (Map.Entry<String, List<IChildrenProvider>> entry : getChildrenProviders().entrySet()) {
					logger.debug("ExplorerTreeStatefulService: for nodeType = {}, these are the children providers = {}", entry.getKey(), entry.getValue());
				}
			}
		}
		
		{
			// explorerNodeDataProvider
			IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.web.explorerNodeDataProvider");
			for (IConfigurationElement configurationElement : configurationElements) {
				INodeDataProvider provider = (INodeDataProvider) configurationElement.createExecutableExtension("provider");
				for (IConfigurationElement nodeTypeConfigurationElement : configurationElement.getChildren()) {
					String nodeType = nodeTypeConfigurationElement.getAttribute("nodeType");
					if (getNodeDataProviders().get(nodeType) != null) {
						logger.error("Trying to register an INodeDataProvider for nodeType = {}, but another one already exists: {}", nodeType, getNodeDataProviders().get(nodeType));
					} else {
						if (provider instanceof IGenericTreeStatefulServiceAware) {
							((IGenericTreeStatefulServiceAware) provider).setGenericTreeStatefulService(this);
						}
						getNodeDataProviders().put(nodeType, provider);
					}
				}
			}
			if (logger.isDebugEnabled()) {
				for (Map.Entry<String, INodeDataProvider> entry : getNodeDataProviders().entrySet()) {
					logger.debug("ExplorerTreeStatefulService: for nodeType = {}, this is the node data provider = {}", entry.getKey(), entry.getValue());
				}
			}
		}			
		
		{
			// explorerAdditionalNodePopulator
			IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.web.explorerAdditionalNodePopulator");
			for (IConfigurationElement configurationElement : configurationElements) {
				INodePopulator populator = (INodePopulator) configurationElement.createExecutableExtension("populator");
				for (IConfigurationElement childConfigurationElement : configurationElement.getChildren()) {
					if ("nodeType".equals(childConfigurationElement.getName())) {
						// case #1: add only for a node type
						String nodeType = childConfigurationElement.getAttribute("nodeType");
						addAdditionalNodePopulator(nodeType, populator);
					} else if ("nodeTypeCategory".equals(childConfigurationElement.getName())) {
						// case #2: add for a node category type, i.e. for all its node associated node types
						String nodeTypeCategory = childConfigurationElement.getAttribute("nodeTypeCategory");
						List<String> nodeTypes = WebPlugin.getInstance().getNodeTypeCategoryToNodeTypesMap().get(nodeTypeCategory);
						if (nodeTypes == null) {
							throw new RuntimeException("There are no node types for node type category = " + nodeTypeCategory);
						}
						for (String nodeType : nodeTypes) {
							addAdditionalNodePopulator(nodeType, populator);
						}
					}
				}
			}
			if (logger.isDebugEnabled()) {
				for (Map.Entry<String, List<INodePopulator>> entry : getAdditionalNodePopulators().entrySet()) {
					logger.debug("ExplorerTreeStatefulService: for nodeType = {}, these are the additional node populators = {}", entry.getKey(), entry.getValue());
				}
			}
		}
	}
	
	private void addAdditionalNodePopulator(String nodeType, INodePopulator populator) {
		List<INodePopulator> populators = getAdditionalNodePopulators().get(nodeType);
		if (populators == null) {
			populators = new ArrayList<INodePopulator>();
			getAdditionalNodePopulators().put(nodeType, populators);
		}
		if (populator instanceof IGenericTreeStatefulServiceAware) {
			((IGenericTreeStatefulServiceAware) populator).setGenericTreeStatefulService(this);
		}
		populators.add(populator);
	}
	
	/**
	 * @author Tache Razvan Mihai
	 */
	@Override
	public void notify(FileEvent event) {
		if(event.getEvent() == FileEvent.FILE_CREATED || event.getEvent() == FileEvent.FILE_DELETED ||
				event.getEvent() == FileEvent.FILE_RENAMED) {
			File file = event.getFile();
			File parent = file.getParentFile();
			
			// Update for file system subtree
			Object node = new Pair<File, String>(parent, FsFile_FileSystemChildrenProvider.NODE_TYPE_FS_FILE);
			dispatchContentUpdate(node);
			
			// update for Special nodes
			Map<File, Pair<File, IProject>> projectToWorkingDirectoryAndIProjectMap = ProjectsService.getInstance().getProjectToWorkingDirectoryAndIProjectMap();
			Map<File, List<File>> workingDirectoryToProjectsMap = ProjectsService.getInstance().getWorkingDirectoryToProjectsMap();
			if(workingDirectoryToProjectsMap.containsKey(file)) {
				// it's an working directory -> parent is workingDirectories
			} else if(workingDirectoryToProjectsMap.containsKey(parent)) {
				// it's an project -> parent is working directory
				String organizationName = ProjectsService.getInstance().getOrganizationNameFromFile(parent);
				String pathFromOrganization = ProjectsService.getInstance().getRelativePathFromOrganization(parent);
				WorkingDirectory workingDirectory = ProjectsService.getInstance().getWorkingDirectory(organizationName, pathFromOrganization.substring(0, pathFromOrganization.length() - 1));
				dispatchContentUpdate(workingDirectory);
			} else if(projectToWorkingDirectoryAndIProjectMap.containsKey(parent)) {
				// it's a projFile with project parent
				node = new Pair<File, String>(parent, Project_WorkingDirectoryChildrenProvider.NODE_TYPE_PROJECT);
				dispatchContentUpdate(node);			
			} else {
				// it may be an indirect projFile -> parent is projFile 
				for(File project : projectToWorkingDirectoryAndIProjectMap.keySet()) {
					if( parent.getPath().contains(project.getPath())) {
						node = new Pair<File, String>(parent, ProjFile_ProjectChildrenProvider.NODE_TYPE_PROJ_FILE);
						dispatchContentUpdate(node);
					}
				}
			}
		} else if(event.getEvent() == FileEvent.FILE_REFRESHED) {
			// refresh the tree recursively
			File file = event.getFile();
			Object node = findNode(file);
			refreshTree(node);
		}
	}
	
	private Object findNode(File file) {
		File parent = file.getParentFile();
		Object node = null;
		
		Map<File, Pair<File, IProject>> projectToWorkingDirectoryAndIProjectMap = ProjectsService.getInstance().getProjectToWorkingDirectoryAndIProjectMap();
		Map<File, List<File>> workingDirectoryToProjectsMap = ProjectsService.getInstance().getWorkingDirectoryToProjectsMap();
		if(workingDirectoryToProjectsMap.containsKey(file)) {
			// it's an working directory -> parent is workingDirectories
			String organizationName = ProjectsService.getInstance().getOrganizationNameFromFile(file);
			String pathFromOrganization = ProjectsService.getInstance().getRelativePathFromOrganization(file);
			WorkingDirectory workingDirectory = ProjectsService.getInstance().getWorkingDirectory(organizationName, pathFromOrganization.substring(0, pathFromOrganization.length() - 1));
			node = workingDirectory;
		} else if(workingDirectoryToProjectsMap.containsKey(parent) && projectToWorkingDirectoryAndIProjectMap.containsKey(file)) {
			// it's an project -> parent is working directory
			node = new Pair<File, String>(file, Project_WorkingDirectoryChildrenProvider.NODE_TYPE_PROJECT);
		} else if(projectToWorkingDirectoryAndIProjectMap.containsKey(parent)) {
			node = new Pair<File, String>(file, ProjFile_ProjectChildrenProvider.NODE_TYPE_PROJ_FILE);
		} else {
			// it may be an indirect projFile -> parent is projFile 
			for(File project : projectToWorkingDirectoryAndIProjectMap.keySet()) {
				if( file.getPath().contains(project.getPath())) {
					node = new Pair<File, String>(file, ProjFile_ProjectChildrenProvider.NODE_TYPE_PROJ_FILE);
				}
			}
		}
		
		if(node == null) {
			node = new Pair<File, String>(file, FsFile_FileSystemChildrenProvider.NODE_TYPE_FS_FILE);
		}
		
		return node;
	}
	
	public void refreshTree(Object node) {
		dispatchContentUpdate(node);
		NodeInfo nodeInfo = visibleNodes.get(node);
		if (nodeInfo == null) { // not opened, return
			return;
		}
		for(NodeInfo nodeInfoIter : nodeInfo.getChildren()) {
			refreshTree(nodeInfoIter.getNode());
		}
	}
}