package db;

import java.util.List;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.InternalEObject;
import org.flowerplatform.model_access_dao.DAOFactory;
import org.flowerplatform.model_access_dao.NodeDAO;
import org.flowerplatform.model_access_dao.UUIDGenerator;
import org.flowerplatform.model_access_dao.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao.model.ModelFactory;
import org.flowerplatform.model_access_dao.model.Node1;
import org.flowerplatform.model_access_dao.model.ResourceInfo;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;

public class DBNodeDAO implements NodeDAO {

	@Override
	public UUID createNode(UUID repoId, UUID resourceId, UUID parentId, UUID id) {
		if (id == null) {
			id = UUIDGenerator.newUUID();
		}
		
		System.out.println("> create node " + id);
		
		PreparedStatement stmt = CassandraData.getSession().prepare(
				"INSERT INTO node (repoId, resourceId, nodeId, parentId) VALUES (?, ?, ?, ?)");
		CassandraData.getSession().execute(stmt.bind(repoId, resourceId, id, parentId));
		
		return id;
	}

	@Override
	public Node1 getNode(UUID repoId, UUID resourceId, UUID id) {
		String query = "SELECT * FROM node WHERE repoId = ? AND resourceId = ? AND nodeId = ?";
		PreparedStatement stmt = CassandraData.getSession().prepare(query);
		List<Row> results = CassandraData.getSession().execute(stmt.bind(repoId, resourceId, id)).all();
		
		if (results.size() == 0) {
			return null;
		}
		
		if (results.size() > 1) {
			throw new RuntimeException("Multiple rows returned for query = " + query);
		}
		
		Row row = results.get(0);
		Node1  node = ModelFactory.eINSTANCE.createNode1();
		node.setId(row.getUUID("nodeId").toString());
		node.setName(row.getString("name"));
		
		return node;
	}
	
	@Override
	public void updateNode(UUID repoId, UUID resourceId, Node1 node) {
		PreparedStatement stmt = CassandraData.getSession().prepare(
				"UPDATE node SET name = ? WHERE repoId = ? AND resourceId = ? AND nodeId = ?");
		CassandraData.getSession().execute(stmt.bind(node.getName(), repoId, resourceId, UUID.fromString(node.getId())));
	}

	@Override
	public List<Node1> getChildren(Node1 node, UUID repoId, UUID resourceId) {
		return node.getChildren();
	}

	@Override
	public Node1 getParent(Node1 node, UUID repoId, UUID resourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(Node1 parent, Node1 node) {
		// TODO Auto-generated method stub

	}

	@Override
	public CodeSyncElement1 getDiagrammableElement(Node1 node, UUID repoId) {
		CodeSyncElement1 element = node.getDiagrammableElement();
		if (element.eIsProxy()) {
			URI uri = ((InternalEObject) element).eProxyURI();
			UUID uid = UUID.fromString(uri.fragment());
			UUID resourceId = UUID.fromString(uri.opaquePart());
			element = DAOFactory.codeSyncElementDAO.getCodeSyncElement(repoId, resourceId, uid);
		}
		return element;
	}

	@Override
	public void setDiagrammableElement(Node1 node, CodeSyncElement1 codeSyncElement) {
		PreparedStatement stmt = CassandraData.getSession().prepare(
				"UPDATE node SET diagrammableElementId = ? WHERE repoId = ? AND resourceId = ? AND nodeId = ?");
		String repoId = ((ResourceInfo) node.eResource().getContents().get(0)).getRepoId();
		String nodeId = node.getId();
		URI uri = node.eResource().getURI();
		String resourceId = uri.opaquePart();
		CassandraData.getSession().execute(stmt.bind(
				UUID.fromString(codeSyncElement.getId()),
				UUID.fromString(repoId), 
				UUID.fromString(resourceId), 
				UUID.fromString(nodeId)));
		node.setDiagrammableElement(codeSyncElement);
	}

	@Override
	public void deleteNode(Node1 node, UUID repoId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveNode(UUID repoId, UUID resourceId, Node1 node) {
		UUID parentId = null;
		if (node.eContainer() != null) {
			Node1 parent = (Node1) node.eContainer();
			parentId = UUID.fromString(parent.getId());
		}
		UUID id = UUID.fromString(node.getId());
		createNode(repoId, resourceId, parentId, id);
		updateNode(repoId, resourceId, node);
	}

}
