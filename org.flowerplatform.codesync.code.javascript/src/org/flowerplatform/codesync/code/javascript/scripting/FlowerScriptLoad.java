package org.flowerplatform.codesync.code.javascript.scripting;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.eclipse.core.runtime.FileLocator;
import org.flowerplatform.codesync.code.javascript.CodeSyncCodeJavascriptPlugin;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.google.code.scriptengines.js.util.ExtendedScriptException;

/**
 * Allows loading scripts from scripts
 * 
 * @author Mircea Negreanu
 */
public class FlowerScriptLoad {
	// temporary cache to allow us not to compile a loaded script multiple times
	private Map<String, String> compiledScriptsNameCache = new HashMap<>();
	
	private ScriptEngineManager manager;
	
	private ScriptContext context;
	
	public FlowerScriptLoad(ScriptEngineManager manager, ScriptContext context) {
		this.manager = manager;
		this.context = context;
	}
	
	/**
	 * Called from js scripts to load another script (also compiles and evaluates it) 
	 * 
	 * <p>Stores the name of the script in a cache, so that it is not compiled and evaluated
	 * every time a script requests it</p>
	 */
	public void load(String fileName) {
		if (compiledScriptsNameCache.containsKey(fileName)) {
			// already compiled (so should be available in the context)
			// exit
			return;
		}
		
		// add to the cache (so it is not compiled again, on the same session)
		compiledScriptsNameCache.put(fileName,  fileName);
		
		try {
			URL url = CodeSyncPlugin.getInstance().getBundleContext().getBundle().getResource("scripts");
			File folder = new File(FileLocator.resolve(url).toURI());
			
			File fl = new File(folder, fileName);
			if (fl.getName() != null && fl.getName().toLowerCase().endsWith(".js")) {
				ScriptEngine engine = manager.getEngineByName("rhino-nonjdk");
				// compile and execute the script
				CodeSyncCodeJavascriptPlugin.getInstance().compileAndExecuteScript(fl, engine, context, fileName);
			}
		} catch (IOException | URISyntaxException ex) {
			throw new RuntimeException(ex);
		} catch (ExtendedScriptException ex) {
			throw new RuntimeException(ex.getCause());
		} catch (ScriptException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * Clears the filename cache.
	 */
	public void clearCompileScriptsCache() {
		compiledScriptsNameCache.clear();
	}
	
	public void setManager(ScriptEngineManager manager) {
		this.manager = manager;
	}
	
	public void setContext(ScriptContext context) {
		this.context = context;
	}
}
