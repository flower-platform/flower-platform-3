package org.flowerplatform.codesync.code.javascript.config.extension;

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
 * @author Cristina Constantinescu
 */
public class AddNewExtension_BackboneCollectionView implements AddNewExtension {
	
	@Override
	public boolean addNew(CodeSyncElement codeSyncElement, View parent,	Resource codeSyncMappingResource, Map<String, Object> parameters) {
		String initializationType = (String) Utils.getValueSafe(parameters, CodeSyncPlugin.CONTEXT_INITIALIZATION_TYPE);
		if (!JavaScriptDescriptors.INIT_TYPE_BACKBONE_COLLECTION_VIEW.equals(initializationType)) {
			return true;
		}
		
		CodeSyncOperationsService.getInstance().setFeatureValue(codeSyncElement, JavaScriptDescriptors.FEATURE_NAME, "CollectionView");	
		
		{
			CodeSyncElement child = CodeSyncOperationsService.getInstance().create(JavaScriptDescriptors.TYPE_JAVASCRIPT_ATTRIBUTE);
			CodeSyncOperationsService.getInstance().setFeatureValue(child, JavaScriptDescriptors.FEATURE_NAME, "sampleObjectGenerator");
			CodeSyncOperationsService.getInstance().add(codeSyncElement, child);		
		}
		
		return true;		
	}
	
}
