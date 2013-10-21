package org.flowerplatform.orion.server;

import java.io.InputStream;

import org.flowerplatform.common.IFlowerPropertiesProvider;

public class OrionFlowerPropertiesProvider implements IFlowerPropertiesProvider {

	@Override
	public InputStream getFlowerPropertiesAsInputStream() {
		return this.getClass().getClassLoader().getResourceAsStream("META-INF/flower-orion.properties");
	}

}
