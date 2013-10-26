package org.flowerplatform.editor.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * @author Cristina Constantinescu
 */
public abstract class PlainFileAccessController implements IFileAccessController {

	@Override
	public long getLastModifiedTimestamp(Object file) {
		return ((File) file).lastModified();
	}

	@Override
	public String getName(Object file) {
		return ((File) file).getName();		
	}
	
	/**
	 * @see IOUtils
	 * @see FileUtils	
	 */
	@Override
	public InputStream getContent(Object file) {		
		try {			
			return FileUtils.openInputStream((File) file);
		} catch (Throwable e) {
			throw new RuntimeException("Error while loading file content " + file, e);
		}		
	}

	@Override
	public void setContent(Object file, String content) {
		try {			
			FileUtils.writeStringToFile((File) file, content);
		} catch (IOException e) {
			throw new RuntimeException("Error while saving the file " + file, e);
		}
	}
	
}
