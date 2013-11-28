package tests;

import static tests.ModelAccessDAOTests.getResourceId;
import static tests.ModelAccessDAOTests.printContents;

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.model_access_dao.DAOFactory;
import org.flowerplatform.model_access_dao.RegistryDAO;
import org.flowerplatform.model_access_dao.registry.Repository;
import org.junit.Test;

public class MoveAndOpenDiscussableDesignDiagram {

	@Test
	public void test() {
		System.out.println("MOVE AND OPEN DIAGRAM");
		
		Repository repo = DAOFactory.registryDAO.getRepositories().get(0);
		if (repo.getMasterId() == null) {
			repo = DAOFactory.registryDAO.getRepositories().get(1);
		}

		String masterRepoId = repo.getMasterId();
		String slaveRepoId = repo.getId();
		
//		String resourceId = getResourceId(repo, "dgr.notation");
//		String pathRelativeToRepo = "myProj/myDir/mySubDir/" + RegistryDAO.FLOWER_PLATFORM_DIAGRAMS + "/dgr.notation";
//		
//		resourceId = DAOFactory.registryDAO.moveResource(slaveRepoId, resourceId, pathRelativeToRepo, masterRepoId, resourceId);
//		
//		Resource resource = DAOFactory.registryDAO.loadResource(masterRepoId, resourceId);
//		
//		printContents(resource, masterRepoId);
	}
	
}
