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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.IGenericTreeStatefulServiceAware;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.INodePopulator;
import org.flowerplatform.communication.tree.remote.DelegatingGenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.file_event.FileEvent;
import org.flowerplatform.file_event.IFileEventListener;
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
		if(event.getEvent() == FileEvent.FILE_CREATED || event.getEvent() == FileEvent.FILE_DELETED) {
			File parent = event.getFile().getParentFile();
			
			// Update for ws_trunk subtree
			Object node = new Pair<File, String>(parent, FsFile_FileSystemChildrenProvider.NODE_TYPE_FS_FILE);
			dispatchContentUpdate(node);
			
			// Update for project subtree
			IResource resource = ProjectsService.getInstance().getProjectWrapperResourceFromFile(parent);
			// Update only if a project exists
			if(resource != null) {
				if(resource.getType() == IResource.FOLDER ) {
					node = new Pair<File, String>(parent, ProjFile_ProjectChildrenProvider.NODE_TYPE_PROJ_FILE);
					dispatchContentUpdate(node);	
				} else {
					node = new Pair<File, String>(parent, Project_WorkingDirectoryChildrenProvider.NODE_TYPE_PROJECT);
					dispatchContentUpdate(node);
				}
			} else if(event.getEvent() == FileEvent.FILE_DELETED) {
				String org = ProjectsService.getInstance().getOrganizationNameFromFile(parent);
				List<WorkingDirectory> workingDirectories = ProjectsService.getInstance().getWorkingDirectoriesForOrganizationName(org);
				for( WorkingDirectory workingDirectory : workingDirectories) {
					String name = workingDirectory.getOrganization().getName();
					if(name.equals(org)) {				
						node = new Pair<WorkingDirectory, String>(workingDirectory, WorkingDirectory_WorkingDirectoriesChildrenProvider.NODE_TYPE_WORKING_DIRECTORY);
						dispatchContentUpdate(workingDirectory);
					}
				}
			}
		}
	}
}