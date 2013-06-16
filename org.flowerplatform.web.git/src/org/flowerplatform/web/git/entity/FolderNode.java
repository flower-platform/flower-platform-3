package org.flowerplatform.web.git.entity;

import java.io.File;

import org.eclipse.jgit.lib.Repository;

/**
 * @author Cristina Constantienscu
 */
public class FolderNode extends GitNode<File> {

	public FolderNode(Object parent, Repository repository, File file) {
		super(parent, GitNodeType.NODE_TYPE_FOLDER, repository, file);
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
