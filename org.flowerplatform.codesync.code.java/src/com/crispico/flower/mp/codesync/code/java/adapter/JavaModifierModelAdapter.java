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

import org.eclipse.jdt.core.dom.Modifier;

import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * Mapped to {@link Modifier}. Does not have a corresponding {@link CodeSyncElement}.
 * 
 * @author Mariana
 */
public class JavaModifierModelAdapter extends JavaAbstractAstNodeModelAdapter {

	public static final String MODIFIER = "modifier";
	
	@Override
	public Object getMatchKey(Object element) {
		return String.valueOf(getModifierType(element));
	}

	@Override
	public String getLabel(Object modelElement) {
		return ((Modifier) modelElement).getKeyword().toString();
	}

	@Override
	public Object createCorrespondingModelElement(Object element) {
		com.crispico.flower.mp.model.astcache.code.Modifier modifier = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createModifier();
		modifier.setType(((Modifier) element).getKeyword().toFlagValue());
		return modifier;
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		return Collections.emptyList();
	}
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (AstCacheCodePackage.eINSTANCE.getModifier_Type().equals(feature)) {
			return getModifierType(element);
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (AstCacheCodePackage.eINSTANCE.getModifier_Type().equals(feature)) {
			if (element instanceof Modifier) {
				Modifier modifier = (Modifier) element;
				int flag = (int) value;
				modifier.setKeyword(Modifier.ModifierKeyword.fromFlagValue(flag));
			}
			return;
		}
		super.setValueFeatureValue(element, feature, value);
	}

	private Object getModifierType(Object element) {
		if (element instanceof Modifier) {
			return ((Modifier) element).getKeyword().toFlagValue();
		}
		return null;
	}
	
}