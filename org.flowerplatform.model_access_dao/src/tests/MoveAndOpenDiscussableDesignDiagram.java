package tests;

import static tests.ModelAccessDAOTests.getResourceId;
import static tests.ModelAccessDAOTests.printContents;

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.model_access_dao.DAOFactory;
import org.flowerplatform.model_access_dao.RegistryDAO;
import org.flowerplatform.model_access_dao.registry.DiscussableDesign;
import org.junit.Test;

public class MoveAndOpenDiscussableDesignDiagram {

	@Test
	public void test() {
		System.out.println("MOVE AND OPEN DIAGRAM");
		
		String repoId = (String) DAOFactory.registryDAO.getRepositories().get(0).getId();
		String ddId = (String) DAOFactory.registryDAO.getRepository(repoId).getDiscussableDesigns().keySet().toArray()[0];
		
		DiscussableDesign discussableDesign = DAOFactory.registryDAO.getDiscussableDesign(repoId, ddId);
		String resourceId = getResourceId(discussableDesign, "dgr.notation");
		String pathRelativeToRepo = "myProj/myDir/mySubDir/" + RegistryDAO.FLOWER_PLATFORM_DIAGRAMS + "/dgr.notation";
		
		resourceId = DAOFactory.registryDAO.moveResource(repoId, ddId, resourceId, pathRelativeToRepo, repoId, null, resourceId);
		
		Resource resource = DAOFactory.registryDAO.loadResource(repoId, null, resourceId);
		
		printContents(resource, repoId, null);
	}
	
}
