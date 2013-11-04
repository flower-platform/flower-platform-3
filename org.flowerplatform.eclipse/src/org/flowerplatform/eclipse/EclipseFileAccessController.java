package org.flowerplatform.eclipse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.flowerplatform.editor.file.IFileAccessController;

/**
 * @author Cristina Constantinescu
 * @author Sebastian Solomon
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
	
//	@Override
//	public String getAbsolutePath(Object file) {
//		return ((IFile) file).getFullPath().toFile().getAbsolutePath();
//	}

	@Override
	public String getPath(Object file) {
		return ((IFile) file)
				.getFullPath()
				.makeRelativeTo(
						ResourcesPlugin.getWorkspace().getRoot().getFullPath())
				.toString();
	}

	@Override
	public Object getFile(String path) {
		return ResourcesPlugin
				.getWorkspace()
				.getRoot()
				.findMember(path);
	}

	@Override
	public boolean isDirectory(Object file) {
		return ((file instanceof IFolder) || (file instanceof IProject));
	}

	@Override
	public Object getParentFile(Object file) {
		return ((IFile) file).getParent();
	}

	@Override
	public boolean createNewFile(Object file) {
		String contents = "";
		InputStream source = new ByteArrayInputStream(contents.getBytes());
		try {
			((IFile) file).create(source, true, null);
			return true;
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Object createNewFile(Object parent, String digramName) {
		IFile iFile = null;
		if (parent instanceof IFolder) {
			iFile = ((IFolder) parent).getFile(digramName);
		} else if (parent instanceof IProject) {
			iFile = ((IProject) parent).getFile(digramName);
		}
		return iFile;
	}

	@Override
	public boolean exists(Object file) {
		return ((IFile) file).exists();
	}
}
