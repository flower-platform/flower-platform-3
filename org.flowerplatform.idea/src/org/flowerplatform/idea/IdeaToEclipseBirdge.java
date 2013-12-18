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
package org.flowerplatform.idea;

import myPackage.IIdeaToEclipseBridge;

import org.flowerplatform.idea.action.IdeaNewDiagramAction;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author Sebastian Solomon
 */
public class IdeaToEclipseBirdge implements IIdeaToEclipseBridge{

	@Override
	public String getUrl() {
		return IdeaPlugin.getInstance().getServer().getUrl();
	}
	
	@Override
	public void createDiagram(VirtualFile vFile) {
		IdeaNewDiagramAction inda = new IdeaNewDiagramAction();
		inda.parentPath = CodeSyncPlugin.getInstance().getProjectAccessController().getPathRelativeToProject(vFile); 
		inda.name = "NewDiagram.notation";
		inda.executeCommand();
	}

}
