package emf;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.model_access_dao.DAOFactory;
import org.flowerplatform.model_access_dao.NodeDAO;
import org.flowerplatform.model_access_dao.UUID;
import org.flowerplatform.model_access_dao.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao.model.ModelFactory;
import org.flowerplatform.model_access_dao.model.Node1;

public class EMFNodeDAO implements NodeDAO {

	@Override
	public String createNode(String repoId, String resourceId, String parentId) {
		Node1 node = ModelFactory.eINSTANCE.createNode1();
		node.setId(UUID.newUUID());
		
		System.out.println("> create node " + node.getId());
		
		if (parentId == null) {
			Resource resource = DAOFactory.registryDAO.loadResource(repoId, resourceId);
			resource.getContents().add(node);
		} else {
			Node1 parent = getNode(repoId, resourceId, parentId);
			setParent(parent, node);
		}
		
		return node.getId();
	}
	
	@Override
	public Node1 getNode(String repoId, String resourceId, String id) {
		Resource resource = DAOFactory.registryDAO.loadResource(repoId, resourceId);
		return (Node1) resource.getEObject(id);
	}
	
	@Override
	public List<Node1> getChildren(Node1 node, String repoId, String resourceId) {
		return node.getChildren();
	}
	
	@Override
	public Node1 getParent(Node1 element, String repoId, String resourceId) {
		return (Node1) element.eContainer();
	}

	@Override
	public void setParent(Node1 parent, Node1 node) {
		if (parent != null) {
			parent.getChildren().add(node);
		} else {
			parent = (Node1) node.eContainer();
			if (parent == null) {
				// remove from resource TODO
			} else {
				parent.getChildren().remove(node);
			}
		}
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
		node.setDiagrammableElement(codeSyncElement);
	}
	
	@Override
	public void deleteNode(Node1 node, String repoId) {
		// remove from parent
		setParent(null, node);
		CodeSyncElement1 diagrammableElement = getDiagrammableElement(node, repoId);
		setDiagrammableElement(node, null);
		DAOFactory.codeSyncElementDAO.deleteCodeSyncElement(diagrammableElement);
	}
	
}
