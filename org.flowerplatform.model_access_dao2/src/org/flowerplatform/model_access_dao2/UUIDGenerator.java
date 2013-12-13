package org.flowerplatform.model_access_dao2;

import java.util.UUID;

import com.datastax.driver.core.utils.UUIDs;

public class UUIDGenerator {

	public static UUID newUUID() {
		return UUIDs.timeBased();
	}
	
	public static UUID fromString(String uuid) {
		return UUID.fromString(uuid);
	}
	
}
