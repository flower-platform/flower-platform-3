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

import static org.eclipse.jdt.core.dom.ASTNode.PROTECT;
import static org.eclipse.jdt.core.dom.Modifier.ABSTRACT;
import static org.eclipse.jdt.core.dom.Modifier.FINAL;
import static org.eclipse.jdt.core.dom.Modifier.PRIVATE;
import static org.eclipse.jdt.core.dom.Modifier.PUBLIC;
import static org.eclipse.jdt.core.dom.Modifier.STATIC;
import static org.eclipse.jdt.core.dom.Modifier.ModifierKeyword.PRIVATE_KEYWORD;
import static org.eclipse.jdt.core.dom.Modifier.ModifierKeyword.PROTECTED_KEYWORD;
import static org.eclipse.jdt.core.dom.Modifier.ModifierKeyword.PUBLIC_KEYWORD;

import java.util.List;

import org.flowerplatform.codesync.feature_converter.CodeSyncElementFeatureConverter;

import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.astcache.code.ExtendedModifier;
import com.crispico.flower.mp.model.astcache.code.Modifier;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 */
public class JavaElementFeatureConverter extends CodeSyncElementFeatureConverter {

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
	
	public JavaElementFeatureConverter() {
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
				if (hasFlags(value, PUBLIC)) {
					return PUBLIC_KEYWORD.toString();
				}
				if (hasFlags(value, PROTECT)) {
					return PROTECTED_KEYWORD.toString();
				}
				if (hasFlags(value, PRIVATE)) {
					return PRIVATE_KEYWORD.toString();
				}
				return null;
			}
			case IS_ABSTRACT: {
				return hasFlags(value, ABSTRACT);
			}
			case IS_FINAL: {
				return hasFlags(value, FINAL);
			}
			case IS_STATIC: {
				return hasFlags(value, STATIC);
			}
		}
		
		return value;
	}
	
	protected boolean hasFlags(Object value, int flags) {
		List<ExtendedModifier> modifiers = (List<ExtendedModifier>) value;
		 if (modifiers != null) {
			 for (ExtendedModifier modifier : modifiers) {
				 if (modifier instanceof Modifier) {
					int type = ((Modifier) modifier).getType();
					if ((type & flags) != 0) {
						return true;
					}
				 }
			 }
		 }
		 return false;
	}
}
