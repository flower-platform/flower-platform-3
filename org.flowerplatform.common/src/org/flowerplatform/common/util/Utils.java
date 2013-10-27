package org.flowerplatform.common.util;

import java.util.Map;

/**
 * @author Cristian Spiescu
 */
public class Utils {
	public static <T> T getValueSafe(Map<?, T> map, Object key) {
		if (map == null) {
			return null;
		} else {
			return map.get(key);
		}
	}
}
