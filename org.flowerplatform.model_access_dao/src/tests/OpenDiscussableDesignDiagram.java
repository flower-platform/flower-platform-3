package tests;

import static tests.ModelAccessDAOTests.getResourceId;
import static tests.ModelAccessDAOTests.printContents;

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.model_access_dao.DAOFactory;
import org.flowerplatform.model_access_dao.registry.DiscussableDesign;
import org.junit.Test;

public class OpenDiscussableDesignDiagram {

	@Test
	public void test() {
		System.out.println("OPEN DISCUSSABLE DESIGN DIAGRAM");
		
		String repoId = (String) DAOFactory.registryDAO.getRepositories().get(0).getId();
		String discussableDesignId = (String) DAOFactory.registryDAO.getRepository(repoId).getDiscussableDesigns().keySet().toArray()[0];
		
		DiscussableDesign discussableDesign = DAOFactory.registryDAO.getDiscussableDesign(repoId, discussableDesignId);
		String resourceId = getResourceId(discussableDesign, "dgr.notation");
		Resource resource = DAOFactory.registryDAO.loadResource(repoId, discussableDesignId, resourceId);
		
		printContents(resource, repoId, discussableDesignId);
	}

}
