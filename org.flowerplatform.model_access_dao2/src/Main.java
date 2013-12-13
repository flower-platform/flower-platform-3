import static junit.framework.Assert.assertNotNull;

import java.io.File;
import java.util.UUID;

import org.flowerplatform.model_access_dao2.CodeSyncElementDAO;
import org.flowerplatform.model_access_dao2.DAOFactory;
import org.flowerplatform.model_access_dao2.NodeDAO;
import org.flowerplatform.model_access_dao2.UUIDGenerator;
import org.flowerplatform.model_access_dao2.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao2.model.Diagram1;
import org.flowerplatform.model_access_dao2.model.Node1;
import org.flowerplatform.model_access_dao2.registry.Design;
import org.flowerplatform.model_access_dao2.registry.Repository;
import org.flowerplatform.model_access_dao2.service.DiagramService;
import org.flowerplatform.model_access_dao2.service.RegistryService;
import org.junit.Test;

public class Main {

	@Test
	public void test() {
		clearDir(new File("repos"));
		
		DAOFactory.mode = DAOFactory.EMF;
		
		// create repo
		String repoPath = "repos/myRepo";
		UUID repoId = RegistryService.INSTANCE.createRepository(repoPath);
		Repository repo = DAOFactory.getRegistryDAO().getRepository(repoId);
		assertNotNull(repo);
		
		// create design
		String designPath = "myDesign";
		UUID designId = RegistryService.INSTANCE.createDesign(designPath, repoId);
		Design design = DAOFactory.getRegistryDAO().getDesign(designId);
		assertNotNull(design);
		
		// create model resource
		UUID modelId = DAOFactory.getRegistryDAO().createResource("/mapping", repoId, null);
		
		CodeSyncElementDAO cseDao = DAOFactory.getCodeSyncElementDAO();
		UUID folderId = createCSE(cseDao, repoId, modelId, null, "myFolder");
		UUID classId = createCSE(cseDao, repoId, modelId, folderId, "myClass");
		UUID metId = createCSE(cseDao, repoId, modelId, classId, "myMethod");
		
		DAOFactory.getRegistryDAO().saveResource(repoId, modelId);
		
		// create diagram
		DiagramService service = new DiagramService();
		String diagramPath = "proj1/diagrams/NewDgr.fpd";
		UUID diagramId = service.createDiagram(diagramPath, designId);
		Diagram1 diagram = DAOFactory.getDiagramDAO().getDiagram(designId, diagramPath);
		
		// add nodes on diagram
		NodeDAO nodeDao = DAOFactory.getNodeDAO();
		UUID classNodeId = addOnDiagram(nodeDao, designId, diagramId, UUIDGenerator.fromString(diagram.getId()),
				cseDao.getCodeSyncElement(repoId, modelId, classId));
		addOnDiagram(nodeDao, designId, diagramId, classNodeId,
				cseDao.getCodeSyncElement(repoId, modelId, metId));
		
		DAOFactory.getRegistryDAO().saveResource(designId, diagramId);
		
		// test: unload to check how the dgrElt refs are resolved
		DAOFactory.getRegistryDAO().loadResource(designId, diagramId)
				.getResourceSet().getResources().clear();
		
		System.out.println("PRINT FROM DISK");
		for (Node1 node : service.openDiagram(diagramPath, designId)) {
			printNode(node, designId);
		}
		
		// make a modification
		service.updateFeatureValue(designId, modelId, metId, null, null);
		DAOFactory.getRegistryDAO().saveResource(designId, modelId);
		
		System.out.println("PRINT FROM DISK - DESIGN AFTER MODIF");
		for (Node1 node : service.openDiagram(diagramPath, designId)) {
			printNode(node, designId);
		}
		
		System.out.println("PRINT FROM DISK - REPO AFTER MODIF");
		for (Node1 node : service.openDiagram(diagramPath, repoId)) {
			printNode(node, repoId);
		}
		
		DAOFactory.mode = DAOFactory.DB;
		
		// import into db
		DAOFactory.getRegistryDAO().saveRepository(repo);
		DAOFactory.getRegistryDAO().saveDesign(design);
		
		System.out.println("PRINT FROM DB - DESIGN");
		for (Node1 node : service.openDiagram(diagramPath, designId)) {
			printNode(node, designId);
		}
		
		System.out.println("PRINT FROM DB - REPO");
		for (Node1 node : service.openDiagram(diagramPath, repoId)) {
			printNode(node, repoId);
		}
	}
	
	private UUID createCSE(CodeSyncElementDAO dao, UUID designId, UUID resourceId, UUID parentId, String name) {
		UUID cseId = dao.createCodeSyncElement(designId, resourceId, parentId, null);
		CodeSyncElement1 cse = dao.getCodeSyncElement(designId, resourceId, cseId);
		cse.setName(name);
		dao.updateCodeSyncElement(designId, resourceId, cse);
		return cseId;
	}
	
	private UUID addOnDiagram(NodeDAO nodeDao, UUID designId, UUID diagramId, UUID parentId,
			CodeSyncElement1 cse) {
		UUID nodeId = nodeDao.createNode(designId, diagramId, parentId, null);
		Node1 node = nodeDao.getNode(designId, diagramId, nodeId);
		nodeDao.setDiagrammableElement(designId, diagramId, nodeId, cse);
		node.setName(cse.getName() + "_NODE");
		nodeDao.updateNode(designId, diagramId, node);
		
		return nodeId;
	}
	
	private void printNode(Node1 node, UUID designId) {
		String className = node.eClass().getName();
		String id = node.getId();
		String name = node.getName();
		System.out.println(String.format("%s [id = %s, name = %s]", className, id, name));
		CodeSyncElement1 dgrElt = DAOFactory.getNodeDAO().getDiagrammableElement(designId, node);
		if (dgrElt == null) {
			return;
		}
		className = dgrElt.eClass().getName();
		id = dgrElt.getId();
		name = dgrElt.getName();
		System.out.println(String.format("%s [id = %s, name = %s]", className, id, name));
	}
	
	private void clearDir(File dir) {
		if (dir.listFiles() == null) {
			return;
		}
		
		for (File child : dir.listFiles()) {
			clearDir(child);
			child.delete();
		}
	}
	
}
