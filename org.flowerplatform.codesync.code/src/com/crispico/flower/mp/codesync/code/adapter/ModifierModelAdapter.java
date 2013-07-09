package com.crispico.flower.mp.codesync.code.adapter;

import java.util.Arrays;
import java.util.List;

import com.crispico.flower.mp.codesync.merge.SyncElementModelAdapter;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.astcache.code.Modifier;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * Mapped to {@link Modifier}. Does not have a corresponding {@link CodeSyncElement}.
 * 
 * @author Mariana
 */
public class ModifierModelAdapter extends SyncElementModelAdapter {

	@Override
	public List<?> getFeatures(Object element) {
		return Arrays.asList(
			AstCacheCodePackage.eINSTANCE.getModifier_Type()
		);
	}
	
	@Override
	public Object getMatchKey(Object element) {
		return String.valueOf(((Modifier) element).getType());
	}
	
}
