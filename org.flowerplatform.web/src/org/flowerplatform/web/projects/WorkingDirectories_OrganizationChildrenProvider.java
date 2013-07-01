package org.flowerplatform.web.projects;

import java.util.Collections;

import org.flowerplatform.web.explorer.AbstractVirtualItemChildrenProvider;

/**
 * Parent node = Organization (i.e. File).<br/>
 * Child node is a virtual node, i.e. Pair<Org File, nodeType>.
 * 
 * @author Cristian Spiescu
 */
public class WorkingDirectories_OrganizationChildrenProvider extends AbstractVirtualItemChildrenProvider {

	protected static final String NODE_TYPE_WORKING_DIRECTORIES = "workingDirectories";

	public WorkingDirectories_OrganizationChildrenProvider() {
		super();
		childNodeTypes = Collections.singletonList(NODE_TYPE_WORKING_DIRECTORIES);
	}

}
