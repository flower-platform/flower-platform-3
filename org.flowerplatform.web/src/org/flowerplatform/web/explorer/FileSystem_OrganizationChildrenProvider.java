package org.flowerplatform.web.explorer;

import java.util.Collections;


/**
 * Parent node = Organization (i.e. File).<br/>
 * Child node is a virtual item, i.e. Pair<Org File, nodeType>.
 * 
 * @author Cristian Spiescu
 */
public class FileSystem_OrganizationChildrenProvider extends AbstractVirtualItemChildrenProvider {

	protected static final String NODE_TYPE_FILE_SYSTEM = "fileSystem";

	public FileSystem_OrganizationChildrenProvider() {
		super();
		childNodeTypes = Collections.singletonList(NODE_TYPE_FILE_SYSTEM);
	}

}
