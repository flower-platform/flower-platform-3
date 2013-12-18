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

import org.flowerplatform.editor.file.IFileAccessController;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;


/**
 * @author Sebastian Solomon
 */
public class IdeaFileAccessController implements IFileAccessController {

	@Override
	public String getName(Object file) {
		return ((VirtualFile) file).getName();
	}

	@Override
	public long getLastModifiedTimestamp(Object file) {
        return ((VirtualFile)file).getTimeStamp();
	}

	@Override
	public InputStream getContent(Object file) {
        //((VirtualFile)file).contentsToByteArray()
		throw new UnsupportedOperationException();
	}

	@Override
	public void setContent(Object file, String content) {		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getPath(Object virtualFile) {
		// TODO test
//		File file = new File(((VirtualFile)virtualFile).getPath());
//		return CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(file);
		
		return ((VirtualFile)virtualFile).getPath();
	}

	@Override
	public Object getFile(String path) throws FileNotFoundException {
        //File file = new File(MyEditor.getWorkspaceRoot(), path);
		File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException(path);
        }
        return LocalFileSystem.getInstance().findFileByIoFile(file);
	}

	@Override
	public boolean isDirectory(Object file) {
		//TODO test
		return (((VirtualFile)file).isDirectory());
	}

	@Override
	public Object getParentFile(Object file) {
		return ((VirtualFile) file).getParent();
	}

	@Override
	public boolean createNewFile(Object file) {
		//TODO test
		boolean isFileCreated = false;
		try {
            isFileCreated = ((File) file).createNewFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		//VirtualFileManager.getInstance().asyncRefresh(null);
        return isFileCreated;
	}

	@Override
	public Object createNewFile(Object parent, String digramName) {
		//TODO test
//        File f = new File(((VirtualFile)parent).getPath(), digramName);
//        try {
//            f.createNewFile();
//            ((VirtualFile) parent).refresh(false, true);
//            return LocalFileSystem.getInstance().findFileByIoFile(f); //nu merge, intoarce null
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        try {
           return ((VirtualFile)parent).createChildData(null, digramName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
	
	@Override
	public Object getFile(Object parent, String name) {
		 return ((VirtualFile) parent).findChild(name);
	}

	@Override
	public boolean exists(Object file) {
		//TODO test
        if (file == null) {
            return false;
        }
		return ((VirtualFile) file).exists();
	}

	@Override
	public String getPathRelativeToFile(Object file, Object relativeTo) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getAbsolutePath(Object file) {
		return ((VirtualFile)file).getPath();
	}

	@Override
	public String getFileExtension(Object file) {
		return ((VirtualFile)file).getExtension();
		
	}

	@Override
	public boolean isFile(Object file) {
		//TODO test
		return (!((VirtualFile)file).isDirectory());
	}

	@Override
	public Class getFileClass() {
		return VirtualFile.class;
	}

	@Override
	public Object[] listFiles(Object folder) {
		//TODO test
		return ((VirtualFile)folder).getChildren();
	}

	@Override
	public boolean delete(Object file) {
		//TODO test
		File fileToDelete = new File(((VirtualFile)file).getPath());
		return fileToDelete.delete();
//		VirtualFileManager.getInstance().asyncRefresh(null);
	
		
	}
	

	@Override
	public String getParent(Object file){
		return ((VirtualFile)file).getParent().getPath();
	}

	@Override
	public void rename(Object file, Object dest) {
		// TODO to test
		try {
			((VirtualFile)file).rename(null, ((VirtualFile)dest).getName());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String readFileToString(Object file) {
		//TODO test
		try {
			//((VirtualFile)file).getOutputStream(null);
			return ((VirtualFile)file).contentsToByteArray().toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public void writeStringToFile(Object file, String str) {
        //TODO test
		try {
			((VirtualFile)file).setBinaryContent(str.getBytes());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
