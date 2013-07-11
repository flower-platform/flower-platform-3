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
