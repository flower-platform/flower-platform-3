package com.crispico.flower.mp.codesync.code.java;

import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.merge.CodeSyncElementFeatureProvider;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;

/**
 * @author Mariana
 */
public class JavaFeatureProvider extends CodeSyncElementFeatureProvider {

	/**
	 * Returns {@link IModelAdapter#FEATURE_TYPE_CONTAINMENT} for <code>superInterfaces</code> and
	 * enum constant <code>arguments</code>.
	 */
	@Override
	public int getFeatureType(Object feature) {
		if (AstCacheCodePackage.eINSTANCE.getClass_SuperInterfaces().equals(feature)) {
			return IModelAdapter.FEATURE_TYPE_CONTAINMENT;
		}
		if (AstCacheCodePackage.eINSTANCE.getEnumConstant_Arguments().equals(feature)) {
			return IModelAdapter.FEATURE_TYPE_CONTAINMENT;
		}
		return super.getFeatureType(feature);
	}
	
}
