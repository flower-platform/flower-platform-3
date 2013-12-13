package org.flowerplatform.model_access_dao;

import java.util.UUID;

import com.datastax.driver.core.utils.UUIDs;

public class UUIDGenerator {

	public static UUID newUUID() {
		return UUIDs.timeBased();
	}
	
}
