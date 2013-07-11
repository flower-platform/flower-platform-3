package com.crispico.flower.mp.codesync.code.java.adapter;

import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;

import com.crispico.flower.mp.model.astcache.code.Modifier;

/**
 * @author Mariana
 */
public class ModifierModelAdapter extends
		com.crispico.flower.mp.codesync.code.adapter.ModifierModelAdapter {

	@Override
	public String getLabel(Object modelElement) {
		return ModifierKeyword.fromFlagValue(((Modifier) modelElement).getType()).toString();
	}
	
}
