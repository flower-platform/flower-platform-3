package org.flowerplatform.web.explorer2;

import org.flowerplatform.web.explorer2.AbstractVirtualItemInOrganizationChildrenProvider;

/**
 * Parent node = Organization (i.e. File).<br/>
 * Child node is a virtual item, i.e. Pair<Org File, nodeType>.
 * 
 * @author Cristian Spiescu
 */
public class FileSystem_OrganizationChildrenProvider extends AbstractVirtualItemInOrganizationChildrenProvider {

	protected static final String NODE_TYPE_FILE_SYSTEM = "fileSystem";

	public FileSystem_OrganizationChildrenProvider() {
		super();
		childNodeType = NODE_TYPE_FILE_SYSTEM;
	}

}
