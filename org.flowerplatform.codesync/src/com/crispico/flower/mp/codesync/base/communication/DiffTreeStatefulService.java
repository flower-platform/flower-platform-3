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
package com.crispico.flower.mp.codesync.base.communication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.channel.ICommunicationChannelLifecycleListener;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;

import com.crispico.flower.mp.codesync.base.Diff;
import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.IModelAdapterUI;
import com.crispico.flower.mp.codesync.base.Match;
import com.crispico.flower.mp.codesync.base.MatchModelAdapterUI;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;
import com.crispico.flower.mp.codesync.base.action.ActionResult;
import com.crispico.flower.mp.codesync.base.action.ActionSynchronize;
import com.crispico.flower.mp.codesync.base.action.DiffAction;
import com.crispico.flower.mp.codesync.base.action.DiffActionRegistry;
import com.crispico.flower.mp.codesync.base.action.MatchActionRemoveAbstract;
import com.crispico.flower.mp.codesync.base.action.MatchActionRemoveLeftRight;


/**
 * @author Mariana
 */
public class DiffTreeStatefulService extends GenericTreeStatefulService implements ICommunicationChannelLifecycleListener {

	public static final String SERVICE_ID = "DiffTreeStatefulService";
	
	public static final int TREE_TYPE_DIFF = 0;
	
	public static final int TREE_TYPE_ANCESTOR = 1;
	
	public static final int TREE_TYPE_LEFT = 2;
	
	public static final int TREE_TYPE_RIGHT = 3;
	
	public static final String TREE_TYPE = "treeType";
	
	public static final String PROJECT_PATH = "projectPath";
	
	private MatchModelAdapterUI matchModelAdapterUI = new MatchModelAdapterUI();
	
	@Override
	public String getStatefulClientPrefixId() {
		return "Diff Tree";
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {
		return node.toString();
	}
	
	public void openNode(StatefulServiceInvocationContext context, DiffTreeNode node, Map<Object, Object> treeContext) {
		openNode(context, getPathForNode(node), treeContext);
	}
	
	public List<PathFragment> getPathForNode(DiffTreeNode node) {
		List<PathFragment> path = new ArrayList<PathFragment>();
		TreeNode crtNode = node;
		while (crtNode != null) {
			path.add(0, crtNode.getPathFragment());
			crtNode = crtNode.getParent();
		}
		return path;
	}

	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		IModelAdapterUI adapter = getModelAdapterUI(node, getTreeTypeFromContext(context), getProjectPathFromContext(context));
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>();
		for (Object child : adapter.getChildren(node)) {
			result.add(new Pair<Object, String>(child, String.valueOf(getTreeTypeFromContext(context))));
		}
		return result;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		IModelAdapterUI adapter = getModelAdapterUI(node, getTreeTypeFromContext(context), getProjectPathFromContext(context));
		return adapter.hasChildren(node);
	}

//	public Object getParent(Object node, GenericTreeContext context) {
//		if (node instanceof Match) {
//			return ((Match) node).getParentMatch();
//		}
//		return null;
//	}

	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		IModelAdapterUI adapter = getModelAdapterUI(source, getTreeTypeFromContext(context), getProjectPathFromContext(context));
		destination.setLabel(adapter.getLabel(source));
//		destination.setIcon((adapter.getIconUrls(source)));

		matchModelAdapterUI.processDiffTreeNode(source, (DiffTreeNode) destination);
		
