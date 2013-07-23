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
package org.flowerplatform.web.projects.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.INodeByPathRetriever;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.AbstractTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.entity.WorkingDirectory;
import org.flowerplatform.web.explorer.Organization_RootChildrenProvider;
import org.flowerplatform.web.projects.WorkingDirectory_WorkingDirectoriesChildrenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shows content of a given working directory.
 * 
 * <p>
 * The working directory info is provided by client using
 * {@link #WORKING_DIRECTORY_KEY} and {@link #ORGANIZATION_KEY}.
 * 
 * @author Cristina Constantinescu
 */
public class WorkingDirectoryTreeStatefulService extends AbstractTreeStatefulService {

	private static String WORKING_DIRECTORY_KEY = "workingDirectory";
	private static String ORGANIZATION_KEY = "organization";
	
	private static Logger logger = LoggerFactory.getLogger(WorkingDirectoryTreeStatefulService.class);
	
	private String statefulClientPrefixId;
	
	private Map<String, List<IChildrenProvider>> childrenProviders = new HashMap<String, List<IChildrenProvider>>();
	
	private Map<String, INodeDataProvider> nodeDataProviders = new HashMap<String, INodeDataProvider>();
	
	public WorkingDirectoryTreeStatefulService() throws CoreException {
		setStatefulClientPrefixId("Working Directory");
		
		{
			// workingDirChildrenProvider
			IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.web.workingDirChildrenProvider");
			for (IConfigurationElement configurationElement : configurationElements) {
				IChildrenProvider provider = (IChildrenProvider) configurationElement.createExecutableExtension("provider");
				for (IConfigurationElement nodeTypeConfigurationElement : configurationElement.getChildren()) {
					String nodeType = nodeTypeConfigurationElement.getAttribute("nodeType");
					
					List<IChildrenProvider> list = getChildrenProviders().get(nodeType);
					if (list == null) {
						list = new ArrayList<IChildrenProvider>(configurationElement.getChildren().length);
						getChildrenProviders().put(nodeType, list);
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
			// workingDirNodeDataProvider
			IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.web.workingDirNodeDataProvider");
			for (IConfigurationElement configurationElement : configurationElements) {
				INodeDataProvider provider = (INodeDataProvider) configurationElement.createExecutableExtension("provider");
				for (IConfigurationElement nodeTypeConfigurationElement : configurationElement.getChildren()) {
					String nodeType = nodeTypeConfigurationElement.getAttribute("nodeType");
					if (getNodeDataProviders().get(nodeType) != null) {
						logger.error("Trying to register an INodeDataProvider for nodeType = {}, but another one already exists: {}", nodeType, getNodeDataProviders().get(nodeType));
					} else {
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
		
	}
	
	protected String getNodeType(TreeNode treeNode) {
		if (treeNode.getPathFragment() == null) {
			// root node is a Working Directory
			return WorkingDirectory_WorkingDirectoriesChildrenProvider.NODE_TYPE_WORKING_DIRECTORY;
		} else {
			return treeNode.getPathFragment().getType();
		}
	}
	
	protected String getNodeType(PathFragment pathFragment) {
		if (pathFragment == null) {
			// root node is a Working Directory
			return WorkingDirectory_WorkingDirectoriesChildrenProvider.NODE_TYPE_WORKING_DIRECTORY;
		} else {
			return pathFragment.getType();
		}
	}
	@Override
	public String getStatefulClientPrefixId() {
		return statefulClientPrefixId;
	}

	public void setStatefulClientPrefixId(String statefulClientPrefixId) {
		this.statefulClientPrefixId = statefulClientPrefixId;
	}
	
	public Map<String, List<IChildrenProvider>> getChildrenProviders() {
		return childrenProviders;
	}

	public Map<String, INodeDataProvider> getNodeDataProviders() {
		return nodeDataProviders;
	}
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node,	TreeNode treeNode, GenericTreeContext context) {
		String nodeType = getNodeType(treeNode);
		List<IChildrenProvider> providers = childrenProviders.get(nodeType);
		
		if (providers == null) {
			logger.error("Tree delegate not found, for method getChildrenForNode(); node = {}, nodeType = {}", getLabelForLog(node, nodeType), nodeType);
			return Collections.emptyList();
		}
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>();
		for (IChildrenProvider provider : providers) {
			result.addAll(provider.getChildrenForNode(node, treeNode, context));
		}
		return result;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		String nodeType = getNodeType(treeNode);
		List<IChildrenProvider> providers = childrenProviders.get(nodeType);
		
		if (providers == null) {
			logger.error("Tree delegate not found, for method nodeHasChildren(); node = {}, nodeType = {}", getLabelForLog(node, nodeType), nodeType);
			return false;
		}
		for (IChildrenProvider provider : providers) {
			if (provider.nodeHasChildren(node, treeNode, context)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		String nodeType = getNodeType(destination);
		INodeDataProvider provider = nodeDataProviders.get(nodeType);
		if (provider == null) {
			logger.error("Tree delegate not found, for method populateTreeNode(); node = {}, nodeType = {}", getLabelForLog(source, nodeType), nodeType);
			return false;
		}
		return provider.populateTreeNode(source, destination, context);
	}
	
	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		INodeDataProvider provider = nodeDataProviders.get(nodeType);
		if (provider == null) {
			logger.error("Tree delegate not found, for method getPathFragmentForNode(); node = {}, nodeType = {}", getLabelForLog(node, nodeType), nodeType);
			return null;
		}
		return provider.getPathFragmentForNode(node, nodeType, context);
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {
		if (WorkingDirectory_WorkingDirectoriesChildrenProvider.NODE_TYPE_WORKING_DIRECTORY.equals(nodeType)) {
			return "root";
		}
		INodeDataProvider provider = nodeDataProviders.get(nodeType);
		if (provider == null) {
			logger.error("Tree delegate not found, for method getLabelForLog(); nodeType = {}", nodeType);
			return null;
		}
		return provider.getLabelForLog(node, nodeType);
	}
	
	/**
	 * The implementation of this method is different. If the provider returns something,
	 * we use it; otherwise we delegate to super, who will run the algorithm that will call
	 * {@link #getNodeByPathFragment()}.
	 */
	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {
		if (fullPath != null && !fullPath.isEmpty()) {
			String nodeType = fullPath.get(fullPath.size() - 1).getType();
			INodeDataProvider provider = nodeDataProviders.get(nodeType);
			if (provider instanceof INodeByPathRetriever) {
				Object result = ((INodeByPathRetriever) provider).getNodeByPath(fullPath, context);
				if (result != null) {
					return result;
				}
			}
		}		
		// root node, search for working directory based on valuew from context
		String organization = (String) context.get(ORGANIZATION_KEY);
		String workingDirectory = (String) context.get(WORKING_DIRECTORY_KEY);
		for (WorkingDirectory wd : ProjectsService.getInstance().getWorkingDirectoriesForOrganizationName(organization)) {
			if (wd.getPathFromOrganization().equals(workingDirectory)) {				
				return wd;
			}
		}
		return null;
	}

	public TreeNode openNodeInternal(CommunicationChannel channel, String statefulClientId, List<PathFragment> fullPath, Map<Object, Object> context) {
		String workingDirectory = (String) context.get(WORKING_DIRECTORY_KEY);
		String organization = (String) context.get(ORGANIZATION_KEY);
		if (fullPath != null) {
			// add organization & working directory fragments to path
			// needed to get data from providers
			fullPath.add(0, new PathFragment(workingDirectory, WorkingDirectory_WorkingDirectoriesChildrenProvider.NODE_TYPE_WORKING_DIRECTORY));
			fullPath.add(0, new PathFragment(organization, Organization_RootChildrenProvider.NODE_TYPE_ORGANIZATION));
		}
		TreeNode node = super.openNodeInternal(channel, statefulClientId, fullPath, context);
		
		if (fullPath != null) {
			// remove added fragments
			fullPath.remove(0);
			fullPath.remove(0);
		}
		return node;		
	}
	
	@Override
	public List<PathFragment> getPathForNode(Object node, String nodeType, GenericTreeContext context) {
		return null; // isn't used
	}

	@Override
	public void subscribe(StatefulServiceInvocationContext context,	IStatefulClientLocalState statefulClientLocalState) {		
	}

	@Override
	public void unsubscribe(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState) {		
	}

}