package org.flowerplatform.web.git.explorer;

import java.io.File;
import java.util.List;

import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeDataProvider;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.git.GitPlugin;
import org.flowerplatform.web.git.entity.FileNode;
import org.flowerplatform.web.git.entity.FolderNode;
import org.flowerplatform.web.git.entity.GitNode;
import org.flowerplatform.web.git.entity.GitNodeType;
import org.flowerplatform.web.git.entity.RepositoryNode;
import org.flowerplatform.web.git.entity.SimpleNode;
import org.flowerplatform.web.git.entity.WorkingDirNode;

/**
 * @author Cristina Constantienscu
 */
public class WorkingDirNodeDataProvider implements INodeDataProvider {

	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		@SuppressWarnings("rawtypes")
		GitNode gitNode = (GitNode) source;
		destination.setLabel(((File) gitNode.getObject()).getName());
		
		String icon;
		if (source instanceof FileNode) {
			icon = "images/file.gif";
		} else {
			icon = "images/folder.gif";
		}
		destination.setIcon(WebPlugin.getInstance().getResourceUrl(icon));
		return true;
	}
	
	@Override
	public Object getParent(Object node, String nodeType, GenericTreeContext context) {
		@SuppressWarnings("rawtypes")
		GitNode gitNode = (GitNode) node;
		return gitNode.getParent();
	}

	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		@SuppressWarnings("rawtypes")
		GitNode gitNode = (GitNode) node;
		return new PathFragment(((File) gitNode.getObject()).getName(), nodeType);
	}

	@Override
	public Object getNodeByPathFragment(Object parent,	PathFragment pathFragment, GenericTreeContext context) {
		if (GitNodeType.NODE_TYPE_WDIR.equals(pathFragment.getType())) {
			SimpleNode simpleNode = (SimpleNode) parent;
			RepositoryNode repoNode = (RepositoryNode) simpleNode.getParent();
			
			File wdirFile = new File(repoNode.getObject(), pathFragment.getName());
			Repository repo = GitPlugin.getInstance().getUtils().getRepository(wdirFile);
			
			if (repo != null) {
				return new WorkingDirNode(simpleNode, repo, wdirFile);				
			}			
		} else if (GitNodeType.NODE_TYPE_FOLDER.equals(pathFragment.getType())) {
			@SuppressWarnings("rawtypes")
			GitNode gitNode = (GitNode) parent;
			return new FolderNode(parent, gitNode.getRepository(), new File((File) gitNode.getObject(), pathFragment.getName()));
		} else {
			@SuppressWarnings("rawtypes")
			GitNode gitNode = (GitNode) parent;
			return new FileNode(parent, gitNode.getRepository(), new File((File) gitNode.getObject(), pathFragment.getName()));
		}
		return null;
	}

	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {
		return null;
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {
		@SuppressWarnings("rawtypes")
		GitNode gitNode = (GitNode) node;
		return ((File) gitNode.getObject()).getName();
	}

	@Override
	public String getInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> fullPath) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> path, String text) {
		throw new UnsupportedOperationException();
	}

}
