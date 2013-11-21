package tests;

import static tests.ModelAccessDAOTests.getResourceId;
import static tests.ModelAccessDAOTests.printContents;

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.model_access_dao.DAOFactory;
import org.flowerplatform.model_access_dao.registry.Repository;
import org.junit.Test;

public class OpenDiscussableDesignDiagram {

	@Test
	public void test() {
		System.out.println("OPEN DISCUSSABLE DESIGN DIAGRAM");
		
		Repository repo = DAOFactory.registryDAO.getRepositories().get(0);
		if (repo.getMasterId() == null) {
			repo = DAOFactory.registryDAO.getRepositories().get(1);
		}
		String repoId = repo.getId();
		
		Repository discussableDesign = DAOFactory.registryDAO.getRepository(repoId);
		String resourceId = getResourceId(discussableDesign, "dgr.notation");
		Resource resource = DAOFactory.registryDAO.loadResource(repoId, resourceId);
		
		printContents(resource, repoId);
	}

}
