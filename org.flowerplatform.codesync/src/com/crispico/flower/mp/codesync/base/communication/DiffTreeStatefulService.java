package com.crispico.flower.mp.codesync.base.communication;


/**
 * @author Mariana
 */
public class DiffTreeStatefulService {
//extends GenericTreeStatefulService implements ICommunicationChannelLifecycleListener {
//
//	public static final String SERVICE_ID = "DiffTreeStatefulService";
//	
//	public static final int TREE_TYPE_DIFF = 0;
//	
//	public static final int TREE_TYPE_ANCESTOR = 1;
//	
//	public static final int TREE_TYPE_LEFT = 2;
//	
//	public static final int TREE_TYPE_RIGHT = 3;
//	
//	public static final String TREE_TYPE = "treeType";
//	
//	public static final String PROJECT_PATH = "projectPath";
//	
//	private MatchModelAdapterUI matchModelAdapterUI = new MatchModelAdapterUI();
//	
//	@Override
//	public String getStatefulClientPrefixId() {
//		return "Diff Tree";
//	}
//
//	@Override
//	public String getLabelForLog(Object node) {
//		return node.toString();
//	}
//
//	@Override
//	public Collection<?> getChildrenForNode(Object node, GenericTreeContext context) {
//		IModelAdapterUI adapter = getModelAdapterUI(node, getTreeTypeFromContext(context), getProjectPathFromContext(context));
//		return adapter.getChildren(node);
//	}
//
//	@Override
//	public Boolean nodeHasChildren(Object node, GenericTreeContext context) {
//		IModelAdapterUI adapter = getModelAdapterUI(node, getTreeTypeFromContext(context), getProjectPathFromContext(context));
//		return adapter.hasChildren(node);
//	}
//
//	@Override
//	public Object getParent(Object node, GenericTreeContext context) {
//		if (node instanceof Match) {
//			return ((Match) node).getParentMatch();
//		}
//		return null;
//	}
//
//	@Override
//	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
//		IModelAdapterUI adapter = getModelAdapterUI(source, getTreeTypeFromContext(context), getProjectPathFromContext(context));
//		destination.setLabel(adapter.getLabel(source));
//		destination.setIconUrls(adapter.getIconUrls(source));
//
//		matchModelAdapterUI.processDiffTreeNode(source, (DiffTreeNode) destination);
//		
//		return true;
//	}
//
//	@Override
//	public Object getNodeByPathFragment(Object parent, PathFragment pathFragment, GenericTreeContext context) {
//		Object node = null;
//		
//		if (parent == null) {
//			Match match = getMatch(getProjectPathFromContext(context));
//			switch (getTreeTypeFromContext(context)) {
//			case TREE_TYPE_DIFF:
//				node = match;
//				break;
//			case TREE_TYPE_ANCESTOR:
//				node = match.getAncestor();
//				break;
//			case TREE_TYPE_LEFT:
//				node = match.getLeft();
//				break;
//			case TREE_TYPE_RIGHT:
//				node = match.getRight();
//				break;
//			default:
//				return null;
//			}
//		} else {
//			IModelAdapterUI adapter = getModelAdapterUI(parent, getTreeTypeFromContext(context), getProjectPathFromContext(context));
//			for (Object child : adapter.getChildren(parent)) {
//				if (getPathFragmentForNode(child, context).equals(pathFragment)) {
//					node = child;
//					break;
//				}
//			}
//		}
//		
//		return node;
//	}
//
//	@Override
//	public PathFragment getPathFragmentForNode(Object node) {
//		throw new UnsupportedOperationException("Cannot provide PathFragment without tree context!");
//	}
//	
//	@Override
//	public PathFragment getPathFragmentForNode(Object node, GenericTreeContext context) {
//		String name = getModelAdapterUI(node, getTreeTypeFromContext(context), getProjectPathFromContext(context)).getLabel(node);
//		return new PathFragment(name, "");
//	}
//	
//	private IModelAdapterUI getModelAdapterUI(Object modelElement, int treeType, String path) {
//		if (modelElement instanceof Match)
//			return matchModelAdapterUI;
//		else {
//			switch (treeType) {
//			case TREE_TYPE_ANCESTOR:
//				return getModelAdapterFactorySet(path).getAncestorFactory().getModelAdapter(modelElement);
//			case TREE_TYPE_LEFT:
//				return getModelAdapterFactorySet(path).getLeftFactory().getModelAdapter(modelElement);
//			case TREE_TYPE_RIGHT:
//				return getModelAdapterFactorySet(path).getRightFactory().getModelAdapter(modelElement);
//			default:
//				return null;
//			}
//		}
//	}
//	
//	@Override
//	public String getInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> fullPath) {
//		return null;
//	}
//
//	@Override
//	public boolean setInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> path, String text) {
//		return false;
//	}
//
//	@Override
//	protected TreeNode createTreeNode() {
//		return new DiffTreeNode();
//	}
//	
//	private int getTreeTypeFromContext(GenericTreeContext context) {
//		if (context == null)
//			return TREE_TYPE_DIFF;
//		return (Integer) context.getClientContext().get(TREE_TYPE);
//	}
//	
//	private String getProjectPathFromContext(GenericTreeContext context) {
//		return (String) context.getClientContext().get(PROJECT_PATH);
//	}
//	
//	@RemoteInvocation
//	public void executeDiffAction(StatefulServiceInvocationContext context, int actionType, int diffIndex, TreeNode node, Map<Object, Object> treeContext) {
//		List<PathFragment> fullPath = getFullPath(node);
//		GenericTreeContext genericTreeContext = getTreeContext(context.getCommunicationChannel(), context.getStatefulClientId());
//		Match match = (Match) getNodeByPath(fullPath, genericTreeContext);
//		Match parentMatch = match.getParentMatch();
//		
//		if (actionType == -1) {
//			ActionSynchronize syncAction = new ActionSynchronize();
//			ActionResult[] results = syncAction.execute(match);
//			for (int i = 0; i < results.length; i++) {
//				Object feature = match.getDiffs().get(i).getFeature();
//				// notify that an action was performed
//				actionPerformed(match, true, false, feature, results[i], genericTreeContext);
//				actionPerformed(match, false, false, feature, results[i], genericTreeContext);
//			}
//			return;
//		}
//		
//		DiffAction diffAction = DiffActionRegistry.ActionType.values()[actionType].diffAction;
//		ActionResult result = diffAction.execute(match, diffIndex);
//		if (diffIndex >= 0) {
//			Diff diff = match.getDiffs().get(diffIndex);
//			actionPerformed(match, true, false, diff.getFeature(), result, genericTreeContext);
//			actionPerformed(match, false, false, diff.getFeature(), result, genericTreeContext);
//		} else {
//			actionPerformed(parentMatch, true, false, match.getFeature(), result, genericTreeContext);
//			actionPerformed(parentMatch, false, false, match.getFeature(), result, genericTreeContext);
//		}
//		
//		if (diffIndex >= 0) // refresh diff flags only if a "diff" action
//			match.refreshDiffFlags(result.conflict, result.modifiedLeft, result.modifiedRight);
//		
//		List<Match> modifiedMatches = new ArrayList<Match>();
//		if (parentMatch != null) // match.getParentMatch() might be null (i.e. the match was removed)
//			modifiedMatches.addAll(match.propagateConflictAndModifiedTrueOrFalse(parentMatch, result.conflict, result.modifiedLeft, result.modifiedRight));
//		
//		if (match.getParentMatch() != null) { // if the match was not deleted ...
//			// even if the match might be unmodified, it's actions
//			// need to be recalculated
//			modifiedMatches.add(match);
//		}
//		
//		if (diffAction instanceof MatchActionRemoveAbstract || diffAction instanceof MatchActionRemoveLeftRight) {
//			Match thoroughUpdateMatch = match.getParentMatch() == null ? parentMatch : match;
//			// for a delete action, remove it from the "shallow" update list, and do a "thorough" update here (i.e. including children)
//			modifiedMatches.remove(thoroughUpdateMatch);
//			dispatchDiffTreeNodeUpdate(context, thoroughUpdateMatch, treeContext);
//		}
//		for (Match modifiedMatch : modifiedMatches) {
//			dispatchDiffTreeNodeUpdate(context, modifiedMatch, treeContext);
//		}
//	}
//	
//	private void actionPerformed(Match match, boolean isLeft, boolean fromParent, Object feature, ActionResult result, GenericTreeContext context) {
//		Match m = fromParent ? match.getParentMatch() : match;
//		Object lateral = isLeft ? m.getLeft() : m.getRight();
//		if (lateral != null) {
//			IModelAdapter adapter = (IModelAdapter) getModelAdapterUI(lateral, isLeft ? TREE_TYPE_LEFT : TREE_TYPE_RIGHT, getProjectPathFromContext(context));
//			adapter.actionPerformed(lateral, feature, result);
//		}
//	}
//	
//	private void dispatchDiffTreeNodeUpdate(StatefulServiceInvocationContext context, Object source, Map<Object, Object> treeContext) {
//		List<PathFragment> path = getPathForNode(source, getTreeContext(context.getCommunicationChannel(), context.getStatefulClientId()));
//		TreeNode node = openNodeInternal(context.getCommunicationChannel(), context.getStatefulClientId(), path.size() > 0 ? path : null, treeContext);
//		updateNode(context.getCommunicationChannel(), context.getStatefulClientId(), path, node, false, false, false, true);
//	}
//	
//	private List<PathFragment> getFullPath(TreeNode node) {
//		List<PathFragment> path = new ArrayList<PathFragment>();
//		while (node != null && node.getPathFragment() != null) {
//			path.add(0, node.getPathFragment());
//			node = node.getParent();
//		}
//		return path;
//	}
//
//	private Match getMatch(String path) {
//		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) ServiceRegistry.INSTANCE.getService(CodeSyncEditorStatefulService.SERVICE_ID);
//		return service.getMatchForPath(path);
//	}
//	
//	private ModelAdapterFactorySet getModelAdapterFactorySet(String path) {
//		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) ServiceRegistry.INSTANCE.getService(CodeSyncEditorStatefulService.SERVICE_ID);
//		return service.getModelAdapterFactorySetForPath(path);
//	}
//	
//	@Override
//	public void webCommunicationChannelCreated(CommunicationChannel webCommunicationChannel) {
//		// nothing to do
//	}
	
}
