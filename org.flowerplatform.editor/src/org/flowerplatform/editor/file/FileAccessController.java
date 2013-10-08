package org.flowerplatform.editor.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * @author Cristina Constantinescu
 */
public abstract class FileAccessController implements IFileAccessController {

	@Override
	public long getLastModifiedTimestamp(Object file) {
		return ((File) file).lastModified();
	}

	@Override
	public String getName(Object file) {
		return ((File) file).getName();		
	}

	@Override
	public StringBuffer getContent(Object file) {
		String content;
		try {
			content = FileUtils.readFileToString((File) file);
		} catch (Throwable e) {
			throw new RuntimeException("Error while loading file content " + file, e);
		}
		
		return new StringBuffer(content);
	}

	@Override
	public void setContent(Object file, StringBuffer content) {
		try {
			FileUtils.writeByteArrayToFile((File) file, content.toString().getBytes());
		} catch (IOException e) {
			throw new RuntimeException("Error while saving the file " + file, e);
		}
	}

	@Override
	public String getAbsolutePath(Object file) {		
		return ((File) file).getAbsolutePath();
	}
	
}
