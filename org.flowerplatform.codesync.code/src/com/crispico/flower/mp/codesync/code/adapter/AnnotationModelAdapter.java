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
package com.crispico.flower.mp.codesync.code.adapter;

import com.crispico.flower.mp.codesync.code.CodeSyncCodePlugin;
import com.crispico.flower.mp.codesync.merge.SyncElementModelAdapter;
import com.crispico.flower.mp.model.astcache.code.Annotation;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * Mapped to {@link Annotation}. Does not have a corresponding {@link CodeSyncElement}.
 * 
 * @author Mariana
 */
public class AnnotationModelAdapter extends SyncElementModelAdapter {

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