package org.flowerplatform.web.git.explorer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.git.entity.GitNodeType;
import org.flowerplatform.web.git.entity.RefNode;
import org.flowerplatform.web.git.entity.SimpleNode;

/**
 * @author Cristina Constantienscu
 */
public class RefsChildrenProvider implements IChildrenProvider {

	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {	
		SimpleNode simpleNode = (SimpleNode) node;
		Repository repo = simpleNode.getRepository();
		
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>();
		try {			
			Git git = new Git(repo);
			
			String childType = null;			
			List<Ref> refs = new ArrayList<>();
			
			if (GitNodeType.NODE_TYPE_LOCAL_BRANCHES.equals(simpleNode.getType())) {
				refs = git.branchList().call();
				childType = GitNodeType.NODE_TYPE_LOCAL_BRANCH;			
			} else if (GitNodeType.NODE_TYPE_REMOTE_BRANCHES.equals(simpleNode.getType())) {
				refs = git.branchList().setListMode(ListMode.REMOTE).call();
				childType = GitNodeType.NODE_TYPE_REMOTE_BRANCH;
			} else if (GitNodeType.NODE_TYPE_TAGS.equals(simpleNode.getType())) {
				refs = git.tagList().call();
				childType = GitNodeType.NODE_TYPE_TAG;
			}
			
			Pair<Object, String> child;		
			RefNode childNode;			
			for (Ref ref : refs) {
				childNode = new RefNode(simpleNode, childType, repo, ref);
				child = new Pair<Object, String>(childNode, childType);
				result.add(child);
			}
		} catch (Exception e) {
			// TODO CC: log
			e.printStackTrace();
		}		
		return result;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		SimpleNode simpleNode = (SimpleNode) node;
		Repository repo = simpleNode.getRepository();		
		try {
			Git git = new Git(repo);		
			if (GitNodeType.NODE_TYPE_LOCAL_BRANCHES.equals(simpleNode.getType())) {				
				return git.branchList().call().size() > 0;				
			} else if (GitNodeType.NODE_TYPE_REMOTE_BRANCHES.equals(simpleNode.getType())) {
				return true;
			} else if (GitNodeType.NODE_TYPE_TAGS.equals(simpleNode.getType())) {
				return git.tagList().call().size() > 0;
			}
		} catch (GitAPIException e) {
			// TODO CC: log
		}		
		return false;
	}

}
