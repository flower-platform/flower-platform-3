package org.flowerplatform.web.projects.remote;

import java.util.List;

import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.WorkingDirectory;
import org.hibernate.Query;

public class ProjectsService {
	
	public static ProjectsService getInstance() {
		return (ProjectsService) CommunicationPlugin.getInstance().getServiceRegistry().getService("projectsService");
	}
	
	public void markAsWorkingDir(List<PathFragment> path, long organizationId) {
		
	}
	
	public void importProject(List<PathFragment> path) {
		
	}
	
	@SuppressWarnings("unchecked")
	public List<WorkingDirectory> getWorkingDirectoriesForOrganizationName(final String organizationName) {
		return (List<WorkingDirectory>) new DatabaseOperationWrapper(new DatabaseOperation() {
			@Override
			public void run() {
				Query q = wrapper.createQuery(String.format("SELECT e from WorkingDirectory e where e.organization.name = '%s'", organizationName));			
				wrapper.setOperationResult(q.list());
			}
		}).getOperationResult();
	}
}
