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
