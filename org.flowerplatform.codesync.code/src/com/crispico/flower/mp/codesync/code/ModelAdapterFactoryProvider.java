package com.crispico.flower.mp.codesync.code;

import java.util.HashMap;
import java.util.Map;

import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;

/**
 * @author Mariana
 */
public class ModelAdapterFactoryProvider {

	private Map<String, ModelAdapterFactory> factories = new HashMap<String, ModelAdapterFactory>();
	
	public Map<String, ModelAdapterFactory> getFactories() {
		return factories;
	}
	
}
