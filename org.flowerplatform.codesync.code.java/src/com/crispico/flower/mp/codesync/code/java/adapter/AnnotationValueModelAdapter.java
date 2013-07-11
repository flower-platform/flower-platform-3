package com.crispico.flower.mp.codesync.code.java.adapter;

import com.crispico.flower.mp.model.astcache.code.AnnotationValue;

/**
 * @author Mariana
 */
public class AnnotationValueModelAdapter extends
		com.crispico.flower.mp.codesync.code.adapter.AnnotationValueModelAdapter {

	@Override
	public String getLabel(Object modelElement) {
		AnnotationValue value = (AnnotationValue) modelElement;
		return String.format("%s = %s", value.getName(), value.getValue());
	}
	
}
