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
package org.flowerplatform.properties.providers;

import java.util.List;

import org.flowerplatform.properties.remote.Property;
import org.flowerplatform.properties.remote.SelectedItem;

/**
 * @author Razvan Tache
 */
public interface IPropertiesProvider<SEL_ITEM extends SelectedItem, RES_SEL_ITEM> {
	
	public List<String> getPropertyNames(SEL_ITEM selectedItem, RES_SEL_ITEM resolvedSelectedItem);
	public Property getProperty(SEL_ITEM selectedItem, RES_SEL_ITEM resolvedSelectedItem, String propertyName);
	public RES_SEL_ITEM resolveSelectedItem(SEL_ITEM selectedItem);
	public void setProperty(SEL_ITEM selectedItem, RES_SEL_ITEM resolvedSelectedItem, String propertyName, Object propertyValue);
	
}
