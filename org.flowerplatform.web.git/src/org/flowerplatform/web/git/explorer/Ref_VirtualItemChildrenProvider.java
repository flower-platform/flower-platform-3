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
import org.flowerplatform.web.git.GitNodeType;
import org.flowerplatform.web.git.explorer.entity.RefNode;

/**
 * Parent node = Virtual node (Local Branches, Remote Branches, Tags) (i.e. Pair<Repository, nodeType>).<br/>
 * Child node = refs, i.e. {@link Ref}
 * 
 * @author Cristina Constantinescu
 */
public class Ref_VirtualItemChildrenProvider implements IChildrenProvider {

	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {	
		@SuppressWarnings("unchecked")
		Repository repository = ((Pair<Repository, String>) node).a;
		String nodeType = treeNode.getPathFragment().getType();
		
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>();
		try {			
			Git git = new Git(repository);
			
			String childType = null;			
			List<Ref> refs = new ArrayList<>();
			
			if (GitNodeType.NODE_TYPE_LOCAL_BRANCHES.equals(nodeType)) {
				refs = git.branchList().call();
				childType = GitNodeType.NODE_TYPE_LOCAL_BRANCH;			
			} else if (GitNodeType.NODE_TYPE_REMOTE_BRANCHES.equals(nodeType)) {
				refs = git.branchList().setListMode(ListMode.REMOTE).call();
				childType = GitNodeType.NODE_TYPE_REMOTE_BRANCH;
			} else if (GitNodeType.NODE_TYPE_TAGS.equals(nodeType)) {
				refs = git.tagList().call();
				childType = GitNodeType.NODE_TYPE_TAG;
			}
			
			Pair<Object, String> child;	
	
			for (Ref ref : refs) {			
				child = new Pair<Object, String>(new RefNode(repository, ref), childType);
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
		@SuppressWarnings("unchecked")
		Repository repository = ((Pair<Repository, String>) node).a;
		String nodeType = treeNode.getPathFragment().getType();
		
		try {
			Git git = new Git(repository);		
			if (GitNodeType.NODE_TYPE_LOCAL_BRANCHES.equals(nodeType)) {				
				return git.branchList().call().size() > 0;				
			} else if (GitNodeType.NODE_TYPE_REMOTE_BRANCHES.equals(nodeType)) {
				return true;
			} else if (GitNodeType.NODE_TYPE_TAGS.equals(nodeType)) {
				return git.tagList().call().size() > 0;
			}
		} catch (GitAPIException e) {
			// TODO CC: log
		}		
		return false;
	}

}