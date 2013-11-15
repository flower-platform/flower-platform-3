package org.flowerplatform.model_access_dao;

import java.util.List;

import org.flowerplatform.model_access_dao.model.CodeSyncElement1;

public interface CodeSyncElementDAO {
	
	String createCodeSyncElement(String repoId, String discussableDesignId, String resourceId, String id, String parentId);
	CodeSyncElement1 getCodeSyncElement(String repoId, String discussableDesignId, String resourceId, String id);
	
	/**
	 * Merge with global resource, if this is a local resource.
	 */
	List<CodeSyncElement1> getCodeSyncElements(String repoId, String discussableDesignId, String resourceId);
	
	/**
	 * Merge with global resource, if this is a local resource.
	 */
	List<CodeSyncElement1> getChildren(CodeSyncElement1 element, String repoId, String discussableDesignId, String resourceId);
	
	CodeSyncElement1 getParent(CodeSyncElement1 element, String repoId, String discussableDesignId, String resourceId);
	void setParent(CodeSyncElement1 parent, CodeSyncElement1 element);
	
	void deleteCodeSyncElement(CodeSyncElement1 element);
	
}
