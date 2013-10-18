package org.flowerplatform.codesync.remote;

import java.util.List;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

public interface CodeSyncOperationsService {

	CodeSyncElement create(String codeSyncType);
	
	void add(CodeSyncElement parent, CodeSyncElement elementToAdd);
	
	Object getFeatureValue(CodeSyncElement element, Object feature);
	
	void setFeatureValue(CodeSyncElement element, Object feature, Object value);
	
	void markDeleted(CodeSyncElement element);
	
	List<Object> getFeatures(String codeSyncType);
	
}
