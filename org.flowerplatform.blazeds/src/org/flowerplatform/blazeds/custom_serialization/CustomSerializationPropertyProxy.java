package org.flowerplatform.blazeds.custom_serialization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

//	protected abstract String getIdProperty();
//	
//	protected abstract Object getIdForObject(Object object); 
//	
//	protected abstract AbstractReferenceHolder createReferenceHolder();
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public List getPropertyNames(Object arg0) {
		return descriptor.getDeclaredProperties();
	}
	
	@Override
	public Object getValue(Object instance, String propertyName) {
		try {
//			if (getIdProperty().equals(propertyName)) {
//				return getIdForObject(instance);
//			} else if (propertyName.endsWith(REFERENCE_HOLDER_SUFFIX)) {
//				return convertToReferenceHolder(instance, propertyName);
//			} else
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

//	protected Object convertToReferenceHolder(Object o, String propertyName) {
//		if (o == null) {
//			return null;
//		}
//		String realPropertyName = propertyName.replaceFirst(REFERENCE_HOLDER_SUFFIX, "");
//		Object referencedObject = super.getValue(o, realPropertyName);
//
//		if (referencedObject == null)
//			return null;
//		else if (referencedObject instanceof Collection<?>) {
//			Collection<?> src = (Collection<?>) referencedObject;
//			List<AbstractReferenceHolder> list = new ArrayList<AbstractReferenceHolder>(src.size());
//			for (Object crt : src) {
//				AbstractReferenceHolder ref = createReferenceHolder();
//				ref.setReferenceId(getIdForObject(crt));
//				list.add(ref);
//			}
//			return list;
//		} else {
//			AbstractReferenceHolder ref = createReferenceHolder();
//			ref.setReferenceId(getIdForObject(referencedObject));
//			return ref;
//		}
//	}


}
