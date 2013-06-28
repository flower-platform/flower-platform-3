package org.flowerplatform.web.explorer2;


/**
 * Parent node = fsFile or Working Directories virtual node (i.e. Pair<File, String>).<br/>
 * Child node is a Pair<File, String>.
 * 
 * There was no need
 * to override {@link #getFile(Object)} to get the file from the "File System" node because
 * it is stored in the same format: <code>Pair<file, node_type>/code> where the file points
 * to the organization dir, which is exactly what we need.
 * 
 * @author Cristian Spiescu
 */
public class FsFile_FileSystemChildrenProvider extends AbstractFileWrapperChildrenProvider {

	protected static final String NODE_TYPE_FS_FILE = "fsFile";
	
	@Override
	protected String getNodeType() {
		return NODE_TYPE_FS_FILE;
	}
	
}
