package com.crispico.flower.mp.codesync.code.adapter;

import com.crispico.flower.mp.codesync.merge.SyncElementModelAdapter;
import com.crispico.flower.mp.model.astcache.code.Parameter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * Mapped to {@link Parameter}. Does not have a corresponding {@link CodeSyncElement}.
 * 
 * @author Mariana
 */
public class ParameterModelAdapter extends SyncElementModelAdapter {
	
	@Override
	public Object getMatchKey(Object element) {
		return ((Parameter) element).getName();
	}
	
}
