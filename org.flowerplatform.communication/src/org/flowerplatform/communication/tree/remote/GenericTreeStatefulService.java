package org.flowerplatform.communication.tree.remote;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.flowerplatform.common.log.AuditDetails;
import org.flowerplatform.common.log.LogUtil;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.common.util.RunnableWithParam;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
import org.flowerplatform.communication.stateful_service.NamedLockPool;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.stateful_service.StatefulService;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildNodeAdjusterBeforeProcessing;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.NodeInfo;
import org.flowerplatform.communication.tree.NodeInfoClient;
import org.flowerplatform.communication.tree.TreeInfoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service used to provide functionality for generic trees.
 * 
 * <p>
 * There are 3 different types of trees:
 * <ul>
 * 	<li> non-dispatched trees (simple trees) -> only shows tree data
 * 	<li> dispatched trees -> shows and updates tree data by dispatching notifications to 
 * 		a list of subscribed clients
 * 	<li> partial dispatched trees -> only a limited number of tree nodes are dispatched;
 * 		if a node is dispatched, all its upper structure must be also in dispatched mode.
 * </ul>
 * 
 * To provide functionality for a non-dispatched tree, the following methods must be implemented:
 * <ul>
 * 	<li> {@link #populateTreeNode()}
 * 	<li> {@link #getPathFragmentForNode()}
 * 	<li> {@link #getNodeByPathFragment()}
 * 	<li> {@link #getChildrenForNode()}
 * 	<li> {@link #getParent()}
 * </ul>
 * 
 * To provide functionality for a dispatched/partial dispatched tree, the following methods must be implemented
 * in addition to the list mentioned above:
 * <ul>
 * 	<li> {@link #isDispatchEnabled()()} 
 * </ul>
 * Also, a service for dispatched trees is responsible to listen for label/content updates
 * and to call {@link #dispatchLabelUpdate()}/{@link #dispatchContentUpdate()}.
 * 
 * <p>
 * For a dispatched tree, when its lifeline (on client side) ends, it must perform cleanup, i.e.
 * close the root node. E.g. in Eclipse, a listener must be put when the dialog is closing and a call to <code>closeNode(null, -1, null)</code>
 * must be done in order to clean the data used. <br>
 * In web, all services must extend <code>WebGenericTreeService</code> which already performs this requirement when a client is destroyed.
 * This stands for trees that are always open (e.g. Project Explorer). If you use dispatched trees with limited lifeline (e.g. in a dialog)
 * the same remarks for the above Eclipse example still stands.
 *  
 * 
 * <p>
 * For trees with inplace editing active, their services must implement the following methods:
 * <ul>
 * 	<li> {@link #setInplaceEditorText(List, String)}
 * 	<li> {@link #getInplaceEditorText(List)}
 * </ul>
 * 
 * @author Cristi
 * @author Cristina
 * 
 * @flowerModelElementId _qwPpwA7rEeKbvNML8mcTuA
 */
public abstract class GenericTreeStatefulService extends StatefulService implements INodeInfoStatefulServiceMXBean, IChildrenProvider, INodeDataProvider, IChildNodeAdjusterBeforeProcessing {
	
	public final static String NODE_TYPE_ROOT = "r";
	
	/**
	 * @flowerModelElementId _G-cP8Kr2EeG3eZ1Jezjhtw
	 */
	public static final String WHOLE_TREE_KEY = "wholeTree";
	
	/**
	 * @flowerModelElementId _G-eFIar2EeG3eZ1Jezjhtw
	 */
	private static final String EXPAND_NODE_KEY = "expandNode";
	
	/**
	 * @flowerModelElementId _G-f6Uar2EeG3eZ1Jezjhtw
	 */
	private static final String SELECT_NODE_KEY = "selectNode";
	
	public static final String DONT_UPDATE_MAP_KEY = "dontUpdateMap";	
	
	/**
	 * NO USED
	 * Adding this key to <code>context</code> will retrieve the node's 
	 * path by going up the {@link NodeInfo} hierarchy, instead of using
	 * {@link #getParent(Object)}. Should be used when the node was deleted
	 * to ensure that it will be removed from {@link #openNodes} map, for
	 * example, if deleting model files.
	 * 
	 * @see #dispatchExpandedUpdate(Object, CommunicationChannel, boolean)
	 * @see #getPathForNode(Object, Map)
	 * 
	 * @author Mariana
	 * @flowerModelElementId _PlC_0AIeEeKqVJdl4mrwww
	 */
	private static final String GO_UP_ON_NODE_INFO_KEY = "goUpOnNodeInfo";
	
	/**
	 * This key will be used in the getNodeByPath method. 
	 * If the context parameter will have this key set, then the node
	 * will be search only by using the rootNodeInfo and the pathFragments 
	 * making no use for the stored tree structure 
	 * 
	 * @see #getNodeByPath(List, Map)
	 * @see ProjectExplorerTreeService#getNodeByPath(List, Map)
	 * @flowerModelElementId _yYoOAAh1EeKBk5gFLRykjQ
	 */
	public static final String GO_DOWN_ON_PATH_FRAGMENT_KEY = "goDownOnPathFragment";
	
	/**
	 * Notifications will only be dispatched to the client with {@link CommunicationChannel#getClientId()}
	 * mapped by this key in the <code>context</code> map.
	 * 
	 * @see #dispatchContentUpdate(Object, ClientInvocationOptions, Map)
	 * 
	 * @author Mariana
	 */
	protected static final String DISPATCH_ONLY_FOR_CLIENT = "dispatchOnlyForClient";
		
	/**
	 * @flowerModelElementId _3pTm4A7rEeKbvNML8mcTuA
	 */
	protected Map<Object, NodeInfo> openNodes = new ConcurrentHashMap<Object, NodeInfo>();
	
	/**
	 * Holds the tree structure of all displayed nodes on clients (opened or not).
	 * 
	 * @flowerModelElementId _pXDLkKD1EeG5ENNne79MAQ
	 */
	private NodeInfo rootNodeInfo;
	
	/**
	 * Holds the contexts of all subscribed trees.
	 */
	protected Map<TreeInfoClient, GenericTreeContext> treeContexts = new ConcurrentHashMap<TreeInfoClient, GenericTreeContext>();
	
	/**
	 * We use this instead of normal locking, because, during subscription there is a small
	 * time window where 2 threads subscribing for the same resource could create the {@link NodeInfo}
	 * twice. Of course, we could have locked on the entire map, which would have solved this, but with
	 * a big performance impact. 
	 * 
	 * @see NamedLockPool
	 * @see #subscribe()
	 */
	protected NamedLockPool namedLockPool = new NamedLockPool();
	
	/**
	 * @flowerModelElementId _SbysYKDXEeG5ENNne79MAQ
	 */
	private static final Logger logger = LoggerFactory.getLogger(GenericTreeStatefulService.class);
		
	///////////////////////////////////////////////////////////////
	// JMX Methods
	///////////////////////////////////////////////////////////////
	
	/**
	 * @flowerModelElementId _shOcoBN0EeKR8sYuzDGiDQ
	 */
	public String printNodeInfos() {
		StringBuffer sb = new StringBuffer();
		
		for (NodeInfo node : openNodes.values()) {			
			sb.append(node).append("\n");
			for (NodeInfoClient client : node.getClients()) {
				sb.append("  ").append(client).append("\n");
			}
		}
		
		return sb.toString();
	}
	
	public String printTreeStatefulContext(String webCommunicationChannelIdFilter, String linePrefix) {
		// clean parameters
		if ("".equals(webCommunicationChannelIdFilter) || "String".equals(webCommunicationChannelIdFilter)) {
			webCommunicationChannelIdFilter = null;
		}
		if ("String".equals(linePrefix)) { 
			linePrefix = "";
		}
						
		StringBuffer sb = new StringBuffer();
	
		for (TreeInfoClient tree : treeContexts.keySet()) {
			// execute if no filter or if filter matches
			if (webCommunicationChannelIdFilter == null || webCommunicationChannelIdFilter.equals(tree.getCommunicationChannel().getId())) {
				GenericTreeContext treeContext = getTreeContext(tree.getCommunicationChannel(), tree.getStatefulClientId());
				sb.append(linePrefix).append("  ").append(treeContext.getStatefulContext()).append("\n");
			}
		}				
		return sb.toString();	
	}
	
	private class CommunicationChannelAndNodeInfos {
		private CommunicationChannel communicationChannel;
		private List<NodeInfo> nodeInfos = new ArrayList<NodeInfo>();
	}
			
	/**
	 * @flowerModelElementId _qRVZgBK3EeKz7oFAPW_0ZA
	 */
	public String printStatefulDataPerCommunicationChannel(String webCommunicationChannelIdFilter, String linePrefix) {		
		// clean parameters
		if ("".equals(webCommunicationChannelIdFilter) || "String".equals(webCommunicationChannelIdFilter)) {
			webCommunicationChannelIdFilter = null;
		}
		if ("String".equals(linePrefix)) { 
			linePrefix = "";
		}
				
		StringBuffer sb = new StringBuffer();
		Map<String, CommunicationChannelAndNodeInfos> map = new HashMap<String, CommunicationChannelAndNodeInfos>();
				
		// build the inverse hierarchy
		for (NodeInfo node : openNodes.values()) {	
			for (NodeInfoClient client : node.getClients()) {
				// execute if no filter or if filter matches
				if (webCommunicationChannelIdFilter == null || webCommunicationChannelIdFilter.equals(client.getCommunicationChannel().getId())) {
					
					// find or create entry
					CommunicationChannelAndNodeInfos entry = map.get(client.getCommunicationChannel().getId());
					if (entry == null) {
						entry = new CommunicationChannelAndNodeInfos();
						entry.communicationChannel = client.getCommunicationChannel();
						map.put((String) client.getCommunicationChannel().getId(), entry);
					}							
					// add node to the list
					entry.nodeInfos.add(node);
				}
			}			
		}				
		// print
		for (CommunicationChannelAndNodeInfos entry : map.values()) {
			sb.append(linePrefix).append(entry.communicationChannel).append("\n");
			for (NodeInfo nodeInfo : entry.nodeInfos) {
				sb.append(linePrefix).append("  ").append(nodeInfo).append("\n");
			}
		}			
		return sb.toString();
	}
	
	public Collection<String> getStatefulClientIdsForCommunicationChannel(CommunicationChannel communicationChannel) {
		List<String> ids = new ArrayList<String>();
		for (NodeInfo nodeInfo : openNodes.values()) {		
			for (NodeInfoClient clientInfo : nodeInfo.getClients()) {
				if (clientInfo.getCommunicationChannel().equals(communicationChannel) && !ids.contains(clientInfo.getStatefulClientId(this))) {				
					ids.add(clientInfo.getStatefulClientId(this));
				}				
			}
		}
		return ids;
	}
	
	///////////////////////////////////////////////////////////////
	// Normal methods
	///////////////////////////////////////////////////////////////

	
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
	 * @flowerModelElementId _HA01kKr2EeG3eZ1Jezjhtw
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
	 * Subclasses that implement this method must provide 
	 * a parent for given node.	
	 *  
	 * @flowerModelElementId _NxVukJ9aEeGYPK0E1LmMXw
	 */
	public abstract Object getParent(Object node, String nodeType, GenericTreeContext context);

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
	 * @flowerModelElementId _HA9YcKr2EeG3eZ1Jezjhtw
	 * @author Cristi
	 * @return The return result is not taken into account by the platform. By convention, everyone
	 * should return <code>true</code>. The return value may be used by tree services that have "sub"
	 * tree services that they use for delegation (e.g. if result == null => the sub-service didn't know
	 * how to handle the call). 
	 */
	public abstract boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context);

	/**
	 * Subclasses that implement this method must provide 
	 * a node for given {@link PathFragment}.
	 * 	
	 * @flowerModelElementId _i-hGwKD2EeG5ENNne79MAQ
	 */
	public abstract Object getNodeByPathFragment(Object parent, PathFragment pathFragment, GenericTreeContext context);

	/**
	 * Subclasses that implement this method must provide 
	 * a {@link PathFragment} for given node.
	 * @author Mariana
	 */
	public abstract PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context);

	/**
	 * Returns an object for the given path, 
	 * including for root (i.e. <code>fullPath = null</code>. 
	 * 
	 * <p>
	 * In the case of root, a dummy
	 * object is accepted as well (e.g. an instance of this
	 * service, etc.), but it shouldn't be null.
	 * 
	 * <br>
	 * Note:
	 * This method must be implemented if other implementation seems to be more effective.
	 * 	 
	 * @flowerModelElementId _vBmpcaP8EeGeHqktJlHXmA
	 */
	/**
	 * @param fullPath
	 * @param context
	 * @return
	 * @flowerModelElementId _vBmpcaP8EeGeHqktJlHXmA
	 */
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {		
		NodeInfo nodeInfo;
		NodeInfo parentInfo;		
		if (isDispatchEnabled(null) && rootNodeInfo != null) {
			// get the root node
			nodeInfo = rootNodeInfo;
		} else {
			// create a dummy root node
			nodeInfo = new NodeInfo();
			nodeInfo.setParent(null);
			nodeInfo.setNode(getNodeByPathFragment(null, null, context));
		}
		if (fullPath != null) {
			for (PathFragment pathFragment : fullPath) {
				// hold the parent
				parentInfo = nodeInfo;
				// this will be filled if node found
				nodeInfo = null;
				// if in the call context for this method there is no special
				// requirement for taking the node in real time by using only its
				// pathFragment and if node dispatched we first try to search in 
				// the stored tree structure
				if (context != null && !context.containsKey(GO_DOWN_ON_PATH_FRAGMENT_KEY) && parentInfo != null && isDispatchEnabled(parentInfo.getNode())) {							
					for (NodeInfo child : parentInfo.getChildren()) {		
						if (child.getPathFragment().getName().equals(pathFragment.getName()) && 
							child.getPathFragment().getType().equals(pathFragment.getType())) {
							nodeInfo = child;
							break;
						}
					}				
				}
				// if special requirement for taking the node in real time by using only its
				// pathFragment or not dispatched or not found in tree structure
				if (nodeInfo == null) {
					// get node from fragment and create a dummy nodeInfo
					// to be used in next iteration
					Object node = getNodeByPathFragment(parentInfo.getNode(), pathFragment, context);
					
					// There was a problem with the given path because no node is uniquely identify by it
					if (node == null)
						return null;
					else {
						// create a dummy nodeInfo
						nodeInfo = new NodeInfo();
						nodeInfo.setParent(parentInfo);
						nodeInfo.setNode(node);
					}
				}
			}
		}
		return nodeInfo.getNode();
	}

	/**
	 * Subclasses that implement this method must the path fragment for given node.
	 * <p>
	 * If path fragment isn't human readable, subclasses must return a suggestive string instead.
	 *  
	 * @flowerModelElementId _disIUKDXEeG5ENNne79MAQ
	 */
	public abstract String getLabelForLog(Object node, String nodeType);

	/**
	 * @flowerModelElementId _YYXu4KKgEeGYz6sIcvSzpg
	 */
	@RemoteInvocation
	public abstract String getInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> fullPath);

	/**
	 * 	 
	 * @flowerModelElementId _gznc4KKgEeGYz6sIcvSzpg
	 */
	@RemoteInvocation
	public abstract boolean setInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> path, String text);

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
	 * Should return <code>true</code> if the service provides functionality
	 * for a dispatched tree. <br>
	 * By default, returns <code>false</code> (functionality for simple tree).
	 * <p>
	 * If a node is provided, then it should return whether or not
	 * the given node will be seen as dispatched. <br>
	 * This is the case of a "partial dispatched tree":
	 * some nodes are dispatched, but not all.
	 * <p>
	 * Note: all the above structure of a dispatched node must be in dispatched mode
	 * (the parents must be seen as dispatched nodes) in order to work properly.
	 * @flowerModelElementId _NcjBsKTpEeGJQ4vD1xX4gA
	 */
	protected boolean isDispatchEnabled(Object node) {
		return false;
	}

	/**
	 * Factory method returning the instance of tree node used.
	 * 
	 * @see #openNode()
	 * @see #dispatchContentUpdate()
	 * @see #dispatchLabelUpdate()
	 * @flowerModelElementId _i1kT08BrEeG5PP70DrXYIQ
	 */
	protected TreeNode createTreeNode() {
		return new TreeNode();
	}

	/**
	 * This method might be used for trees that send all the data at the beginning (i.e. no
	 * more openNode/closeNode afterwards).
	 * 
	 * @param recurse - if <code>true</code>, creates the whole tree structure for given node.
	 * 					Otherwise creates only its direct children.
	 * @flowerModelElementId _G-y1RKr2EeG3eZ1Jezjhtw
	 */
	private void populateChildren(CommunicationChannel channel, String statefulClientId, Object node, TreeNode treeNode, GenericTreeContext context, boolean recurse) {
		// create and populate the children list
		for (Pair<Object, String> pair : getChildrenForNode(node, treeNode, context)) {
			NodeInfo newNodeInfoWithCustomData = new NodeInfo();
			Object child = pair.a;
			child = adjustChild(child, pair.b, newNodeInfoWithCustomData);
			TreeNode childNode = createTreeNode();
			childNode.setPathFragment(new PathFragment(null, pair.b));
			populateTreeNodeInternal(child, childNode, context);	
			treeNode.getChildren().add(childNode);
			childNode.setParent(treeNode);
			
			if (recurse) { // get child whole structure
				childNode.setChildren(new ArrayList<TreeNode>());
				populateChildren(channel, statefulClientId, child, childNode, context, recurse);
			}
			
			// for dispatched trees, update the server tree structure
			if (isDispatchEnabled(child)) {	
				boolean updateMap = true;
				if (context.get(DONT_UPDATE_MAP_KEY) != null) {
					updateMap = !((Boolean) context.get(DONT_UPDATE_MAP_KEY)).booleanValue();
				}
				if (updateMap) {
					addNodeInfo(channel, statefulClientId, child, getNodeType(childNode), false, false, context, newNodeInfoWithCustomData);
				}
			}
		}		
	}
	
	/**
	 * Adds node information to tree structure and 
	 * updates the {@link #openNodes} map if requested.
	 * <p>
	 * At the end, subscribe the given channel and clientId to node.
	 * 
	 * @flowerModelElementId _G-7_NKr2EeG3eZ1Jezjhtw
	 */
	private void addNodeInfo(CommunicationChannel channel, String statefulClientId, Object node, String nodeType, boolean isRoot, boolean addInOpenNodes, GenericTreeContext context, NodeInfo newNodeInfoWithCustomData) {
		NodeInfo nodeInfo = openNodes.get(node);
		if (nodeInfo == null) {	
			namedLockPool.lock(node);	
			try {
				if (isRoot) { // must create the root node
					logger.trace("Root node never opened; adding it to structure & map");				
					// if root node, create one, don't add it to open nodes					
					rootNodeInfo = new NodeInfo();
					rootNodeInfo.setNode(node);					
					nodeInfo = rootNodeInfo;
					openNodes.put(node, nodeInfo);				
				} else { // not opened
					// get parent open node
					Object parent = getParent(node, nodeType, context);
					if (parent == null) { // this shouldn't happen
						throw new RuntimeException("Parent node not found for node " + getLabelForLog(node, nodeType));
					}
					NodeInfo parentInfo = openNodes.get(parent); // parent must be opened
					if (parentInfo == null) {
						logger.debug("Parent info for {} was already closed!", parent);
						return;
					}
					// search node info in list of parent's children
					for (NodeInfo childInfo : parentInfo.getChildren()) {
						if (childInfo.getNode().equals(node)) {
							nodeInfo = childInfo;
							break;
						}
					}
					if (nodeInfo == null) { // not found in parent, add it
						logger.trace("Node {} never added in structure; adding it", getLabelForLog(node, nodeType));	
						// create new open node entry and populate it
						if (newNodeInfoWithCustomData != null) {
							nodeInfo = newNodeInfoWithCustomData;
						} else {
							nodeInfo = new NodeInfo();
						}
						nodeInfo.setNode(node);
						nodeInfo.setParent(parentInfo);
						nodeInfo.setPathFragment(getPathFragmentForNode(node, nodeType, context));					
						// add node to tree
						parentInfo.getChildren().add(nodeInfo);	
					}
					if (addInOpenNodes) { // add node to map
						logger.trace("Node {} added to openNodes", getLabelForLog(node, nodeType));	
						openNodes.put(node, nodeInfo);
					}
				}
			} finally {
				namedLockPool.unlock(node);
			}
		}				
		
		// add new subscribed client if necessary 
		boolean exists = false;		
		for (NodeInfoClient nodeClient : nodeInfo.getClients()) {	
			if (nodeClient.getCommunicationChannel().equals(channel) &&
				nodeClient.getStatefulClientId(this).equals(statefulClientId)) {
				exists = true;
				break;
			}
		}
		if (!exists) {
			logger.trace("Subscribing client [{} with statefulClientId={}] to node {}", new Object[] { channel, statefulClientId, getLabelForLog(node, nodeType)});
			nodeInfo.addNodeInfoClient(new NodeInfoClient(channel, statefulClientId, this));
		} else {
			logger.trace("Client [{} with statefulClientId={}] already subscribed to node {}", new Object[] { channel, statefulClientId, getLabelForLog(node, nodeType)});
		}		
	}
	
	/**
	 * Should be called by java listener for label changes.
	 * Available only for dispatched trees.
	 * 
	 * <p>
	 * Verifies if the parent is opened. If <code>true</code>,
	 * creates and populates a {@link TreeNode} with new data and sends updates to all
	 * subscribed clients. 	
	 * @flowerModelElementId _KIZOsJF0EeGZxtPbjaCZxw
	 */
	public void dispatchLabelUpdate(Object node, String nodeType) {
		NodeInfo nodeInfo = openNodes.get(node); // check if opened
		if (nodeInfo == null) { // not found, check if parent is opened and gets its info
			// check if parent is opened
			Object parent = getParent(node, nodeType, null);
			if (parent == null) {
				// this shouldn't normally happen, because the root node is not visible
				// so a label update doesn't make sense
				return;
			}
			nodeInfo = openNodes.get(parent);
		}		
		if (nodeInfo != null) {
			for (NodeInfoClient nodeClient : nodeInfo.getClients()) {	
				dispatchLabelUpdateForClient(node, nodeInfo, nodeClient);
			}
		}					
	}
	
	protected void dispatchLabelUpdateForClient(Object node, NodeInfo nodeInfo, NodeInfoClient nodeClient) {	
		GenericTreeContext context = getTreeContext(nodeClient.getCommunicationChannel(), nodeClient.getStatefulClientId(this));
		
		List<PathFragment> path = getPathForNode(node, nodeInfo.getPathFragment().getType(), context);
		TreeNode treeNode = createTreeNode();
		treeNode.setPathFragment(path.get(path.size() - 1));

		populateTreeNodeInternal(node, treeNode, context);	
		
		if (logger.isTraceEnabled()) {
			logger.trace("Dispatching label update for node {} to client [{} with statefulClientId={}]", new Object[] { getLabelForLog(node, nodeInfo.getPathFragment().getType()), nodeClient.getCommunicationChannel(), nodeClient.getStatefulClientId(this) });
		}
		updateNode(
				nodeClient.getCommunicationChannel(), 
				nodeClient.getStatefulClientId(this), 
				path, treeNode, 
				false, false, false, false);
	}
	
	/**
	 * Should be called by java listener for content changes.
	 * Available only for dispatched trees.
	 * 
	 * <p>
	 * Verifies if the node is opened.
	 * If <code>true</code>, compares the list of new children with 
	 * the one stored in {@link NodeInfo}. <br>
	 * The ones displayed but not found in the new list are considered to be deleted,
	 * so a cleanup is called. <br>
	 * The ones displayed and opened but not found in the new list will be closed.
	 * All their children structure it will be also updated to close their opened nodes.
	 * 
	 * <p>
	 * Also, creates and populates a new tree node (including object children) and
	 * sends updates to all subscribed clients. 
	 * @flowerModelElementId _GlGHEJF0EeGZxtPbjaCZxw
	 */
	public void dispatchContentUpdate(Object node) {
		NodeInfo nodeInfo = openNodes.get(node);
		if (nodeInfo == null) { // not opened, return
			return;
		}		
		// send updates to all subscribed clients
		for (NodeInfoClient nodeClient : nodeInfo.getClients()) {	
			GenericTreeContext context = getTreeContext(nodeClient.getCommunicationChannel(), nodeClient.getStatefulClientId(this));			
			if (context != null && context.containsKey(DISPATCH_ONLY_FOR_CLIENT)) {
				if (!nodeClient.getCommunicationChannel().getId().equals(context.get(DISPATCH_ONLY_FOR_CLIENT))) {
					continue; // don't send this update to the other clients except the client in the context map
				}
			}
			dispatchContentUpdateForClient(node, nodeClient);
		}
	}
	
	protected void dispatchContentUpdateForClient(Object node, NodeInfoClient nodeClient) {	
		GenericTreeContext context = getTreeContext(nodeClient.getCommunicationChannel(), nodeClient.getStatefulClientId(this));
		
		NodeInfo nodeInfo = openNodes.get(node);
		// cleanup map if unavailable nodes
		List<NodeInfo> openChildren = nodeInfo.getChildren();

		HashMap<Object, NodeInfo> oldNodes = new HashMap<Object, NodeInfo>();
		for (NodeInfo oldChild : openChildren) {
			oldNodes.put(oldChild.getNode(), oldChild);
		}
		// create/populate tree
		TreeNode treeNode = createTreeNode();
		treeNode.setPathFragment(nodeInfo.getPathFragment());
		treeNode.setChildren(new ArrayList<TreeNode>());
		populateTreeNodeInternal(node, treeNode, context);

		// create/populate children
		for (Pair<Object, String> pair : getChildrenForNode(node, treeNode, context)) {
			Object child = pair.a;
			TreeNode childNode = createTreeNode();
			childNode.setPathFragment(new PathFragment(null, pair.b));
			populateTreeNodeInternal(child, childNode, context);
			treeNode.getChildren().add(childNode);
			childNode.setParent(treeNode);
			oldNodes.remove(child);
		}

		// cleanup old nodes
		for (NodeInfo oldChild : oldNodes.values()) {
			cleanupAfterNodeClosed(oldChild, oldChild.getPathFragment().getType(), nodeClient.getStatefulClientId(this), nodeClient.getCommunicationChannel(), null);
		}

		List<PathFragment> path = getPathForNode(node, nodeInfo.getPathFragment().getType(), context);
		if (logger.isTraceEnabled()) {
			logger.trace(
					"Dispatching content update for node {} to client [{} with statefulClientId={}]",
					new Object[] { getLabelForLog(node, nodeInfo.getPathFragment().getType()),
							nodeClient.getCommunicationChannel(),
							nodeClient.getStatefulClientId(this) });
		}
		updateNode(nodeClient.getCommunicationChannel(),
				nodeClient.getStatefulClientId(this), path, treeNode,
				false, false, false, true);
				
	}

	/**
	 * Notifies the client to expand or collapse the node in its active tree.
	 * 
	 * @param node node to expand/collapse
	 * @param client 
	 * @param expandNode true to expand the node, false to collapse
	 * @author Mariana
	 * @flowerModelElementId _js9gMP0DEeGZPtPdwyatgg
	 */
	public void dispatchExpandedUpdate(Object node, CommunicationChannel client, boolean expandNode) {
		NodeInfo nodeInfo = openNodes.get(node);
		if (nodeInfo == null) {
			return;
		}
		for (NodeInfoClient nodeClient : nodeInfo.getClients()) {
			if (nodeClient.getCommunicationChannel().equals(client)) {
				dispatchExpandedUpdateForClient(nodeInfo.getNode(), expandNode, nodeInfo, nodeClient);
				break;
			}
		}
	}

	protected void dispatchExpandedUpdateForClient(Object node, boolean expandNode, NodeInfo nodeInfo, NodeInfoClient nodeClient) {
		GenericTreeContext treeContext = getTreeContext(nodeClient.getCommunicationChannel(), nodeClient.getStatefulClientId(this));

		List<PathFragment> path = getPathForNode(node, nodeInfo.getPathFragment().getType(), treeContext);
		TreeNode treeNode = createTreeNode();
		treeNode.setPathFragment(path.get(path.size() - 1));
		
		populateTreeNodeInternal(node, treeNode, treeContext);
	
		Map<Object, Object> context = new HashMap<Object, Object>();
		// adding this key will ensure that the correct path will be found using the NodeInfo hierarchy
		context.put(GO_UP_ON_NODE_INFO_KEY, true);		
		treeContext.setClientContext(context);
		
		updateNode(
				nodeClient.getCommunicationChannel(), 
				nodeClient.getStatefulClientId(this), 
				path, treeNode, 
				expandNode, !expandNode, false, false);	
									
		if (!expandNode) {
			closeNode(new StatefulServiceInvocationContext(nodeClient.getCommunicationChannel(), null, nodeClient.getStatefulClientId(this)), path, context);
		}
	}

	public GenericTreeContext getTreeContext(CommunicationChannel channel, String statefulClientId) {
		TreeInfoClient treeInfoClient = new TreeInfoClient(channel, statefulClientId);
		
		if (!treeContexts.containsKey(treeInfoClient)) {
			treeContexts.put(treeInfoClient, new GenericTreeContext());
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
	 * Cleans up the {@link #openNodes} and {@link #rootNodeInfo}.
	 * <p>
	 * Removes the given client form list of subscribed clients. <br>
	 * After that, verifies if the node has multiple subscribed clients.
	 * If not, deletes the node and the entry found on parent's list of children.
	 * 
	 * <p>
	 * This steps are done by iterating recursively on children list.
	 * 
	 * @param channel - if <code>null</code>, the node is
	 * 					considered to be deleted so it will be removed from map.
	 * 
	 * @see #closeNode()
	 * @see #dispatchContentUpdate()
	 * @see #cleanupChildren()
	 * 
	 * @flowerModelElementId _p0v2QJF1EeGZxtPbjaCZxw
	 */
	protected void cleanupAfterNodeClosed(Object node, String nodeType, String statefulClientId, CommunicationChannel channel, RunnableWithParam<Void, NodeInfo> removeNodeInfoRunnable) {
		if (logger.isTraceEnabled()) {
			logger.trace("Cleanup node {} to client [{} with statefulClientId={}]", new Object[] { getLabelForLog(node, nodeType), channel, statefulClientId });			
		}
		
		if (removeNodeInfoRunnable == null) {
			removeNodeInfoRunnable = new RunnableWithParam<Void, NodeInfo>() {				
				public Void run(NodeInfo nodeInfo) {
					logger.debug("Removing node from openNodes {}", nodeInfo);
					openNodes.remove(nodeInfo.getNode());					
					return null;
				}
			};
		}		
		NodeInfo nodeInfo = (NodeInfo) ((node instanceof NodeInfo) ? node : openNodes.get(node));
		if (nodeInfo == null) {
			if (rootNodeInfo.getNode().equals(node)) {
				nodeInfo = rootNodeInfo;
			} else {
				// CS: cf. remarca mm
				return;
//				NodeInfo parentNodeInfo = openNodes.get(getParent(node, getTreeContext(channel,statefulClientId)));
//				for (NodeInfo child : parentNodeInfo.getChildren()) {
//					if (node.equals(child.getNode())) {
//						nodeInfo = child;
//						break;
//					}
//				}
			}
		}		
		if (openNodes.containsKey(nodeInfo.getNode())) { // open node
			for (final Iterator<NodeInfo> it = nodeInfo.getChildren().iterator(); it.hasNext();) {	
				NodeInfo childInfo = it.next();			
				NodeInfoClient childNodeInfoClient = childInfo.getNodeInfoClientByCommunicationChannelThreadSafe(channel, statefulClientId, this);
				if (childNodeInfoClient != null) {
					cleanupAfterNodeClosed(childInfo, childInfo.getPathFragment().getType(), statefulClientId, channel, new RunnableWithParam<Void, NodeInfo>() {
						
						public Void run(NodeInfo nodeInfo) {
							logger.debug("Removing node from openNodes & rootNode {}", nodeInfo);
							openNodes.remove(nodeInfo.getNode());							
							it.remove();
							return null;
						}
					});		
				}
			}
		}
			
		boolean removeOpenNode = false;
		if (channel == null) { // no channel, no open node, mark to be deleted
			removeOpenNode = true;			
		} else {
			if (nodeInfo.equals(rootNodeInfo)) {
				for (Iterator<TreeInfoClient> iter = treeContexts.keySet().iterator(); iter.hasNext(); ) {
					TreeInfoClient treeInfoClient = iter.next();
					if (treeInfoClient.getCommunicationChannel().equals(channel) &&
							(statefulClientId == null || treeInfoClient.getStatefulClientId().equals(statefulClientId))) {
						// found
						treeContexts.remove(treeInfoClient);
						if (statefulClientId != null) { // only this node must be removed, so return
							break;
						}
					}
				}			
			}
			
			NodeInfoClient client = nodeInfo.removeNodeInfoClientByCommunicationChannel(channel, statefulClientId, this);
			if (statefulClientId != null && client == null) { // a specific client wasn't found, maybe it was removed while executing this method
				logger.debug("The client = {} is not subscribed to the Node Info with path = {}", channel, nodeInfo.getPathFragment());
			}
			
			if (logger.isTraceEnabled()) {
				logger.trace("Removing client = {} to NodeInfo with path = {}. Now there are {} clients subscribed to this resource.", new Object[] {channel, nodeInfo.getPathFragment(), nodeInfo.getClients().size() });
			}
			
			if (nodeInfo.getClients().size() == 0) { // no other clients, mark to be deleted
				removeOpenNode = true;
			}		
		}
		
		if (removeOpenNode)	{
			namedLockPool.lock(nodeInfo.getNode());
			try {
				if (logger.isTraceEnabled()) {
					logger.trace("Removing open node {} for statefulClientId={}]", new Object[] { getLabelForLog(node, nodeType), statefulClientId });			
				}
				removeNodeInfoRunnable.run(nodeInfo);
				if (nodeInfo.equals(rootNodeInfo)) {
					treeContexts.clear();
				}
			} finally {
				namedLockPool.unlock(nodeInfo.getNode());
			}
		}
	}

	/**
	 * Populates {@link TreeNode#getPathFragment()} with data form {@link #getPathFragmentForNode()} and
	 * then delegates to the abstract method {@link #populateChildren()}.
	 * 
	 * @author Cristi
	 * @flowerModelElementId _i1pMUcBrEeG5PP70DrXYIQ
	 */
	private void populateTreeNodeInternal(Object source, TreeNode destination, GenericTreeContext context) {
		destination.setPathFragment(getPathFragmentForNode(source, getNodeType(destination), context));
		destination.setHasChildren(nodeHasChildren(source, destination, context));
		populateTreeNode(source, destination, context);
	}

	/**
	 * Returns a path for give node by iterating recursively up through parents.
	 * Recursive method.
	 * 
	 * @see #getParent()
	 * 
	 * @flowerModelElementId _NxT5Y59aEeGYPK0E1LmMXw
	 */
	public List<PathFragment> getPathForNode(Object node, String nodeType, GenericTreeContext context) {		
		List<PathFragment> path = new ArrayList<PathFragment>();
		// when renaming occurs, the node is destroyed and recreated in another thread
		// because if this, there are cases when it comes in this method as null
		if (node == null) {
			return null;
		}
		
		while (node != null && !node.equals(getNodeByPath(null, context))) {
			// search in map
			NodeInfo parentNodeInfo = openNodes.get(node);
			if (parentNodeInfo != null) { // found, get its path fragment
				path.add(0, parentNodeInfo.getPathFragment());
				node = parentNodeInfo.getParent().getNode();
			} else { // not found				
				path.add(0, getPathFragmentForNode(node, nodeType, context));
				node = getParent(node, nodeType, context);
				// TODO: The above operations works only if this service has dispatchEnabled and only one provider registered!
				// TODO: must be modified to support all cases
			}
		}		

		return path;		
	}

	/**
	 * @flowerModelElementId _sjREARN0EeKR8sYuzDGiDQ
	 */
	protected void updateNode(CommunicationChannel channel, String statefulClientId, List<PathFragment> path, TreeNode treeNode, boolean expandNode, boolean colapseNode, boolean selectNode, boolean isContentUpdate) {
		invokeClientMethod(
				channel, 
				statefulClientId, 
				"updateNode", 
				new Object[] {path, treeNode, expandNode, colapseNode, selectNode});		
	}

	/**
	 * @flowerModelElementId _sjdRQRN0EeKR8sYuzDGiDQ
	 */
	public void startInplaceEditor(StatefulServiceInvocationContext context, String contributionId, List<PathFragment> nodePath, Boolean autoCreateElementAfterEditing) { 
		invokeClientMethod(
				context.getCommunicationChannel(), 
				context.getStatefulClientId(), 
				"startInplaceEditor", 
				new Object[] {contributionId, nodePath, autoCreateElementAfterEditing});	
	
	}

	/**
	 * Creates the client tree structure and
	 * modifies the server structure by updating {@link #rootNodeInfo} and {@link #openNodes}.	
	 * 
	 * @return - the {@link TreeNode} created
	 * 
	 * @see #populateChildren()
	 * @see #addNodeInfo()
	 * @flowerModelElementId _G-suoKr2EeG3eZ1Jezjhtw
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
		
		// for dispatched trees, update the server tree structure
		if (isDispatchEnabled(source)) {	
			boolean updateMap = true;
			if (context.get(DONT_UPDATE_MAP_KEY) != null) {
				updateMap = !((Boolean) context.get(DONT_UPDATE_MAP_KEY)).booleanValue();
			}
			if (updateMap) {
				addNodeInfo(channel, statefulClientId, source, getNodeType(treeNode), fullPath == null, true, treeContext, null);
			}
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

	private TreeNode openNodeInternalFromExistingNodes(CommunicationChannel channel, String statefulClientId, List<PathFragment> fullPath, Map<Object, Object> context, Set<String> existingNodes) {
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

	public static Object getNodeByPathFor(List<PathFragment> path, GenericTreeContext context) {
		PathFragment firstNodePath = path.get(0);
		if (NODE_TYPE_ROOT.equals(firstNodePath.getType())) {			
			GenericTreeStatefulService service = 
					(GenericTreeStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(firstNodePath.getName());
			List<PathFragment> pathWithoutRootFragment = path.subList(1, path.size());
			
			return service.getNodeByPath(pathWithoutRootFragment, context);
		}
		return null;
	}
	
	///////////////////////////////////////////////////////////////
	// @RemoteInvocation methods
	///////////////////////////////////////////////////////////////
	
	/**
	 * @flowerModelElementId _y5V30A7rEeKbvNML8mcTuA
	 */
	@RemoteInvocation
	public void subscribe(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState) {
		logger.info("Subscribing to {} with {}", context.getStatefulClientId(), context.getCommunicationChannel());
		
		GenericTreeStatefulClientLocalState localState = (GenericTreeStatefulClientLocalState) statefulClientLocalState;
			
		Set<String> existingNodes = new HashSet<String>();
		
		for (List<PathFragment> path : localState.getOpenNodes()) {
			String fullPath = "";
			for (PathFragment pathFragment : path) {
				if (fullPath != "") {
					fullPath += "/";
				}
				fullPath += pathFragment.getName();
			}			
			existingNodes.add(fullPath);
		}
	
		// set tree context
		GenericTreeContext treeContext = getTreeContext(context.getCommunicationChannel(), context.getStatefulClientId());
		treeContext.setStatefulContext(localState.getStatefulContext());
		treeContext.setClientContext(localState.getClientContext());
		
		if (existingNodes.size() > 0) {			
			TreeNode node = openNodeInternalFromExistingNodes(
					context.getCommunicationChannel(), 
					context.getStatefulClientId(), 
					null, 
					treeContext.getClientContext(), 
					existingNodes);		
			updateNode(context.getCommunicationChannel(), context.getStatefulClientId(), null, node, false, false, false, true);
		}
	}

	/**
	 * @flowerModelElementId _y5dMkA7rEeKbvNML8mcTuA
	 */
	@RemoteInvocation
	public void unsubscribe(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState) {
		logger.info("Unsubscribing from {} with {}", context.getStatefulClientId(), context.getCommunicationChannel());
		if (isDispatchEnabled(null)) {
			cleanupAfterNodeClosed(rootNodeInfo.getNode(), null, context.getStatefulClientId(), context.getCommunicationChannel(), null);
		}
	}

	/**
	 * @flowerModelElementId _CkJxERnzEeKiTvwNCJnHJw
	 */
	@RemoteInvocation
	public boolean performDrop(StatefulServiceInvocationContext context, List<PathFragment> target, List<List<PathFragment>> selectedResources) { 
		return false;
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
	 * For dispatched trees, registers the object in {@link #openNodes} map
	 * and adds it in the list of parent's children (the parent must be already in map).
	 * 
	 * @param siContext
	 * @param fullPath - path of the node that must be opened. 
	 * 				If <code>null</code>, the node is considered to be the root.
	 * @param context - context used to customize the method (e.g. filter for the children list)
	 * 
	 * @see #openNodeInternal()
	 * 
	 * @flowerModelElementId _vAAuAKP8EeGeHqktJlHXmA
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

	/**
	 * Called from client when a node is closed.
	 * Used only for dispatched trees.
	 * 
	 * <p>
	 * Cleans the {@link #openNodes} map.
	 * 
	 * @param context	
	 * @param path - path of the node that must be closed. 
	 * 				If <code>null</code>, the node is considered to be the root.
	 * 
	 * @flowerModelElementId _NxNLsJ9aEeGYPK0E1LmMXw
	 */
	@RemoteInvocation
	public void closeNode(StatefulServiceInvocationContext context, List<PathFragment> path, Map<Object, Object> clientContext) {
		GenericTreeContext treeContext = getTreeContext(context.getCommunicationChannel(), context.getStatefulClientId());
		treeContext.setClientContext(clientContext);
		Object source = getNodeByPath(path, treeContext);
		if (source == null) { // something happened
			throw new RuntimeException("Source node not found for path " + path);
		}
		if (isDispatchEnabled(source)) {
			if (logger.isTraceEnabled()) {
				logger.trace("Closing node with path {} for client [{} with statefulClientId={}]", new Object[] { path, context.getCommunicationChannel(), context.getStatefulClientId() });				
			}
			String nodeType = null;
			if (path != null && path.size() > 0) {
				nodeType = path.get(path.size() - 1).getType();
			}
			cleanupAfterNodeClosed(source, nodeType, context.getStatefulClientId(), context.getCommunicationChannel(), null);
		}
	}

	@RemoteInvocation	
	public boolean updateTreeStatefulContext(StatefulServiceInvocationContext context, String key, Object value) {
		TreeInfoClient treeInfo = new TreeInfoClient(context.getCommunicationChannel(), context.getStatefulClientId());
		GenericTreeContext treeContext = treeContexts.get(treeInfo);
					
		if (treeContext == null) {
			treeContext = new GenericTreeContext();			
		}		
		treeContext.getStatefulContext().put(key, value);
		
		treeContexts.put(treeInfo, treeContext);
		
		return true;
	}

	@Override
	public Object adjustChild(Object originalChild, String nodeType, NodeInfo nodeInfo) {
		return originalChild;
	}
	
	public NodeInfo getNodeInfoForObject(Object node, String nodeType) {
		NodeInfo result = openNodes.get(node);
		if (result != null) {
			// we found the node info for a node that's expanded
			return result;
		}
		
		Object parentNode = getParent(node, nodeType, new GenericTreeContext());
		NodeInfo parentNodeInfo = openNodes.get(parentNode);
		if (parentNodeInfo == null) {
			// parent node not open; this should not happen
			return null;
		}
		
		for (NodeInfo childNodeInfo : parentNodeInfo.getChildren()) {
			if (childNodeInfo.getNode().equals(node)) {
				// we found the node info for a node which is closed
				return childNodeInfo;
			}
		}
		
		// this shouldn't happen either
		return null;
	}
	
}