package com.crispico.flower.mp.codesync.code.adapter;

import java.util.Arrays;
import java.util.List;

import com.crispico.flower.mp.codesync.merge.SyncElementModelAdapter;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.astcache.code.Parameter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * Mapped to {@link Parameter}. Does not have a corresponding {@link CodeSyncElement}.
 * 
 * @author Mariana
 */
public class ParameterModelAdapter extends SyncElementModelAdapter {

	@Override
	public List<?> getFeatures(Object element) {
		return Arrays.asList(
			AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers(),
			AstCacheCodePackage.eINSTANCE.getTypedElement_Type(),
			AstCacheCodePackage.eINSTANCE.getParameter_Name()
		);
	}
	
	@Override
	public Object getMatchKey(Object element) {
		return ((Parameter) element).getName();
	}
	
}
