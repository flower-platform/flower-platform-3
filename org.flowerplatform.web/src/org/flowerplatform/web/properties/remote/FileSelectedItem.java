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
package org.flowerplatform.web.properties.remote;

import java.io.File;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.properties.remote.SelectedItem;
/**
 * @author Tache Razvan Mihai
 * @return
 */
public class FileSelectedItem extends SelectedItem {
	
	private final String TYPE_FILE = "File";

	private String fileName;
	
	public FileSelectedItem(String fileName, File file) {
		super();
		this.setItemType(TYPE_FILE);
		this.fileName = fileName;
	}
	public FileSelectedItem() {
		
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public File getFile() {
		return new File(CommonPlugin.getInstance().getWorkspaceRoot(), fileName);
	}

}
