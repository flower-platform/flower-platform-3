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
package org.flowerplatform.blazeds.custom_serialization;

import java.util.ArrayList;
import java.util.List;

import flex.messaging.io.PropertyProxyRegistry;

/**
 * This class has a list of the attributes of the class, and it's qualified name.
 * It is used for the custom serialization mechanism. Please look at the AS
 * equivalent class for more details.
 *
 * @author Cristi
 */
public class CustomSerializationDescriptor {
	
	private String javaClassName;

	private List<String> declaredProperties;
	
	private String flexAlias;
	
	protected CustomSerializationPropertyProxy createPropertyProxy() {
		return new CustomSerializationPropertyProxy(this);
	}

	public String getJavaClassName() {
		return javaClassName;
	}

	public void setJavaClassName(String javaClassName) {
		this.javaClassName = javaClassName;
	}

	public List<String> getDeclaredProperties() {
		return declaredProperties;
	}

	public void setDeclaredProperties(List<String> declaredProperties) {
		this.declaredProperties = declaredProperties;
	}

	public String getFlexAlias() {
		return flexAlias;
	}

	public CustomSerializationDescriptor setFlexAlias(String flexAlias) {
		this.flexAlias = flexAlias;
		return this;
	}

	public CustomSerializationDescriptor(Class<?> clazz) {
		super();
		this.javaClassName = clazz.getName();
	}

	public CustomSerializationDescriptor addDeclaredProperty(String propertyName) {
		if (declaredProperties == null) {
			declaredProperties = new ArrayList<String>();
		}
		declaredProperties.add(propertyName);
		return this;
	}
	
	public CustomSerializationDescriptor addDeclaredProperties(List<String> propertiesToCopy) {
		if (declaredProperties == null) {
			declaredProperties = new ArrayList<String>();
		}
		declaredProperties.addAll(propertiesToCopy);
		return this;
	}
	
	public CustomSerializationDescriptor register() {
		@SuppressWarnings("rawtypes")
		Class clazz;
		try {
			clazz = Class.forName(getJavaClassName());
			CustomSerializationPropertyProxy propertyProxy = createPropertyProxy();
			PropertyProxyRegistry.getRegistry().register(clazz, propertyProxy);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Error registering TransferableObjectDescriptor", e);
		}
		return this;
	}
	
	public String toString() {
		return "TransferableObjectDescriptor: " + javaClassName + declaredProperties;
	}
}