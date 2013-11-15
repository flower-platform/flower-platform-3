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
package org.flowerplatform.codesync.regex;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.regex.ide.remote.RegexMatchDto;
import org.flowerplatform.codesync.regex.ide.remote.RegexMatchSelectedItem;
import org.flowerplatform.codesync.regex.ide.remote.RegexSubMatchDto;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.properties.providers.IPropertiesProvider;
import org.flowerplatform.properties.remote.Property;
import org.flowerplatform.properties.remote.SelectedItem;

/**
 * @author Cristina Constantinescu
 */
public class RegexMatchPropertiesProvider implements IPropertiesProvider {

	@Override
	public List<Property> getProperties(SelectedItem selectedItem) {
		RegexMatchDto match = ((RegexMatchSelectedItem) selectedItem).getMatch();

		List<Property> properties = new ArrayList<Property>();	
		
		properties.add(new Property("Regex Name", match.getRegexName()));
		properties.add(new Property("Start", match.getStart().toString()));
		properties.add(new Property("End", match.getEnd().toString()));
		
		if (match.getSubMatches() != null) {
			for (RegexSubMatchDto subMatch : match.getSubMatches()) {
				properties.add(new Property(String.format("$%d", match.getSubMatches().indexOf(subMatch) + 1), subMatch.getValue()));
			}
		}
		return properties;
	}

	@Override
	public List<String> getPropertyNames() {
		// doesn't support this
		return null;
	}

	@Override
	public Property getProperty(SelectedItem selectedItem, String propertyName) {	
		// doesn't support this
		return null;
	}
	
	@Override
	public boolean setProperty(ServiceInvocationContext context, SelectedItem selectedItem, String propertyName, Object propertyValue) {		
		// doesn't support this
		return false;
	}

}
