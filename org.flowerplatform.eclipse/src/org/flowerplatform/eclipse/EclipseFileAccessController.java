package org.flowerplatform.eclipse;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.flowerplatform.editor.file.IFileAccessController;

/**
 * @author Cristina Constantinescu
 */
public class EclipseFileAccessController implements IFileAccessController {

	@Override
	public String getName(Object file) {		
		return ((IFile) file).getName();
	}

	@Override
	public long getLastModifiedTimestamp(Object file) {		
		return ((IFile) file).getLocalTimeStamp();
	}

	@Override
	public InputStream getContent(Object file) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setContent(Object file, String content) {		
		throw new UnsupportedOperationException();
	}

	@Override
	public String getPath(Object file) {
		return ((IFile) file).getFullPath().makeRelativeTo(ResourcesPlugin.getWorkspace().getRoot().getFullPath()).toString();
	}

	@Override
	public Object getFile(String path) {		
		return ResourcesPlugin.getWorkspace().getRoot().getFile(ResourcesPlugin.getWorkspace().getRoot().getFullPath().append(path));
	}

}
