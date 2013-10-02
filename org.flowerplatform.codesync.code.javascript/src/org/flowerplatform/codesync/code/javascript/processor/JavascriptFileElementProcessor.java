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
package org.flowerplatform.codesync.code.javascript.processor;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.codesync.code.javascript.parser.Parser;
import org.flowerplatform.codesync.code.javascript.regex_ast.Parameter;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage;
import org.flowerplatform.editor.model.change_processor.IconDiagrammableElementFeatureChangesProcessor;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Mariana Gheorghe
 */
public class JavascriptFileElementProcessor extends IconDiagrammableElementFeatureChangesProcessor {

	@Override
	protected String getLabel(EObject object) {
		CodeSyncElement cse = (CodeSyncElement) object;
		String name = (String) CodeSyncPlugin.getInstance().getFeatureValue(cse, CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name());
//		if (Parser.JS_FUNCTION.equals(cse.getType())) {
//			List<Parameter> parameters = (List<Parameter>) CodeSyncPlugin.getInstance().getFeatureValue(cse, 
//					RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters());
//			if (parameters != null) {
//				for (Parameter parameter : parameters) {
//					if (parameter.getName().equals("signature")) {
//						name += "(" + parameter.getValue() + ")";
//						break;
//					}
//				}
//			}
//		}
		return name;
	}

	@Override
	protected String getIconUrls(EObject object) {
		return null;
	}

}
