package org.flowerplatform.web.git.explorer;

import java.io.File;

import org.flowerplatform.web.explorer.AbstractFileWrapperChildrenProvider;
import org.flowerplatform.web.git.GitNodeType;

/**
 * Parent node = gitFile or git Working Directories virtual node (i.e. Pair<File, String>).<br/>
 * Child node is a Pair<File, String>.
 * 
 * @author Cristina Constantinescu
 */
public class GitFile_WorkingDirectoryChildrenProvider extends AbstractFileWrapperChildrenProvider {

	@Override
	protected String getNodeType() {
		return GitNodeType.NODE_TYPE_FILE;
	}

	protected File getFile(Object node) {
		if (node instanceof File) {			
			return (File) node;
		} 
		return super.getFile(node);		
	}
	
}
