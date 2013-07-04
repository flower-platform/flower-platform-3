package com.crispico.flower.mp.codesync.base;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * @author Mariana
 */
public class CodeSyncPlugin extends Plugin {

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
//		ServiceRegistry.INSTANCE.registerService(CodeSyncEditorStatefulService.SERVICE_ID, new CodeSyncEditorStatefulService(CodeSyncEditorStatefulService.CODE_SYNC_EDITOR));
//		ServiceRegistry.INSTANCE.registerService(DiffTreeStatefulService.SERVICE_ID, new DiffTreeStatefulService());
	}
	
}
