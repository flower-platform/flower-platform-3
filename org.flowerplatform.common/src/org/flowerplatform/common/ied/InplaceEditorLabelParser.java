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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

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
		if (propertyName.length() >= 1 && provider.isVisibilityCharacter(propertyName.charAt(0))) {
			visibility = propertyName.charAt(0)+"";
			propertyName = propertyName.substring(1).trim();
		}
		if (propertyName.length() == 0) {
			// the property name is missing
			throw new InplaceEditorException("_UI_Metamodel_Edit_Name_Null");
		}
		
		if (splitedText.length == 1 || splitedText[1].trim().length() == 0) {
			// the property type is null
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
	
	public InplaceEditorLabelParseResult parseOperationLabel(String text) {
		String operationName = null;
		String operationReturnTypeValue = null;
		LinkedHashMap<String, InplaceEditorLabelParseResult> operationParameters = new LinkedHashMap<String, InplaceEditorLabelParseResult>();
		String aux = null;
		char visibility = 0;
//		String errorTitle = ClassMetamodelAssets.INSTANCE.getMessage("_UI_ClassEdit_Operation_Error");
		String paranthesisContent = null;
		HashMap<String, String> addedTypes = new HashMap<String, String>();

		if (text == null || text.length() == 0) {
			// the new text is null
			throw new InplaceEditorException("_UI_ClassEdit_Operation_No_Label");
		}
		// remove \n and \t if there is any
		text = clearString(text);

		// use checkBrackets() instead of checkContainingCharOnlyOnce('(', ')')
		// because brackets can appear in parameter declarations
		// in default value expressions as: f(p:Type = new Type(10));
		if (!checkBrackets(text)) {
			// the operation must have one open bracket and closed bracket
			throw new InplaceEditorException("_UI_ClassEdit_Operation_Must_Contain_Brackets");
		}

		// split the text using the following separator: (,),;
		StringTokenizer token = new StringTokenizer(text, "(");
		if (token != null && token.hasMoreTokens())
			operationName = token.nextToken();
		if (operationName == null || text.charAt(0) == '(' || text.charAt(0) == ')') {
			throw new InplaceEditorException("_UI_ClassEdit_Operation_Name_Missing");
		}
		operationName = operationName.trim();
		if (provider.isVisibilityCharacter(operationName.charAt(0))) {
			visibility = operationName.charAt(0);
			operationName = operationName.substring(1).trim();
		}
		if (operationName.length() == 0) {
			// only white spaces instead of the operation name
			throw new InplaceEditorException("_UI_ClassEdit_Operation_Name_Missing");
		}
		if (withoutParams(text.substring(text.indexOf('('))) == true) {
			// the operation has no parameters
			paranthesisContent = null;
			aux = text.substring(text.lastIndexOf(')') + 1); // should contain
																// the return
																// type
		} else {
			// paramters
			int paranthesisContentEnd = text.lastIndexOf(')');
			paranthesisContent = text.substring(text.indexOf('(') + 1, paranthesisContentEnd);
			// aux should contain the return type
			if (paranthesisContentEnd + 1 < text.length()) {
				if (text.endsWith(";")) {
					aux = text.substring(paranthesisContentEnd + 1, text.length() - 1);
				} else {
					aux = text.substring(paranthesisContentEnd + 1);
				}
			} else {
				aux = null;
			}

		}

		// parse the string which should contain the return value
		if (aux == null || aux.trim().length() == 0) {
			// no return type
			operationReturnTypeValue = null;
		} else {
			int pos = -1;
			pos = aux.indexOf(":");
			if (pos < 0) {
				// the ':' character is missing => syntax error
				throw new InplaceEditorException("_UI_ClassEdit_Operation_Points_Are_Missing");
			}
			if (!checkContaingCharOnlyOnce(aux, ':'))
				throw new InplaceEditorException("_UI_ClassEdit_Operation_Points");
			aux = aux.substring(pos + 1, aux.length()).trim();
			if (aux == null || aux.length() == 0) {
				// the operation type is missing when expecting to be present
				// (':' character is present)
				throw new InplaceEditorException("_UI_ClassEdit_Operation_Type_Missing");
			}

			if (aux != null) { // if there is a string type value
				if (aux.lastIndexOf('.') == aux.length() - 1)
					throw new InplaceEditorException("_UI_ClassEdit_Operation_Type_Invalid");

				operationReturnTypeValue = aux;
			}
		}

		aux = "";
		String parameterError = null;
		// parse the string which should contain the parameters
		if (paranthesisContent != null && paranthesisContent.length() > 0) {
			for (int i = 0; i < paranthesisContent.length(); i++) {
				if (paranthesisContent.charAt(i) == ',' && !insideOpenBrackets(paranthesisContent, i)) {
					parameterError = addParameterEntry(operationParameters, aux, addedTypes);
					if (parameterError != null)
						throw new InplaceEditorException(parameterError);
					aux = ""; // reset aux
				} else if (i == paranthesisContent.length() - 1) {
					aux += paranthesisContent.charAt(i);
					parameterError = addParameterEntry(operationParameters, aux, addedTypes);
					if (parameterError != null)
						throw new InplaceEditorException(parameterError);
				} else {
					aux += paranthesisContent.charAt(i);
				}
			}
		}
			
		operationName += "(";
		for (InplaceEditorLabelParseResult parameter : operationParameters.values()) {
			operationName += parameter.getType() + ",";
		}
		if (operationName.endsWith(",")) {
			operationName = operationName.substring(0, operationName.length() - 1);
		}
		operationName += ")";
		
		return new InplaceEditorLabelParseResult()
			.setName(operationName)
			.setType(operationReturnTypeValue)
			.setVisibility(String.valueOf(visibility))
			.setParameters((Collection<InplaceEditorLabelParseResult>) operationParameters.values());
	}
	
	public InplaceEditorLabelParseResult parseParameterLabel(String text) {
		return parseAttributeLabel(text);
	}
	
	/////////////////////////////////
	// Model -> Label
	/////////////////////////////////
	
	public String createAttributeLabel(InplaceEditorLabelParseResult model, boolean forEditing) {
		String label = String.format("%s%s : %s", 
				model.getVisibility() == null ? "" : model.getVisibility(), 
				model.getName(),
				forEditing ? model.getType() : getSimpleName(model.getType()));
		if (model.getDefaultValue() != null) {
			label += String.format(" = %s", model.getDefaultValue());
		}
		return label;
	}
	
	public String createOperationLabel(InplaceEditorLabelParseResult model, boolean forEditing) {
		String parameters = new String();
		for (InplaceEditorLabelParseResult parameter : model.getParameters()) {
			parameters += createParameterLabel(parameter, forEditing) + ", ";
		}
		if (parameters.length() > 0) {
			parameters = parameters.substring(0, parameters.length() - 2);
		}
		String label = String.format("%s%s(%s) : %s",
				model.getVisibility(),
				model.getName(),
				parameters,
				forEditing ? model.getType() : getSimpleName(model.getType()));
		return label;
	}
	
	public String createParameterLabel(InplaceEditorLabelParseResult model, boolean forEditing) {
		return createAttributeLabel(model, forEditing);
	}
	
	/////////////////////////////////
	// Utils
	/////////////////////////////////
	
	protected String clearString(String text) {
		text.replaceAll("\n", "");
		text.replaceAll("\t", "");
		return text;
	}
	
	/**
	 * Checks if String <code>s</code> contains a valid set of 
	 * open and close brackets
	 *  
	 * @param s
	 * @return <code>true</code> only if the given String contains at least one pair of brackets
	 * correctly opened and closed, meaning one <code>'('</code> closed by <code>')'</code>
	 */
	protected boolean checkBrackets(String s) {
		int bracketPairs = 0;
		boolean found = false;	// ensure there is at least one pair
		
		for (int i = 0; i < s.length() ; i++) {
			char ch = s.charAt(i);
			if (ch == '(') {
				found = true;
				bracketPairs ++;
			}
			else if (ch == ')')
				bracketPairs --;
			
			// more closing brackets then opened
			if (bracketPairs < 0) {
				return false;
			}
		}
		
		if (bracketPairs != 0 || !found) {
			return false;
		} else
			return true;
	}

	protected boolean withoutParams(String s) {
		for (int i = 1; i < s.length(); i++)
			if (s.charAt(i) == ' ') {
			} else if (s.charAt(i) == ')')
				return true;
			else
				return false;
		return true;
	}
	
	/**
	 * Searches <code>s</code> for the given character, <code>c</code>.
	 * Note that when the checked character is not <em>"</em>  or <em>'</em> the algorithm will
	 * ignore the character's aparitions inside String Literals or Character
	 * Literals (between <em>"</em> and <em>'</em> characters).
	 * 
	 * @param s =
	 *            the string to search in
	 * @param c =
	 *            the character to be searched for
	 * @return true if the character 'c' appears only once in the given string
	 *         's'
	 */
	// TODO:: rename in closing brackets
	protected boolean checkContaingCharOnlyOnce(String s, char c) {
		
		int pos = -1;
		
		if (c != '"' && c != '\'') {
			// this will mark the entry and the exit to and from a String literal
			boolean insideStringLiteral = false;
			// will mark the entry and exit from a char literal
			boolean insideCharLiteral = false;
			int index = 0;
			char lastChar = 0;
			char chars[] = s.toCharArray();
			
			for (char ch : chars) {
				if (ch == '"') {
					if (lastChar != '\\')
						insideStringLiteral = !insideStringLiteral;
				} else if (ch == '\'') {
					if (lastChar != '\\')
						insideCharLiteral = !insideCharLiteral;
					
				} else if (ch == c && !insideStringLiteral && !insideCharLiteral) {
					// found the searched char and it's not inside a String Literal
					
					if (pos >= 0) // found it again
						return false;
					else
						pos = index; // found it for the first time
				}
				lastChar = ch;
				index ++;
			}
			if (pos < 0)
				return false; // char not found;
			else
				return true;
		} else {
			pos = s.indexOf(c);
			if (pos < 0) {
				return false;
			}
			if (s.indexOf(c, pos + 1) > 0) {
				return false;
			}
			return true;
		}
	}
	

	/**
	 * If the given index is inside open brackets it returns true. Otherwise, it
	 * returns false.
	 */
	protected boolean insideOpenBrackets(String s, int index) {
		int countOpen = 0;
		int countClosed = 0;
		for (int i = 0; i < index; i++)
			if (s.charAt(i) == '<')
				countOpen++;
			else if (s.charAt(i) == '>')
				countClosed++;
		if (countOpen != countClosed) // inside brackets
			return true;
		else
			// outside brackets
			return false;
	}
	
	/**
	 * The method receives a string representing a potential pair:
	 * parameterName:parameterValue=defaultValue. The method calls
	 * <code>ClassMetamodelPropertyItemProvider.parsePropertyOrParameter()</code>
	 * which parses the string. If the parsing doesn't throw an exception, the
	 * new parameter is added to the parameter link list.
	 * 
	 * @param operationParameters
	 *            = <code>LinkedHashMap</code> containing the parameter name as
	 *            key and an Object[] array with parameter type and parameter
	 *            default value
	 * @param text
	 * @return
	 * @flowerModelElementId _IrLtVdXXEd6axtupYmR2VA
	 */
	protected String addParameterEntry(LinkedHashMap<String, InplaceEditorLabelParseResult> operationParameters, String text, HashMap<String, String> addedTypes) {
		InplaceEditorLabelParseResult result = parseParameterLabel(text);
		String parameterType = null;
		if (result.getType() != null) { // if there is a type value
			parameterType = result.getType();
		}
		if (parameterType == null)
			return "_UI_ClassEdit_Type_CouldNot_Be_Created";
		// all the conditions are satisfied => add the new parameter and its
		// value to the parameter's map
		operationParameters.put(result.getName(), result);
		return null;
	}
	
	
	protected String getSimpleName(String fullyQualifiedName) {
		int index = fullyQualifiedName.lastIndexOf(".");
		if (index >= 0) {
			return fullyQualifiedName.substring(++index);
		}
		return fullyQualifiedName;
	}
}
