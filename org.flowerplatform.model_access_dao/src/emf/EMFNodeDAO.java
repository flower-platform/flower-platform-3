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
import org.flowerplatform.model_access_dao.model.Node1EMF;

public class EMFNodeDAO implements NodeDAO {

	@Override
	public String createNode(String repoId, String discussableDesignId, String resourceId, String parentId) {
		Node1 node = ModelFactory.eINSTANCE.createNode1EMF();
		node.setId(UUID.newUUID());
		
		System.out.println("> create node " + node.getId());
		
		if (parentId == null) {
			Resource resource = DAOFactory.registryDAO.loadResource(repoId, discussableDesignId, resourceId);
			resource.getContents().add(node);
		} else {
			Node1 parent = getNode(repoId, discussableDesignId, resourceId, parentId);
			setParent(parent, node);
		}
		
		return node.getId();
	}
	
	@Override
	public Node1 getNode(String repoId, String discussableDesignId, String resourceId, String id) {
		Resource resource = DAOFactory.registryDAO.loadResource(repoId, discussableDesignId, resourceId);
		return (Node1) resource.getEObject(id);
	}
	
	@Override
	public Node1 getParent(Node1 element, String repoId, String discussableDesignId, String resourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(Node1 parent, Node1 node) {
		if (parent != null) {
			getChildren(parent, null, null, null).add(node);
		} else {
			parent = (Node1) node.eContainer();
			if (parent == null) {
				// remove from resource TODO
			} else {
				getChildren(parent, null, null, null).remove(node);
			}
		}
	}
	
	@Override
	public List<Node1> getChildren(Node1 node, String repoId, String discussableDesignId, String resourceId) {
		return getNode(node).getChildren();
	}
	
	@Override
	public CodeSyncElement1 getDiagrammableElement(Node1 node, String repoId, String discussableDesignId) {
		CodeSyncElement1 element = getNode(node).getDiagrammableElement();
		if (element.eIsProxy()) {
			URI uri = ((InternalEObject) element).eProxyURI();
			String uid = uri.fragment();
			String resourceId = uri.opaquePart();
			element = DAOFactory.codeSyncElementDAO.getCodeSyncElement(repoId, discussableDesignId, resourceId, uid);
		}
		return element;
	}

	@Override
	public void setDiagrammableElement(Node1 node, CodeSyncElement1 codeSyncElement) {
		getNode(node).setDiagrammableElement(codeSyncElement);
	}
	
	@Override
	public void deleteNode(Node1 node, String repoId, String discussableDesignId) {
		// remove from parent
		setParent(null, node);
		CodeSyncElement1 diagrammableElement = getDiagrammableElement(node, repoId, discussableDesignId);
		setDiagrammableElement(node, null);
		DAOFactory.codeSyncElementDAO.deleteCodeSyncElement(diagrammableElement);
	}
	
	protected Node1EMF getNode(Node1 node) {
		return (Node1EMF) node;
	}

}
