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
import java.util.List;
import java.util.Map;

import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.editor.remote.EditableResource;
import org.flowerplatform.editor.remote.EditableResourceClient;
import org.flowerplatform.editor.remote.EditorStatefulService;

import com.crispico.flower.mp.codesync.base.CodeSyncEditableResource;
import com.crispico.flower.mp.codesync.base.Match;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;
import com.crispico.flower.mp.codesync.base.action.ActionSynchronize;
import com.crispico.flower.mp.codesync.base.action.DiffAction;
import com.crispico.flower.mp.codesync.base.action.MatchActionAddLeftToRight;
import com.crispico.flower.mp.codesync.base.action.MatchActionAddRightToLeft;
import com.crispico.flower.mp.codesync.base.action.MatchActionRemoveLeft;
import com.crispico.flower.mp.codesync.base.action.MatchActionRemoveRight;

/**
 * @author Mariana
 */
public class CodeSyncEditorStatefulService extends EditorStatefulService {

	public static final String SERVICE_ID = "codeSyncEditorStatefulService";
	
	public static final String CODE_SYNC_EDITOR = "codeSync";
	
	private DiffTreeStatefulService diffTree = new DiffTreeStatefulService();

	public CodeSyncEditorStatefulService() {
		setEditorName("codeSync");
		CommunicationPlugin.getInstance().getCommunicationChannelManager().addCommunicationLifecycleListener(this);
	}
	
	private String getProjectPath(String editableResourcePath) {
		return editableResourcePath;
	}

	@Override
	protected boolean areLocalUpdatesAppliedImmediately() {
		return true;
	}

	@Override
	protected EditableResource createEditableResourceInstance() {
		return new CodeSyncEditableResource();
	}

	@Override
	protected void loadEditableResource(StatefulServiceInvocationContext context, EditableResource editableResource) {
	}

	@Override
	protected void disposeEditableResource(EditableResource editableResource) {
	}

	@Override
	protected void doSave(EditableResource editableResource) {
	}

	@Override
	protected void sendFullContentToClient(EditableResource editableResource, EditableResourceClient client) {
	}

	@Override
	protected void updateEditableResourceContentAndDispatchUpdates(StatefulServiceInvocationContext context, EditableResource editableResource, Object updatesToApply) {
		Runnable runnable = (Runnable) updatesToApply;
		runnable.run();
		
		List<Object> result = null;
		if (updatesToApply instanceof DiffTreeNodeRunnable) {
			DiffTreeNode node = ((DiffTreeNodeRunnable) updatesToApply).nodeToSendToClient;
			Object source = null;
			do {
				List<PathFragment> path = diffTree.getPathForNode(node);
				GenericTreeContext genericTreeContext = diffTree.getTreeContext(context.getCommunicationChannel(), context.getStatefulClientId());
				source = diffTree.getNodeByPath(path, genericTreeContext);
				if (source != null) {
					node = (DiffTreeNode) diffTree.openNodeInternal(context.getCommunicationChannel(), context.getStatefulClientId(), path, genericTreeContext.getClientContext());
					result = new ArrayList<Object>();
					result.add(path);
					result.add(node);
				} else {
					node = (DiffTreeNode) node.getParent();
				}
			} while (source == null && node != null);
		}
		
		for (EditableResourceClient client : editableResource.getClients()) {
			sendContentUpdateToClient(editableResource, client, result, true);
		}
	}

	public Match getMatchForPath(String path) {
		CodeSyncEditableResource er = (CodeSyncEditableResource) getEditableResource(getProjectPath(path));
		if (er != null) {
			return er.getMatch();
		}
		return null;
	}
	
	public ModelAdapterFactorySet getModelAdapterFactorySetForPath(String path) {
		CodeSyncEditableResource er = (CodeSyncEditableResource) getEditableResource(getProjectPath(path));
		if (er != null) {
			return er.getModelAdapterFactorySet();
		}
		return null;
	}
	
