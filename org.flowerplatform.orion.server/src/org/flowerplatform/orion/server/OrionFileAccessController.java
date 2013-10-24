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
package org.flowerplatform.orion.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.orion.internal.server.servlets.file.NewFileServlet;
import org.eclipse.orion.internal.server.servlets.workspace.WebProject;
import org.eclipse.orion.internal.server.servlets.workspace.WebWorkspace;
import org.eclipse.orion.server.core.users.OrionScope;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.file.FileAccessController;
import org.osgi.service.prefs.BackingStoreException;

/**
 * @author Cristina Constantinescu
 */
@SuppressWarnings({ "deprecation", "restriction" })
public class OrionFileAccessController extends FileAccessController {
	
	@Override
	public String getPath(Object file) {
		// because orion doesn't provide a better solution to get client path from server path
		// we iterate through prefs files to get it
		// BE AWARE: this works if orion.core.metastore=legacy in web-ide.conf
		String path = CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot((File) file);
		// first token is project id, so we start from here
		String projectId = path.split("/")[0];		
		// get project from Projects.prefs
		WebProject project = WebProject.fromId(projectId);
		// get workspace for this project from Workspaces.prefs
		WebWorkspace workspace = null;
		for (WebWorkspace ww : allWorkspaces()) {
			if (ww.getProjectByName(project.getName()) != null) {
				workspace = ww;
				break;
			}
		}
		// compute path
		return "file/" + workspace.getId() + "/" + project.getName() + path.substring(projectId.length());
	}

	@Override
	public Object getFile(String path) {			
		String decodedPath = EditorPlugin.getInstance().getFriendlyNameDecoded(path);
		
		IPath iPath = new Path(decodedPath).removeFirstSegments(1);			
		
		IFileStore fileStore = NewFileServlet.getFileStore(null, iPath);
		try {
			return fileStore.toLocalFile(EFS.NONE, null);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<WebWorkspace> allWorkspaces() {
		List<WebWorkspace> result = new ArrayList<WebWorkspace>();
		IEclipsePreferences workspaceRoot = new OrionScope().getNode("Workspaces");
		try {
			String[] ids = workspaceRoot.childrenNames();
			for (String id : ids) {
				result.add(WebWorkspace.fromId(id));
			}
		} catch (BackingStoreException e) {
		}
		return result;
	}
}
