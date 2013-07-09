package org.flowerplatform.web.git.explorer.entity;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

/**
 * @author Cristina Constantinescu
 */
public class RefNode {

	private Repository repository;
	
	private Ref ref;

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public Ref getRef() {
		return ref;
	}

	public void setRef(Ref ref) {
		this.ref = ref;
	}

	public RefNode(Repository repository, Ref ref) {
		this.repository = repository;
		this.ref = ref;
	}
	
	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		
		result = prime * result + ((ref == null) ? 0 : ref.hashCode());
		result = prime * result + ((repository == null) ? 0 : repository.getDirectory().hashCode());
		
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		RefNode other = (RefNode) obj;

		return ((repository == null && other.getRepository() == null) || (repository != null && repository.getDirectory().equals(other.getRepository().getDirectory()))) &&
				((ref == null && other.getRef() == null) || (ref != null && ref.equals(other.getRef())));
	}
}
