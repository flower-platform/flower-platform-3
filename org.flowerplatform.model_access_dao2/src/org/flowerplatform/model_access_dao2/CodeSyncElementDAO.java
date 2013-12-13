package org.flowerplatform.model_access_dao2;

import java.util.UUID;

import org.flowerplatform.model_access_dao2.model.CodeSyncElement1;

public interface CodeSyncElementDAO {

	UUID createCodeSyncElement(UUID designId, UUID resourceId, UUID parentId, UUID codeSyncElementId);
	CodeSyncElement1 getCodeSyncElement(UUID designId, UUID resourceId, UUID codeSyncElementId);
	void updateCodeSyncElement(UUID designId, UUID resourceId, CodeSyncElement1 codeSyncElement);
	void saveCodeSyncElement(UUID designId, UUID resourceId, CodeSyncElement1 codeSyncElement);
	void deleteCodeSyncElement(UUID designId, UUID resourceId, UUID codeSyncElementId);
	
	void setParent(UUID designId, UUID resourceId, UUID parentId, CodeSyncElement1 child);
	
}
