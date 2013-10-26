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
import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.WorkingDirectory;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.flowerplatform.web.properties.remote.FileSelectedItem;
/**
 * @author Razvan Tache
 */
public class FilePropertiesProvider implements IPropertiesProvider<FileSelectedItem, File> {
	
	public static final String PROPERTY_NAME = "Name";
	public static final String PROPERTY_LOCATION = "Location";
	public static final String PROPERTY_SIZE = "Size";
	public static final String PROPERTY_LAST_MODIFIED = "Last Modified";
	public static final String PROPERTY_TEST_ENABLED = "Test Enabled";
	public static final String PROPERTY_TEST_DISABLED = "Test Disabled";

	@Override
	public void setProperty(FileSelectedItem selectedItem, File file, String propertyName, Object propertyValue) {

	}
	
	@Override
	public List<String> getPropertyNames(FileSelectedItem selectedItem, File resolvedSelectedItem) {
		List<String> propertiesNames = new ArrayList<String>();
		
		propertiesNames.add(PROPERTY_NAME);
		propertiesNames.add(PROPERTY_LOCATION);
		propertiesNames.add(PROPERTY_SIZE);
		propertiesNames.add(PROPERTY_LAST_MODIFIED);
		propertiesNames.add(PROPERTY_TEST_ENABLED);
		propertiesNames.add(PROPERTY_TEST_DISABLED);
		
		return propertiesNames;
	}
	@Override
	public Property getProperty(FileSelectedItem selectedItem, File file, String propertyName) {
//		

//		File file = getFile(pathWithRoot);
		
		switch (propertyName) {
			case PROPERTY_NAME: {
				return new Property()
						.setName(PROPERTY_NAME)
						.setValue(file.getName())
						.setReadOnly(false);
			}
			case PROPERTY_LOCATION: {
				return new Property()
					.setName(PROPERTY_LOCATION)
					.setValue(file.getAbsolutePath());
			}
			case PROPERTY_SIZE: {
				return new Property()
					.setName(PROPERTY_SIZE)
					.setValue(file.length());
			}
			case PROPERTY_LAST_MODIFIED: {
				return new Property()
					.setName(PROPERTY_LAST_MODIFIED)
					.setValue(new Date(file.lastModified()));
			}
			case PROPERTY_TEST_ENABLED: {
				return new Property()
					.setName(PROPERTY_TEST_ENABLED)
					.setValue(true)
					.setType("Boolean")
					.setReadOnly(false);
			}
			case PROPERTY_TEST_DISABLED: {
				return new Property()
					.setName(PROPERTY_TEST_DISABLED)
					.setValue(true)
					.setType("Boolean");
			}
		}
		return null;
	}

	@Override
	public File resolveSelectedItem(FileSelectedItem selectedItem) {
		List<PathFragment> pathWithRoot = selectedItem.getPathWithRoot();
		
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
	public Pair<String, String> getIconAndLabel(FileSelectedItem selectedItem, File file) {
		String label;
		String icon;
		if (file.isDirectory()) {
			icon = WebPlugin.getInstance().getResourceUrl("images/folder.gif");
		} else {
			icon = WebPlugin.getInstance().getResourceUrl("images/file.gif");
		}
		label = file.getName();
		
		return new Pair<String, String>(icon, label);
	}
	
}
