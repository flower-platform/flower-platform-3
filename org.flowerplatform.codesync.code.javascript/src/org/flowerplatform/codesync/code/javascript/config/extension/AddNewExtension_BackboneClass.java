package org.flowerplatform.codesync.code.javascript.config.extension;

import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.codesync.code.javascript.config.JavaScriptDescriptors;
import org.flowerplatform.codesync.operation_extension.AddNewExtension;
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
		{
			CodeSyncElement child = CodeSyncOperationsService.getInstance().create(JavaScriptDescriptors.TYPE_REQUIRE_ENTRY);
			CodeSyncOperationsService.getInstance().setFeatureValue(child, JavaScriptDescriptors.FEATURE_VAR_NAME, "$");
			CodeSyncOperationsService.getInstance().setFeatureValue(child, JavaScriptDescriptors.FEATURE_DEPENDENCY_PATH, "jQuery");
			CodeSyncOperationsService.getInstance().add(codeSyncElement, child);		
		}
		{
			CodeSyncElement child = CodeSyncOperationsService.getInstance().create(JavaScriptDescriptors.TYPE_REQUIRE_ENTRY);
			CodeSyncOperationsService.getInstance().setFeatureValue(child, JavaScriptDescriptors.FEATURE_VAR_NAME, "_");
			CodeSyncOperationsService.getInstance().setFeatureValue(child, JavaScriptDescriptors.FEATURE_DEPENDENCY_PATH, "underscore");
			CodeSyncOperationsService.getInstance().add(codeSyncElement, child);		
		}
		
		return true;
	}

}
