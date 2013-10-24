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
package org.flowerplatform.orion.server;

import java.io.InputStream;

import org.flowerplatform.common.IFlowerPropertiesProvider;

/**
 * @author Cristina Constantinescu
 */
public class OrionFlowerPropertiesProvider implements IFlowerPropertiesProvider {

	@Override
	public InputStream getFlowerPropertiesAsInputStream() {
		return this.getClass().getClassLoader().getResourceAsStream("META-INF/flower-orion.properties");
	}

}
