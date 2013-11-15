package tests;

import static junit.framework.Assert.assertEquals;
import static tests.CreateGlobalModel.CLASS1;
import static tests.CreateGlobalModel.FOLDER1;
import static tests.CreateGlobalModel.MET1;
import static tests.CreateGlobalModel.REPO;
import static tests.CreateGlobalModel.class1Id;
import static tests.CreateGlobalModel.folder1Id;
import static tests.CreateGlobalModel.met1Id;
import static tests.CreateGlobalModel.methodsEntityId;
import static tests.ModelAccessDAOTests.assertCSE;
import static tests.ModelAccessDAOTests.assertResourceInfo;
import static tests.ModelAccessDAOTests.getResourceId;
import static tests.ModelAccessDAOTests.loadResource;
import static tests.ModelAccessDAOTests.printContents;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.model_access_dao.CodeSyncElementDAO;
import org.flowerplatform.model_access_dao.DAOFactory;
import org.flowerplatform.model_access_dao.RegistryDAO;
import org.flowerplatform.model_access_dao.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao.model.CodeSyncElement1EMF;
import org.flowerplatform.model_access_dao.model.Diagram1;
import org.flowerplatform.model_access_dao.model.ModelFactory;
import org.flowerplatform.model_access_dao.model.Node1;
import org.flowerplatform.model_access_dao.registry.DiscussableDesign;
import org.junit.Test;

public class CreateDiscussableDesign {
	
	public static final String MY_DISCUSSABLE_DESIGN = "myDesignDiscussion";

	private final String MODIF_FROM_DIAGRAM = "ModifFromDiagram";
	private final String ADDED_FROM_DIAGRAM = "AddedFromDiagram";
	
