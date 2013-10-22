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
package org.flowerplatform.codesync.code.java.feature_converter;

import static org.eclipse.jdt.core.dom.Modifier.ABSTRACT;
import static org.eclipse.jdt.core.dom.Modifier.FINAL;
import static org.eclipse.jdt.core.dom.Modifier.PRIVATE;
import static org.eclipse.jdt.core.dom.Modifier.PROTECTED;
import static org.eclipse.jdt.core.dom.Modifier.PUBLIC;
import static org.eclipse.jdt.core.dom.Modifier.STATIC;
import static org.eclipse.jdt.core.dom.Modifier.ModifierKeyword.PRIVATE_KEYWORD;
import static org.eclipse.jdt.core.dom.Modifier.ModifierKeyword.PROTECTED_KEYWORD;
import static org.eclipse.jdt.core.dom.Modifier.ModifierKeyword.PUBLIC_KEYWORD;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.flowerplatform.codesync.feature_converter.CodeSyncElementFeatureValueConverter;

import com.crispico.flower.mp.model.astcache.code.AstCacheCodeFactory;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.astcache.code.ExtendedModifier;
import com.crispico.flower.mp.model.astcache.code.Modifier;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 */
public class JavaElementFeatureValueConverter extends CodeSyncElementFeatureValueConverter {

	// for DocumentableElements
	protected final String DOCUMENTATION 	= "documentation";
	
	// for TypedElements
	protected final String TYPE				= "type";
	
	// for ModifiableElements
	protected final String VISIBILITY 		= "visibility";
	protected final String IS_ABSTRACT 		= "isAbstract";
	protected final String IS_FINAL			= "isFinal";
	protected final String IS_STATIC		= "isStatic";
	// TODO add all from jdt Modifier
	
	// for Classes
	protected final String SUPER_CLASS 		= "superClass";
	protected final String SUPER_INTERFACES = "superInterfaces";
	
	// for Attributes
	protected final String INITIALIZER		= "initializer";
	
	public JavaElementFeatureValueConverter() {
		super();
		
		addFeature(DOCUMENTATION, 	 AstCacheCodePackage.eINSTANCE.getDocumentableElement_Documentation());
		
		addFeature(TYPE, 			 AstCacheCodePackage.eINSTANCE.getTypedElement_Type());
		
		addFeature(VISIBILITY, 		 AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers());
		addFeature(IS_ABSTRACT, 	 AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers());
		addFeature(IS_FINAL, 		 AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers());
		addFeature(IS_STATIC, 		 AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers());
		
		addFeature(SUPER_CLASS, 	 AstCacheCodePackage.eINSTANCE.getClass_SuperClasses());
		addFeature(SUPER_INTERFACES, AstCacheCodePackage.eINSTANCE.getClass_SuperInterfaces());
		
		addFeature(INITIALIZER, 	 AstCacheCodePackage.eINSTANCE.getAttribute_Initializer());
	}

	@Override
	public Object getValue(CodeSyncElement codeSyncElement, String name) {
		Object value = super.getValue(codeSyncElement, name);
		
		switch (name) {
			case VISIBILITY: {
				if (getModifier(value, PUBLIC) != null) {
					return PUBLIC_KEYWORD.toString();
				}
				if (getModifier(value, PROTECTED) != null) {
					return PROTECTED_KEYWORD.toString();
				}
				if (getModifier(value, PRIVATE) != null) {
					return PRIVATE_KEYWORD.toString();
				}
				return null;
			}
			case IS_ABSTRACT: {
				return getModifier(value, ABSTRACT) != null;
			}
			case IS_FINAL: {
				return getModifier(value, FINAL) != null;
			}
			case IS_STATIC: {
				return getModifier(value, STATIC) != null;
			}
		}
		
		return value;
	}
	
	@Override
	public void setValue(CodeSyncElement codeSyncElement, String name, Object newValue) {
		Object fromClient = newValue;
		switch (name) {
			case VISIBILITY: {
				newValue = super.getValue(codeSyncElement, name);
				int visibilityType = PUBLIC | PROTECTED | PRIVATE;
				if (PUBLIC_KEYWORD.toString().equals(fromClient)) {
					newValue = addModifier(newValue, PUBLIC, visibilityType);
					break;
				}
				if (PROTECTED_KEYWORD.toString().equals(fromClient)) {
					newValue = addModifier(newValue, PROTECTED, visibilityType);
					break;
				}
				if (PRIVATE_KEYWORD.toString().equals(fromClient)) {
					newValue = addModifier(newValue, PRIVATE, visibilityType);
					break;
				}
			}
			case IS_ABSTRACT: {
				newValue = super.getValue(codeSyncElement, name);
				if (Boolean.parseBoolean((String) fromClient)) {
					newValue = addModifier(newValue, ABSTRACT);
				} else {
					newValue = removeModifier(newValue, ABSTRACT);
				}
				
				break;
			}
			case IS_FINAL: {
				newValue = super.getValue(codeSyncElement, name);
				if (Boolean.parseBoolean((String) fromClient)) {
					newValue = addModifier(newValue, FINAL);
				} else {
					newValue = removeModifier(newValue, FINAL);
				}
				break;
			}
			case IS_STATIC: {
				newValue = super.getValue(codeSyncElement, name);
				if (Boolean.parseBoolean((String) fromClient)) {
					newValue = addModifier(newValue, STATIC);
				} else {
					newValue = removeModifier(newValue, STATIC);
				}
				
				break;
			}
		}
		
		super.setValue(codeSyncElement, name, newValue);
	}
	
	protected Object addModifier(Object value, int type) {
		return addModifier(value, type, type);
	}
	
	protected Object addModifier(Object value, int newType, int type) {
		List<ExtendedModifier> modifiers = (List<ExtendedModifier>) 
				EcoreUtil.copyAll((Collection<ExtendedModifier>) value);
		Modifier modifier = getModifier(modifiers, type);
		if (modifier == null || modifier.getType() != newType) {
			if (modifier != null) {
				modifiers.remove(modifier);
			}
			Modifier newModifier = AstCacheCodeFactory.eINSTANCE.createModifier();
			newModifier.setType(newType);
			modifiers.add(newModifier);
		}
		return modifiers;
	}
	
	protected Object removeModifier(Object value, int type) {
		List<ExtendedModifier> modifiers = (List<ExtendedModifier>) 
				EcoreUtil.copyAll((Collection<ExtendedModifier>) value);
		Modifier modifier = getModifier(modifiers, type);
		if (modifier != null) {
			modifiers.remove(modifier);
		}
		return modifiers;
	}
	
	protected Modifier getModifier(Object value, int flags) {
		List<ExtendedModifier> modifiers = (List<ExtendedModifier>) value;
		 if (modifiers != null) {
			 for (ExtendedModifier modifier : modifiers) {
				 if (modifier instanceof Modifier) {
					int type = ((Modifier) modifier).getType();
					if ((type & flags) != 0) {
						return (Modifier) modifier;
					}
				 }
			 }
		 }
		 return null;
	}
	
}
