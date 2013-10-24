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
package com.crispico.flower.mp.codesync.code.java.adapter;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.astcache.code.Parameter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * Mapped to {@link SingleVariableDeclaration}. Does not return any children. Does not have a corresponding {@link CodeSyncElement}.
 * 
 * @author Mariana
 */
public class JavaParameterModelAdapter extends JavaAbstractAstNodeModelAdapter {

	public static final String PARAMETER = "parameter";
	
	@Override
	public List<?> getChildren(Object modelElement) {
		return Collections.emptyList();
	}

	@Override
	public Object getMatchKey(Object modelElement) {
		return getVariableDeclaration(modelElement).getName().getIdentifier();
	}
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (AstCacheCodePackage.eINSTANCE.getTypedElement_Type().equals(feature)) {
			return getStringFromType(getVariableDeclaration(element).getType());
		}
		if (AstCacheCodePackage.eINSTANCE.getParameter_Name().equals(feature)) {
			return getVariableDeclaration(element).getName().getIdentifier();
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (AstCacheCodePackage.eINSTANCE.getParameter_Name().equals(feature)) {
			SingleVariableDeclaration parameter = getVariableDeclaration(element);
			String name = (String) value;
			parameter.setName(parameter.getAST().newSimpleName(name));
		}
		if (AstCacheCodePackage.eINSTANCE.getTypedElement_Type().equals(feature)) {
			SingleVariableDeclaration parameter = getVariableDeclaration(element);
			Type type = getTypeFromString(parameter.getAST(), (String) value);
			parameter.setType(type);
		}
		super.setValueFeatureValue(element, feature, value);
	}

	private SingleVariableDeclaration getVariableDeclaration(Object element) {
		return (SingleVariableDeclaration) element;
	}

	/**
	 * Creates a {@link Parameter} instance. Also set the parameter name, in case the AST cache was deleted.
	 */
	@Override
	public Object createCorrespondingModelElement(Object element) {
		Parameter parameter = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createParameter();
		parameter.setName(getVariableDeclaration(element).getName().getIdentifier());
		return parameter;
	}

}