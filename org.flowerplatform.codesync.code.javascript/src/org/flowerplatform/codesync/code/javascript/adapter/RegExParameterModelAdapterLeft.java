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
package org.flowerplatform.codesync.code.javascript.adapter;

import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage;

import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.action.ActionResult;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.FeatureChange;
import com.crispico.flower.mp.model.codesync.impl.EStructuralFeatureToFeatureChangeEntryImpl;

/**
 * @author Mariana Gheorghe
 */
public class RegExParameterModelAdapterLeft extends RegExParameterModelAdapter {

	@Override
	public void actionPerformed(Object element, Object feature, ActionResult result) {
		RegExAstNodeParameter parameter = (RegExAstNodeParameter) element;
		if (parameter.eContainer() instanceof FeatureChange) {
			FeatureChange featureChange = (FeatureChange) parameter.eContainer();
			EStructuralFeatureToFeatureChangeEntryImpl entry = 
					(EStructuralFeatureToFeatureChangeEntryImpl) featureChange.eContainer();
			CodeSyncElement codeSyncElement = (CodeSyncElement) entry.eContainer();
			IModelAdapter parentModelAdapter = getModelAdapterFactorySet().getLeftFactory().getModelAdapter(codeSyncElement);
			parentModelAdapter.actionPerformed(codeSyncElement, entry.getKey(),
					new ActionResult(result.conflict, result.modifiedLeft, result.modifiedRight, getMatchKey(parameter), false, feature, true));
		}
	}
	
}
