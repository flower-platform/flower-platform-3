package com.crispico.flower.mp.codesync.code.adapter;

import java.util.List;

import com.crispico.flower.mp.codesync.merge.SyncElementModelAdapter;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;

/**
 * @author Mariana
 */
public class EnumConstantModelAdapter extends SyncElementModelAdapter {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<?> getFeatures(Object element) {
		List features = super.getFeatures(element);
		features.add(AstCacheCodePackage.eINSTANCE.getEnumConstant_Arguments());
		return features;
	}
	
}
