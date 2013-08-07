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
package org.flowerplatform.communication.tree.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.flowerplatform.common.log.AuditDetails;
import org.flowerplatform.common.log.LogUtil;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.stateful_service.StatefulService;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.TreeInfoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTreeStatefulService extends StatefulService {

	public final static String NODE_TYPE_ROOT = "r";
	
	public static final String WHOLE_TREE_KEY = "wholeTree";
	
	private static final String EXPAND_NODE_KEY = "expandNode";
	
	private static final String SELECT_NODE_KEY = "selectNode";
	
	/**
	 * Useful, for trees that want to display a subset of a bigger tree, with data
	 * taken in "one-shot". E.g. some GIT tree that displays only a part of what
	 * is already displayed in the explorer tree. 
	 */
	public static final String DONT_UPDATE_MAP_KEY = "dontUpdateMap";	
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractTreeStatefulService.class);
	
	/**
	 * Holds the contexts of all subscribed trees.
	 */
	protected Map<TreeInfoClient, GenericTreeContext> treeContexts = new ConcurrentHashMap<TreeInfoClient, GenericTreeContext>();
	
	public abstract String getStatefulClientPrefixId();

	protected String getNodeType(TreeNode treeNode) {
		if (treeNode.getPathFragment() == null) {
			// root node
			return NODE_TYPE_ROOT;
		} else {
			return treeNode.getPathFragment().getType();
		}
	}
	
	protected String getNodeType(PathFragment pathFragment) {
		if (pathFragment == null) {
			return NODE_TYPE_ROOT;
		} else {
			return pathFragment.getType();
		}
	}
	
	/**
	 * Factory method returning the instance of tree node used.
	 * 
	 * @see #openNode()
	 * @see #dispatchContentUpdate()
	 * @see #dispatchLabelUpdate()
	 * 
	 */
	protected TreeNode createTreeNode() {
		return new TreeNode();
	}


	/**
	 * Populates {@link TreeNode#getPathFragment()} with data form {@link #getPathFragmentForNode()} and
	 * then delegates to the abstract method {@link #populateChildren()}.
	 * 
	 * @author Cristi
	 * 
	 */
	protected void populateTreeNodeInternal(Object source, TreeNode destination, GenericTreeContext context) {
		destination.setPathFragment(getPathFragmentForNode(source, getNodeType(destination), context));
		destination.setHasChildren(nodeHasChildren(source, destination, context));
		populateTreeNode(source, destination, context);
	}
	
	/**
	 * This method might be used for trees that send all the data at the beginning (i.e. no
	 * more openNode/closeNode afterwards).
	 * 
	 * @param recurse - if <code>true</code>, creates the whole tree structure for given node.
	 * 					Otherwise creates only its direct children.
	 * 
	 */
	protected void populateChildren(CommunicationChannel channel, String statefulClientId, Object node, TreeNode treeNode, GenericTreeContext context, boolean recurse) {
		// create and populate the children list
		for (Pair<Object, String> pair : getChildrenForNode(node, treeNode, context)) {
			Object child = pair.a;
			
			TreeNode childNode = createTreeNode();
			childNode.setPathFragment(new PathFragment(null, pair.b));
			populateTreeNodeInternal(child, childNode, context);	
			treeNode.getChildren().add(childNode);
			childNode.setParent(treeNode);
			
			if (recurse) { // get child whole structure
				childNode.setChildren(new ArrayList<TreeNode>());
				populateChildren(channel, statefulClientId, child, childNode, context, recurse);
			}
		}		
	}
	
	/**
	 * Subclasses that implement this method must provide 
	 * a list of children for given node. If the parameter
	 * is the dummy object used as root, that means that the content for
	 * the root node should be returned.
	 * 
	 * <p>
	 * Mandatory for all tree types.
	 * 
	 * <p>
	 * Also a context can be provided to filter the children list.
	 * 
	 */
	public abstract Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context);

	/**
	 * Should return whether the current node has children or not, preferably
	 * by an efficient method (i.e. something better than <code>getChildrenForNode() != null</code>).
	 * This recommendation is related to possible performance impact. E.g. this method
	 * may trigger the load mechanism of a resource. 
	 * 
	 * @author Cristi
	 * @return a <code>Boolean</code> which has 3 states. The 3rd state (i.e. null) may
	 * be handy if implementing services that delegate to other "sub"-services.
	 */
	public abstract Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context);

	/**
	 * Subclasses that implement this method must 
	 * populate the <code>destination</code> tree node with
	 * information stored in <code>source</code>.
	 * 
	 * <p>
	 * This must operate only on current node properties, not on its children
	 * (e.g. label, icon, etc.). {@link TreeNode#isHasChildren()} and {@link TreeNode#getPathFragment())
	 * are already automatically populated. 
	 * 
	 * <p>
	 * This method is never invoked for the root node.
	 * 
	 * 
	 * @author Cristi
	 * @return The return result is not taken into account by the platform. By convention, everyone
	 * should return <code>true</code>. The return value may be used by tree services that have "sub"
	 * tree services that they use for delegation (e.g. if result == null => the sub-service didn't know
	 * how to handle the call). 
	 */
	public abstract boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context);

	/**
	 * Subclasses that implement this method must provide 
	 * a {@link PathFragment} for given node.
	 * @author Mariana
	 */
	public abstract PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context);


	/**
	 * Subclasses that implement this method must the path fragment for given node.
	 * <p>
	 * If path fragment isn't human readable, subclasses must return a suggestive string instead.
	 *  
	 * 
	 */
	public abstract String getLabelForLog(Object node, String nodeType);
	
	public abstract Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context);	

	public abstract List<PathFragment> getPathForNode(Object node, String nodeType, GenericTreeContext context);
	
	/**
	 * Creates the client tree structure and
	 * modifies the server structure by updating {@link #rootNodeInfo} and {@link #visibleNodes}.	
	 * 
	 * @return - the {@link TreeNode} created
	 * 
	 * @see #populateChildren()
	 * @see #addNodeInfo()
	 * 
	 */
	public TreeNode openNodeInternal(CommunicationChannel channel, String statefulClientId, List<PathFragment> fullPath, Map<Object, Object> context) {
		GenericTreeContext treeContext = getTreeContext(channel, statefulClientId);
		treeContext.setClientContext(context);
		
		// gets the source node corresponding to given path			
		Object source = getNodeByPath(fullPath, treeContext);
		// create and populate the destination node
		TreeNode treeNode = createTreeNode();
		treeNode.setChildren(new ArrayList<TreeNode>());
		if (fullPath != null) {
			// we populate the node only for non-root nodes
			if (fullPath.isEmpty()) {
				throw new IllegalArgumentException("Trying to open with an empty path. It should be null or non-empty");
			}
			treeNode.setPathFragment(fullPath.get(fullPath.size() - 1));
			populateTreeNodeInternal(source, treeNode, treeContext);
		} else {
			treeNode.setHasChildren(true);
		}
		
		// create structure for current tree node or create structure for entire tree
		boolean entireStructure = false;
		if (treeContext.get(WHOLE_TREE_KEY) != null) {
			entireStructure = ((Boolean) context.get(WHOLE_TREE_KEY)).booleanValue();
		}
		// populate node with children data
		populateChildren(channel, statefulClientId, source, treeNode, treeContext, entireStructure);
		
		return treeNode;
	}

	protected TreeNode openNodeInternalFromExistingNodes(CommunicationChannel channel, String statefulClientId, List<PathFragment> fullPath, Map<Object, Object> context, Set<String> existingNodes) {
		TreeNode node = openNodeInternal(channel, statefulClientId, fullPath, context);
		List<TreeNode> children = new ArrayList<TreeNode>();
		for (TreeNode child : node.getChildren()) {
			// get child path as list of pathFragment
			List<PathFragment> childFullPath = new ArrayList<PathFragment>();
			if (fullPath != null) {
				childFullPath.addAll(fullPath);
			}
			childFullPath.add(child.getPathFragment());		
			// get child path as string
			String path = "";
			for (PathFragment pathFragment : childFullPath) {
				if (path != "") {
					path += "/";
				}
				path += pathFragment.getName();
			}
			if (existingNodes.contains(path)) {
				children.add(openNodeInternalFromExistingNodes(channel, statefulClientId, childFullPath, context, existingNodes));
			} else {
				children.add(child);
			}
		}
		node.setChildren(children);
		return node;
	}
	
	protected void updateNode(CommunicationChannel channel, String statefulClientId, List<PathFragment> path, TreeNode treeNode, boolean expandNode, boolean colapseNode, boolean selectNode, boolean isContentUpdate) {
		invokeClientMethod(
				channel, 
				statefulClientId, 
				"updateNode", 
				new Object[] {path, treeNode, expandNode, colapseNode, selectNode});		
	}
	
	public GenericTreeContext getTreeContext(CommunicationChannel channel, String statefulClientId) {
		TreeInfoClient treeInfoClient = new TreeInfoClient(channel, statefulClientId);
		
		if (!treeContexts.containsKey(treeInfoClient)) {
			treeContexts.put(treeInfoClient, new GenericTreeContext(this));
		}
		return treeContexts.get(treeInfoClient);
	}

	public void revealNode(StatefulServiceInvocationContext context, Object node, String nodeType) {		
		if (logger.isTraceEnabled()) {
			logger.trace("Revealing node {} to client [{} with statefulClientId={}]", new Object[] { getLabelForLog(node, nodeType), context.getCommunicationChannel(), context.getStatefulClientId() });
		}
		invokeClientMethod(
				context.getCommunicationChannel(), 
				context.getStatefulClientId(), 
				"revealNode", new Object[] {getPathForNode(node, nodeType, getTreeContext(context.getCommunicationChannel(), context.getStatefulClientId()))});		
	}
	
	/**
	 * Called from client when a node is opened.
	 * Used for both types of trees.
	 * 
	 * <p>
	 * Creates the corresponding {@link TreeNode} and its children and sends
	 * an update command to client.
	 * 
	 * <p>
	 * For dispatched trees, registers the object in {@link #visibleNodes} map
	 * and adds it in the list of parent's children (the parent must be already in map).
	 * 
	 * @param siContext
	 * @param fullPath - path of the node that must be opened. 
	 * 				If <code>null</code>, the node is considered to be the root.
	 * @param context - context used to customize the method (e.g. filter for the children list)
	 * 
	 * @see #openNodeInternal()
	 * 
	 * 
	 */
	@RemoteInvocation
	public void openNode(StatefulServiceInvocationContext context, List<PathFragment> path, Map<Object, Object> clientContext) {
		AuditDetails auditDetails = new AuditDetails(logger, "OPEN_NODE", path, context.getStatefulClientId());
		
		// create structure
		TreeNode treeNode = openNodeInternal(context.getCommunicationChannel(), context.getStatefulClientId(), path, clientContext);		
		
		updateNode(
				context.getCommunicationChannel(), 
				context.getStatefulClientId(), 
				path, 
				treeNode, 
				clientContext != null && clientContext.get(EXPAND_NODE_KEY) != null, 
				false, 
				clientContext != null && clientContext.get(SELECT_NODE_KEY) != null, 
				true);	
		
		LogUtil.audit(auditDetails);
	}
	
	@RemoteInvocation	
	public boolean updateTreeStatefulContext(StatefulServiceInvocationContext context, String key, Object value) {
		TreeInfoClient treeInfo = new TreeInfoClient(context.getCommunicationChannel(), context.getStatefulClientId());
		GenericTreeContext treeContext = treeContexts.get(treeInfo);
					
		if (treeContext == null) {
			treeContext = new GenericTreeContext(this);			
		}		
		treeContext.getStatefulContext().put(key, value);
		
		treeContexts.put(treeInfo, treeContext);
		
		return true;
	}
	
}