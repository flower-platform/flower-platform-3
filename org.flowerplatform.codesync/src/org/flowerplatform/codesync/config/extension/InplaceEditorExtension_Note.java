package org.flowerplatform.codesync.config.extension;

import java.util.Map;

import org.flowerplatform.emf_model.notation.Note;
import org.flowerplatform.emf_model.notation.View;

/**
 * @author Cristina Constantinescu
 */
public class InplaceEditorExtension_Note implements InplaceEditorExtension {

	@Override
	public boolean getInplaceEditorText(View view, Map<String, Object> parameters) throws  InplaceEditorParseException {
		if (view instanceof Note) {
			parameters.put(VIEW_TEXT, ((Note) view).getText());
			return false;			
		}
		return true;
	}

	@Override
	public boolean setInplaceEditorText(View view, String text, Map<String, Object> parameters) throws  InplaceEditorParseException {
		if (view instanceof Note) {
			((Note) view).setText(text);
			return false;			
		}
		return true;
	}

}
