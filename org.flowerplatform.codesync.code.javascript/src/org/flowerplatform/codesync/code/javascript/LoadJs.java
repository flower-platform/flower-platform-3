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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.ToolErrorReporter;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;

/**
 * Utility class for functions called from JS.
 * 
 * @author Mircea Negreanu
 */
public class LoadJs {
	private static Map<String, String> compiledScriptsNameCache = new HashMap<>();
	
	/**
	 * Called from js scripts to load another script (also compiles and evaluates it) 
	 * 
	 * <p>Stores the name of the script in a cache, so that it is not compiled and evaluated
	 * every time a script requests it</p>
	 */
	public static void load(Context cx, Scriptable thisObj, Object[] args, Function func) {
		for (Object arg: args) {
			String fileName = Context.toString(arg);
			
			if (compiledScriptsNameCache.containsKey(fileName)) {
				// already compiled (so should be available in the context
				// exit
				return;
			}
			
			// add to the cache (so it is not compiled again, on the same session)
			compiledScriptsNameCache.put(fileName,  fileName);
			
			System.out.println("Compiling " + fileName);
			
			try {
				URL url = CodeSyncPlugin.getInstance().getBundleContext().getBundle().getResource("scripts");
				File folder = new File(FileLocator.resolve(url).toURI());
				
				File fl = new File(folder, fileName);
				
				Script compScript = cx.compileString(FileUtils.readFileToString(fl), fileName, 1, null);
				compScript.exec(cx, thisObj);
			} catch (IOException | URISyntaxException ex) {
				String msg = ToolErrorReporter.getMessage(
						"msg.couldnt.read.source", fileName, ex.getMessage());
				throw Context.reportRuntimeError(msg);
			}
		}
	}
	
	/**
	 * Clears the filename cache.
	 */
	public static void clearCompileScriptsCache() {
		compiledScriptsNameCache.clear();
	}
	
}