	@Test
	public void test() {
		System.out.println("CREATE DISCUSSABLE DESIGN");
		
		String repoId = (String) DAOFactory.registryDAO.getRepositories().get(0).getId();
		
		// create discussable design
		String ddId = DAOFactory.registryDAO.createDiscussableDesign(repoId, MY_DISCUSSABLE_DESIGN);
		DiscussableDesign dd = DAOFactory.registryDAO.getDiscussableDesign(repoId, ddId);
		
		assertEquals("Resources not created", 2, dd.getResources().size());
		
		String localMappingId = getResourceId(dd, RegistryDAO.MAPPING_LOCATION);
		
		// create diagram
		String path = "flower-platform-diagrams/dgr.notation";
		String diagramResourceId = DAOFactory.registryDAO.createResource(path, repoId, ddId, null);
		Resource resource = DAOFactory.registryDAO.loadResource(repoId, ddId, diagramResourceId);
		Diagram1 diagram = ModelFactory.eINSTANCE.createDiagram1();
		diagram.setId(org.flowerplatform.model_access_dao.UUID.newUUID());
		resource.getContents().add(diagram);
		
		// create nodes
		Node1 node = addOnDiagram(diagram, "CLASS NODE", class1Id, DAOFactory.codeSyncElementDAO, repoId, ddId, localMappingId, diagramResourceId, diagram.getId());
		node = addOnDiagram(diagram, "MET NODE", met1Id, DAOFactory.codeSyncElementDAO, repoId, ddId, localMappingId, diagramResourceId, node.getId());
		
		String localAppWizardMapping = getResourceId(dd, RegistryDAO.APP_WIZARD_LOCATION);
		addOnDiagram(diagram, "ENTITY NODE", methodsEntityId, DAOFactory.entityDAO, repoId, ddId, localAppWizardMapping, diagramResourceId, diagram.getId());
		
		DAOFactory.registryDAO.saveResource(repoId, ddId, diagramResourceId);
		
		Resource localMapping = loadResource(REPO + "/" + RegistryDAO.FLOWER_PLATFORM_DATA + "/" + RegistryDAO.DISCUSSABLE_DESIGNS + "/" + 
				MY_DISCUSSABLE_DESIGN + "/" + RegistryDAO.MAPPING_LOCATION);
		
		// assert resource info
		assertResourceInfo(localMapping, repoId, ddId, localMappingId);
		
		// assert resource contents
		CodeSyncElement1 folder1 = (CodeSyncElement1) localMapping.getContents().get(1);
		assertCSE(folder1, folder1Id, FOLDER1);
		CodeSyncElement1 class1 = ((CodeSyncElement1EMF) folder1).getChildren().get(0);
		assertCSE(class1, class1Id, CLASS1);
		CodeSyncElement1 met1 = ((CodeSyncElement1EMF) class1).getChildren().get(0);
		assertCSE(met1, met1Id, MET1);
		
		// TODO assert diagram resource ?
		
		printContents(resource, repoId, ddId);
		
		class1 = DAOFactory.codeSyncElementDAO.getCodeSyncElement(repoId, ddId, localMappingId, class1Id);
		class1.setName(class1.getName() + MODIF_FROM_DIAGRAM);
		CodeSyncElement1 met2 = DAOFactory.codeSyncElementDAO.getCodeSyncElement(repoId, ddId, localMappingId, 
				DAOFactory.codeSyncElementDAO.createCodeSyncElement(repoId, ddId, localMappingId, null, class1Id));
		met2.setName("met2" + ADDED_FROM_DIAGRAM);
		
		removeFromDiagram(node, repoId, ddId);
		DAOFactory.registryDAO.saveResource(repoId, ddId, diagramResourceId);

		printContents(resource, repoId, ddId);
		
		List<CodeSyncElement1> globalCodeSyncElements = DAOFactory.codeSyncElementDAO.getCodeSyncElements(repoId, null, localMappingId);
		List<CodeSyncElement1> localCodeSyncElements = DAOFactory.codeSyncElementDAO.getCodeSyncElements(repoId, ddId, localMappingId);
		
		System.out.println("Global Contents");
		for (CodeSyncElement1 cse : globalCodeSyncElements) {
			printCodeSyncElement(cse);
		}
		
		System.out.println("Local Contents");
		for (CodeSyncElement1 cse : localCodeSyncElements) {
			printCodeSyncElement(cse);
		}
		
		assertEquals(3, globalCodeSyncElements.size());
		assertEquals(4, localCodeSyncElements.size());
		
		assertCSE(globalCodeSyncElements.get(0), folder1Id, FOLDER1);
		assertCSE(localCodeSyncElements.get(0), folder1Id, FOLDER1);
		
		assertCSE(globalCodeSyncElements.get(1), class1Id, CLASS1);
		assertCSE(localCodeSyncElements.get(1), class1Id, CLASS1 + MODIF_FROM_DIAGRAM);
		
		assertCSE(globalCodeSyncElements.get(2), met1Id, MET1);
		assertCSE(localCodeSyncElements.get(2), met1Id, MET1);
		
		assertCSE(localCodeSyncElements.get(3), met2.getId(), met2.getName());
	}
	
	private Node1 addOnDiagram(Diagram1 diagram, String name, String codeSyncElementId, CodeSyncElementDAO dao,  String repoId, String ddId, String localMappingId, String diagramResourceId, String parentId) {
		String nodeId = DAOFactory.nodeDAO.createNode(repoId, ddId, diagramResourceId, parentId);
		Node1 node = DAOFactory.nodeDAO.getNode(repoId, ddId, diagramResourceId, nodeId);
		node.setName(name);
		
		CodeSyncElement1 element = dao.getCodeSyncElement(repoId, ddId, localMappingId, codeSyncElementId);
		DAOFactory.nodeDAO.setDiagrammableElement(node, element);
		
		System.out.println("> added on diagram " + element.getName());
		
		return node;
	}
	
	private void removeFromDiagram(Node1 node, String repoId, String discussableDesignId) {
		DAOFactory.nodeDAO.deleteNode(node, repoId, discussableDesignId);
		
		System.out.println("> removed from diagram " + node.getName());
	}
	
	private void printCodeSyncElement(CodeSyncElement1 codeSyncElement) {
		System.out.println(String.format("CSE [id = %s, name = %s]", codeSyncElement.getId(), codeSyncElement.getName()));
	}
	
}
