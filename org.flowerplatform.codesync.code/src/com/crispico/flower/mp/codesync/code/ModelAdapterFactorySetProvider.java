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
package com.crispico.flower.mp.codesync.code;

import java.util.HashMap;
import java.util.Map;

import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;

/**
 * @author Mariana
 */
public class ModelAdapterFactorySetProvider {

	private Map<String, ModelAdapterFactorySet> factorieSets = new HashMap<String, ModelAdapterFactorySet>();
	
	public Map<String, ModelAdapterFactorySet> getFactorieSets() {
		return factorieSets;
	}
	
}