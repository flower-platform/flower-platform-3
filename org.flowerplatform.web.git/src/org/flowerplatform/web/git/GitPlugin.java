package org.flowerplatform.web.git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.web.git.decorator.GitDecorator;
import org.flowerplatform.web.git.decorator.IndexDiffCache;
import org.osgi.framework.BundleContext;

/**
 * @author Cristina Constantienscu
 */
public class GitPlugin extends AbstractFlowerJavaPlugin {

	protected static GitPlugin INSTANCE;
	
	private GitUtils utils = new GitUtils();

	private List<GenericTreeStatefulService>treeStatefulServicesDisplayingGitContent = new ArrayList<GenericTreeStatefulService>();
	
	private IndexDiffCache indexDiffCache;

	public static GitPlugin getInstance() {
		return INSTANCE;
	}

	public GitUtils getUtils() {
		return utils;
	}

	public List<GenericTreeStatefulService> getTreeStatefulServicesDisplayingGitContent() {
		return treeStatefulServicesDisplayingGitContent;
	}

	public IndexDiffCache getIndexDiffCache() {
		return indexDiffCache;
	}
	
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		INSTANCE = this;		
		
		indexDiffCache = new IndexDiffCache();
		
//		File root = new File("d:\\data\\java_work\\git_repo_fp\\org.flowerplatform.web.app\\workspace\\");
//		try {
//			GitPlugin.getInstance().getUtils().listenForChanges(root);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
		
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		
		INSTANCE = null;		
	}
	
}
