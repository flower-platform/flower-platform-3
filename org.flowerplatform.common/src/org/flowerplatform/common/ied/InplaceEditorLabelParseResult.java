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
package org.flowerplatform.common.ied;

import java.util.List;

/**
 * @author Mariana Gheorghe
 */
public class InplaceEditorLabelParseResult {

	private String name;
	private String type;
	private String visibility;
	private String defaultValue;
	private List<InplaceEditorLabelParseResult> parameters;
	
	public String getName() {
		return name;
	}
	
	public InplaceEditorLabelParseResult setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getType() {
		return type;
	}
	
	public InplaceEditorLabelParseResult setType(String type) {
		this.type = type;
		return this;
	}

	public String getVisibility() {
		return visibility;
	}
	
	public char getVisibilityCharacter() {
		return visibility == null ? 0 : visibility.charAt(0); 
	}

	public InplaceEditorLabelParseResult setVisibility(String visibility) {
		this.visibility = visibility;
		return this;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public InplaceEditorLabelParseResult setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	public List<InplaceEditorLabelParseResult> getParameters() {
		return parameters;
	}

	public InplaceEditorLabelParseResult setParameters(List<InplaceEditorLabelParseResult> parameters) {
		this.parameters = parameters;
		return this;
	}
	
}
