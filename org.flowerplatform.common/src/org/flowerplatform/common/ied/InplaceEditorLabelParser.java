/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.common.ied;

/**
 * @author Mariana Gheorghe
 */
public class InplaceEditorLabelParser {
	
	protected IInplaceEditorProvider provider;
	
	public InplaceEditorLabelParser(IInplaceEditorProvider provider) {
		this.provider = provider;
	}
	
	/////////////////////////////////
	// Label -> Model
	/////////////////////////////////

	public InplaceEditorLabelParseResult parseAttributeLabel(String text) {
		String propertyName = null;
		String propertyStringType = null;
		String visibility = null;
		String defaultValue = null;
//		String errorTitle = ClassMetamodelAssets.INSTANCE.getMessage("_UI_Metamodel_Edit_Error");
		
		if (text == null || text.trim().length() == 0) {
			// the new text is null
			throw new InplaceEditorException("_UI_Metamodel_Edit_Property_No_Label");
		}
		
		// remove \n and \t if there is any
		text = clearString(text);
		
		int pos = 0;
		pos = text.indexOf(":");
		if (pos >= 0 && text.indexOf(":", pos+1) > 0) {
			// the new text shouldn't contain ":" character more than one
			throw new InplaceEditorException("_UI_Metamodel_Semicolumn_No");
		}
		
		String[] splitedText = text.split(":");
		propertyName = splitedText.length > 0 ? splitedText[0] : "";
		if (propertyName == null || propertyName.length() == 0) {
			// the property name is null
			throw new InplaceEditorException("_UI_Metamodel_Edit_Name_Null");
		}
	
		// remove white spaces from the beginning and the end of the name
		propertyName = propertyName.trim();
		if (propertyName.length() > 1 && provider.isVisibilityCharacter(propertyName.charAt(0))) {
			visibility = propertyName.charAt(0)+"";
			propertyName = propertyName.substring(1).trim();
		}
		if (propertyName.length() == 0) {
			// the property name is missing
			throw new InplaceEditorException("_UI_Metamodel_Edit_Name_Null");
		}
		
		if ((splitedText.length == 1 || splitedText[1].trim().length() == 0) && pos >= 0) {
			// the property type is null and ":" character is present
			throw new InplaceEditorException("_UI_Metamodel_Edit_Type_Null");
		} else if (splitedText.length > 1)
			propertyStringType = splitedText[1].trim();
		
		if (propertyStringType != null) { // if there is a type
			pos = propertyStringType.indexOf('=');
			if (pos >= 0) { // there is a default value set
				defaultValue = propertyStringType.substring(pos + 1).trim();
				propertyStringType = propertyStringType.substring(0, pos).trim();
				if (defaultValue.length() == 0) {
					throw new InplaceEditorException("_UI_Metamodel_Edit_Default_Value_Missing");
				}
				if (propertyStringType.length() == 0) {
					throw new InplaceEditorException("_UI_Metamodel_Edit_Type_Null");
				}
			}
			if (propertyStringType.lastIndexOf('.') == propertyStringType.length() - 1) {
				throw new InplaceEditorException("_UI_Metamodel_Edit_Type_Invalid");
			}
		}		
		
		return new InplaceEditorLabelParseResult()
			.setName(propertyName)
			.setType(propertyStringType)
			.setVisibility(visibility)
			.setDefaultValue(defaultValue);
	}
	
	/////////////////////////////////
	// Label -> Model
	/////////////////////////////////
	
	public String createAttributeLabel(InplaceEditorLabelParseResult model) {
		String label = String.format("%s%s : %s", 
				model.getVisibility(), 
				model.getName(),
				model.getType());
		if (model.getDefaultValue() != null) {
			label += String.format(" = %s", model.getDefaultValue());
		}
		return label;
	}
	
	/////////////////////////////////
	// Utils
	/////////////////////////////////
	
	protected String clearString(String text) {
		text.replaceAll("\n", "");
		text.replaceAll("\t", "");
		return text;
	}
	
}
