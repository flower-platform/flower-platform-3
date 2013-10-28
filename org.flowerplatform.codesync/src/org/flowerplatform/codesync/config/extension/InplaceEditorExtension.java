package org.flowerplatform.codesync.config.extension;

import java.util.Map;

import org.flowerplatform.emf_model.notation.View;

/**
 * @author Cristina Constantinescu
 */
public interface InplaceEditorExtension {

	public static final String VIEW_TEXT = "text";
	
	public boolean getInplaceEditorText(View view, Map<String, Object> parameters) throws  InplaceEditorParseException;
	
	public boolean setInplaceEditorText(View view, String text, Map<String, Object> parameters) throws  InplaceEditorParseException;
	
}
