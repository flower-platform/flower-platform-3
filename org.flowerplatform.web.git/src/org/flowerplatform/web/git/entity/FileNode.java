package org.flowerplatform.web.git.entity;

import java.io.File;

import org.eclipse.jgit.lib.Repository;

/**
 * @author Cristina Constantienscu
 */
public class FileNode extends GitNode<File> {

	public FileNode(Object parent, Repository repository, File file) {
		super(parent, GitNodeType.NODE_TYPE_FILE, repository, file);
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
