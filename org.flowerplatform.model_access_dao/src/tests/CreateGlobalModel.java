package tests;

import static org.junit.Assert.assertEquals;
import static tests.ModelAccessDAOTests.assertCSE;
import static tests.ModelAccessDAOTests.assertResourceInfo;
import static tests.ModelAccessDAOTests.createCSEAndAssertNotNull;
import static tests.ModelAccessDAOTests.getResourceId;
import static tests.ModelAccessDAOTests.loadResource;

import java.io.File;
import java.util.UUID;

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.model_access_dao.DAOFactory;
import org.flowerplatform.model_access_dao.RegistryDAO;
import org.flowerplatform.model_access_dao.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao.registry.Repository;
import org.junit.Test;

public class CreateGlobalModel {
	
	public static final String REPO = "repos/myRepo";

	public static final String FOLDER1 = "folder1";
	public static final String CLASS1 = "Class1";
	public static final String MET1 = "met1";
	
	public static UUID folder1Id, class1Id, met1Id, methodsEntityId;
	
	@Test
	public void test() {
		clearDir(new File(REPO));
		
		System.out.println("CREATE GLOBAL MODEL WITH EMF");
		DAOFactory.internal = DAOFactory.emf;
		
		// create repo
		UUID repoId = DAOFactory.registryDAO.createRepository(REPO, null);
		Repository repo = DAOFactory.registryDAO.getRepository(repoId);
		
//		assertEquals("Resources not created", 2, repo.getResources().size());
		
//		String globalMappingId = getResourceId(repo, RegistryDAO.MAPPING_LOCATION); 
		UUID globalMappingId = repo.getResourcesAlias().get(Repository.MAPPING);
		
		// create CodeSyncElements
		CodeSyncElement1 folder1 = createCSEAndAssertNotNull(repoId, globalMappingId, null, null);
		folder1.setName(FOLDER1);
		folder1Id = UUID.fromString(folder1.getId());
		
		CodeSyncElement1 class1 = createCSEAndAssertNotNull(repoId, globalMappingId, null, folder1Id);
		class1.setName(CLASS1);
		class1Id = UUID.fromString(class1.getId());
		
		CodeSyncElement1 met1 = createCSEAndAssertNotNull(repoId, globalMappingId, null, class1Id);
		met1.setName(MET1);
		met1Id = UUID.fromString(met1.getId());
		
		DAOFactory.codeSyncElementDAO.updateCodeSyncElement(repoId, globalMappingId, folder1);
		DAOFactory.codeSyncElementDAO.updateCodeSyncElement(repoId, globalMappingId, class1);
		DAOFactory.codeSyncElementDAO.updateCodeSyncElement(repoId, globalMappingId, met1);
		
//		DAOFactory.registryDAO.saveResource(repoId, globalMappingId);
//		
//		Resource resource = loadResource(REPO + "/" + RegistryDAO.GLOBAL_MAPPING_LOCATION);
//	
//		// assert resource info
//		assertResourceInfo(resource, repoId, globalMappingId);
//		
//		// assert resource contents
//		folder1 = (CodeSyncElement1) resource.getContents().get(1);
//		assertCSE(folder1, folder1Id, FOLDER1);
//		class1 = folder1.getChildren().get(0);
//		assertCSE(class1, class1Id, CLASS1);
//		met1 = class1.getChildren().get(0);
//		assertCSE(met1, met1Id, MET1);
		
//		String globalAppWizardId = getResourceId(repo, RegistryDAO.APP_WIZARD_LOCATION);
		UUID globalAppWizardId = repo.getResourcesAlias().get(Repository.APP_WIZARD);
		
		// create entities
		CodeSyncElement1 methods = createCSEAndAssertNotNull(repoId, globalAppWizardId, null, null);
		methods.setName("methods");
		methodsEntityId = UUID.fromString(methods.getId());
		
		DAOFactory.codeSyncElementDAO.updateCodeSyncElement(repoId, globalAppWizardId, methods);
		
		met1 = DAOFactory.codeSyncElementDAO.getCodeSyncElement(repoId, globalMappingId, met1Id);
		DAOFactory.codeSyncElementDAO.addRelation(methods, met1, repoId, globalAppWizardId);
		
//		DAOFactory.registryDAO.saveResource(repoId, globalAppWizardId);
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
