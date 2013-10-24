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
import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.properties.remote.SelectedItem;
/**
 * @author Tache Razvan Mihai
 * @return
 */
public class FileSelectedItem extends SelectedItem {
	
	private List<PathFragment> pathWithRoot = new ArrayList<PathFragment>();
	
	public FileSelectedItem() {
		
	}
	
	public List<PathFragment> getPathWithRoot() {
		return pathWithRoot;
	}
	
	public void setPathWithRoot(List<PathFragment> pathWithRoot) {
		this.pathWithRoot = pathWithRoot;
	}

}
