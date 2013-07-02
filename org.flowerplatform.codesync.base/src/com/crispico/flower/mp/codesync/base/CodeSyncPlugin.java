package com.crispico.flower.mp.codesync.base;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.osgi.internal.serviceregistry.ServiceRegistry;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.base.communication.CodeSyncEditorStatefulService;
import com.crispico.flower.mp.codesync.base.communication.DiffTreeStatefulService;

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
