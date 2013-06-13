package org.flowerplatform.web.git.repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.DelegatingGenericTreeStatefulService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristina Constantinescu
 */
public class GitExplorerTreeStatefulService extends DelegatingGenericTreeStatefulService {
	
	private static Logger logger = LoggerFactory.getLogger(GitExplorerTreeStatefulService.class);
	
	public GitExplorerTreeStatefulService() throws CoreException {
		setStatefulClientPrefixId("Git Explorer");
		
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
				
				list.add(provider);
			}
		}
		
		configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.web.explorerNodeDataProvider");
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
			for (Map.Entry<String, List<IChildrenProvider>> entry : getChildrenProviders().entrySet()) {
				logger.debug("GitExplorerTreeStatefulService: for nodeType = {}, these are the children providers = {}", entry.getKey(), entry.getValue());
			}
			for (Map.Entry<String, INodeDataProvider> entry : getNodeDataProviders().entrySet()) {
				logger.debug("GitExplorerTreeStatefulService: for nodeType = {}, this is the node data provider = {}", entry.getKey(), entry.getValue());
			}
		}
	}

}
