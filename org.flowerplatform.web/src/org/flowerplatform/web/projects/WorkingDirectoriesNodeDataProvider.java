package org.flowerplatform.web.projects;

import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.explorer.AbstractVirtualItemInOrganizationNodeDataProvider;

public class WorkingDirectoriesNodeDataProvider extends AbstractVirtualItemInOrganizationNodeDataProvider {

	public WorkingDirectoriesNodeDataProvider() {
		super();
		nodeLabel = WebPlugin.getInstance().getMessage("explorer.workingDirectories");
		nodeIcon = WebPlugin.getInstance().getResourceUrl("images/workset.gif");
	}

}
