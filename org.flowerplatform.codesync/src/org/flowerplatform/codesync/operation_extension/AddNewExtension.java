package org.flowerplatform.codesync.operation_extension;

import java.util.Map;

public interface AddNewExtension {
	String addNew(String diagramId, String viewIdOfParent, String codeSyncType, Map<String, Object> parameters);

}
