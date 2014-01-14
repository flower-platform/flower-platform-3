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
package org.flowerplatform.properties;

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.flowerplatform.blazeds.custom_serialization.CustomSerializationDescriptor;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.editor.remote.EditableResource;
import org.flowerplatform.properties.providers.IPropertiesProvider;
import org.flowerplatform.properties.remote.Property;
import org.flowerplatform.properties.remote.SelectedItem;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author Razvan Tache
 */
public class PropertiesPlugin extends AbstractFlowerJavaPlugin {

	protected static PropertiesPlugin INSTANCE;

	private final static Logger logger = LoggerFactory.getLogger(PropertiesPlugin.class);

	private HashMap<String, IPropertiesProvider<SelectedItem, Object>> propertiesProviders;

	public static final String PROVIDER_EXTENSION_POINT = "org.flowerplatform.properties.propertiesProvider";

	public static PropertiesPlugin getInstance() {
		return INSTANCE;
	}

	public void addPropertiesProvider(String itemType, IPropertiesProvider<SelectedItem, Object> provider) {
		propertiesProviders.put(itemType, provider);
	}

	public HashMap<String, IPropertiesProvider<SelectedItem, Object>> getPropertiesProviders() {
		return propertiesProviders;
	}

	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		initExtensionPoint_propertiesProvider();
		// We do this, so you can use the builder pattern
		new CustomSerializationDescriptor(Property.class)
		.addDeclaredProperty("name")
		.addDeclaredProperty("value")
		.addDeclaredProperty("readOnly")
		.addDeclaredProperty("type")
		.register();
		INSTANCE = this;
	}

	protected void initExtensionPoint_propertiesProvider() throws CoreException {
		propertiesProviders = new HashMap<>();
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor(
				PROVIDER_EXTENSION_POINT);
		for (IConfigurationElement configurationElement : configurationElements) {
			String itemType = configurationElement.getAttribute("itemType");
			IPropertiesProvider<SelectedItem, Object> propertiesProvider = (IPropertiesProvider<SelectedItem, Object>) configurationElement
					.createExecutableExtension("propertiesProviderClass");
			propertiesProviders.put(itemType, propertiesProvider);
			logger.debug("Added property provider mapping with itemType = {} for class = {}", itemType,
					propertiesProvider.getClass());
		}
	}

	
	@Override
	public void registerMessageBundle() throws Exception {
		// no messages yet for java
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}
}
