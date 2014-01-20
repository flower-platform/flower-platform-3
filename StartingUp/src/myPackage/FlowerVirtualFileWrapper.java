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

package myPackage;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

public class FlowerVirtualFileWrapper {
    private VirtualFile virtualFile;
    private String path;

    public FlowerVirtualFileWrapper(VirtualFile virtualFile){
           this.virtualFile = virtualFile;
           path = virtualFile.getPath();
    }

    public FlowerVirtualFileWrapper(String path){
        this.path = path;
        this.virtualFile = null;
    }

    public String getPath() {
        return path;
    }

    public VirtualFile getVirtualFile() {
        if (virtualFile == null) {
            virtualFile = LocalFileSystem.getInstance().findFileByPath(path);
        }
        return virtualFile;
    }

}
