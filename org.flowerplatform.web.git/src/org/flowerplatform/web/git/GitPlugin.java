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