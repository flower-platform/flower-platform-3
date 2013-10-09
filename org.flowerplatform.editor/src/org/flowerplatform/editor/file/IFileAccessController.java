package org.flowerplatform.editor.file;

/**
 * @author Cristina Constantinescu
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
	
}
