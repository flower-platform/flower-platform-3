package org.flowerplatform.common.util;

import java.lang.reflect.Field;

public class ReflectionUtils {

	public static void setPrivateField(Object instance, Class<?> classOfInstance, String fieldName, Object value) {
		try {
			Field field = classOfInstance.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(instance, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getPrivateField(Object instance, Class<?> classOfInstance, String fieldName) {
		try {
			Field field = classOfInstance.getDeclaredField(fieldName);
			field.setAccessible(true);
			return (T) field.get(instance);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
