package org.flowerplatform.model_access_dao2;

import java.util.UUID;

import org.flowerplatform.model_access_dao2.model.Diagram1;

public interface DiagramDAO {

	UUID createDiagram(UUID designId, UUID resourceId, UUID diagramId);
	Diagram1 getDiagram(UUID designId, UUID resourceId, UUID diagramId);
	void updateDiagram(UUID designId, UUID resourceId, Diagram1 diagram);
	void saveDiagram(UUID designId, UUID resourceId, Diagram1 diagram);
	void deleteDiagram(UUID designId, UUID resourceId, UUID diagramId);
	
	Diagram1 getDiagram(UUID designId, String path);
	
}
