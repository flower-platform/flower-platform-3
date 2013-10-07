package org.flowerplatform.eclipse;

import java.io.InputStream;

import org.flowerplatform.common.IFlowerPropertiesProvider;

public class EclipseFlowerPropertiesProvider implements IFlowerPropertiesProvider {

	@Override
	public InputStream getFlowerPropertiesAsInputStream() {
		return this.getClass().getClassLoader().getResourceAsStream("META-INF/flower.properties");
	}

}
