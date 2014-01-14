package org.flowerplatform.codesync.code.javascript.config.extension;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.codesync.code.javascript.config.JavaScriptDescriptors;
import org.flowerplatform.codesync.config.extension.AddNewExtension;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.common.util.Utils;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Cristian Spiescu
 */
public class AddNewExtension_BackboneView implements AddNewExtension {

	protected Collection<String> getInitializationTypes() {
		return Collections.singleton(JavaScriptDescriptors.INIT_TYPE_BACKBONE_VIEW);
	}
	
	@Override
	public boolean configNew(CodeSyncElement codeSyncElement, Resource codeSyncMappingResource, Map<String, Object> parameters) {
		String initializationType = (String) Utils.getValueSafe(parameters, CodeSyncPlugin.CONTEXT_INITIALIZATION_TYPE);
		if (!getInitializationTypes().contains(initializationType)) {
			return true;
		}
		doAddNew(codeSyncElement, codeSyncMappingResource, parameters);
		return true;
	}
	
	protected void doAddNew(CodeSyncElement codeSyncElement, Resource codeSyncMappingResource, Map<String, Object> parameters) {
		
		CodeSyncOperationsService.getInstance().setFeatureValue(codeSyncElement, JavaScriptDescriptors.FEATURE_NAME, "View");	
		{
			CodeSyncElement child = CodeSyncOperationsService.getInstance().create(JavaScriptDescriptors.TYPE_JAVASCRIPT_ATTRIBUTE);
			CodeSyncOperationsService.getInstance().setFeatureValue(child, JavaScriptDescriptors.FEATURE_NAME, "tpl");
			CodeSyncOperationsService.getInstance().add(codeSyncElement, child);		
		}
	}

	@Override
	public boolean addNewView(CodeSyncElement codeSyncElement, View parent,	Resource codeSyncMappingResource, Map<String, Object> parameters) {	
		return true;
	}

}
