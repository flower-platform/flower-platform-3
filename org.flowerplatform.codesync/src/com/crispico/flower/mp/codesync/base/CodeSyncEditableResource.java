package com.crispico.flower.mp.codesync.base;

import org.flowerplatform.editor.remote.EditableResource;

/**
 * @author Mariana
 */
public class CodeSyncEditableResource extends EditableResource {

	private Match match;
	
	private ModelAdapterFactorySet modelAdapterFactorySet;
	
	@Override
	public String getLabel() {
		return getEditorInput().toString();
	}

	@Override
	public String getIconUrl() {
		return "icons/Web/icons/file.gif";
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public ModelAdapterFactorySet getModelAdapterFactorySet() {
		return modelAdapterFactorySet;
	}

	public void setModelAdapterFactorySet(ModelAdapterFactorySet modelAdapterFactorySet) {
		this.modelAdapterFactorySet = modelAdapterFactorySet;
	}

}
