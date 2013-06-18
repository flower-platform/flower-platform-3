package org.flowerplatform.web.git.entity;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

/**
 * @author Cristina Constantienscu
 */
public class RefNode extends GitNode<Ref> {

	public RefNode(SimpleNode parent, String type, Repository repository, Ref ref) {
		super(parent, type, repository, ref);
	}
	
	@Override
	protected boolean checkObjectEqual(Ref otherObject) {
		return object.getName().equals(otherObject.getName());
	}
	
	@Override
	protected int getObjectHashCode() {
		return object.getName().hashCode();
	}
	
}
