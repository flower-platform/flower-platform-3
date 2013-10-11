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

import org.flowerplatform.properties.Property;
import org.flowerplatform.properties.providers.IPropertiesProvider;
import org.flowerplatform.properties.remote.SelectedItem;
import org.flowerplatform.web.properties.remote.FileSelectedItem;
/**
 * @author Tache Razvan Mihai
 * @return
 */
public class FilePropertiesProvider implements IPropertiesProvider {

	@Override
	public List<Property> getProperties(SelectedItem selectedItem) {
		
		File file = ((FileSelectedItem)selectedItem).getFile();
		List<Property> properties = new ArrayList<Property>();	
		// TODO decide what properties are needed
		properties.add(new Property("Name", file.getName(), false));
		properties.add(new Property("Location", file.getAbsolutePath()));
		properties.add(new Property("Size", file.length()));
		properties.add(new Property("Last modified", new Date(file.lastModified())));
		
		return properties;
	}
	
}
