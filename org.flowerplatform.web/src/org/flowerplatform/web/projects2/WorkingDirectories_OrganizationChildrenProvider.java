package org.flowerplatform.web.projects2;

import org.flowerplatform.web.explorer2.AbstractVirtualItemInOrganizationChildrenProvider;

/**
 * Parent node = Organization (i.e. File).<br/>
 * Child node is a virtual node, i.e. Pair<Org File, nodeType>.
 * 
 * @author Cristian Spiescu
 */
public class WorkingDirectories_OrganizationChildrenProvider extends AbstractVirtualItemInOrganizationChildrenProvider {

	protected static final String NODE_TYPE_WORKING_DIRECTORIES = "workingDirectories";

	public WorkingDirectories_OrganizationChildrenProvider() {
		super();
		childNodeType = NODE_TYPE_WORKING_DIRECTORIES;
	}

}