		return true;
	}

	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {
		Object node = null;
		for (PathFragment fragment : fullPath) {
			node = getNodeByPathFragment(node, fragment, context);
		}
		return node;
	}

	public Object getNodeByPathFragment(Object parent, PathFragment pathFragment, GenericTreeContext context) {
		Object node = null;
		
		if (parent == null) {
			Match match = getMatch(getProjectPathFromContext(context));
			switch (getTreeTypeFromContext(context)) {
			case TREE_TYPE_DIFF:
				node = match;
				break;
			case TREE_TYPE_ANCESTOR:
				node = match.getAncestor();
				break;
			case TREE_TYPE_LEFT:
				node = match.getLeft();
				break;
			case TREE_TYPE_RIGHT:
				node = match.getRight();
				break;
			default:
				return null;
			}
		} else {
			IModelAdapterUI adapter = getModelAdapterUI(parent, getTreeTypeFromContext(context), getProjectPathFromContext(context));
			for (Object child : adapter.getChildren(parent)) {
				if (getPathFragmentForNode(child, null, context).equals(pathFragment)) {
					node = child;
					break;
				}
			}
		}
		
		return node;
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		String name = getModelAdapterUI(node, getTreeTypeFromContext(context), getProjectPathFromContext(context)).getLabel(node);
		return new PathFragment(name, String.valueOf(getTreeTypeFromContext(context)));
	}
	
	private IModelAdapterUI getModelAdapterUI(Object modelElement, int treeType, String path) {
		if (modelElement instanceof Match)
			return matchModelAdapterUI;
		else {
			switch (treeType) {
			case TREE_TYPE_DIFF:
				return matchModelAdapterUI;
			case TREE_TYPE_ANCESTOR:
				return getModelAdapterFactorySet(path).getAncestorFactory().getModelAdapter(modelElement);
			case TREE_TYPE_LEFT:
				return getModelAdapterFactorySet(path).getLeftFactory().getModelAdapter(modelElement);
			case TREE_TYPE_RIGHT:
				return getModelAdapterFactorySet(path).getRightFactory().getModelAdapter(modelElement);
			default:
				return null;
			}
		}
	}
	
	@Override
	public String getInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> fullPath) {
		return null;
	}

	@Override
	public boolean setInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> path, String text) {
		return false;
	}

	@Override
	protected TreeNode createTreeNode() {
		return new DiffTreeNode();
	}
	
	private int getTreeTypeFromContext(GenericTreeContext context) {
		if (context == null)
			return TREE_TYPE_DIFF;
		return (Integer) context.getClientContext().get(TREE_TYPE);
	}
	
	private String getProjectPathFromContext(GenericTreeContext context) {
		return (String) context.getClientContext().get(PROJECT_PATH);
	}
	
	@RemoteInvocation
	public void executeDiffAction(StatefulServiceInvocationContext context, int actionType, int diffIndex, TreeNode node, Map<Object, Object> treeContext) {
		List<PathFragment> fullPath = getFullPath(node);
		GenericTreeContext genericTreeContext = getTreeContext(context.getCommunicationChannel(), context.getStatefulClientId());
		Match match = (Match) getNodeByPath(fullPath, genericTreeContext);
		Match parentMatch = match.getParentMatch();
		
		if (actionType == -1) {
			ActionSynchronize syncAction = new ActionSynchronize();
			ActionResult[] results = syncAction.execute(match);
			for (int i = 0; i < results.length; i++) {
				Object feature = match.getDiffs().get(i).getFeature();
				// notify that an action was performed
				actionPerformed(match, true, false, feature, results[i], genericTreeContext);
				actionPerformed(match, false, false, feature, results[i], genericTreeContext);
			}
			return;
		}
		
		DiffAction diffAction = DiffActionRegistry.ActionType.values()[actionType].diffAction;
		ActionResult result = diffAction.execute(match, diffIndex);
		if (diffIndex >= 0) {
			Diff diff = match.getDiffs().get(diffIndex);
			actionPerformed(match, true, false, diff.getFeature(), result, genericTreeContext);
			actionPerformed(match, false, false, diff.getFeature(), result, genericTreeContext);
		} else {
			actionPerformed(parentMatch, true, false, match.getFeature(), result, genericTreeContext);
			actionPerformed(parentMatch, false, false, match.getFeature(), result, genericTreeContext);
		}
		
		if (diffIndex >= 0) // refresh diff flags only if a "diff" action
			match.refreshDiffFlags(result.conflict, result.modifiedLeft, result.modifiedRight);
		
		List<Match> modifiedMatches = new ArrayList<Match>();
		if (parentMatch != null) // match.getParentMatch() might be null (i.e. the match was removed)
			modifiedMatches.addAll(match.propagateConflictAndModifiedTrueOrFalse(parentMatch, result.conflict, result.modifiedLeft, result.modifiedRight));
		
		if (match.getParentMatch() != null) { // if the match was not deleted ...
			// even if the match might be unmodified, it's actions
			// need to be recalculated
			modifiedMatches.add(match);
		}
		
		if (diffAction instanceof MatchActionRemoveAbstract || diffAction instanceof MatchActionRemoveLeftRight) {
			Match thoroughUpdateMatch = match.getParentMatch() == null ? parentMatch : match;
			// for a delete action, remove it from the "shallow" update list, and do a "thorough" update here (i.e. including children)
			modifiedMatches.remove(thoroughUpdateMatch);
//			dispatchDiffTreeNodeUpdate(context, thoroughUpdateMatch, treeContext);
		}
		for (Match modifiedMatch : modifiedMatches) {
//			dispatchDiffTreeNodeUpdate(context, modifiedMatch, treeContext);
		}
	}
	
	private void actionPerformed(Match match, boolean isLeft, boolean fromParent, Object feature, ActionResult result, GenericTreeContext context) {
		Match m = fromParent ? match.getParentMatch() : match;
		Object lateral = isLeft ? m.getLeft() : m.getRight();
		if (lateral != null) {
			IModelAdapter adapter = (IModelAdapter) getModelAdapterUI(lateral, isLeft ? TREE_TYPE_LEFT : TREE_TYPE_RIGHT, getProjectPathFromContext(context));
			adapter.actionPerformed(lateral, feature, result);
		}
	}
	
	private void dispatchDiffTreeNodeUpdate(StatefulServiceInvocationContext context, Object source, Map<Object, Object> treeContext) {
		List<PathFragment> path = getPathForNode(source, null, getTreeContext(context.getCommunicationChannel(), context.getStatefulClientId()));
		TreeNode node = openNodeInternal(context.getCommunicationChannel(), context.getStatefulClientId(), path.size() > 0 ? path : null, treeContext);
		updateNode(context.getCommunicationChannel(), context.getStatefulClientId(), path, node, false, false, false, true);
	}
	
	@Override
	protected void updateNode(CommunicationChannel channel, String statefulClientId, List<PathFragment> path, TreeNode treeNode, boolean expandNode, boolean colapseNode, boolean selectNode, boolean isContentUpdate) {
		invokeClientMethod(
				channel, 
				statefulClientId, 
				"updateNode", 
				new Object[] {Integer.parseInt(treeNode.getPathFragment().getType()), path, treeNode, expandNode, colapseNode, selectNode});		
	}

	private List<PathFragment> getFullPath(TreeNode node) {
		List<PathFragment> path = new ArrayList<PathFragment>();
		while (node != null && node.getPathFragment() != null) {
			path.add(0, node.getPathFragment());
			node = node.getParent();
		}
		return path;
	}

	private Match getMatch(String path) {
		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
		return service.getMatchForPath(path);
	}
	
	private ModelAdapterFactorySet getModelAdapterFactorySet(String path) {
		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
		return service.getModelAdapterFactorySetForPath(path);
	}
	
	@Override
	public void communicationChannelCreated(CommunicationChannel webCommunicationChannel) {
		// nothing to do
	}
	
}