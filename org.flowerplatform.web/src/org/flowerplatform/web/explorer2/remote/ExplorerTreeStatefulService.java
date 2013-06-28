package org.flowerplatform.web.explorer2.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.IGenericTreeStatefulServiceAware2;
import org.flowerplatform.communication.tree.INodeDataProvider2;
import org.flowerplatform.communication.tree.INodePopulator;
import org.flowerplatform.communication.tree.remote.DelegatingGenericTreeStatefulService2;
import org.flowerplatform.web.WebPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExplorerTreeStatefulService extends DelegatingGenericTreeStatefulService2 {

	private static Logger logger = LoggerFactory.getLogger(ExplorerTreeStatefulService.class);

	public ExplorerTreeStatefulService() throws CoreException {
		setStatefulClientPrefixId("Explorer");
		
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
					if (provider instanceof IGenericTreeStatefulServiceAware2) {
						((IGenericTreeStatefulServiceAware2) provider).setGenericTreeStatefulService(this);
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
				INodeDataProvider2 provider = (INodeDataProvider2) configurationElement.createExecutableExtension("provider");
				for (IConfigurationElement nodeTypeConfigurationElement : configurationElement.getChildren()) {
					String nodeType = nodeTypeConfigurationElement.getAttribute("nodeType");
					if (getNodeDataProviders().get(nodeType) != null) {
						logger.error("Trying to register an INodeDataProvider for nodeType = {}, but another one already exists: {}", nodeType, getNodeDataProviders().get(nodeType));
					} else {
						if (provider instanceof IGenericTreeStatefulServiceAware2) {
							((IGenericTreeStatefulServiceAware2) provider).setGenericTreeStatefulService(this);
						}
						getNodeDataProviders().put(nodeType, provider);
					}
				}
			}
			if (logger.isDebugEnabled()) {
				for (Map.Entry<String, INodeDataProvider2> entry : getNodeDataProviders().entrySet()) {
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
		if (populator instanceof IGenericTreeStatefulServiceAware2) {
			((IGenericTreeStatefulServiceAware2) populator).setGenericTreeStatefulService(this);
		}
		populators.add(populator);
	}

}
