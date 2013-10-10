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
package org.flowerplatform.common;

import java.io.File;
import java.io.InputStream;

import org.eclipse.core.resources.ResourcesPlugin;
import org.flowerplatform.common.file_event.FileEventDispatcher;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;


/**
 * @author Cristi
 */
public class CommonPlugin extends AbstractFlowerJavaPlugin {

	protected static CommonPlugin INSTANCE;
	
	public static CommonPlugin getInstance() {
		return INSTANCE;
	}
	
	public static final String VERSION = "2.0.0.M2_2013-06-04";
	
	/**
	 * @author Mariana
	 */
	private FlowerWebProperties flowerWebProperties;
	
	/**
	 * @author Tache Razvan Mihai
	 */
	private FileEventDispatcher fileEventDispatcher =  new FileEventDispatcher();
	
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	public File getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot().getRawLocation().makeAbsolute().toFile();
	}

	public String getPathRelativeToWorkspaceRoot(File file) {
		return getPathRelativeToFile(file, getWorkspaceRoot());
	}
	
	public String getPathRelativeToFile(File file, File relativeTo) {
		String relative = relativeTo.toURI().relativize(file.toURI()).getPath();
		if (relative.length() > 0 && relative.endsWith("/")) {
			relative = relative.substring(0, relative.length() - 1);
		}
		return relative;
	}
	
	/**
	 * Initialized from the web plugin.
	 * 
	 * @author Mariana
	 */
	public FlowerWebProperties getFlowerWebProperties() {
		return flowerWebProperties;
	}

	/**
	 * @author Mariana
	 */
	public void initializeProperties(InputStream inputStream) {
		flowerWebProperties = new FlowerWebProperties(inputStream);
	}
	
	/**
	 * @author Tache Razvan Mihai
	 */
	public FileEventDispatcher getFileEventDispatcher() {
		return fileEventDispatcher;
	}
}