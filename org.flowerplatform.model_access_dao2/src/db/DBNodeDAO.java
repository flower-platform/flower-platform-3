package db;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.flowerplatform.model_access_dao2.DAOFactory;
import org.flowerplatform.model_access_dao2.FlowerResourceURIHandler;
import org.flowerplatform.model_access_dao2.NodeDAO;
import org.flowerplatform.model_access_dao2.UUIDGenerator;
import org.flowerplatform.model_access_dao2.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao2.model.ModelFactory;
import org.flowerplatform.model_access_dao2.model.Node1;

import com.datastax.driver.core.Row;

public class DBNodeDAO implements NodeDAO {

	@Override
	public UUID createNode(UUID designId, UUID resourceId, UUID parentId, UUID nodeId) {
		if (nodeId == null) {
			nodeId = UUIDGenerator.newUUID();
		}
		
		CassandraData.execute("INSERT INTO node (designId, resourceId, nodeId, parentId) VALUES (?, ?, ?, ?)", 
				designId, resourceId, nodeId, parentId);
		
		return nodeId;
	}

	@Override
	public Node1 getNode(UUID designId, UUID resourceId, UUID nodeId) {
		List<Row> result = CassandraData.execute("SELECT * FROM node WHERE designId = ? AND resourceId = ? AND nodeId = ?", 
				designId, resourceId, nodeId).all();
		if (result.size() == 0) {
			return null;
		}
		return toNode(result.get(0));
	}

	private Node1 toNode(Row row) {
		Node1 node = ModelFactory.eINSTANCE.createNode1();
		node.setId(row.getUUID("nodeId").toString());
		node.setName(row.getString("name"));
		// TODO set parent
		UUID diagrammableElementResourceId = row.getUUID("diagrammableElementResourceId");
		UUID diagrammableElementId = row.getUUID("diagrammableElementId");
		URI proxyUri = FlowerResourceURIHandler.createFlowerResourceURI(diagrammableElementResourceId, diagrammableElementId);
		CodeSyncElement1 dgrElt = ModelFactory.eINSTANCE.createCodeSyncElement1();
		((InternalEObject) dgrElt).eSetProxyURI(proxyUri);
		node.setDiagrammableElement(dgrElt);
		return node;
	}

	@Override
	public void updateNode(UUID designId, UUID resourceId, Node1 node) {
		UUID diagrammableElementResourceId = null;
		UUID diagrammableElementId = null;
		EObject dgrElt = node.getDiagrammableElement();
		if (dgrElt != null && dgrElt.eIsProxy()) {
			URI proxyUri = ((InternalEObject) dgrElt).eProxyURI();
			diagrammableElementResourceId = UUIDGenerator.fromString(proxyUri.opaquePart());
			diagrammableElementId = UUIDGenerator.fromString(proxyUri.fragment());
		}
		CassandraData.execute("UPDATE node SET " +
				"name = ?, " +
				"diagrammableElementResourceId = ?, " +
				"diagrammableElementId = ? " +
				"WHERE designId = ? AND resourceId = ? AND nodeId = ?", 
				node.getName(), diagrammableElementResourceId, diagrammableElementId,
				designId, resourceId, UUIDGenerator.fromString(node.getId()));
	}

	@Override
	public void saveNode(UUID designId, UUID resourceId, Node1 node) {
		UUID parentId = null;
		Node1 parent = (Node1) node.eContainer();
		if (parent != null) {
			parentId = UUIDGenerator.fromString(parent.getId());
		}
		UUID nodeId = UUIDGenerator.fromString(node.getId());
		createNode(designId, resourceId, parentId, nodeId);
		updateNode(designId, resourceId, node);
	}

	@Override
	public void deleteNode(UUID designId, UUID resourceId, UUID nodeId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setParent(UUID designId, UUID resourceId, UUID parentId,
			Node1 child) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<Node1> getChildren(UUID designId, UUID resourceId, UUID parentId) {
		List<Row> result = CassandraData.execute("SELECT * FROM node WHERE designId = ? AND resourceId = ? AND parentId = ?", 
				designId, resourceId, parentId).all();
		List<Node1> children = new ArrayList<Node1>();
		for (Row row : result) {
			children.add(toNode(row));
		}
		return children;
	}

	@Override
	public void setDiagrammableElement(UUID designId, UUID resourceId,
			UUID nodeId, CodeSyncElement1 diagrammableElement) {
		UUID diagrammableElementResourceId = UUIDGenerator.fromString(
				diagrammableElement.eResource().getURI().opaquePart());
		UUID diagrammableElementId = UUIDGenerator.fromString(diagrammableElement.getId());
		CassandraData.execute("UPDATE node " +
				"SET diagrammableElementResourceId = ?, diagrammableElementId = ? " +
				"WHERE designId = ? AND resourceId = ? AND nodeId = ?",
				diagrammableElementResourceId, diagrammableElementId,
				designId, resourceId, nodeId);
	}
	
	@Override
	public CodeSyncElement1 getDiagrammableElement(UUID designId, Node1 node) {
		// TODO copied from the DB implementation
		CodeSyncElement1 dgrElt = node.getDiagrammableElement();
		if (dgrElt != null && dgrElt.eIsProxy()) {
			URI proxyUri = ((InternalEObject) dgrElt).eProxyURI();
			UUID resourceId = UUIDGenerator.fromString(proxyUri.opaquePart());
			UUID eltId = UUIDGenerator.fromString(proxyUri.fragment());
			return DAOFactory.getCodeSyncElementDAO().getCodeSyncElement(designId, resourceId, eltId);
		}
		return dgrElt;
	}

}
