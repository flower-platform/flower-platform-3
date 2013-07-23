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