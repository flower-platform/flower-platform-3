package emf;

import java.util.List;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.model_access_dao2.DAOFactory;
import org.flowerplatform.model_access_dao2.NodeDAO;
import org.flowerplatform.model_access_dao2.UUIDGenerator;
import org.flowerplatform.model_access_dao2.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao2.model.ModelFactory;
import org.flowerplatform.model_access_dao2.model.Node1;

public class EMFNodeDAO implements NodeDAO {

	@Override
	public UUID createNode(UUID designId, UUID resourceId, UUID parentId, UUID nodeId) {
		if (nodeId == null) {
			nodeId = UUIDGenerator.newUUID();
		}
		Node1 node = ModelFactory.eINSTANCE.createNode1();
		node.setId(nodeId.toString());
		
		if (parentId == null) {
			Resource resource = DAOFactory.getRegistryDAO().loadResource(designId, resourceId);
			resource.getContents().add(node);
		} else {
			setParent(designId, resourceId, parentId, node);
		}
		
		return UUIDGenerator.fromString(node.getId());
	}

	@Override
	public Node1 getNode(UUID designId, UUID resourceId, UUID nodeId) {
		Resource resource = DAOFactory.getRegistryDAO().loadResource(designId, resourceId);
		return (Node1) resource.getEObject(nodeId.toString());
	}

	@Override
	public void updateNode(UUID designId, UUID resourceId, Node1 node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveNode(UUID designId, UUID resourceId, Node1 node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteNode(UUID designId, UUID resourceId, UUID nodeId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setParent(UUID designId, UUID resourceId, UUID parentId, Node1 child) {
		Node1 parent = getNode(designId, resourceId, parentId);
		parent.getChildren().add(child);
	}

	@Override
	public List<Node1> getChildren(UUID designId, UUID resourceId, UUID parentId) {
		Node1 parent = getNode(designId, resourceId, parentId);
		return parent.getChildren();
	}
	
	@Override
	public void setDiagrammableElement(UUID designId, UUID resourceId,
			UUID nodeId, CodeSyncElement1 diagrammableElement) {
		Node1 node = getNode(designId, resourceId, nodeId);
		node.setDiagrammableElement(diagrammableElement);
	}

	@Override
	public CodeSyncElement1 getDiagrammableElement(UUID designId, Node1 node) {
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
