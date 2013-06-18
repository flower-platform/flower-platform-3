package org.flowerplatform.web.git.entity;

import org.eclipse.jgit.lib.Repository;

/**
 * @author Cristina Constantienscu
 */
public class RemoteNode extends GitNode<String> {

	public RemoteNode(SimpleNode parent, Repository repository,	String remoteName) {
		super(parent, GitNodeType.NODE_TYPE_REMOTE, repository, remoteName);
	}
	
}
