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
package org.flowerplatform.editor.model.teneo;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil.EqualityHelper;
import org.eclipse.emf.ecore.util.FeatureMap;

public class DebugEqualityHelper extends EqualityHelper {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean equals(EObject eObject1, EObject eObject2) {
		boolean result = super.equals(eObject1, eObject2);
		if (!result) {
			System.err.println(String.format("!.equals: %s AND %s", eObject1, eObject2));
		}
		return result;
	}

	@Override
	public boolean equals(List<EObject> list1, List<EObject> list2) {
		boolean result = super.equals(list1, list2);
		if (!result) {
			System.err.println(String.format("!.equals: %s AND %s", list1, list2));
		}
		return result;
	}

	@Override
	protected boolean haveEqualFeature(EObject eObject1, EObject eObject2,
			EStructuralFeature feature) {
		boolean result = super.haveEqualFeature(eObject1, eObject2, feature);
		if (!result) {
			System.err.println(String.format("!.haveEqualFeature for feature = %s: %s AND %s", feature, eObject1, eObject2));
		}
		return result;
	}

	@Override
	protected boolean haveEqualReference(EObject eObject1, EObject eObject2,
			EReference reference) {
		boolean result = super.haveEqualReference(eObject1, eObject2, reference);
		if (!result) {
			System.err.println(String.format("!.haveEqualReference for feature = %s: %s AND %s", reference, eObject1, eObject2));
		}
		return result;
	}

	@Override
	protected boolean haveEqualAttribute(EObject eObject1, EObject eObject2,
			EAttribute attribute) {
		boolean result = super.haveEqualAttribute(eObject1, eObject2, attribute);
		if (!result) {
			System.err.println(String.format("!.haveEqualAttribute for feature = %s: %s AND %s", attribute, eObject1, eObject2));
		}
		return result;
	}

	@Override
	protected boolean equalFeatureMaps(FeatureMap featureMap1,
			FeatureMap featureMap2) {
		boolean result = super.equalFeatureMaps(featureMap1, featureMap2);
		if (!result) {
			System.err.println(String.format("!.equalFeatureMaps: %s AND %s", featureMap1, featureMap2));
		}
		return result;
	}

	@Override
	protected boolean equalFeatureMapValues(Object value1, Object value2,
			EStructuralFeature feature) {
		boolean result = super.equalFeatureMapValues(value1, value2, feature);
		if (!result) {
			System.err.println(String.format("!.equalFeatureMapValues for feature = %s: %s AND %s", feature, value1, value2));
		}
		return result;
	}

}