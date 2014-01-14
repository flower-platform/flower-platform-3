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

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.properties.remote.Property;
import org.flowerplatform.properties.remote.SelectedItem;

/**
 * @author Razvan Tache
 */
public interface IPropertiesProvider<SEL_ITEM extends SelectedItem, RES_SEL_ITEM> {
		
	/**
	 * @return Should return <code>true</code> if the operation was successful. <code>false</code> otherwise (e.g. if
	 * 		the underlying lock mechanism fails). In this case, the client will request the original value, to update the UI.
	 * 
	 * @author Razvan Tache
	 * @author Cristian Spiescu
	 */
	public boolean setProperty(ServiceInvocationContext context, SEL_ITEM selectedItem, String propertyName, Object propertyValue);
	public List<String> getPropertyNames(SEL_ITEM selectedItem, RES_SEL_ITEM resolvedSelectedItem);
	public Property getProperty(SEL_ITEM selectedItem, RES_SEL_ITEM resolvedSelectedItem, String propertyName);
	public RES_SEL_ITEM resolveSelectedItem(SEL_ITEM selectedItem);
	public Pair<String, String> getIconAndLabel(SEL_ITEM selectedItem, RES_SEL_ITEM resolvedSelectedItem);
	
}
