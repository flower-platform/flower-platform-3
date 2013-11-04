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
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.core.runtime.FileLocator;
import org.flowerplatform.codesync.code.javascript.config.JavaScriptDescriptors;
import org.flowerplatform.codesync.code.javascript.scripting.FlowerScriptLoad;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.mozilla.javascript.Context;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.google.code.scriptengines.js.util.ExtendedScriptException;

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

//		TODO CC: reactivate this mechanism 
//		// descriptors registered from scripts
//		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new Runnable() {
//			@Override
//			public void run() {
//				// search for js files and register them
//				registerExtensionsFromScripts();
//			}
//		});	
		
		CodeSyncPlugin.getInstance().addSrcDir("js");
	}
	
	/**
	 * Loads and executes scripts from codesync/scripts folder.
	 * <p>If the script is JavaScrtipt allows it to load another script from inside. 
	 * The syntax is
	 * <pre>
	 * {@code
	 * utils.load('path/to/script.js');
	 * }
	 * </pre>
	 * The path to script is relative to the scripts folder in CodeSyncPlugin.</p>
	 * 
	 * @author Mircea Negreanu
	 */
	public void registerExtensionsFromScripts() { 
		ScriptEngineManager manager = new ScriptEngineManager();
		Bindings binding = manager.getBindings();

		// build FlowerScriptLoad only if needed (aka js files)
		FlowerScriptLoad scriptLoad = null;
		
		try {
			// get all the scripts from the scripts folder
			URL url = CodeSyncPlugin.getInstance().getBundleContext().getBundle().getResource("scripts");
			File folder = new File(FileLocator.resolve(url).toURI());
			
			// we are interested in only processing files
			for (File file: folder.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.isFile();
				}
			})) {
				ScriptEngine engine = null;
				ScriptContext context = null;
				String scriptExtension = FilenameUtils.getExtension(file.getName());
				// make a special case for js because we use a nonjdk manager
				// (because we want the JavaAdapter from js to also extend classes -
				// and not only interfaces as the one from jdk)
				if ("js".equals(scriptExtension)) {
					engine = manager.getEngineByName("rhino-nonjdk");
					if (scriptLoad == null) {
						context = engine.getContext();
						// script loading is available for js (to load scripts from within scripts)
						scriptLoad = new FlowerScriptLoad(manager, context);
						binding.put("utils", scriptLoad);
					}
				} else {
					engine = manager.getEngineByExtension(scriptExtension);
					if (engine == null) {
						throw new RuntimeException("No ScriptEngine registered for " + file.getName() + ".");
					}
				}
				compileAndExecuteScript(file, engine, context, null);
			}
		} catch (IOException | URISyntaxException ex) {
			throw new RuntimeException(ex);
		} catch (ExtendedScriptException ex) {
			throw new RuntimeException(ex.getCause());
		} catch (ScriptException ex) {
			throw new RuntimeException(ex);
		} finally {
			if (scriptLoad != null) {
				scriptLoad.clearCompileScriptsCache();
			}
		}
	}
	
	/**
	 * Loads, compile (if compilable) and executes a script.
	 * 
	 * @param file
	 * @param engine
	 * @param context
	 * @param fileName file name to appear in the error, if null it is extracted from file
	 * 
	 * @throws ScriptException script compiling/execution error 
	 * @throws IOException reading file error
	 * 
	 * @author Mircea Negreanu
	 */
	public void compileAndExecuteScript(File file, ScriptEngine engine, ScriptContext context, String fileName) throws ScriptException, IOException {
		// compile the script and then execute it
		if (fileName != null) {
			engine.put(ScriptEngine.FILENAME, fileName);
		} else {
			engine.put(ScriptEngine.FILENAME, file.getName());
		}
		
		// for js files we can also set the optimizationLevel for compiling.
		boolean isJS = false;
		if ("js".equals(FilenameUtils.getExtension((String) engine.get(ScriptEngine.FILENAME)))) {
			isJS = true;
		}
		
		if (engine instanceof Compilable) {
			// compile the script if possible
			if (isJS) {
				// set optimization level for compilation/run
				Context cx = Context.enter();
				cx.setOptimizationLevel(9);
			}
			try {
				Compilable compilingEngine = (Compilable) engine;
				CompiledScript script = compilingEngine.compile(FileUtils.readFileToString(file));
				// execute the compiled script
				if (context != null) {
					script.eval(context);
				} else { 
					// need this because 
					// null as parameter will throw a NPE 
					script.eval();
				}
			} finally {
				if (isJS) {
					Context.exit();
				}
			}
		} else {
			// eval the script (if it is not possible to compile it)
			engine.eval(FileUtils.readFileToString(file), context);
		}
	}
}
