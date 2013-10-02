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
package org.flowerplatform.web.tests.codesync;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.flowerplatform.web.tests.EclipseDependentTestSuiteBase;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Mariana
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	CodeSyncTest.class, 
	CodeSyncJavascriptTest.class,
	CodeSyncWikiTest.class })
public class CodeSyncTestSuite extends EclipseDependentTestSuiteBase {

	public static IProject getProject(String project) {
		String absolutePath = CommonPlugin.getInstance().getWorkspaceRoot().getAbsolutePath() + "/org/ws_trunk/" + project;
		IResource resource = ProjectsService.getInstance().getProjectWrapperResourceFromFile(new File(absolutePath));
		return resource.getProject();
	}
	
	public static IFile getFile(String path) {
		String absolutePath = CommonPlugin.getInstance().getWorkspaceRoot().getAbsolutePath() + "/org/ws_trunk/" + path;
		IResource resource = ProjectsService.getInstance().getProjectWrapperResourceFromFile(new File(absolutePath));
		return (IFile) resource;
	}
	
}