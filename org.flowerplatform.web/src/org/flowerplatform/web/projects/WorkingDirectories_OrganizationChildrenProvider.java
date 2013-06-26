package org.flowerplatform.web.projects;

import org.flowerplatform.web.explorer.AbstractVirtualItemInOrganizationChildrenProvider;

public class WorkingDirectories_OrganizationChildrenProvider extends AbstractVirtualItemInOrganizationChildrenProvider {

	public static final String NODE_TYPE_WORKING_DIRECTORIES = "workingDirectories";

	public WorkingDirectories_OrganizationChildrenProvider() {
		super();
		childNodeType = NODE_TYPE_WORKING_DIRECTORIES;
	}

}
