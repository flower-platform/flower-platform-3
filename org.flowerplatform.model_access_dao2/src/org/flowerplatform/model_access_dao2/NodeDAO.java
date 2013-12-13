package org.flowerplatform.model_access_dao2;

import java.util.List;
import java.util.UUID;

import org.flowerplatform.model_access_dao2.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao2.model.Node1;

public interface NodeDAO {

	UUID createNode(UUID designId, UUID resourceId, UUID parentId, UUID nodeId);
	Node1 getNode(UUID designId, UUID resourceId, UUID nodeId);
	void updateNode(UUID designId, UUID resourceId, Node1 node);
	void saveNode(UUID designId, UUID resourceId, Node1 node);
	void deleteNode(UUID designId, UUID resourceId, UUID nodeId);

	void setParent(UUID designId, UUID resourceId, UUID parentId, Node1 child);
	List<Node1> getChildren(UUID designId, UUID resourceId, UUID parentId);
	
	void setDiagrammableElement(UUID designId, UUID resourceId, UUID nodeId, CodeSyncElement1 diagrammableElement);
	CodeSyncElement1 getDiagrammableElement(UUID designId, Node1 node);
	
}
