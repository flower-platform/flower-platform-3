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
 */
public class Property {
	
	private String name;
	private Object value;
	private boolean readOnly;
	private String type;
	
	/**
	 * This constructor is used for deserialisation and should not be used otherwise
	 */
	public Property() {
		// used for deserialisation
	}
	/**
	 * The constructor creates a property with the parameters name and value, provided by user.
	 * And sets the readOnly attribute to the default : true, and the type attribute to String.
	 * Basically it calls new property(name, value, "String", true)
	 * @param name
	 * @param value 
	 */
	public Property(String name, Object value) {	
		this(name, value, null, true);
	}
	
	/**
	 * 
	 * 
	 */
	public Property(String name, Object value, String type) {	
		this(name, value, type, true);
	}
	
	/**
	 * 
	 * 
	 */
	public Property(String name, Object value, boolean readOnly) {
		this(name, value, null, readOnly);
	}
	/**
	 * The constructor creates a property with the name, value, and readOnly(true|false) provided by user
	 * @param name
	 * @param value 
	 * @param type
	 * @param readOnly
	 */
	public Property(String name, Object value, String type, boolean readOnly) {
		super();
		this.name = name;
		this.value = value;
		this.type = type;
		this.readOnly = readOnly;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean getReadOnly() {
		return readOnly;
	}
	
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
