package com.crispico.flower.mp.codesync.base;

import org.eclipse.core.runtime.Plugin;
import org.flowerplatform.communication.CommunicationPlugin;
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
		
		CommunicationPlugin.getInstance().getServiceRegistry().registerService(CodeSyncEditorStatefulService.SERVICE_ID, new CodeSyncEditorStatefulService());
		CommunicationPlugin.getInstance().getServiceRegistry().registerService(DiffTreeStatefulService.SERVICE_ID, new DiffTreeStatefulService());
	}
	
}
