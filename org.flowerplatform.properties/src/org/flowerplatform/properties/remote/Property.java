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
package org.flowerplatform.properties.remote;
/**
 * @author Razvan Tache
 * @author Cristina Constantinescu
 */
public class Property {
	
	private String name;
	private Object value;
	private boolean readOnly = true;
	private String type = null;
	
	/**
	 * This constructor is used for deserialisation and should not be used otherwise
	 */
	public Property() {
		// used for deserialisation
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Property setNameAs(String name) {
		this.name = name;
		return this;
	}
	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public Property setValueAs(Object value) {
		this.value = value;
		return this;
	}

	public boolean getReadOnly() {
		return readOnly;
	}
	
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	public Property setReadOnlyAs(boolean readOnly) {
		this.readOnly = readOnly;
		return this;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Property setTypeAs(String type) {
		this.type = type;
		return this;
	}
}
