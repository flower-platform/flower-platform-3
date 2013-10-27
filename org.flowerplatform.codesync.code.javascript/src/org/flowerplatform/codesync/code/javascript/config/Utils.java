package org.flowerplatform.codesync.code.javascript.config;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

public class Utils {
	
	/**
	 * @author Cristian Spiescu
	 */
	public static String getQualifiedPath(CodeSyncElement element) {
		StringBuffer sb = new StringBuffer();
		while (element != null) {
			if (element instanceof CodeSyncRoot) {
				break;
			}
			if (!CodeSyncPlugin.FILE.equals(element.getType())) {
				if (sb.length() != 0) {
					sb.insert(0, '/');
				}
//				String currentName = (String) CodeSyncOperationsService.getInstance().getFeatureValue(element, FeatureAccessExtension.CODE_SYNC_NAME);
				String currentName = element.getName(); // TODO CS/JS use the API; the FILE special case won't be needed any more I guess
				sb.insert(0, currentName);
			}
			element = element.eContainer() instanceof CodeSyncElement ? (CodeSyncElement) element.eContainer() : null;
		}
		return sb.toString();
	}

}
