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
package org.flowerplatform.codesync.code.javascript;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.flowerplatform.codesync.code.javascript.config.JavaScriptDescriptors;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncCodeJavascriptPlugin extends AbstractFlowerJavaPlugin {

	protected static CodeSyncCodeJavascriptPlugin INSTANCE;
	
	public static String TECHNOLOGY = "js";
	
	public static CodeSyncCodeJavascriptPlugin getInstance() {
		return INSTANCE;
	}

	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		
		// descriptors for js code
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new JavaScriptDescriptors());
	
		// descriptors registered from sripts
		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new Runnable() {
			@Override
			public void run() {
				// search for js files and register them
				jsScriptExtensions();
			}
		});		

		CodeSyncPlugin.getInstance().addSrcDir("js");
	}
			
	/**
	 * Loads and executes javascript files from codesync/scripts
	 * 
	 * @author Mircea Negreanu
	 */
	public void jsScriptExtensions() {
		// use rhino as a scripting engine instead of javax.scripting as we want to give
		// the users the possibility to extend existing java classes (and not only implement
		// interfaces)
		Context cx = Context.enter();
		try {
			// we want ImporterTopLevel so we can just write importClass inside the js and 
			// not use a JavaImporter()
			Scriptable scope = new ImporterTopLevel(cx);
			
			URL url = CodeSyncPlugin.getInstance().getBundleContext().getBundle().getResource("scripts");
			File folder = new File(FileLocator.resolve(url).toURI());
			// read each file and evaluate it
			for (File file: folder.listFiles()) {
				cx.evaluateString(scope, FileUtils.readFileToString(file), file.getName(), 0, null);
			}
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException("JS scripts loading error", e);
		} finally {
			Context.exit();
		}
	}
	
}
