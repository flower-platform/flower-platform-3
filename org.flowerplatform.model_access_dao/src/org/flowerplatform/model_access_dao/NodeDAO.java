package org.flowerplatform.model_access_dao;

import java.util.List;

import org.flowerplatform.model_access_dao.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao.model.Node1;

public interface NodeDAO {

	String createNode(String repoId, String discussableDesignId, String resourceId, String parentId);
	
	Node1 getNode(String repoId, String discussableDesignId, String resourceId, String id);
	
	Node1 getParent(Node1 node, String repoId, String discussableDesignId, String resourceId);
	void setParent(Node1 parent, Node1 node);
	
	List<Node1> getChildren(Node1 node, String repoId, String discussableDesignId, String resourceId);
	
	CodeSyncElement1 getDiagrammableElement(Node1 node, String repoId, String discussableDesignId);
	void setDiagrammableElement(Node1 node, CodeSyncElement1 codeSyncElement);
	
	void deleteNode(Node1 node, String repoId, String discussableDesignId);
	
}
