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
package org.flowerplatform.web.properties;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.properties.providers.IPropertiesProvider;
import org.flowerplatform.properties.remote.Property;
import org.flowerplatform.properties.remote.SelectedItem;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.WorkingDirectory;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.flowerplatform.web.properties.remote.FileSelectedItem;
/**
 * @author Razvan Tache
 */
public class FilePropertiesProvider implements IPropertiesProvider {

	private File getFile(List<PathFragment> pathWithRoot) {
		
		Object object = GenericTreeStatefulService.getNodeByPathFor(
				pathWithRoot, null);

		if (object instanceof WorkingDirectory) {
			String orgName = ((WorkingDirectory) object).getOrganization().getName();
			File orgDir = ProjectsService.getInstance().getOrganizationDir(orgName);
			String path = orgDir.getPath() + "/" + ((WorkingDirectory) object).getPathFromOrganization();
			return new File(path);
		} else if (object instanceof File) {
			return (File)object;
		} else {
			return ((Pair<File, Object>) object).a;
		}
	}
	@Override
	public List<Property> getProperties(SelectedItem selectedItem) {
		// proccessing step;
		List<PathFragment> pathWithRoot = ((FileSelectedItem)selectedItem).getPathWithRoot();

		File file = getFile(pathWithRoot);
		
		List<Property> properties = new ArrayList<Property>();	
		// TODO decide what properties are needed
		properties.add(new Property("Name", file.getName(), false));
		properties.add(new Property("Location", file.getAbsolutePath()));
		properties.add(new Property("Size", file.length()));
		properties.add(new Property("Last modified", new Date(file.lastModified())));
		properties.add(new Property("testEnabled", true, false));
		properties.add(new Property("testDisabled", true));
		
		return properties;
	}

	@Override
	public void setProperty(SelectedItem selectedItem, String propertyName, Object propertyValue) {

	}
	@Override
	public List<String> getPropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Property getProperty(SelectedItem selectedItem, String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
