package org.flowerplatform.communication.tree.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.INodeByPathRetriever;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.INodePopulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelegatingGenericTreeStatefulService extends
		GenericTreeStatefulService {

	private static Logger logger = LoggerFactory.getLogger(DelegatingGenericTreeStatefulService.class);
	
	private String statefulClientPrefixId;
	
	private Map<String, List<IChildrenProvider>> childrenProviders = new HashMap<String, List<IChildrenProvider>>();
	
	private Map<String, INodeDataProvider> nodeDataProviders = new HashMap<String, INodeDataProvider>();
	
	private Map<String, List<INodePopulator>> additionalNodePopulators = new HashMap<String, List<INodePopulator>>();
	
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

	public Map<String, List<INodePopulator>> getAdditionalNodePopulators() {
		return additionalNodePopulators;
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
		boolean result = provider.populateTreeNode(source, destination, context);
		
		List<INodePopulator> populators = additionalNodePopulators.get(nodeType);
		if (populators != null) {
			for (INodePopulator populator : populators) {
				populator.populateTreeNode(source, destination, context);
			}
		}
		
		// used for debugging
//		destination.setLabel("<" + destination.getPathFragment().getType() + "> " + destination.getLabel());
		
		return result;
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

	@Override
	public String getInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> fullPath) {
		if (fullPath == null || fullPath.isEmpty()) {
			// shouldn't happen
			return null;
		}
		String nodeType = fullPath.get(fullPath.size() - 1).getType();
		INodeDataProvider provider = nodeDataProviders.get(nodeType);
		if (provider == null) {
			logger.error("Tree delegate not found, for method getInplaceEditorText(); path = {}, nodeType = {}", getLabelForLog(fullPath, nodeType), nodeType);
			return null;
		}
		return provider.getInplaceEditorText(context, fullPath);
	}

	@Override
	public boolean setInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> fullPath, String text) {
		if (fullPath == null || fullPath.isEmpty()) {
			// shouldn't happen
			return false;
		}
		String nodeType = fullPath.get(fullPath.size() - 1).getType();
		INodeDataProvider provider = nodeDataProviders.get(nodeType);
		if (provider == null) {
			logger.error("Tree delegate not found, for method setInplaceEditorText(); path = {}, nodeType = {}", getLabelForLog(fullPath, nodeType), nodeType);
			return false;
		}
		return provider.setInplaceEditorText(context, fullPath, text);
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
		
		return super.getNodeByPath(fullPath, context);
	}

}
