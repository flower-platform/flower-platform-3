package tests;

import static org.junit.Assert.assertEquals;
import static tests.ModelAccessDAOTests.assertCSE;
import static tests.ModelAccessDAOTests.assertResourceInfo;
import static tests.ModelAccessDAOTests.createCSEAndAsserNotNull;
import static tests.ModelAccessDAOTests.loadResource;

import java.io.File;

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.model_access_dao.DAOFactory;
import org.flowerplatform.model_access_dao.RegistryDAO;
import org.flowerplatform.model_access_dao.model.CodeSyncElement1;
import org.flowerplatform.model_access_dao.model.CodeSyncElement1EMF;
import org.flowerplatform.model_access_dao.registry.Repository;
import org.junit.Test;

public class CreateGlobalModel {
	
	public static final String REPO = "repos/myRepo";

	public static final String FOLDER1 = "folder1";
	public static final String CLASS1 = "Class1";
	public static final String MET1 = "met1";
	
	public static String folder1Id, class1Id, met1Id;
	
	@Test
	public void test() {
		System.out.println("CREATE GLOBAL MODEL");
		
		clearDir(new File(REPO));
		
		// create repo
		String repoId = DAOFactory.registryDAO.createRepository(REPO);
		Repository repo = DAOFactory.registryDAO.getRepository(repoId);
		
		assertEquals("Global mapping not created", 1, repo.getResources().size());
		
		String globalMappingId = (String) DAOFactory.registryDAO.getRepository(repoId).getResources().keySet().toArray()[0];
		
		// create CodeSyncElements
		CodeSyncElement1 folder1 = createCSEAndAsserNotNull(repoId, null, globalMappingId, null, null);
		folder1.setName(FOLDER1);
		folder1Id = folder1.getId();
		
		CodeSyncElement1 class1 = createCSEAndAsserNotNull(repoId, null, globalMappingId, null, folder1Id);
		class1.setName(CLASS1);
		class1Id = class1.getId();
		
		CodeSyncElement1 met1 = createCSEAndAsserNotNull(repoId, null, globalMappingId, null, class1Id);
		met1.setName(MET1);
		met1Id = met1.getId();
		
		DAOFactory.registryDAO.saveResource(repoId, null, globalMappingId);
		
		Resource resource = loadResource(REPO + "/" + RegistryDAO.GLOBAL_MAPPING_LOCATION);
	
		// assert resource info
		assertResourceInfo(resource, repoId, null, globalMappingId);
		
		// assert resource contents
		folder1 = (CodeSyncElement1) resource.getContents().get(1);
		assertCSE(folder1, folder1Id, FOLDER1);
		class1 = ((CodeSyncElement1EMF) folder1).getChildren().get(0);
		assertCSE(class1, class1Id, CLASS1);
		met1 = ((CodeSyncElement1EMF) class1).getChildren().get(0);
		assertCSE(met1, met1Id, MET1);
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
