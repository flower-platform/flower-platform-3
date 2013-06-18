package org.flowerplatform.web.git.entity;

import org.eclipse.jgit.lib.Repository;

/**
 * @author Cristina Constantienscu
 */
public class SimpleNode extends GitNode<Object> {

	public SimpleNode(RepositoryNode parent, Repository repository, String type) {
		super(parent, type, repository,  null);
	}
}
