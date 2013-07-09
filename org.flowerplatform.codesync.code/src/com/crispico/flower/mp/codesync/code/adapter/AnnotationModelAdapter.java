package com.crispico.flower.mp.codesync.code.adapter;

import java.util.Arrays;
import java.util.List;

import com.crispico.flower.mp.codesync.code.CodeSyncCodePlugin;
import com.crispico.flower.mp.codesync.merge.SyncElementModelAdapter;
import com.crispico.flower.mp.model.astcache.code.Annotation;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * Mapped to {@link Annotation}. Does not have a corresponding {@link CodeSyncElement}.
 * 
 * @author Mariana
 */
public class AnnotationModelAdapter extends SyncElementModelAdapter {

	@Override
	public List<?> getFeatures(Object element) {
		return Arrays.asList(
			AstCacheCodePackage.eINSTANCE.getAnnotation_Name(),
			AstCacheCodePackage.eINSTANCE.getAnnotation_Values()
		);
	}
	
	@Override
	public Object getMatchKey(Object element) {
		// for Annotations, the match key contains the name and the type of the annotation
		Annotation annotation = (Annotation) element;
		String matchKey = annotation.getName();
		if (annotation.getValues().size() == 0) {
			matchKey += CodeSyncCodePlugin.MARKER_ANNOTATION;
		} else {
			if (annotation.getValues().size() == 1 && CodeSyncCodePlugin.SINGLE_MEMBER_ANNOTATION_VALUE_NAME.equals(annotation.getValues().get(0).getName())) {
				matchKey += CodeSyncCodePlugin.SINGLE_MEMBER_ANNOTATION;
			} else {
				matchKey += CodeSyncCodePlugin.NORMAL_ANNOTATION;
			}
		}
		return matchKey;
	}
	
}
