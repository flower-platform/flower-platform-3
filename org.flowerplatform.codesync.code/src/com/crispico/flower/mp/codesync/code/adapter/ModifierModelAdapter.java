package com.crispico.flower.mp.codesync.code.adapter;

import com.crispico.flower.mp.codesync.merge.SyncElementModelAdapter;
import com.crispico.flower.mp.model.astcache.code.Modifier;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * Mapped to {@link Modifier}. Does not have a corresponding {@link CodeSyncElement}.
 * 
 * @author Mariana
 */
public class ModifierModelAdapter extends SyncElementModelAdapter {

	@Override
	public Object getMatchKey(Object element) {
		return String.valueOf(((Modifier) element).getType());
	}
	
}
