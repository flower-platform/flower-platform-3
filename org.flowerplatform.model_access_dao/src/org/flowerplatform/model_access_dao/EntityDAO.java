package org.flowerplatform.model_access_dao;

import java.util.List;

import org.flowerplatform.model_access_dao.model.CodeSyncElement1;

public interface EntityDAO extends CodeSyncElementDAO {

	/**
	 * Merge with global resource, if this is a local resource.
	 */
	List<CodeSyncElement1> getReferencedElements(CodeSyncElement1 entity, String repoId, String discussableDesignId, String resourceId);
	
	void addReferencedElement(CodeSyncElement1 entity, CodeSyncElement1 element);
	
}
