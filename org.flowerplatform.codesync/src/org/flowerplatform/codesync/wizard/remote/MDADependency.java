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
package org.flowerplatform.codesync.wizard.remote;

import java.util.List;

import org.flowerplatform.codesync.remote.RelationDescriptor;
import org.flowerplatform.properties.remote.Property;

/**
 * @author Cristina Constantinescu
 */
public class MDADependency {

	private String type;
	
	private String label;
	
	private List<Property> properties;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	
	public MDADependency() {	
	}
	
	public MDADependency(RelationDescriptor relationDescriptor, List<Property> properties) {
		this.type = relationDescriptor.getType();
		this.label = relationDescriptor.getLabel();
		this.properties = properties;
	}		
	
}
