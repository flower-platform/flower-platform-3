package com.crispico.flower.mp.codesync.code.adapter;

import java.util.List;

import com.crispico.flower.mp.codesync.merge.SyncElementModelAdapter;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;

/**
 * @author Mariana
 */
public class AnnotationMemberModelAdapter extends SyncElementModelAdapter {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<?> getFeatures(Object element) {
		List features = super.getFeatures(element);
		features.add(AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers());
		features.add(AstCacheCodePackage.eINSTANCE.getDocumentableElement_Documentation());
		features.add(AstCacheCodePackage.eINSTANCE.getTypedElement_Type());
		features.add(AstCacheCodePackage.eINSTANCE.getAnnotationMember_DefaultValue());
		return features;
	}
	
}
