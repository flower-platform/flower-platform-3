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

import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.web.tests.EclipseDependentTestSuiteBase;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;

/**
 * @author Mariana
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	CodeSyncTest.class, 
	CodeSyncWikiTest.class })
public class CodeSyncTestSuite extends EclipseDependentTestSuiteBase {

	public static File getProject(String project) {
		String absolutePath = "/org/ws_trunk/" + project;
		File resource = null;
		try {
			resource = (File) EditorPlugin.getInstance().getFileAccessController().getFile(absolutePath);
		} catch (Exception e) {
			throw new RuntimeException(String.format("Error while getting resource %s", absolutePath), e);
		}
		return CodeSyncPlugin.getInstance().getProjectsProvider().getContainingProjectForFile(resource);
	}
	
	public static File getFile(String path) {
		String absolutePath = "/org/ws_trunk/" + path;
		try {
			return (File) EditorPlugin.getInstance().getFileAccessController().getFile(absolutePath);
		} catch (Exception e) {			
			throw new RuntimeException(String.format("Error while getting resource %s", absolutePath), e);
		}
	}
	
}