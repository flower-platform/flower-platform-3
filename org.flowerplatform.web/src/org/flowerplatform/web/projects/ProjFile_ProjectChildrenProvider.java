package org.flowerplatform.web.projects;

import org.flowerplatform.web.explorer.AbstractFileWrapperChildrenProvider;

/**
 * Parent node = projFile or project (both of them Pair<File, nodeType>).<br/>
 * Child node = projFile, i.e. Pair<File, nodeType>
 * 
 * There was no need to override {@link #getFile(Object)}.
 * 
 * @author Cristian Spiescu
 */
public class ProjFile_ProjectChildrenProvider extends AbstractFileWrapperChildrenProvider {

	protected static final String NODE_TYPE_PROJ_FILE = "projFile";
	
	@Override
	protected String getNodeType() {
		return NODE_TYPE_PROJ_FILE;
	}
	
}
