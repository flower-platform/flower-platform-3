package org.flowerplatform.eclipse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
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
		return ((IResource) file).getName();
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
		return ((IResource) file)
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
		return (file instanceof IContainer);
	}

	@Override
	public Object getParentFile(Object file) {
		return ((IResource) file).getParent();
	}

	/**
	 * @author Sebastian Solomon
	 * @author Mariana Gheorghe
	 */
	@Override
	public boolean createNewFile(Object file) {
		if (file instanceof IFolder) {
			IFolder folder = (IFolder) file;
			try {
				folder.create(true, true, null);
				return true;
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
		}
		if (file instanceof IFile) {
			String contents = "";
			InputStream source = new ByteArrayInputStream(contents.getBytes());
			try {
				((IFile) file).create(source, true, null);
				return true;
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
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
		return ((IResource) file).exists();
	}

	@Override
	public String getPathRelativeToFile(Object file, Object relativeTo) {
		IResource iFile = (IResource)file;
		String relative = iFile.getFullPath().makeRelativeTo(((IResource)relativeTo).getFullPath()).toString();
		
		if (relative.length() > 0 && relative.endsWith("/")) {
			relative = relative.substring(0, relative.length() - 1);
		}
		return relative;
	}

	@Override
	public String getAbsolutePath(Object file) {
		return ((IResource)file).getFullPath().toString();
	}

	@Override
	public String getFileExtension(Object file) {
		String extension = ((IResource)file).getFileExtension();
		if (extension == null) {
			return "";
		}
		return extension;
	}

	@Override
	public boolean isFile(Object file) {
		return (file instanceof IFile);
	}

	@Override
	public Class getFileClass() {
		return IResource.class;
	}

	@Override
	public Object[] listFiles(Object folder) {
		if (!isDirectory(folder)) {
			return null;
		}
		if (!exists(folder)){
			return new IResource[0];
		}
		try {
			return ((IContainer)folder).members();
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean delete(Object file) {
		try {
			((IResource)file).delete(true, null);
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	

	@Override
	public String getParent(Object file){
		return ((IFile)file).getParent().toString();
	}

	@Override
	public void rename(Object file, Object dest) {
		// TODO to test
		try {
			((IFile)file).move(((IFile)dest).getFullPath(), true, null);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String readFileToString(Object file) {
		try {
			return IOUtils.toString(((IFile)file).getContents());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @author Sebastian Solomon
	 * @author Mariana Gheorghe
	 */
	@Override
	public void writeStringToFile(Object file, String str) {
		InputStream source = new ByteArrayInputStream(str.getBytes());
		try {
			((IFile) file).setContents(source, true, true, null);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}
	
}
