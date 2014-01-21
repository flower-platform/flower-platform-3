/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.idea.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import myPackage.FlowerVirtualFileWrapper;

import org.flowerplatform.editor.file.IFileAccessController;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author Sebastian Solomon
 */
public class IdeaFileAccessController implements IFileAccessController {

	@Override
	public String getName(Object file) {
		String path = ((FlowerVirtualFileWrapper) file).getPath();
		int index = path.lastIndexOf("/");
		return (index > 0) ? path.substring(index +1) : path;
	}

	@Override
	public long getLastModifiedTimestamp(Object file) {
        return ((FlowerVirtualFileWrapper)file).getVirtualFile().getTimeStamp();
	}

	@Override
	public InputStream getContent(Object file) {
        //((FlowerVirtualFileWrapper)file).getVirtualFile().contentsToByteArray()
		throw new UnsupportedOperationException();
	}

	@Override
	public void setContent(Object file, String content) {		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getPath(Object virtualFile) {
		return ((FlowerVirtualFileWrapper)virtualFile).getPath();
	}

	@Override
	public Object getFile(String path) throws FileNotFoundException {
		File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException(path);
        }
        return new FlowerVirtualFileWrapper(LocalFileSystem.getInstance().findFileByIoFile(file));
	}

	@Override
	public boolean isDirectory(Object file) {
		return (((FlowerVirtualFileWrapper)file).getVirtualFile() == null) ?  false : ((FlowerVirtualFileWrapper)file).getVirtualFile().isDirectory()  ;
	}

	@Override
	public Object getParentFile(Object file) {
		return new FlowerVirtualFileWrapper(getParent(file));
	}

	@Override
	public boolean createNewFile(Object vFile) {
        //todo (vFile)
        File file = new File(((FlowerVirtualFileWrapper)vFile).getPath());
		boolean isFileCreated = false;
		try {
            isFileCreated =  file.createNewFile();
            LocalFileSystem.getInstance().refresh(true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
        return isFileCreated;
	}

	@Override
	public Object createNewFile(Object parent, String digramName) {
		String path = ((FlowerVirtualFileWrapper) parent).getPath() + "/"
				+ digramName;
		File file = new File(path);
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		LocalFileSystem.getInstance().refresh(true);
		return new FlowerVirtualFileWrapper(path);
	}
	
	@Override
	public Object getFile(Object parent, String name) {
		 return new FlowerVirtualFileWrapper(((FlowerVirtualFileWrapper) parent).getPath() + "/" + name);
	}

	@Override
	public boolean exists(Object file) {
        VirtualFile vFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(((FlowerVirtualFileWrapper)file).getPath());
        if (vFile == null) {
            return false;
        }
        return vFile.exists();
	}

	@Override
	public String getPathRelativeToFile(Object file, Object relativeTo) {
        String sRelativeTo = ((FlowerVirtualFileWrapper)relativeTo).getPath();
        String path = ((FlowerVirtualFileWrapper)file).getPath();
        return path.substring(path.indexOf(sRelativeTo) + sRelativeTo.length());
	}

	@Override
	public String getAbsolutePath(Object file) {
		return ((FlowerVirtualFileWrapper)file).getPath();
	}

	@Override
	public String getFileExtension(Object file) {
        String path = ((FlowerVirtualFileWrapper)file).getPath();
        int index = ((FlowerVirtualFileWrapper)file).getPath().lastIndexOf('.');
        if (index > 0) {
            return path.substring(index + 1);
        }
        return "";
	}

	@Override
	public boolean isFile(Object file) {
		return (file instanceof FlowerVirtualFileWrapper);
	}

	@Override
	public Class getFileClass() {
		return FlowerVirtualFileWrapper.class;
	}

	@Override
	public Object[] listFiles(Object folder) {
        VirtualFile vFile = ((FlowerVirtualFileWrapper)folder).getVirtualFile();
        if (vFile == null) {
			return null;
		} else {
            VirtualFile[] virtualFiles = vFile.getChildren();
            FlowerVirtualFileWrapper [] virtualFileWrappers = new FlowerVirtualFileWrapper[virtualFiles.length];
            for (int i = 0; i < virtualFiles.length; i++) {
                virtualFileWrappers[i] = new FlowerVirtualFileWrapper(virtualFiles[i]);
            }
            return virtualFileWrappers;
		}
	}

	@Override
	public boolean delete(Object vFileW) {
		File fileToDelete = new File(((FlowerVirtualFileWrapper)vFileW).getPath());
		vFileW = null;
		boolean bool = fileToDelete.delete();
		LocalFileSystem.getInstance().refresh(true);
		return bool;
	}
	

	@Override
	public String getParent(Object file){
		String path = ((FlowerVirtualFileWrapper) file).getPath();
        int index = path.lastIndexOf("/");
        return (index > 0) ? path.substring(0 , index) : null;
	}

	@Override
	public void rename(Object file, Object dest) {
		// TODO to test
		try {
			((FlowerVirtualFileWrapper) file).getVirtualFile().rename(
					null, ((FlowerVirtualFileWrapper) dest).getVirtualFile().getName());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String readFileToString(Object file) {
		try {
            return new String(((FlowerVirtualFileWrapper) file).getVirtualFile().contentsToByteArray(), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public void writeStringToFile(Object file, String str) {
        VirtualFile vf = ((FlowerVirtualFileWrapper)file).getVirtualFile();
        if (vf == null) {
            vf = LocalFileSystem.getInstance().findFileByPath(((FlowerVirtualFileWrapper)file).getPath());
        }
		try {
			vf.setBinaryContent(str.getBytes());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
