package db;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.InternalEObject;
import org.flowerplatform.model_access_dao.DAOFactory;
import org.flowerplatform.model_access_dao.NodeDAO;
import org.flowerplatform.model_access_dao.UUID;
import org.flowerplatform.model_access_dao.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao.model.ModelFactory;
import org.flowerplatform.model_access_dao.model.Node1;
import org.flowerplatform.model_access_dao.model.ResourceInfo;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;

public class DBNodeDAO implements NodeDAO {

	@Override
	public String createNode(String repoId, String resourceId, String parentId) {
		String id = UUID.newUUID();
		
		System.out.println("> create node " + id);
		
		PreparedStatement stmt = CassandraData.getSession().prepare(
				"INSERT INTO node (repoId, nodeId, parentId) VALUES (?, ?, ?)");
		CassandraData.getSession().execute(stmt.bind(repoId, id, parentId));
		
		return id;
	}

	@Override
	public Node1 getNode(String repoId, String resourceId, String id) {
		String query = "SELECT * FROM node WHERE repoId = ? AND nodeId = ?";
		PreparedStatement stmt = CassandraData.getSession().prepare(query);
		List<Row> results = CassandraData.getSession().execute(stmt.bind(repoId, id)).all();
		
		if (results.size() == 0) {
			return null;
		}
		
		if (results.size() > 1) {
			throw new RuntimeException("Multiple rows returned for query = " + query);
		}
		
		Row row = results.get(0);
		Node1  node = ModelFactory.eINSTANCE.createNode1();
		node.setId(row.getString("nodeId"));
		node.setName(row.getString("name"));
		
		return node;
	}
	
	@Override
	public void updateNode(String repoId, String resourceId, Node1 node) {
		PreparedStatement stmt = CassandraData.getSession().prepare(
				"UPDATE node SET name = ? WHERE repoId = ? AND nodeId = ?");
		CassandraData.getSession().execute(stmt.bind(node.getName(), repoId, node.getId()));
	}

	@Override
	public List<Node1> getChildren(Node1 node, String repoId, String resourceId) {
		return node.getChildren();
	}

	@Override
	public Node1 getParent(Node1 node, String repoId, String resourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(Node1 parent, Node1 node) {
		// TODO Auto-generated method stub

	}

	@Override
	public CodeSyncElement1 getDiagrammableElement(Node1 node, String repoId) {
		CodeSyncElement1 element = node.getDiagrammableElement();
		if (element.eIsProxy()) {
			URI uri = ((InternalEObject) element).eProxyURI();
			String uid = uri.fragment();
			String resourceId = uri.opaquePart();
			element = DAOFactory.codeSyncElementDAO.getCodeSyncElement(repoId, resourceId, uid);
		}
		return element;
	}

	@Override
	public void setDiagrammableElement(Node1 node, CodeSyncElement1 codeSyncElement) {
		PreparedStatement stmt = CassandraData.getSession().prepare(
				"UPDATE node SET diagrammableElementId = ? WHERE repoId = ? AND nodeId = ?");
		String repoId = ((ResourceInfo) node.eResource().getContents().get(0)).getRepoId();
		String nodeId = node.getId();
		CassandraData.getSession().execute(stmt.bind(codeSyncElement.getId(), repoId, nodeId));
		node.setDiagrammableElement(codeSyncElement);
	}

	@Override
	public void deleteNode(Node1 node, String repoId) {
		// TODO Auto-generated method stub

	}

}
