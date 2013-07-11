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
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.INodeByPathRetriever;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.NodeInfo;
import org.flowerplatform.communication.tree.NodeInfoClient;
import org.flowerplatform.communication.tree.TreeInfoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service used to provide functionality for generic dispatched trees -> shows and updates tree data by dispatching notifications to 
 * a list of subscribed clients.
 * 
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
public abstract class GenericTreeStatefulService extends AbstractTreeStatefulService implements INodeInfoStatefulServiceMXBean, IChildrenProvider, INodeDataProvider, INodeByPathRetriever {
	
	public final static Object ROOT_NODE_MARKER = GenericTreeStatefulService.class;
		
	/**
	 * NO USED
	 * Adding this key to <code>context</code> will retrieve the node's 
	 * path by going up the {@link NodeInfo} hierarchy, instead of using
	 * {@link #getParent(Object)}. Should be used when the node was deleted
	 * to ensure that it will be removed from {@link #visibleNodes} map, for
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
	protected Map<Object, NodeInfo> visibleNodes = new ConcurrentHashMap<Object, NodeInfo>();
	
	/**
	 * Holds the tree structure of all displayed nodes on clients (opened or not).
	 * 
	 * @flowerModelElementId _pXDLkKD1EeG5ENNne79MAQ
	 */
	private NodeInfo rootNodeInfo;
		
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
		
