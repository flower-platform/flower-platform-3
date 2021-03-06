package org.flowerplatform.editor.file;

import java.io.InputStream;

/**
 * @author Cristina Constantinescu
 * @author Sebastian Solomon
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

	boolean isDirectory(Object file);
	
	Object getParentFile(Object file);
	
	boolean createNewFile(Object file);
	
	Object createNewFile(Object file, String name);
	
	boolean exists(Object file);
	
	String getPathRelativeToFile(Object file, Object relativeTo);
	
	String getAbsolutePath (Object file);
	
	String getFileExtension (Object file);
	
	boolean isFile(Object file);
	
	Class getFileClass();
	
	Object[] listFiles(Object folder);

	boolean delete(Object child);
	
	String getParent(Object file);
	
	void rename(Object file, Object dest);
	
	String readFileToString(Object file);
	
	void writeStringToFile(Object file, String str);
	
}
