package org.flowerplatform.web.projects.remote;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkingDirectoryTreeStatefulService extends AbstractTreeStatefulService {

	private static Logger logger = LoggerFactory.getLogger(WorkingDirectoryTreeStatefulService.class);
	
	private String statefulClientPrefixId;
	
	private Map<String, List<IChildrenProvider>> childrenProviders = new HashMap<String, List<IChildrenProvider>>();
	
	private Map<String, INodeDataProvider> nodeDataProviders = new HashMap<String, INodeDataProvider>();
	
	public WorkingDirectoryTreeStatefulService() throws CoreException {
		setStatefulClientPrefixId("Working Directory");
		
		{
			// explorerChildrenProvider
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
			// explorerNodeDataProvider
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
		if (NODE_TYPE_ROOT.equals(nodeType)) {
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
			} else if (nodeType.equals("r")) {
				String orgName = (String) context.get("orgName");
				String workingDirName = (String) context.get("workingDirectory");
				for (WorkingDirectory wd : ProjectsService.getInstance().getWorkingDirectoriesForOrganizationName(orgName)) {
					if (wd.getPathFromOrganization().equals(workingDirName)) {
						return wd;
					}
				}
			}
		}		
		return null;
	}


	@Override
	public List<PathFragment> getPathForNode(Object node, String nodeType, GenericTreeContext context) {
		return null;
	}

	@Override
	public void subscribe(StatefulServiceInvocationContext context,	IStatefulClientLocalState statefulClientLocalState) {
		// TODO Auto-generated method stub
	}

	@Override
	public void unsubscribe(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState) {
		// TODO Auto-generated method stub
	}

}
