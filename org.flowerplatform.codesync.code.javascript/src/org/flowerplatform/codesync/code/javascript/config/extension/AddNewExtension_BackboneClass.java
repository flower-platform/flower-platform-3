package org.flowerplatform.codesync.code.javascript.config.extension;

import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.codesync.code.javascript.config.JavaScriptDescriptors;
import org.flowerplatform.codesync.config.extension.AddNewExtension;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Cristian Spiescu
 */
public class AddNewExtension_BackboneClass implements AddNewExtension {

	@Override
	public boolean addNew(CodeSyncElement codeSyncElement, View parent,
			Resource codeSyncMappingResource, Map<String, Object> parameters) {
		
		if (!codeSyncElement.getType().equals(JavaScriptDescriptors.TYPE_BACKBONE_CLASS)) {
			return true;
		}
		
		if (!CodeSyncOperationsService.getInstance().hasChildWithKeyFeatureValue(codeSyncElement, JavaScriptDescriptors.TYPE_REQUIRE_ENTRY, "$")) {
			CodeSyncElement child = CodeSyncOperationsService.getInstance().create(JavaScriptDescriptors.TYPE_REQUIRE_ENTRY);
			CodeSyncOperationsService.getInstance().setFeatureValue(child, JavaScriptDescriptors.FEATURE_VAR_NAME, "$");
			CodeSyncOperationsService.getInstance().setFeatureValue(child, JavaScriptDescriptors.FEATURE_DEPENDENCY_PATH, "jquery");
			CodeSyncOperationsService.getInstance().add(codeSyncElement, child);	
		}
		
		if (!CodeSyncOperationsService.getInstance().hasChildWithKeyFeatureValue(codeSyncElement, JavaScriptDescriptors.TYPE_REQUIRE_ENTRY, "_")) {
			CodeSyncElement child = CodeSyncOperationsService.getInstance().create(JavaScriptDescriptors.TYPE_REQUIRE_ENTRY);
			CodeSyncOperationsService.getInstance().setFeatureValue(child, JavaScriptDescriptors.FEATURE_VAR_NAME, "_");
			CodeSyncOperationsService.getInstance().setFeatureValue(child, JavaScriptDescriptors.FEATURE_DEPENDENCY_PATH, "underscore");
			CodeSyncOperationsService.getInstance().add(codeSyncElement, child);
		}
		
		return true;
	}

}