	private void synchronize(Match match, ModelAdapterFactorySet set) {
		DiffAction action = null;
		if (Match.MatchType._1MATCH_LEFT.equals(match.getMatchType())) {
			action = new MatchActionAddLeftToRight(false);
		}
		if (Match.MatchType._1MATCH_RIGHT.equals(match.getMatchType())) {
			action = new MatchActionAddRightToLeft(false);
		}
		if (Match.MatchType._2MATCH_ANCESTOR_LEFT.equals(match.getMatchType())) {
			action = new MatchActionRemoveLeft();
		}
		if (Match.MatchType._2MATCH_ANCESTOR_RIGHT.equals(match.getMatchType())) {
			action = new MatchActionRemoveRight();
		}
		
		if (action != null) {
			action.execute(match, -1);
		}
		
		for (Match subMatch : match.getSubMatches()) {
			synchronize(subMatch, set);
		}
		
		ActionSynchronize syncAction = new ActionSynchronize();
		syncAction.execute(match);
	}
	
	private void saveModifications(CodeSyncEditableResource er) {
		saveModifications(er.getMatch(), er.getModelAdapterFactorySet().getLeftFactory(), true);
		saveModifications(er.getMatch(), er.getModelAdapterFactorySet().getRightFactory(), false);
	}
	
	private void saveModifications(Match match, ModelAdapterFactory factory, boolean isLeft) {
		Object lateral = isLeft ? match.getLeft() : match.getRight();
		if (lateral != null) {
			if (factory.getModelAdapter(lateral).save(lateral)) {
				for (Match subMatch : match.getSubMatches()) {
					saveModifications(subMatch, factory, isLeft);
				}
			}
		}
	}
	
	private void discardModifications(CodeSyncEditableResource er) {
		discardModifications(er.getMatch(), er.getModelAdapterFactorySet().getLeftFactory(), true);
		discardModifications(er.getMatch(), er.getModelAdapterFactorySet().getRightFactory(), false);
	}
	
	private void discardModifications(Match match, ModelAdapterFactory factory, boolean isLeft) {
		Object lateral = isLeft ? match.getLeft() : match.getRight();
		if (lateral != null) {
			if (factory.getModelAdapter(lateral).discard(lateral)) {
				for (Match subMatch : match.getSubMatches()) {
					discardModifications(subMatch, factory, isLeft);
				}
			}
		}
	}
	
	///////////////////////////////////////////////////////////////
	// @RemoteInvocation methods
	///////////////////////////////////////////////////////////////
	
	@RemoteInvocation
	public void openNode(StatefulServiceInvocationContext context, DiffTreeNode node, Map<Object, Object> treeContext) {
		diffTree.openNode(context, node, treeContext);
	}
	
	@RemoteInvocation
	public void executeDiffAction(final StatefulServiceInvocationContext context, String editableResourcePath, final int actionType, final int diffIndex, final DiffTreeNode node, final Map<Object, Object> treeContext) {
		attemptUpdateEditableResourceContent(context, getProjectPath(editableResourcePath), new DiffTreeNodeRunnable() {

			@Override
			public void run() {
				this.nodeToSendToClient = node;
				diffTree.executeDiffAction(context, actionType, diffIndex, node, treeContext);
			}
		});
	}
	
	@RemoteInvocation
	public void synchronize(StatefulServiceInvocationContext context, String editableResourcePath) {
		final CodeSyncEditableResource er = (CodeSyncEditableResource) getEditableResource(getProjectPath(editableResourcePath));
		if (er != null) {
			attemptUpdateEditableResourceContent(context, editableResourcePath, new Runnable() {
				
				@Override
				public void run() {
					synchronize(er.getMatch(), er.getModelAdapterFactorySet());
				}
			});
		}
	}
	
	@RemoteInvocation
	public void applySelectedActions(StatefulServiceInvocationContext context, final String editableResourcePath, final boolean notifyClient) {
		final CodeSyncEditableResource er = (CodeSyncEditableResource) getEditableResource(getProjectPath(editableResourcePath));
		if (er != null) {
			attemptUpdateEditableResourceContent(context, editableResourcePath, new Runnable() {
				
				@Override
				public void run() {
					saveModifications(er);
					unsubscribeAllClientsForcefully(getProjectPath(editableResourcePath), notifyClient);
				}
			});
		}
	}
	
	@RemoteInvocation
	public void cancelSelectedActions(String editableResourcePath, final boolean notifyClient) {
		CodeSyncEditableResource er = (CodeSyncEditableResource) getEditableResource(getProjectPath(editableResourcePath));
		if (er != null) {
			discardModifications(er);
			unsubscribeAllClientsForcefully(getProjectPath(editableResourcePath), notifyClient);
		}
	}
	
}
