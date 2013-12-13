package org.flowerplatform.model_access_dao;

import java.util.List;
import java.util.UUID;

import org.flowerplatform.model_access_dao.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao.model.Relation1;

public interface CodeSyncElementDAO {
	
	UUID createCodeSyncElement(UUID repoId, UUID resourceId, UUID id, UUID parentId);
	CodeSyncElement1 getCodeSyncElement(UUID repoId, UUID resourceId, UUID id);
	
	void updateCodeSyncElement(UUID repoId, UUID resourceId, CodeSyncElement1 element);
	
	void saveCodeSyncElement(UUID repoId, UUID resourceId, CodeSyncElement1 element);
	
	/**
	 * Merge with global resource, if this is a local resource.
	 */
	List<CodeSyncElement1> getCodeSyncElements(UUID repoId, UUID resourceId);
	
	/**
	 * Merge with global resource, if this is a local resource.
	 */
	List<CodeSyncElement1> getChildren(CodeSyncElement1 element, UUID repoId, UUID resourceId);
	
	CodeSyncElement1 getParent(CodeSyncElement1 element, UUID repoId, UUID resourceId);
	void setParent(CodeSyncElement1 parent, CodeSyncElement1 element);
	
	/**
	 * Delete element only if there are no references towards it.
	 */
	void deleteCodeSyncElement(CodeSyncElement1 element);
	
	void addRelation(CodeSyncElement1 source, CodeSyncElement1 target, UUID repoId, UUID resourceId);

	List<CodeSyncElement1> getReferencedElements(CodeSyncElement1 source, UUID repoId, UUID resourceId);
	
	CodeSyncElement1 getSource(Relation1 relation, UUID repoId);
	
	CodeSyncElement1 getTarget(Relation1 relation, UUID repoId);
	
}
