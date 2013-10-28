package org.flowerplatform.codesync.config.extension;

import java.util.IllegalFormatException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.common.ied.InplaceEditorException;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

public class InplaceEditorExtension_RegExFormat implements InplaceEditorExtension {

	private String codeSyncType;
	private String[] features;
	private String generateFormat;
	private String parseRegEx;
		
	public InplaceEditorExtension_RegExFormat(String codeSyncType, String[] features, String generateFormat, String parseRegEx) {
		this.features = features;
		this.generateFormat = generateFormat;
		this.parseRegEx = parseRegEx;
		this.codeSyncType = codeSyncType;
	}

	@Override
	public boolean getInplaceEditorText(View view,	Map<String, Object> parameters) throws  InplaceEditorParseException {
		CodeSyncElement cse = (CodeSyncElement) view.getDiagrammableElement();
		if (cse == null || !codeSyncType.equals(cse.getType())) {
			return true;
		}		
		Object[] values = new Object[features.length];
		for (int i = 0; i < features.length; i++) {
			Object value = CodeSyncOperationsService.getInstance().getFeatureValue(cse, features[i]);
			if (value == null) {
				value = "";
			}
			values[i] = value.toString();
		}
		try {
			parameters.put(VIEW_TEXT, String.format(generateFormat, values));
		} catch (IllegalFormatException e) {
			throw new InplaceEditorParseException(CodeSyncPlugin.getInstance().getMessage("ied.regexParseError"));
		}
		return false;
	}

	@Override
	public boolean setInplaceEditorText(View view, String text, Map<String, Object> parameters) throws InplaceEditorException {
		CodeSyncElement cse = (CodeSyncElement) view.getDiagrammableElement();
		if (cse == null || !codeSyncType.equals(cse.getType())) {
			return true;
		}			
		Matcher matcher = Pattern.compile(parseRegEx).matcher(text);
		if (!matcher.find() || features.length != matcher.groupCount()) {
			throw new InplaceEditorException("ied.regexParseError");
		}
		for (int i = 0; i < features.length; i++) {
			CodeSyncOperationsService.getInstance().setFeatureValue(cse, features[i], matcher.group(i + 1));
		}
		return false;
	}
	
}
