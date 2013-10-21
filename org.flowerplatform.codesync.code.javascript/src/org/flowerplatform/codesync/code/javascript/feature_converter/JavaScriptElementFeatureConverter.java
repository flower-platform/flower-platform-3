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
package org.flowerplatform.codesync.code.javascript.feature_converter;

import java.util.List;

import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage;
import org.flowerplatform.codesync.feature_converter.CodeSyncElementFeatureConverter;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 */
public class JavaScriptElementFeatureConverter extends CodeSyncElementFeatureConverter {

	protected final String BACKBONE_SUPER_CLASS = "backboneSuperClass";
	
	protected final String JAVASCRIPT_ATTRIBUTE_DEFAULT_VALUE = "javaScriptAttributeDefaultValue";
	
	protected final String REQUIRE_ENTRY_VAR_NAME = "requireEntryVarName";
	protected final String REQUIRE_ENTRY_DEPENDENCY_PATH = "requireEntryDependencyPath";
	
	protected final String EVENTS_ATTRIBUTE_ENTRY_EVENT = "eventsAttributeEntryEvent";
	protected final String EVENTS_ATTRIBUTE_ENTRY_SELECTOR = "eventsAttributeEntrySelector";
	protected final String EVENTS_ATRIBUTE_ENTRY_FUNCTION = "events";
	
	protected final String ROUTES_ATTRIBUTE_ENTRY_PATH = "routesAttributeEntryPath";
	protected final String ROUTES_ATTRIBUTE_ENTRY_FUNCTION = "routesAttributeEntryFunction";
	
	protected final String TABLE_ID = "tableId";
	protected final String TABLE_HEADER_ROW_ID = "tableHeaderRowId";
	
	protected final String TABLE_ITEM_URL = "tableItemUrl";
	
	protected final String TABLE_ITEM_ENTRY_VALUE_EXPRESSION = "tableItemEntryValueExpression";

	protected final String FORM_ITEM_VALUE_EXPRESSION = "formItemValueExpression";
	protected final String FORM_ITEM_EDIT_ID = "formItemEditId";
	
	public JavaScriptElementFeatureConverter() {
		super();
		
		addFeature(BACKBONE_SUPER_CLASS, RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters());
		
		addFeature(JAVASCRIPT_ATTRIBUTE_DEFAULT_VALUE, RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters());
		
		addFeature(REQUIRE_ENTRY_VAR_NAME, RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters());
		addFeature(REQUIRE_ENTRY_DEPENDENCY_PATH, RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters());
		
		addFeature(EVENTS_ATTRIBUTE_ENTRY_EVENT, RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters());
		addFeature(EVENTS_ATTRIBUTE_ENTRY_SELECTOR, RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters());
		addFeature(EVENTS_ATRIBUTE_ENTRY_FUNCTION, RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters());
		
		addFeature(ROUTES_ATTRIBUTE_ENTRY_PATH, RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters());
		addFeature(ROUTES_ATTRIBUTE_ENTRY_FUNCTION, RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters());
		
		addFeature(TABLE_ID, RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters());
		addFeature(TABLE_HEADER_ROW_ID, RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters());
		
		addFeature(TABLE_ITEM_URL, RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters());
		
		addFeature(TABLE_ITEM_ENTRY_VALUE_EXPRESSION, RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters());
		
		addFeature(FORM_ITEM_VALUE_EXPRESSION, RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters());
		addFeature(FORM_ITEM_EDIT_ID, RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters());
	}

	@Override
	public Object getValue(CodeSyncElement codeSyncElement, String name) {
		Object value = super.getValue(codeSyncElement, name);
		switch (name) {
			case BACKBONE_SUPER_CLASS: {
				return getParameter(value, "superClass");
			}
			
			case JAVASCRIPT_ATTRIBUTE_DEFAULT_VALUE: {
				return getParameter(value, "defaultValue");
			}
			
			case REQUIRE_ENTRY_VAR_NAME: {
				return getParameter(value, "varName");
			}
			case REQUIRE_ENTRY_DEPENDENCY_PATH: {
				return getParameter(value, "dependencyPath");
			}
			
			case EVENTS_ATTRIBUTE_ENTRY_EVENT: {
				return getParameter(value, "event");
			}
			case EVENTS_ATTRIBUTE_ENTRY_SELECTOR: {
				return getParameter(value, "selector");
			}
			case EVENTS_ATRIBUTE_ENTRY_FUNCTION: {
				return getParameter(value, "function");
			}
			
			case ROUTES_ATTRIBUTE_ENTRY_PATH: {
				return getParameter(value, "path");
			}
			case ROUTES_ATTRIBUTE_ENTRY_FUNCTION: {
				return getParameter(value, "function");
			}
			
			case TABLE_ID: {
				return getParameter(value, "tableId");
			}
			case TABLE_HEADER_ROW_ID: {
				return getParameter(value, "headerRowId");
			}
			
			case TABLE_ITEM_URL: {
				return getParameter(value, "itemUrl");
			}
			
			case TABLE_ITEM_ENTRY_VALUE_EXPRESSION: {
				return getParameter(value, "valueExpression");
			}
			
			case FORM_ITEM_VALUE_EXPRESSION: {
				return getParameter(value, "valueExpression");
			}
			case FORM_ITEM_EDIT_ID:	{
				return getParameter(value, "editId");
			}
		}
		return value;
	}
	
	protected Object getParameter(Object value, String name) {
		List<RegExAstNodeParameter> parameters = (List<RegExAstNodeParameter>) value;
		if (parameters != null) {
			for (RegExAstNodeParameter parameter : parameters) {
				if (parameter.getName().equals(name)) {
					return parameter.getValue();
				}
			}
		}
		return null;
	}
		
}
