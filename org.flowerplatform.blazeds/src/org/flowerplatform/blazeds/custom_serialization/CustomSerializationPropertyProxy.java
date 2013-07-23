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
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flex.messaging.io.BeanProxy;

/**
 * Custom PropertyProxy used for Java to Flex object serialization. It has a
 * propertyDescriptor that indicates which methods should be serialized.
 * 
 * @author Cristi
 */
public class CustomSerializationPropertyProxy extends BeanProxy {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomSerializationPropertyProxy.class);

	private static final long serialVersionUID = 1L;

	private static final String REFERENCE_HOLDER_SUFFIX = "_RH";
	
	private CustomSerializationDescriptor descriptor;

	public CustomSerializationPropertyProxy(
			CustomSerializationDescriptor descriptor) {
		super();
		if (descriptor == null)
			throw new IllegalArgumentException("This PropertyProxy implementation needs a propertyDescriptor.");
		this.descriptor = descriptor;
		setIncludeReadOnly(true);
	}

	protected String getIdProperty() {
		return "id";
	}

	protected Object getIdForObject(Object object) {
		if (!(object instanceof EObject)) {
			throw new IllegalArgumentException(String.format("Trying to obtain an id for a class that's not EObject; class = %s, object = %s", object.getClass(), object));
		}
		EObject eObject = (EObject) object;
		return eObject.eResource().getURIFragment(eObject);
	}

	protected ReferenceHolder createReferenceHolder() {
		return new ReferenceHolder();
	}
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public List getPropertyNames(Object arg0) {
		return descriptor.getDeclaredProperties();
	}
	
	@Override
	public Object getValue(Object instance, String propertyName) {
		try {
			if (getIdProperty().equals(propertyName)) {
				return getIdForObject(instance);
			} else if (propertyName.endsWith(REFERENCE_HOLDER_SUFFIX)) {
				return convertToReferenceHolder(instance, propertyName);
			} else
				return super.getValue(instance, propertyName);
		} catch (Throwable e) {
			logger.error(String.format("Exception caught while serializing property = %s for TransferableObject = %s", propertyName, instance), e);
			return null;
		}
	}

	@Override
	protected String getClassName(Object instance) {
		if (descriptor.getFlexAlias() != null) {
			return descriptor.getFlexAlias();
		} else {
			return descriptor.getJavaClassName();
		}
	}

	protected Object convertToReferenceHolder(Object o, String propertyName) {
		if (o == null) {
			return null;
		}
		String realPropertyName = propertyName.replaceFirst(REFERENCE_HOLDER_SUFFIX, "");
		Object referencedObject = super.getValue(o, realPropertyName);

		if (referencedObject == null)
			return null;
		else if (referencedObject instanceof Collection<?>) {
			Collection<?> src = (Collection<?>) referencedObject;
			List<ReferenceHolder> list = new ArrayList<ReferenceHolder>(src.size());
			for (Object crt : src) {
				ReferenceHolder ref = createReferenceHolder();
				ref.setReferenceId(getIdForObject(crt));
				list.add(ref);
			}
			return list;
		} else {
			ReferenceHolder ref = createReferenceHolder();
			ref.setReferenceId(getIdForObject(referencedObject));
			return ref;
		}
	}


}