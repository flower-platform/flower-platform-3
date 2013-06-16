package org.flowerplatform.web.git.entity;

import org.eclipse.jgit.lib.Repository;

/**
 * @author Cristina Constantienscu
 */
public  abstract class GitNode<T> {

	protected final Object parent;
	
	protected final Repository repository;
	
	protected final String type;
	
	protected final T object;
	
	public Object getParent() {
		return parent;
	}

	public Repository getRepository() {
		return repository;
	}

	public String getType() {
		return type;
	}

	public T getObject() {
		return object;
	}
	
	public GitNode(Object parent,  String type, Repository repository, T treeObject) {
		this.parent = parent;
		this.repository = repository;
		this.type = type;
		this.object = treeObject;
	}
	
	protected boolean checkObjectEqual(T otherObject) {
		return object.equals(otherObject);
	}
	
	protected int getObjectHashCode() {
		return object.hashCode();
	}
	
	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		
		result = prime * result + ((object == null) ? 0 : getObjectHashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + ((repository == null) ? 0 : repository.getDirectory().hashCode());
		result = prime * result + type.hashCode();
		
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		GitNode other = (GitNode) obj;

		return ((type == null && other.getType() == null) || (type != null && type.equals(other.getType()))) &&
				((parent == null && other.getParent() == null) || (parent != null && parent.equals(other.getParent()))) &&
				((repository == null && other.getRepository() == null) || (repository != null && repository.getDirectory().equals(other.getRepository().getDirectory()))) &&
				((object == null && other.getObject() == null) || (object != null && checkObjectEqual((T) other.getObject())));
	}
}
