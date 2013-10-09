package org.flowerplatform.web;

import java.io.InputStream;

import org.flowerplatform.common.IFlowerPropertiesProvider;

/**	
 * @author Cristina Constatinescu
 */
public class WebFlowerPropertiesProvider implements IFlowerPropertiesProvider {
	
	@Override
	public InputStream getFlowerPropertiesAsInputStream() {
		return this.getClass().getClassLoader().getResourceAsStream("META-INF/flower-web.properties");		
	}

}
