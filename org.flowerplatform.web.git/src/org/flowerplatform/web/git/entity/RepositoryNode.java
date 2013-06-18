package org.flowerplatform.web.git.entity;

import java.io.File;

import org.eclipse.jgit.lib.Repository;

/**
 * @author Cristina Constantienscu
 */
public class RepositoryNode extends GitNode<File> {

	public RepositoryNode(Object parent, Repository repository, File repoFile) {
		super(parent, GitNodeType.NODE_TYPE_REPOSITORY, repository, repoFile);
	}

	@Override
	protected boolean checkObjectEqual(File otherObject) {
		return object.getPath().equals(otherObject.getPath());
	}
	
	@Override
	protected int getObjectHashCode() {
		return object.getPath().hashCode();
	}
	
}
