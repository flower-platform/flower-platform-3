package org.flowerplatform.editor.file;

/**
 * @author Cristina Constantinescu
 * @author Sebastian Solomon
 */
public interface IFileAccessController {

	String getName(Object file);
	
	String getAbsolutePath(Object file);
	
	// get relative path to a location
	String getPath(Object file);
	// path is relative
	Object getFile(String path);
	
	long getLastModifiedTimestamp(Object file);
	
	StringBuffer getContent(Object file);
	
	void setContent(Object file, StringBuffer content);

	boolean isDirectory(Object file);
	
	Object getParentFile(Object file);
	
	boolean createNewFile(Object file);
	
	Object createNewFile(Object file, String name);
	
	boolean exists(Object file);
	
}
