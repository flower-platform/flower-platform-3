package com.crispico.flower.mp.codesync.code;

import java.util.List;

import com.crispico.flower.mp.codesync.merge.SyncElementModelAdapter;
import com.crispico.flower.mp.model.astcache.code.Annotation;
import com.crispico.flower.mp.model.astcache.code.AnnotationValue;
import com.crispico.flower.mp.model.astcache.code.Modifier;
import com.crispico.flower.mp.model.astcache.code.Parameter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * Mapped to {@link CodeSyncElement}.
 * 
 * @author Mariana
 */
public class CodeSyncElementModelAdapter extends SyncElementModelAdapter {

	protected CodeSyncElementFeatureProvider common;
	
	public CodeSyncElementModelAdapter() {
		super();
		
		common = new CodeSyncElementFeatureProvider();
	}
	
	public void setFeatureProvider(CodeSyncElementFeatureProvider common) {
		this.common = common;
	}

	@Override
	public String getFeatureName(Object feature) {
		return common.getFeatureName(feature);
	}

	@Override
	public List<?> getFeatures(Object element) {
		return common.getFeatures(element);
	}

	@Override
	public int getFeatureType(Object feature) {
		return common.getFeatureType(feature);
	}
	
	@Override
	public Object getMatchKey(Object element) {
		if (element instanceof Parameter) {
			return ((Parameter) element).getName();
		}
		if (element instanceof Modifier) {
			return String.valueOf(((Modifier) element).getType());
		}
		// for Annotations, the match key contains the name and the type of the annotation
		if (element instanceof Annotation) {
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
		if (element instanceof AnnotationValue) {
			return ((AnnotationValue) element).getName();
		}
		if (element instanceof String) {
			return element;
		}
		return super.getMatchKey(element);
	}

}
