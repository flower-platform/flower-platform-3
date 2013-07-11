package com.crispico.flower.mp.codesync.base;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.base.communication.CodeSyncEditorStatefulService;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

	public class CodeSyncPlugin extends AbstractFlowerJavaPlugin {
	
	protected static CodeSyncPlugin INSTANCE;
	
	protected ComposedFullyQualifiedNameProvider fullyQualifiedNameProvider;
	
	protected ComposedCodeSyncAlgorithmRunner codeSyncAlgorithmRunner;
	
	public static CodeSyncPlugin getInstance() {
		return INSTANCE;
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
		
		fullyQualifiedNameProvider = new ComposedFullyQualifiedNameProvider();
		
		initializeExtensionPoint_codeSyncAlgorithmRunner();
		
		CommunicationPlugin.getInstance().getServiceRegistry().registerService(CodeSyncEditorStatefulService.SERVICE_ID, new CodeSyncEditorStatefulService());
	}
	
	private void initializeExtensionPoint_codeSyncAlgorithmRunner() throws CoreException {
		codeSyncAlgorithmRunner = new ComposedCodeSyncAlgorithmRunner();
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.codesync.codeSyncAlgorithmRunner");
		for (IConfigurationElement configurationElement : configurationElements) {
			String id = configurationElement.getAttribute("id");
			Object instance = configurationElement.createExecutableExtension("codeSyncAlgorithmRunnerClass");
			codeSyncAlgorithmRunner.addRunner((ICodeSyncAlgorithmRunner) instance);
			logger.debug("Added CodeSync algorithm runner with id = {} with class = {}", id, instance.getClass());
		}
	}
	
	public Object getFeatureValue(CodeSyncElement codeSyncElement, EStructuralFeature feature) {
		if (feature.getEContainingClass().isSuperTypeOf(CodeSyncPackage.eINSTANCE.getAstCacheElement())) {
			return codeSyncElement.getAstCacheElement().eGet(feature);
		} else if (feature.getEContainingClass().isSuperTypeOf(CodeSyncPackage.eINSTANCE.getCodeSyncElement())) {
			return codeSyncElement.eGet(feature);
		} 
		
		return null;
	}
	
	public void setFeatureValue(CodeSyncElement codeSyncElement, EStructuralFeature feature, Object value) {
		codeSyncElement.setName(value.toString());
	}
	
}