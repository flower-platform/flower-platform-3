package com.crispico.flower.mp.codesync.code.adapter;

import java.util.Arrays;
import java.util.List;

import com.crispico.flower.mp.codesync.merge.SyncElementModelAdapter;
import com.crispico.flower.mp.model.astcache.code.AnnotationValue;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * Mapped to {@link AnnotationValue}. Does not have a corresponding {@link CodeSyncElement}.
 * 
 * @author Mariana
 */
public class AnnotationValueModelAdapter extends SyncElementModelAdapter {

	@Override
	public List<?> getFeatures(Object element) {
		return Arrays.asList(
			AstCacheCodePackage.eINSTANCE.getAnnotationValue_Name(),
			AstCacheCodePackage.eINSTANCE.getAnnotationValue_Value()
		);
	}
	
	@Override
	public Object getMatchKey(Object element) {
		return ((AnnotationValue) element).getName();
	}
	
}
