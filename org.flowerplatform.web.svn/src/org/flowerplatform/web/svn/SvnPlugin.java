package org.flowerplatform.web.svn;


import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.osgi.framework.BundleContext;

/**
 * @flowerModelElementId _GO_RkP2kEeKrJqcAep-lCg
 */
public class SvnPlugin extends AbstractFlowerJavaPlugin {
	/**
	 * @flowerModelElementId _1w_YoP2kEeKrJqcAep-lCg
	 */
	private SvnPlugin INSTANCE;
	/**
	 * @flowerModelElementId _sK_EsP3FEeKrJqcAep-lCg
	 */
	private List<GenericTreeStatefulService> treeStatefulServicesDisplayingSvnContent = new ArrayList<GenericTreeStatefulService>();;

	/**
	 * @flowerModelElementId _fhKA8P3GEeKrJqcAep-lCg
	 */
	public void start(BundleContext context) {
		// TODO implement
	}

	/**
	 * @flowerModelElementId _wPIaIP3GEeKrJqcAep-lCg
	 */
	public void stop(BundleContext context) {
		// TODO implement
	}
}