		for (NodeInfo node : visibleNodes.values()) {			
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
		for (NodeInfo node : visibleNodes.values()) {	
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
		for (NodeInfo nodeInfo : visibleNodes.values()) {		
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

	public GenericTreeStatefulService() {
		super();
		rootNodeInfo = new NodeInfo();
		rootNodeInfo.setNode(ROOT_NODE_MARKER);
		visibleNodes.put(ROOT_NODE_MARKER, rootNodeInfo);				
	}
	
	public Map<Object, NodeInfo> getVisibleNodes() {
		return visibleNodes;
	}

	
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

		// get the root node
		nodeInfo = rootNodeInfo;
		
		if (fullPath != null) {
			for (PathFragment pathFragment : fullPath) {
				// hold the parent
				parentInfo = nodeInfo;
				// this will be filled if node found
				nodeInfo = null;

				for (NodeInfo child : parentInfo.getChildren()) {		
					if (child.getPathFragment().getName().equals(pathFragment.getName()) && 
						child.getPathFragment().getType().equals(pathFragment.getType())) {
						
						nodeInfo = child;
						break;
					}
				}	
				
				if (nodeInfo == null) {
					return null;
				}
			}
		}
		return nodeInfo.getNode();
	}

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
	
	/**
	 * Adds NodeInfo in map + in the parent's NodeInfo.children.
	 * 
	 * <p>
	 * At the end, subscribe the given channel and clientId to node.
	 * 
	 * @flowerModelElementId _G-7_NKr2EeG3eZ1Jezjhtw
	 */
	private void addNodeInfo(CommunicationChannel channel, String statefulClientId, Object parent, Object node, String nodeType, GenericTreeContext context) {
		boolean updateMap = true;
		if (context.get(DONT_UPDATE_MAP_KEY) != null) {
			updateMap = !((Boolean) context.get(DONT_UPDATE_MAP_KEY)).booleanValue();
		}
		if (!updateMap) {
			return;
		}
		
		NodeInfo nodeInfo = visibleNodes.get(node);
		if (nodeInfo == null) {	
			// the node was not yet visible to the client
			namedLockPool.lock(node);	
			try {
				// get parent open node
				NodeInfo parentInfo = visibleNodes.get(parent); // parent must be opened
				if (parentInfo == null) {
					logger.error("Parent info for {} was already closed!", parent);
					return;
				}

				// create new open node entry and populate it
				nodeInfo = new NodeInfo();
				nodeInfo.setNode(node);
				nodeInfo.setParent(parentInfo);
				nodeInfo.setPathFragment(getPathFragmentForNode(node, nodeType, context));					
				
				parentInfo.getChildren().add(nodeInfo);	
				visibleNodes.put(node, nodeInfo);
				logger.trace("Node {} added to openNodes", getLabelForLog(node, nodeType));	
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
		NodeInfo nodeInfo = visibleNodes.get(node); // check if opened
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
		NodeInfo nodeInfo = visibleNodes.get(node);
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
		
		NodeInfo nodeInfo = visibleNodes.get(node);
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
			cleanupAfterNodeClosed(oldChild, oldChild.getPathFragment().getType(), nodeClient.getStatefulClientId(this), nodeClient.getCommunicationChannel(), null, true);
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
		NodeInfo nodeInfo = visibleNodes.get(node);
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
		// adding this key will ensure that the correct path will be found using the NodeInfo2 hierarchy
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

	/**
	 * Cleans up the {@link #visibleNodes} and {@link #rootNodeInfo}.
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
	protected void cleanupAfterNodeClosed(Object node, String nodeType, String statefulClientId, CommunicationChannel channel, RunnableWithParam<Void, NodeInfo> removeNodeInfoRunnable, final boolean removeFromMapAndParentAndUnsubscribe) {
		if (logger.isTraceEnabled()) {
			logger.trace("Cleanup node {} to client [{} with statefulClientId={}]", new Object[] { getLabelForLog(node, nodeType), channel, statefulClientId });			
		}

		NodeInfo nodeInfo = (NodeInfo) ((node instanceof NodeInfo) ? node : visibleNodes.get(node));
		if (nodeInfo == null) {
			if (rootNodeInfo.getNode().equals(node)) {
				nodeInfo = rootNodeInfo;
			} else {
				// CS: cf. remarca mm
				return;
//				NodeInfo2 parentNodeInfo = openNodes.get(getParent(node, getTreeContext(channel,statefulClientId)));
//				for (NodeInfo2 child : parentNodeInfo.getChildren()) {
//					if (node.equals(child.getNode())) {
//						nodeInfo = child;
//						break;
//					}
//				}
			}
		}		
		if (visibleNodes.containsKey(nodeInfo.getNode())) { // open node
			for (final Iterator<NodeInfo> it = nodeInfo.getChildren().iterator(); it.hasNext();) {	
				NodeInfo childInfo = it.next();			
				NodeInfoClient childNodeInfoClient = childInfo.getNodeInfoClientByCommunicationChannelThreadSafe(channel, statefulClientId, this);
				if (childNodeInfoClient != null) {
					cleanupAfterNodeClosed(childInfo, childInfo.getPathFragment().getType(), statefulClientId, channel, new RunnableWithParam<Void, NodeInfo>() {
						
						public Void run(NodeInfo nodeInfo) {
							logger.debug("Removing node from visibleNodes & parent node {}", nodeInfo);
							visibleNodes.remove(nodeInfo.getNode());							
							it.remove();
							return null;
						}
					}, true);		
				}
			}
		}
			
		boolean removeOpenNode = false;
		if (channel == null) { // no channel, no open node, mark to be deleted
			removeOpenNode = true;			
		} else if (removeFromMapAndParentAndUnsubscribe) {
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
				logger.trace("Removing client = {} to NodeInfo2 with path = {}. Now there are {} clients subscribed to this resource.", new Object[] {channel, nodeInfo.getPathFragment(), nodeInfo.getClients().size() });
			}
			
			if (nodeInfo.getClients().size() == 0) { // no other clients, mark to be deleted
				removeOpenNode = true;
			}		
		}
		
		if (removeOpenNode && nodeInfo != rootNodeInfo) {
			// don't delete the rootNodeInfo
			namedLockPool.lock(nodeInfo.getNode());
			try {
				if (logger.isTraceEnabled()) {
					logger.trace("Removing open node {} for statefulClientId={}]", new Object[] { getLabelForLog(node, nodeType), statefulClientId });			
				}
				
				if (removeNodeInfoRunnable == null) {
					removeNodeInfoRunnable = new RunnableWithParam<Void, NodeInfo>() {				
						public Void run(NodeInfo nodeInfo) {
							logger.debug("Removing node from openNodes {}", nodeInfo);
							// remove from parent
							nodeInfo.getParent().getChildren().remove(nodeInfo);
							// remove from map
							visibleNodes.remove(nodeInfo.getNode());					
							return null;
						}
					};
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
	 * This method might be used for trees that send all the data at the beginning (i.e. no
	 * more openNode/closeNode afterwards).
	 * 
	 * @param recurse - if <code>true</code>, creates the whole tree structure for given node.
	 * 					Otherwise creates only its direct children.
	 * @flowerModelElementId _G-y1RKr2EeG3eZ1Jezjhtw
	 */
	protected void populateChildren(CommunicationChannel channel, String statefulClientId, Object node, TreeNode treeNode, GenericTreeContext context, boolean recurse) {
		// create and populate the children list
		for (Pair<Object, String> pair : getChildrenForNode(node, treeNode, context)) {
			Object child = pair.a;
			
			addNodeInfo(channel, statefulClientId, node, child, pair.b, context);
			
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
	 * Returns a path for give node by iterating recursively up through parents.
	 * Recursive method.
	 * 
	 * @see #getParent()
	 * 
	 * @flowerModelElementId _NxT5Y59aEeGYPK0E1LmMXw
	 */
	public List<PathFragment> getPathForNode(Object node, String nodeType, GenericTreeContext context) {		
		List<PathFragment> path = new ArrayList<PathFragment>();
		
		NodeInfo nodeInfo = visibleNodes.get(node);
		
		while (nodeInfo != null && !nodeInfo.equals(rootNodeInfo)) {
			path.add(0, nodeInfo.getPathFragment());
			nodeInfo = nodeInfo.getParent();
		}		

		return path;		
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

	public static Object getNodeByPathFor(List<PathFragment> path, GenericTreeContext context) {
		GenericTreeStatefulService service = getServiceFromPathWithRoot(path);		
		if (service != null) {			
			List<PathFragment> pathWithoutRootFragment = path.subList(1, path.size());			
			return service.getNodeByPath(pathWithoutRootFragment, context);
		}
		return null;
	}
	
	public static GenericTreeStatefulService getServiceFromPathWithRoot(List<PathFragment> path) {
		PathFragment firstNodePath = path.get(0);
		if (NODE_TYPE_ROOT.equals(firstNodePath.getType())) {			
			return (GenericTreeStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(firstNodePath.getName());			
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
		cleanupAfterNodeClosed(rootNodeInfo.getNode(), null, context.getStatefulClientId(), context.getCommunicationChannel(), null, true);
	}

	/**
	 * @flowerModelElementId _CkJxERnzEeKiTvwNCJnHJw
	 */
	@RemoteInvocation
	public boolean performDrop(StatefulServiceInvocationContext context, List<PathFragment> target, List<List<PathFragment>> selectedResources) { 
		return false;
	}

	/**
	 * Called from client when a node is closed.
	 * Used only for dispatched trees.
	 * 
	 * <p>
	 * Cleans the {@link #visibleNodes} map.
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
		if (logger.isTraceEnabled()) {
			logger.trace("Closing node with path {} for client [{} with statefulClientId={}]", new Object[] { path, context.getCommunicationChannel(), context.getStatefulClientId() });				
		}
		String nodeType = null;
		if (path != null && path.size() > 0) {
			nodeType = path.get(path.size() - 1).getType();
		}
		cleanupAfterNodeClosed(source, nodeType, context.getStatefulClientId(), context.getCommunicationChannel(), null, false);
	}
	
}