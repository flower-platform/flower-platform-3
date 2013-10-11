package org.flowerplatform.editor.file;

import java.io.InputStream;

/**
 * @author Cristina Constantinescu
 */
public interface IFileAccessController {

	String getName(Object file);
	
	// get relative path to a location
	String getPath(Object file);
	// path is relative
	Object getFile(String path) throws Exception;
	
	long getLastModifiedTimestamp(Object file);
	
	InputStream getContent(Object file);
	
	void setContent(Object file, String content);
	
}
