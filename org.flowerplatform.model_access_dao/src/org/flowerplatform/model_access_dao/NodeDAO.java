package org.flowerplatform.model_access_dao;

import java.util.List;
import java.util.UUID;

import org.flowerplatform.model_access_dao.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao.model.Node1;

public interface NodeDAO {

	UUID createNode(UUID repoId, UUID resourceId, UUID parentId, UUID id);
	Node1 getNode(UUID repoId, UUID resourceId, UUID id);
	
	void updateNode(UUID repoId, UUID resourceId, Node1 node);
	
	void saveNode(UUID repoId, UUID resourceId, Node1 node);
	
	List<Node1> getChildren(Node1 node, UUID repoId, UUID resourceId);
	
	Node1 getParent(Node1 node, UUID repoId, UUID resourceId);
	void setParent(Node1 parent, Node1 node);
	
	CodeSyncElement1 getDiagrammableElement(Node1 node, UUID repoId);
	void setDiagrammableElement(Node1 node, CodeSyncElement1 codeSyncElement);
	
	void deleteNode(Node1 node, UUID repoId);
	
}
