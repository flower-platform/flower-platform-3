package org.flowerplatform.codesync.config.extension;

import java.util.Map;

import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Cristina Constantinescu
 */
public class InplaceEditorExtension_Default implements InplaceEditorExtension {

	@Override
	public boolean getInplaceEditorText(View view, Map<String, Object> parameters) throws  InplaceEditorParseException {
		CodeSyncElement cse = (CodeSyncElement) view.getDiagrammableElement();
		if (cse == null) {
			return true;
		}	
		
		String inplaceEditorFeature = getInplaceEditorFeature(cse);
		if (inplaceEditorFeature != "") {
			// TODO CC: temporary code (to be replaced will the commented one
			parameters.put(VIEW_TEXT, CodeSyncOperationsService.getInstance().getFeatureValue(cse, FeatureAccessExtension.CODE_SYNC_NAME));
//			parameters.put(VIEW_TEXT, CodeSyncOperationsService.getInstance().getFeatureValue(cse, inplaceEditorFeature));
			return false;
		}
		return true;
	}

	@Override
	public boolean setInplaceEditorText(View view, String text,	Map<String, Object> parameters) throws  InplaceEditorParseException {
		CodeSyncElement cse = (CodeSyncElement) view.getDiagrammableElement();
		String inplaceEditorFeature = getInplaceEditorFeature(cse);
		if (inplaceEditorFeature  != "") {
			// TODO CC: temporary code (to be replaced will the commented one
			CodeSyncOperationsService.getInstance().setFeatureValue(cse, FeatureAccessExtension.CODE_SYNC_NAME, text);
//			CodeSyncOperationsService.getInstance().setFeatureValue(cse, inplaceEditorFeature, text);
			return false;
		}
		return true;
	}
	
	protected String getInplaceEditorFeature(CodeSyncElement cse) {
		CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(cse.getType());
		String inplaceEditorFeature;
		if (descriptor.getInplaceEditorFeature() != null) { // feature detected for ied, use it
			inplaceEditorFeature = descriptor.getInplaceEditorFeature();
		} else { // use default ied feature -> keyFeature
			inplaceEditorFeature = descriptor.getKeyFeature();
		}
		// if "", this extension will not perform its logic
		return inplaceEditorFeature;
	}

}
