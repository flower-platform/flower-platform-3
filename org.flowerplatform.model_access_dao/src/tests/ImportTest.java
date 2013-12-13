package tests;

import static org.junit.Assert.*;

import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.model_access_dao.DAOFactory;
import org.flowerplatform.model_access_dao.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao.model.Diagram1;
import org.flowerplatform.model_access_dao.model.Node1;
import org.flowerplatform.model_access_dao.registry.Repository;
import org.junit.Test;

public class ImportTest {

	@Test
	public void test() {
		// open diagram
		System.out.println("IMPORT TO DB");
		
		Repository repo = DAOFactory.registryDAO.getRepositories().get(0);
		if (repo.getMasterId() == null) {
			repo = DAOFactory.registryDAO.getRepositories().get(1);
		}
		UUID repoId = repo.getId();
		
		Repository discussableDesign = DAOFactory.registryDAO.getRepository(repoId);
		UUID resourceId = ModelAccessDAOTests.getResourceId(discussableDesign, "dgr.notation");
		importDiagram(repoId, resourceId);
	}
	
	protected void importNode(Node1 node, UUID repoId, UUID resourceId, UUID parentId) {
		DAOFactory.db.getNodeDAO().saveNode(repoId, resourceId, node);
		
		CodeSyncElement1 diagrammableElement = DAOFactory.nodeDAO.getDiagrammableElement(node, repoId);
		URI uri = diagrammableElement.eResource().getURI();
		diagrammableElement = importCSE(diagrammableElement, repoId, UUID.fromString(uri.opaquePart()));
		DAOFactory.db.getNodeDAO().setDiagrammableElement(node, diagrammableElement);
		
		for (Node1 child : DAOFactory.nodeDAO.getChildren(node, repoId, resourceId)) {
			importNode(child, repoId, resourceId, UUID.fromString(node.getId()));
		}
	}
	
	protected void importDiagram(UUID repoId, UUID resourceId) {
		Resource resource = DAOFactory.registryDAO.loadResource(repoId, resourceId);
		
		URI uri = DAOFactory.registryDAO.getResource(repoId, resourceId);
		DAOFactory.db.getRegistryDao().createResource(uri.toFileString(), repoId, resourceId);
		
		Diagram1 diagram = (Diagram1) resource.getContents().get(1);
		for (Node1 node : diagram.getChildren()) {
			importNode(node, repoId, resourceId, UUID.fromString(node.getId()));
		} 
	}
	
	protected CodeSyncElement1 importCSE(CodeSyncElement1 diagrammableElement, UUID repoId, UUID resourceId) {
		if (DAOFactory.db.getRegistryDao().getResource(repoId, resourceId) == null) {
			importResource(repoId, resourceId);
		}
		
		UUID id = UUID.fromString(diagrammableElement.getId());
		CodeSyncElement1 imported = DAOFactory.db.getCSEDAO().getCodeSyncElement(repoId, resourceId, id);
		assertNotNull("CSE was not imported", imported);
		return imported;
	}
	
	protected void importResource(UUID repoId, UUID resourceId) {
		URI uri = DAOFactory.registryDAO.getResource(repoId, resourceId);
		DAOFactory.db.getRegistryDao().createResource(uri.toFileString(), repoId, resourceId);
		
		Resource resource = DAOFactory.registryDAO.loadResource(repoId, resourceId);
		
		CodeSyncElement1 cse = (CodeSyncElement1) resource.getContents().get(1);
		importCSE(cse, repoId, resourceId, null);
	}
	
	protected void importCSE(CodeSyncElement1 codeSyncElement, UUID repoId, UUID resourceId, UUID parentId) {
		DAOFactory.db.getCSEDAO().saveCodeSyncElement(repoId, resourceId, codeSyncElement);
		for (CodeSyncElement1 child : DAOFactory.codeSyncElementDAO.getChildren(codeSyncElement, repoId, resourceId)) {
			importCSE(child, repoId, resourceId, UUID.fromString(codeSyncElement.getId()));
		}
		
	}

}
