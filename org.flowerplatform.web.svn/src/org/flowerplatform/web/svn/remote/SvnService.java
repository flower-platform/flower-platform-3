package org.flowerplatform.web.svn.remote;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.SVNRepositoryURLEntity;
import org.hibernate.Query;
import org.hibernate.Session;



/**
 * @flowerModelElementId _bcYyQAMcEeOrJqcAep-lCg
 */
public class SvnService {
	/**
	 * @flowerModelElementId _yaKVkAMcEeOrJqcAep-lCg
	 */
//	public void createRemoteFolder(List<PathFragment> parentPath,
//			String folderName, String comment) {
//		// TODO implement
//	}
	
	
	public static ArrayList<String> getRepositoriesForOrganization(final String organizationName) {
		
		DatabaseOperationWrapper wrapper = new DatabaseOperationWrapper(new DatabaseOperation() {
			@Override
			public void run() {
				ArrayList<String> result = new ArrayList<String>();				
//				Query q = wrapper.getSession().createQuery(String.format("SELECT e from %s e where e.%s='%s'", SVNRepositoryURLEntity.class.getSimpleName(), "Organization.name", organizationName));
				String myQuerry = String.format("SELECT e from SVNRepositoryURLEntity e where e.organization.name ='%s'", organizationName);
				Query q = wrapper.getSession().createQuery(myQuerry);
				List<SVNRepositoryURLEntity> urlEntityList = q.list();				
				for(SVNRepositoryURLEntity urlEntity : urlEntityList)
						result.add(urlEntity.getName());
					wrapper.setOperationResult(result);				
				
			}
		});		
		return (ArrayList<String>) wrapper.getOperationResult();
	}
	
	
	
}