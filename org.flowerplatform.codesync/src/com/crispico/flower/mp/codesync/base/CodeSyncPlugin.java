package com.crispico.flower.mp.codesync.base;

import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.base.communication.CodeSyncEditorStatefulService;
import com.crispico.flower.mp.codesync.base.communication.DiffTreeStatefulService;

	public class CodeSyncPlugin extends AbstractFlowerJavaPlugin {
	
	protected static CodeSyncPlugin INSTANCE;
	
	protected ComposedDragOnDiagramHandler dragOnDiagramHandler;
	
	protected ComposedFullyQualifiedNameProvider fullyQualifiedNameProvider;
	
	protected ComposedCodeSyncAlgorithmRunner codeSyncAlgorithmRunner;
	
	public static CodeSyncPlugin getInstance() {
		return INSTANCE;
	}
	
	public ComposedDragOnDiagramHandler getDragOnDiagramHandler() {
		return dragOnDiagramHandler;
	}
	
	public ComposedFullyQualifiedNameProvider getFullyQualifiedNameProvider() {
		return fullyQualifiedNameProvider;
	}
	
	public ComposedCodeSyncAlgorithmRunner getCodeSyncAlgorithmRunner() {
		return codeSyncAlgorithmRunner;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		
		dragOnDiagramHandler = new ComposedDragOnDiagramHandler();
		fullyQualifiedNameProvider = new ComposedFullyQualifiedNameProvider();
		codeSyncAlgorithmRunner = new ComposedCodeSyncAlgorithmRunner();
		
		CommunicationPlugin.getInstance().getServiceRegistry().registerService(CodeSyncEditorStatefulService.SERVICE_ID, new CodeSyncEditorStatefulService());
		CommunicationPlugin.getInstance().getServiceRegistry().registerService(DiffTreeStatefulService.SERVICE_ID, new DiffTreeStatefulService());
	}
	
	
	
}
