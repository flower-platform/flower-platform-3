package org.flowerplatform.codesync.code.javascript.remote;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.code.CodeSyncCodePlugin;
import com.crispico.flower.mp.codesync.code.adapter.FolderModelAdapter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Cristina Constatinescu
 */
public class LocationForNewElementsStatefulService extends GenericTreeStatefulService {
	
	public static final String DIAGRAM_EDITABLE_RESOURCE_PATH_KEY = "diagramEditableResourcePath";
	public static final String NEW_ELEMENTS_PATH_KEY = "newElementsPath";
	public static final String ADDITIONAL_PATH_KEY = "additionalPath";
	public static final String PATHS_TO_OPEN_KEY = "pathsToOpen";
	
	@Override
	public String getStatefulClientPrefixId() {		
		return "Location For New Elements Tree";
	}

	private DiagramEditorStatefulService getDiagramEditorStatefulService() {
		return (DiagramEditorStatefulService) EditorPlugin.getInstance().getEditorStatefulServiceByEditorName("diagram");
	}
			
	@Override
	public void openNode(StatefulServiceInvocationContext context, List<PathFragment> path, Map<Object, Object> clientContext) {		
		super.openNode(context, path, clientContext);
		
		invokeClientMethod(
				context.getCommunicationChannel(), 
				context.getStatefulClientId(), 
				"setAdditionalInfo", 
				new Object[] {clientContext.get(ADDITIONAL_PATH_KEY), clientContext.get(PATHS_TO_OPEN_KEY)});
	}

	@Override
	public TreeNode openNodeInternal(CommunicationChannel channel, String statefulClientId, List<PathFragment> fullPath, Map<Object, Object> context) {
		TreeNode node = super.openNodeInternal(channel, statefulClientId, fullPath, context);
		
		String newElementsPath = (String) context.get(NEW_ELEMENTS_PATH_KEY);
		if (newElementsPath != null) {
			String[] tokens = newElementsPath.split("/");
			int i = 0;
			TreeNode startNode = node;
			String additionalPath = "";		
			List<List<PathFragment>> pathsToOpen = new ArrayList<List<PathFragment>>();
			while (i < tokens.length) {
				TreeNode tokenNode = pathFragmentExistsInTreeNode(startNode, new PathFragment(tokens[i], FolderModelAdapter.FOLDER));
				if (tokenNode != null) {
					List<PathFragment> path = new ArrayList<PathFragment>();
					computePathForNode(tokenNode, path);	
					pathsToOpen.add(path);
					startNode = tokenNode;
				} else {
					additionalPath += tokens[i] + "/";
				}
				i++;
			}
			context.put(ADDITIONAL_PATH_KEY, additionalPath);
			context.put(PATHS_TO_OPEN_KEY, pathsToOpen);			
		}
		return node;
	}
	
	
	
	private void computePathForNode(TreeNode node, List<PathFragment> path) {
		if (node.getPathFragment() != null) {
			path.add(0, node.getPathFragment());
		}
		if (node.getParent() != null) {
			computePathForNode(node.getParent(), path);
		}
	}
	
	private TreeNode pathFragmentExistsInTreeNode(TreeNode node, PathFragment pathFragment) {
		if (node.getPathFragment() != null && node.getPathFragment().equals(pathFragment)) {
			return node;
		}
		for (TreeNode child : node.getChildren()) {
			if (pathFragmentExistsInTreeNode(child, pathFragment) != null) {
				return child;
			}
		}
		return null;
	}

	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		String diagramEditableResourcePath = (String) context.getClientContext().get(DIAGRAM_EDITABLE_RESOURCE_PATH_KEY);
				
		if (node instanceof Class) {			
			DiagramEditableResource der = (DiagramEditableResource) getDiagramEditorStatefulService().getEditableResource(diagramEditableResourcePath);
			ResourceSet resourceSet = der.getResourceSet();
			File project = CodeSyncPlugin.getInstance().getProjectsProvider().getContainingProjectForFile((File) der.getFile());
			Resource codeSyncMappingResource = CodeSyncCodePlugin.getInstance().getCodeSyncMapping(project, resourceSet);
			
			List<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>();			
			for (EObject eObject : codeSyncMappingResource.getContents()) {
				String type = ((CodeSyncElement) eObject).getType();
				if (type.equals(FolderModelAdapter.FOLDER)) {
					Pair<Object, String> pair = new Pair<Object, String>(eObject, type);				
					result.add(pair);
				}
			}
			return result;
		} else {
			CodeSyncElement cse = (CodeSyncElement) node;
			List<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>();
			for (CodeSyncElement child : cse.getChildren()) {
				if (child.getType().equals(FolderModelAdapter.FOLDER)) {
					Pair<Object, String> pair = new Pair<Object, String>(child, child.getType());
					result.add(pair);
				}
			}
			return result;
		}
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		CodeSyncElement cse = (CodeSyncElement) node;
		if (cse.getChildren() != null && cse.getChildren().size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		if (source instanceof CodeSyncElement) {
			destination.setLabel(((CodeSyncElement) source).getName());			
		}
		return false;
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		return new PathFragment(((CodeSyncElement) node).getName(), ((CodeSyncElement) node).getType());
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {		
		return node.toString();
	}

	@Override
	public String getInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> fullPath) {		
		// doesn't support this
		return null;
	}

	@Override
	public boolean setInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> path, String text) {
		// doesn't support this
		return false;
	}
	
}
