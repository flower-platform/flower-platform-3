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
package org.flowerplatform.codesync.wizard;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.remote.RelationDescriptor;
import org.flowerplatform.properties.remote.Property;

/**
 * @author Cristina Constantinescu
 */
public class WizardDependencyDescriptor extends RelationDescriptor {

	private List<Property> properties;
	
	private List<String> requiredWizardDependencyTypes;
	
	private String targetCodeSyncElementLocation;
	
	public List<Property> getProperties() {
		return properties;
	}

	public WizardDependencyDescriptor addProperty(Property property) {
		if (properties == null) {
			properties = new ArrayList<Property>();
		}
		properties.add(property);
		return this;
	}

	public List<String> getRequiredWizardDependencyTypes() {
		return requiredWizardDependencyTypes;
	}

	public WizardDependencyDescriptor addRequiredWizardDependencyType(String requiredWizardDependencyType) {
		if (requiredWizardDependencyTypes == null) {
			requiredWizardDependencyTypes = new ArrayList<String>();
		}
		requiredWizardDependencyTypes.add(requiredWizardDependencyType);
		return this;
	}

	public String getTargetCodeSyncElementLocation() {
		return targetCodeSyncElementLocation;
	}

	public WizardDependencyDescriptor setTargetCodeSyncElementLocation(String targetCodeSyncElementLocation) {
		this.targetCodeSyncElementLocation = targetCodeSyncElementLocation;
		return this;
	}
			
}

