package org.flowerplatform.web.git;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.osgi.framework.BundleContext;

/**
 * @author Cristina Constantienscu
 */
public class GitPlugin extends AbstractFlowerJavaPlugin {

	protected static GitPlugin INSTANCE;
	
	private GitUtils utils = new GitUtils();

	private List<GenericTreeStatefulService>treeStatefulServicesDisplayingGitContent = new ArrayList<GenericTreeStatefulService>();
	
	public static GitPlugin getInstance() {
		return INSTANCE;
	}

	public GitUtils getUtils() {
		return utils;
	}

	public List<GenericTreeStatefulService> getTreeStatefulServicesDisplayingGitContent() {
		return treeStatefulServicesDisplayingGitContent;
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		INSTANCE = this;		
	}
		
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		
		INSTANCE = null;		
	}
	
}
