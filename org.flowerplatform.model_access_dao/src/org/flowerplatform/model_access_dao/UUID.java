package org.flowerplatform.model_access_dao;

public class UUID {

	public static String newUUID() {
		return java.util.UUID.randomUUID().toString();
	}
	
}
