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
package org.flowerplatform.codesync.operation_extension;

import java.util.List;

import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;

import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Mariana Gheorghe
 */
public class NamedElementFeatureAccessExtension extends FeatureAccessExtension {

	public static final String NAME = "name";
	
	public NamedElementFeatureAccessExtension(List<CodeSyncElementDescriptor> descriptors) {
		super(descriptors);
		
		addFeature(NAME, CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name());
	}
	
}
