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
package org.flowerplatform.orion.server;

import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;

/**
 * @author Cristina Constantinescu
 */
public class OrionPlugin implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		CodeSyncPlugin.getInstance().setProjectsProvider(new OrionProjectsProvider());
		EditorModelPlugin.getInstance().setModelAccessController(new OrionModelAccessController());
		EditorPlugin.getInstance().setFileAccessController(new OrionFileAccessController());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
	}

}
